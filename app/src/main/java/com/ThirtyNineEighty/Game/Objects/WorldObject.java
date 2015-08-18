package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.Collisions.Collidable;
import com.ThirtyNineEighty.Game.Objects.Descriptions.PhysicalDescription;
import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.States.State;
import com.ThirtyNineEighty.Renderable.GL.GLModel;
import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.Bindable;
import com.ThirtyNineEighty.System.GameContext;

public abstract class WorldObject
  extends Bindable
{
  private static int lastId = 1;
  private static final String generatedNameTemplate = "object_";

  protected final String name;
  protected Vector3 position;
  protected Vector3 angles;
  protected Description description;
  protected Properties properties;

  public final ICollidable collidable;

  protected WorldObject(State state)
  {
    this(state.getName(), state.getType(), state.getProperties());

    position.setFrom(state.getPosition());
    angles.setFrom(state.getAngles());
  }

  protected WorldObject(String type, Properties properties)
  {
    this(generatedNameTemplate + Integer.toString(lastId++), type, properties);
  }

  protected WorldObject(String name, String type, Properties properties)
  {
    this.name = name;
    this.properties = properties;
    this.position = new Vector3();
    this.angles = new Vector3();
    this.description = GameContext.resources.getDescription(new FileDescriptionSource(type));

    // Build visual models
    for (VisualDescription current : description.getVisuals())
    {
      ILocationProvider<Vector3> provider = createPositionProvider(current);
      bind(new GLModel(current.modelName, current.textureName, provider));
    }

    // Build physical model
    PhysicalDescription physicalModel = description.getPhysical();
    if (physicalModel == null)
      collidable = null;
    else
    {
      ILocationProvider<Vector3> provider = createPositionProvider(physicalModel);
      collidable = new Collidable(physicalModel.modelName, provider);
    }
  }

  public String getName()
  {
    return name;
  }

  protected State createState()
  {
    return new State();
  }

  public State getState()
  {
    State state = createState();
    state.setAngles(angles);
    state.setPosition(position);
    state.setProperties(properties);
    state.setType(description.getType());
    state.setName(name);
    return state;
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

  protected ILocationProvider<Vector3> createPositionProvider(VisualDescription visual)
  {
    return new LocationProvider(this);
  }

  protected ILocationProvider<Vector3> createPositionProvider(PhysicalDescription physical)
  {
    return new LocationProvider(this);
  }

  protected static class LocationProvider
    implements ILocationProvider<Vector3>
  {
    private WorldObject source;

    public LocationProvider(WorldObject object)
    {
      source = object;
    }

    @Override
    public Location<Vector3> getLocation()
    {
      Location<Vector3> location = new Location<>(3);
      location.position.setFrom(source.position);
      location.angles.setFrom(source.angles);
      return location;
    }
  }
}
