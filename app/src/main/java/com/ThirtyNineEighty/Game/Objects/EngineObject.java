package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.Collisions.Collidable;
import com.ThirtyNineEighty.Game.Objects.Descriptions.PhysicalDescription;
import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.GLModel;
import com.ThirtyNineEighty.System.Bindable;

public abstract class EngineObject
  extends Bindable
{
  private static int lastId = 1;
  private static final String generatedNameTemplate = "object_";

  protected final String name;
  protected Vector3 position;
  protected Vector3 angles;
  protected Description description;
  protected Properties properties;

  public final I3DRenderable[] renderables;
  public final ICollidable collidable;

  protected EngineObject(Description description, Properties properties)
  {
    this(generatedNameTemplate + Integer.toString(lastId++), description, properties);
  }

  protected EngineObject(String name, Description description, Properties properties)
  {
    this.name = name;
    this.description = description;
    this.properties = properties;
    this.position = new Vector3();
    this.angles = new Vector3();

    // Build visual models
    VisualDescription[] visuals = description.getVisuals();
    renderables = new I3DRenderable[visuals.length];
    for (int i = 0; i < visuals.length; i++)
    {
      VisualDescription current = visuals[i];
      renderables[i] = new GLModel(current.modelName, current.textureName);
      renderables[i].setGlobal(position, angles);
    }

    // Build physical model
    PhysicalDescription physicalModel = description.getPhysical();
    if (physicalModel == null)
      collidable = null;
    else
    {
      collidable = new Collidable(physicalModel.modelName);
      collidable.setGlobal(position, angles);
    }
  }

  public String getName()
  {
    return name;
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

  public Description getDescription() { return description; }
  public Properties getProperties() { return properties; }

  public Vector3 getPosition() { return position; }
  public void setPosition(Vector3 value) { position.setFrom(value); }

  public Vector3 getAngles() { return angles; }
  public void setAngles(Vector3 value)
  {
    angles.setFrom(value);
    angles.correctAngles();
  }

  public final void setGlobalCollidablePosition()
  {
    if (collidable != null)
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
