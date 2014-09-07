package com.ThirtyNineEighty.Game;

import android.view.MotionEvent;

import com.ThirtyNineEighty.Game.Collide.CollideManager;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.IGameObject;
import com.ThirtyNineEighty.Renderable.I2DRenderable;
import com.ThirtyNineEighty.Renderable.I3DRenderable;
import com.ThirtyNineEighty.System.DeltaTime;

import java.util.Collection;
import java.util.Vector;

public class World
{
  protected Vector<IGameObject> objects;
  protected AIManager aiManager;
  protected CollideManager collideManager;

  private GameObject playerTank;
  private GameObject otherTank;
  private GameObject land;

  public void initialize(String mapName)
  {
    aiManager = new AIManager();
    collideManager = new CollideManager();

    playerTank = new GameObject(0, this, "tank");
    otherTank = new GameObject(1, this, "tank");
    land = new GameObject(2, this, "land");

    objects = new Vector<IGameObject>();
    objects.add(playerTank);
    objects.add(otherTank);
    //objects.add(land);
  }

  public AIManager getAI()
  {
    return aiManager;
  }

  public I3DRenderable getCameraTarget()
  {
    return playerTank.getVisualModel();
  }

  public Collection<I3DRenderable> get3DRenderable()
  {
    Vector<I3DRenderable> result = new Vector<I3DRenderable>();
    for(IGameObject object : objects)
      result.add(object.getVisualModel());

    return result;
  }

  public Collection<I2DRenderable> get2DRenderable()
  {
    return null;
  }

  public void move(IGameObject gameObject, float length)
  {
    collideManager.move(gameObject, objects, length);
  }

  public void rotate(IGameObject gameObject, float angleX, float angleY, float angleZ)
  {
    collideManager.rotate(gameObject, objects, angleX, angleY, angleZ);
  }

  public void updatePlayer(MotionEvent event, float width, float height)
  {
    int pointerCount = event.getPointerCount();

    for(int i = 0; i < pointerCount; i++)
    {
      float leftBorder = (1.0f / 3.0f) * width;
      float rightBorder = (2.0f / 3.0f) * width;

      float x = event.getX(i);

      if (x > leftBorder && x < rightBorder)
        playerTank.move(5.0f * DeltaTime.getDelta());

      if (x <= leftBorder)
        playerTank.rotate(0.0f, 0.0f, 45.0f * DeltaTime.getDelta());

      if (x >= rightBorder)
        playerTank.rotate(0.0f, 0.0f, -45.0f * DeltaTime.getDelta());
    }
  }

  public void update()
  {
    aiManager.update(objects);
  }
}
