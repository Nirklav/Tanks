package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.Collisions.PhysicalModel;
import com.ThirtyNineEighty.Game.World;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.Model3D;

public class GameObject
  implements IGameObject
{
  private World world;

  private long id;
  private Model3D visualModel;
  private PhysicalModel physicalModel;

  public GameObject(int id, World world, String name)
  {
    visualModel = new Model3D(String.format("Models/%s.raw", name), String.format("Textures/%s.png", name));
    physicalModel = new PhysicalModel(String.format("Models/%s.ph", name));

    this.id = id;
    this.world = world;
  }

  @Override
  public void move(float length)
  {
    world.move(this, length);
  }

  @Override
  public void rotate(float angleX, float angleY, float angleZ)
  {
    world.rotate(this, angleX, angleY, angleZ);
  }

  @Override
  public void onMoved(float length)
  {
    visualModel.move(length);
  }

  @Override
  public void onMoved(Vector3 vector, float length)
  {
    visualModel.move(vector, length);
  }

  @Override
  public void onRotates(float angleX, float angleY, float angleZ)
  {
    visualModel.rotateAboutX(angleX);
    visualModel.rotateAboutY(angleY);
    visualModel.rotateAboutZ(angleZ);
  }

  @Override
  public long getId()
  {
    return id;
  }

  @Override
  public float getRadius()
  {
    return physicalModel.getRadius();
  }

  @Override
  public Vector3 getPosition()
  {
    return visualModel.getPosition();
  }

  @Override
  public float getAngleX()
  {
    return visualModel.getXAngle();
  }

  @Override
  public float getAngleY()
  {
    return visualModel.getYAngle();
  }

  @Override
  public float getAngleZ()
  {
    return visualModel.getZAngle();
  }

  @Override
  public I3DRenderable getRenderable()
  {
    return visualModel;
  }

  @Override
  public ICollidable getCollidable()
  {
    physicalModel.setGlobal(visualModel.getPosition(), visualModel.getXAngle(), visualModel.getYAngle(), visualModel.getZAngle());
    return physicalModel;
  }
}
