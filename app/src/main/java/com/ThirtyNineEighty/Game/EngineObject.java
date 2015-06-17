package com.ThirtyNineEighty.Game;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.Collisions.Collidable;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.GLModel;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.ISubprogram;

import java.util.ArrayList;

public abstract class EngineObject
{
  private static int lastId = 1;
  private static final String generatedNameTemplate = "object_";

  protected final String name;

  protected Vector3 position;
  protected Vector3 angles;

  private I3DRenderable[] visualModels;
  private ICollidable physicalModel;

  private ArrayList<ISubprogram> subprograms;

  protected EngineObject(EngineObjectDescription description)
  {
    this(generatedNameTemplate + Integer.toString(lastId++), description);
  }

  protected EngineObject(String objectName, EngineObjectDescription description)
  {
    name = objectName;

    position = new Vector3();
    angles = new Vector3();
    subprograms = new ArrayList<>();

    int visualModelsCount = description.VisualModels.length;
    visualModels = new I3DRenderable[visualModelsCount];
    for (int i = 0; i < visualModelsCount; i++)
    {
      EngineObjectDescription.VisualModel vmInit = description.VisualModels[i];
      visualModels[i] = new GLModel(vmInit.ModelName, vmInit.TextureName);
      visualModels[i].setGlobal(position, angles);
    }

    if (description.PhysicalModel != null)
    {
      physicalModel = new Collidable(description.PhysicalModel.ModelName);
      physicalModel.setGlobal(position, angles);
    }
  }

  public String getName()
  {
    return name;
  }

  public void dispose()
  {
    for (ISubprogram subprogram : subprograms)
      GameContext.content.unbindProgram(subprogram);
  }

  public void enable()
  {
    for (ISubprogram subprogram : subprograms)
      subprogram.enable();
  }

  public void disable()
  {
    for (ISubprogram subprogram : subprograms)
      subprogram.disable();
  }

  public void bindProgram(ISubprogram subprogram)
  {
    GameContext.content.bindProgram(subprogram);
    subprograms.add(subprogram);
  }

  public void unbindProgram(ISubprogram subprogram)
  {
    GameContext.content.unbindProgram(subprogram);
    subprograms.remove(subprogram);
  }

  public void onCollide(EngineObject object) { }

  public void onRotates(Vector3 value)
  {
    angles.add(value);
    angles.correctAngles();
  }

  public void onMoved(float length)
  {
    position.move(length, angles);
  }

  public void onMoved( float length, Vector3 vector)
  {
    vector.normalize();
    vector.multiply(length);
    position.add(vector);
  }

  public Vector3 getPosition() { return position; }
  public void setPosition(Vector3 value) { position.setFrom(value); }

  public Vector3 getAngles() { return angles; }
  public void setAngles(Vector3 value)
  {
    angles.setFrom(value);
    angles.correctAngles();
  }


  public final I3DRenderable[] getRenderables() { return visualModels; }
  public final ICollidable getCollidable() { return physicalModel; }

  public final void setGlobalCollidablePosition()
  {
    if (physicalModel == null)
      return;
    physicalModel.setGlobal(position, angles);
  }

  public final void setGlobalRenderablePosition()
  {
    int index = 0;
    for (I3DRenderable vm : visualModels)
      setGlobalRenderablePosition(index++, vm);
  }

  protected void setGlobalRenderablePosition(int index, I3DRenderable renderable)
  {
    renderable.setGlobal(position, angles);
  }
}
