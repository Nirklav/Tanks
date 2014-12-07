package com.ThirtyNineEighty.Game.Worlds;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.AI;
import com.ThirtyNineEighty.Game.Collisions.CollisionManager;
import com.ThirtyNineEighty.Game.EngineObject;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Game.Menu.GameMenu;
import com.ThirtyNineEighty.Game.Menu.IMenu;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.System.Content;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.List;

public class GameWorld implements IGameWorld
{
  private Content content;

  private AI ai;
  private GameMenu menu;
  private CollisionManager collisionManager;

  private ArrayList<IEngineObject> objects;

  private EngineObject playerTank;

  private Vector3 vector;

  @Override
  public void initialize(Content content, Object args)
  {
    this.content = content;

    ai = new AI();
    menu = new GameMenu();
    collisionManager = new CollisionManager();
    vector = new Vector3();

    playerTank = new Tank(this);
    playerTank.onMoved(-20);

    EngineObject otherTank = new Tank(this);
    EngineObject land = new EngineObject(this, "land");

    land.onMoved(Vector3.zAxis, -0.8f);

    objects = new ArrayList<IEngineObject>();
    objects.add(playerTank);
    objects.add(otherTank);
    objects.add(land);
  }

  @Override
  public void update()
  {
    if (menu.getForwardState())
      playerTank.move(0.2f);//* DeltaTime.getDelta());

    if (menu.getLeftState())
    {
      vector.setFrom(0, 0, 45 * GameContext.getDelta());
      playerTank.rotate(vector);
    }

    if (menu.getRightState())
    {
      vector.setFrom(0, 0, -45 * GameContext.getDelta());
      playerTank.rotate(vector);
    }

    ai.update(objects);
  }

  @Override
  public void setViewMatrix(float[] viewMatrix)
  {
    Vector3 center = playerTank.getPosition();
    Vector3 eye = new Vector3(playerTank.getPosition());
    Vector3 angles = playerTank.getAngles();

    eye.addToX(-8.0f * (float)Math.cos(Math.toRadians(angles.getZ())));
    eye.addToY(-8.0f * (float)Math.sin(Math.toRadians(angles.getZ())));
    eye.addToZ(6);

    Matrix.setLookAtM(viewMatrix, 0, eye.getX(), eye.getY(), eye.getZ(), center.getX(), center.getY(), center.getZ(), 0.0f, 0.0f, 1.0f);
  }

  @Override
  public void addObject(IEngineObject object) { objects.add(object); }

  @Override
  public void fillRenderable(List<I3DRenderable> renderables)
  {
    for(IEngineObject engineObject : objects)
      renderables.add(engineObject.getRenderable());
  }

  @Override
  public void move(IEngineObject gameObject, float length) { collisionManager.move(gameObject, objects, length); }

  @Override
  public void rotate(IEngineObject gameObject, Vector3 angles) { collisionManager.rotate(gameObject, objects, angles); }

  @Override
  public IMenu getMenu() { return menu; }
}
