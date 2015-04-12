package com.ThirtyNineEighty.Game;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.Collisions.Collidable;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.GLModel;

public abstract class EngineObject
  implements IEngineObject
{
  protected Vector3 position;
  protected Vector3 angles;

  private I3DRenderable[] visualModels;
  private ICollidable physicalModel;

  protected EngineObject(EngineObjectDescription initializer)
  {
    position = new Vector3();
    angles = new Vector3();

    int visualModelsCount = initializer.VisualModels.length;
    visualModels = new I3DRenderable[visualModelsCount];
    for (int i = 0; i < visualModelsCount; i++)
    {
      EngineObjectDescription.VisualModelDescription vmInit = initializer.VisualModels[i];
      visualModels[i] = new GLModel(vmInit.ModelName, vmInit.TextureName);
      visualModels[i].setGlobal(position, angles);
    }

    if (initializer.PhysicalModel != null)
    {
      physicalModel = new Collidable(initializer.PhysicalModel.ModelName);
      physicalModel.setGlobal(position, angles);
    }
  }

  @Override
  public void onCollide(IEngineObject object) { }

  @Override
  public void onRemoved() { }

  @Override
  public void onRotates(Vector3 value)
  {
    angles.add(value);
    angles.correctAngles();
  }

  @Override
  public void onMoved(float length)
  {
    position.move(length, angles);
  }

  @Override
  public void onMoved( float length, Vector3 vector)
  {
    vector.normalize();
    vector.multiply(length);
    position.add(vector);
  }

  @Override
  public Vector3 getPosition() { return position; }

  @Override
  public Vector3 getAngles() { return angles; }

  @Override
  public void setAngles(Vector3 value)
  {
    angles.setFrom(value);
    angles.correctAngles();
  }

  @Override
  public void setPosition(Vector3 value) { position.setFrom(value); }

  @Override
  public final I3DRenderable[] getRenderables() { return visualModels; }

  @Override
  public final ICollidable getCollidable() { return physicalModel; }

  @Override
  public void setGlobalCollidablePosition()
  {
    if (physicalModel == null)
      return;
    physicalModel.setGlobal(position, angles);
  }

  @Override
  public void setGlobalRenderablePosition()
  {
    int index = 0;
    for(I3DRenderable vm : visualModels)
      setGlobalRenderablePosition(index++, vm);
  }

  protected void setGlobalRenderablePosition(int index, I3DRenderable renderable)
  {
    renderable.setGlobal(position, angles);
  }
}
