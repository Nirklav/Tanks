package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Collisions.*;
import com.ThirtyNineEighty.Game.Objects.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Providers.CollidableTankProvider;
import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.*;

public abstract class WorldObject
  extends Bindable
  implements ISaveble
{
  protected Vector3 position;
  protected Vector3 angles;
  protected Description description;
  protected Properties properties;

  public ICollidable collidable;

  protected WorldObject(ObjectState state)
  {
    this(state.name, state.type, state.properties);

    position.setFrom(state.position);
    angles.setFrom(state.angles);
  }

  protected WorldObject(String name, String type, Properties properties)
  {
    super(name);

    this.properties = properties;
    this.position = new Vector3();
    this.angles = new Vector3();
    this.description = GameContext.resources.getDescription(new FileDescriptionSource(type));

    build();
  }

  private void build()
  {
    // Build visual models
    for (VisualDescription visual : description.getVisuals())
    {
      IDataProvider provider = GameContext.factory.createProvider(visual.providerType, this, visual);
      IRenderable renderable = GameContext.factory.createRenderable(visual.type, visual.renderable, provider);

      bind(renderable);
    }

    // Build physical model
    PhysicalDescription physical = description.getPhysical();
    if (physical != null)
      collidable = new Collidable(physical.modelName, new CollidableTankProvider(this, physical));
  }

  public void collide(WorldObject object) { }

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

  protected State createState()
  {
    return new ObjectState();
  }

  public State getState()
  {
    ObjectState state = (ObjectState)createState();
    state.angles = angles;
    state.position = position;
    state.properties = properties;
    state.name = getName();
    state.type = description.getType();
    return state;
  }

  protected static class ObjectState
    extends State
  {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected String type;
    protected Properties properties;

    protected Vector3 position;
    protected Vector3 angles;
  }
}
