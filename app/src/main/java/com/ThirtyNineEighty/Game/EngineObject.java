package com.ThirtyNineEighty.Game;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.Collisions.Collidable;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.Model3D;

public class EngineObject
  implements IEngineObject
{
  protected Vector3 position;
  protected Vector3 angles;

  private I3DRenderable visualModel;
  private ICollidable physicalModel;

  public EngineObject(String visualModelName, String phModelName, String textureName)
  {
    visualModel = new Model3D(visualModelName, textureName);
    physicalModel = new Collidable(phModelName);

    position = new Vector3();
    angles = new Vector3();

    visualModel.setGlobal(position, angles);
    physicalModel.setGlobal(position, angles);
  }

  @Override
  public void onCollide(IEngineObject object) { }

  @Override
  public void onRemoved() { }

  @Override
  public void onRotates(Vector3 value)
  {
    angles.add(value);
    angles.setFrom(correctAngle(angles.getX()),
                   correctAngle(angles.getY()),
                   correctAngle(angles.getZ()));

    visualModel.setGlobal(position, angles);
  }

  private float correctAngle(float angle)
  {
    if (angle < 0.0f)
      angle += 360.0f;

    if (angle >= 360.0f)
      angle -= 360.0f;

    return angle;
  }

  @Override
  public void onMoved(float length)
  {
    Vector3 vector = new Vector3();
    float[] translateMatrix = new float[16];
    Matrix.setIdentityM(translateMatrix, 0);

    Matrix.rotateM(translateMatrix, 0, angles.getX(), 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(translateMatrix, 0, angles.getY(), 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(translateMatrix, 0, angles.getZ(), 0.0f, 0.0f, 1.0f);

    Matrix.multiplyMV(vector.getRaw(), 0, translateMatrix, 0, Vector3.xAxis.getRaw(), 0);

    onMoved(vector, length);
  }

  @Override
  public void onMoved(Vector3 vector, float length)
  {
    vector.normalize();

    position.addToX(vector.getX() * length);
    position.addToY(vector.getY() * length);
    position.addToZ(vector.getZ() * length);

    visualModel.setGlobal(position, angles);
  }

  @Override
  public Vector3 getPosition() { return position; }

  @Override
  public Vector3 getAngles() { return angles; }

  @Override
  public I3DRenderable getRenderable() { return visualModel; }

  @Override
  public ICollidable getCollidable()
  {
    physicalModel.setGlobal(position, angles);
    return physicalModel;
  }
}
