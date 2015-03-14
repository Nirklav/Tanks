package com.ThirtyNineEighty.Game.Worlds;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.Collisions.CollisionManager;
import com.ThirtyNineEighty.Game.Gameplay.Characteristics.CharacteristicFactory;
import com.ThirtyNineEighty.Game.Gameplay.Land;
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
import java.util.List;

public class GameWorld
  implements IWorld
{
  private ArrayList<IEngineObject> objects;
  private IEngineObject player;
  private GameMenu menu;

  private ISubprogram otherTankSubprogram;
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
    menu = new GameMenu();

    player = new Tank(CharacteristicFactory.TANK);
    player.onMoved(-20);

    final Tank otherTank = new Tank(CharacteristicFactory.TANK);

    Land land = new Land();
    land.onMoved(Vector3.zAxis, -0.8f);

    add(player);
    add(land);
    add(otherTank);

    IContent content = GameContext.getContent();

    content.setMenu(menu);
    content.bindProgram(worldSubprogram = new ISubprogram()
    {
      @Override
      public void update()
      {
        if (menu.getForwardState())
          collisionManager.move(player, 5f * GameContext.getDelta());

        Vector3 vector = Vector.getInstance(3);

        if (menu.getLeftState())
        {
          vector.setFrom(0, 0, 45 * GameContext.getDelta());
          collisionManager.rotate(player, vector);
        }

        if (menu.getRightState())
        {
          vector.setFrom(0, 0, -45 * GameContext.getDelta());
          collisionManager.rotate(player, vector);
        }

        Vector.release(vector);
      }
    });

    content.bindProgram(otherTankSubprogram = new ISubprogram()
    {
      @Override
      public void update()
      {
        collisionManager.move(otherTank, 5f * GameContext.getDelta());

        Vector3 vector = Vector.getInstance(3);
        vector.setFrom(0, 0, -45 * GameContext.getDelta());
        collisionManager.rotate(otherTank, vector);

        Vector.release(vector);
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
    content.unbindProgram(otherTankSubprogram);
  }

  @Override
  public void setViewMatrix(float[] viewMatrix)
  {
    Vector3 center = player.getPosition();
    Vector3 eye = Vector.getInstance(3, player.getPosition());

    eye.addToX(0.0f);
    eye.addToY(14.0f);
    eye.addToZ(30);

    Matrix.setLookAtM(viewMatrix, 0, eye.getX(), eye.getY(), eye.getZ(), center.getX(), center.getY(), center.getZ(), 0.0f, 0.0f, 1.0f);

    Vector.release(eye);
  }

  @Override
  public void fillRenderable(List<I3DRenderable> renderables)
  {
    for(IEngineObject engineObject : objects)
      renderables.add(engineObject.getRenderable());
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
