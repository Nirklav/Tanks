package com.ThirtyNineEighty.Game.Worlds;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.Collisions.CollisionManager;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Gameplay.Land;
import com.ThirtyNineEighty.Game.Gameplay.MapDescription;
import com.ThirtyNineEighty.Game.Gameplay.MapLoader;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Menu.GameMenu;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.IContent;
import com.ThirtyNineEighty.System.ISubprogram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameWorld
  implements IWorld
{
  private ArrayList<IEngineObject> objects;
  private Tank player;
  private GameMenu menu;

  private ISubprogram worldSubprogram;

  public final CollisionManager collisionManager;

  public GameWorld()
  {
    objects = new ArrayList<IEngineObject>();
    collisionManager = new CollisionManager(objects);
  }

  @Override
  public void initialize(Object args)
  {
    MapDescription map = MapLoader.load("standard");

    player = new Tank(CharacteristicFactory.TANK);
    player.setPosition(map.player.getPosition());
    player.setAngles(map.player.getAngles());
    add(player);
    add(new Land());

    IContent content = GameContext.getContent();

    menu = new GameMenu();
    content.setMenu(menu);

    content.bindProgram(worldSubprogram = new ISubprogram() // TODO: move this code in button callbacks
    {
      @Override
      public void update()
      {
        Vector3 vector = Vector.getInstance(3);

        float joyAngle = menu.getJoystickAngle();
        float playerAngle = player.getAngles().getZ();

        if (Math.abs(joyAngle - playerAngle) < 90)
          collisionManager.move(player);

        if (Math.abs(joyAngle - playerAngle) > 5)
          collisionManager.rotate(player, joyAngle);

        if (menu.getLeftTurretState())
          player.turnTurret(45f * GameContext.getDelta());

        if (menu.getRightTurretState())
          player.turnTurret(-45f * GameContext.getDelta());

        Vector.release(vector);
      }
    });

    content.bindLastProgram(new ISubprogram()
    {
      @Override
      public void update()
      {
        // resolve all collisions
        collisionManager.resolve();
      }
    });
  }

  @Override
  public void uninitialize()
  {
    for(IEngineObject object : objects)
      object.onRemoved();

    objects.clear();

    IContent content = GameContext.getContent();
    content.unbindProgram(worldSubprogram);
    content.unbindLastProgram();
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
  public void fillRenderable(List<I3DRenderable> renderables)
  {
    for (IEngineObject engineObject : objects)
    {
      engineObject.setGlobalRenderablePosition();
      Collections.addAll(renderables, engineObject.getRenderables());
    }
  }

  @Override
  public IEngineObject getPlayer() { return player; }

  @Override
  public Collection<IEngineObject> getObjects() { return objects; }

  @Override
  public void add(IEngineObject engineObject)
  {
    objects.add(engineObject);
  }

  @Override
  public void remove(IEngineObject engineObject)
  {
    objects.remove(engineObject);
    engineObject.onRemoved();
  }
}
