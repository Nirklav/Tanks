package com.ThirtyNineEighty.Game.Objects;

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

  private ArrayList<ISubprogram> subprograms;

  public final I3DRenderable[] renderables;
  public final ICollidable collidable;
  public final EngineObjectProperties properties;

  protected EngineObject(EngineObjectDescription description)
  {
    this(generatedNameTemplate + Integer.toString(lastId++), description);
  }

  protected EngineObject(String objectName, EngineObjectDescription description)
  {
    name = objectName;

    properties = new EngineObjectProperties(description);
    position = new Vector3();
    angles = new Vector3();
    subprograms = new ArrayList<>();

    // Build visual models
    int visualModelsCount = description.VisualModels.length;
    renderables = new I3DRenderable[visualModelsCount];
    for (int i = 0; i < visualModelsCount; i++)
    {
      EngineObjectDescription.VisualModel vmInit = description.VisualModels[i];
      renderables[i] = new GLModel(vmInit.ModelName, vmInit.TextureName);
      renderables[i].setGlobal(position, angles);
    }

    // Build physical model
    if (description.PhysicalModel == null)
      collidable = null;
    else
    {
      collidable = new Collidable(description.PhysicalModel.ModelName);
      collidable.setGlobal(position, angles);
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

  public void collide(EngineObject object) { }

  public void rotate(Vector3 value)
  {
    angles.add(value);
    angles.correctAngles();
  }

  public void move(float length)
  {
    position.move(length, angles);
  }

  public void move(float length, Vector3 vector)
  {
    vector.normalize();
    vector.multiply(length);
    position.add(vector);
  }

  public Vector3 getPosition() { return position; }
  public void setPosition(Vector3 value) { position.setFrom(value);
  }

  public Vector3 getAngles() { return angles; }
  public void setAngles(Vector3 value)
  {
    angles.setFrom(value);
    angles.correctAngles();
  }

  public final void setGlobalCollidablePosition()
  {
    if (collidable == null)
      return;
    collidable.setGlobal(position, angles);
  }

  public final void setGlobalRenderablePosition()
  {
    int index = 0;
    for (I3DRenderable vm : renderables)
      setGlobalRenderablePosition(index++, vm);
  }

  protected void setGlobalRenderablePosition(int index, I3DRenderable renderable)
  {
    renderable.setGlobal(position, angles);
  }
}
