package com.ThirtyNineEighty.Game.Worlds;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Gameplay.Land;
import com.ThirtyNineEighty.Game.Gameplay.MapDescription;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Menu.GameMenu;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.ISubprogram;
import com.ThirtyNineEighty.System.Subprogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWorld
  implements IWorld
{
  private volatile boolean initialized;

  private final ArrayList<IEngineObject> objects;
  private Tank player;

  private ISubprogram worldProgram;
  private ISubprogram collideProgram;

  public GameWorld()
  {
    objects = new ArrayList<>();
  }

  @Override
  public boolean isInitialized()
  {
    return initialized;
  }

  @Override
  public void initialize(Object args)
  {
    if (!(args instanceof String))
      throw new IllegalArgumentException("Illegal args type");

    MapDescription map = GameContext.mapLoader.load((String)args);

    player = new Tank(CharacteristicFactory.TANK);
    player.setPosition(map.player.getPosition());
    player.setAngles(map.player.getAngles());
    add(player);
    add(new Land());

    GameContext.content.setMenu(new GameMenu());
    GameContext.content.bindProgram(worldProgram = new Subprogram() // TODO: move this code in button callbacks
    {
      @Override
      public void onUpdate()
      {
        GameMenu menu = (GameMenu) GameContext.content.getMenu();

        Vector3 vector = Vector.getInstance(3);

        float joyAngle = menu.getJoystickAngle();
        float playerAngle = player.getAngles().getZ();

        if (Math.abs(joyAngle - playerAngle) < 90)
          GameContext.collisionManager.move(player);

        if (Math.abs(joyAngle - playerAngle) > 3)
          GameContext.collisionManager.rotate(player, joyAngle);

        if (menu.getLeftTurretState())
          player.addTurretAngle(45f * GameContext.getDelta());

        if (menu.getRightTurretState())
          player.addTurretAngle(-45f * GameContext.getDelta());

        Vector.release(vector);
      }
    });

    GameContext.content.bindLastProgram(collideProgram = new Subprogram()
    {
      @Override
      public void onUpdate()
      {
        // resolve all collisions
        GameContext.collisionManager.resolve();
      }
    });

    initialized = true;
  }

  @Override
  public void uninitialize()
  {
    ArrayList<IEngineObject> disposed = new ArrayList<>();
    synchronized (objects)
    {
      for (IEngineObject object : objects)
        disposed.add(object);
      objects.clear();
    }

    for (IEngineObject object : disposed)
      object.dispose();

    GameContext.content.unbindProgram(worldProgram);
    GameContext.content.unbindLastProgram();
  }

  @Override
  public void enable()
  {
    worldProgram.enable();
    collideProgram.enable();

    ArrayList<IEngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (IEngineObject object : enabling)
      object.enable();
  }

  @Override
  public void disable()
  {
    worldProgram.disable();
    collideProgram.disable();

    ArrayList<IEngineObject> enabling = new ArrayList<>();
    fillObjects(enabling);
    for (IEngineObject object : enabling)
      object.disable();
  }

  @Override
  public void setViewMatrix(float[] viewMatrix)
  {
    Vector3 center = player.getPosition();
    Vector3 eye = Vector.getInstance(3, player.getPosition());

    eye.addToX(0.0f);
    eye.addToY(14.0f);
    eye.addToZ(35);

    Matrix.setLookAtM(viewMatrix, 0, eye.getX(), eye.getY(), eye.getZ(), center.getX(), center.getY(), center.getZ(), 0.0f, 0.0f, 1.0f);

    Vector.release(eye);
  }

  @Override
  public void fillRenderable(List<I3DRenderable> filled)
  {
    synchronized (objects)
    {
      for (IEngineObject engineObject : objects)
      {
        engineObject.setGlobalRenderablePosition();
        Collections.addAll(filled, engineObject.getRenderables());
      }
    }
  }

  @Override
  public void fillObjects(List<IEngineObject> filled)
  {
    synchronized (objects)
    {
      for (IEngineObject object : objects)
        filled.add(object);
    }
  }

  @Override
  public IEngineObject getPlayer() { return player; }

  @Override
  public void add(IEngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.add(engineObject);
    }
  }

  @Override
  public void remove(IEngineObject engineObject)
  {
    synchronized (objects)
    {
      objects.remove(engineObject);
    }
    engineObject.dispose();
  }
}
