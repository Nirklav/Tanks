package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Game.Collisions.*;
import com.ThirtyNineEighty.Game.Objects.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Renderable.GL.GLModel;
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

  public final ICollidable collidable;

  protected WorldObject(State s)
  {
    this(((ObjectState) s).name, ((ObjectState) s).type, ((ObjectState) s).properties);

    ObjectState state = (ObjectState) s;

    position.setFrom(state.position);
    angles.setFrom(state.angles);
  }

  protected WorldObject(String type, Properties properties)
  {
    this(null, type, properties);
  }

  protected WorldObject(String name, String type, Properties properties)
  {
    super(name);

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
