package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Game.Collisions.ICollidable;
import com.ThirtyNineEighty.Game.Collisions.Collidable;
import com.ThirtyNineEighty.Game.Objects.Descriptions.PhysicalDescription;
import com.ThirtyNineEighty.Game.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Renderable.GLModel;
import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Renderable.IRenderable;
import com.ThirtyNineEighty.Renderable.Renderer;
import com.ThirtyNineEighty.System.Bindable;

import java.util.ArrayList;
import java.util.List;

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
  protected List<IRenderable> renderables;

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
    renderables = new ArrayList<>();
    for (VisualDescription current : description.getVisuals())
    {
      ILocationProvider<Vector3> provider = createPositionProvider(current);
      renderables.add(new GLModel(current.modelName, current.textureName, provider));
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

  @Override
  public void initialize()
  {
    super.initialize();

    for (IRenderable current : renderables)
      Renderer.add(current);
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    for (IRenderable current : renderables)
      Renderer.remove(current);
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
    private EngineObject source;

    public LocationProvider(EngineObject object)
    {
      source = object;
    }

    @Override
    public Location<Vector3> getLocation()
    {
      Location<Vector3> location = new Location<>();
      location.position = Vector.getInstance(3, source.position);
      location.angles = Vector.getInstance(3, source.angles);
      location.localPosition = Vector.getInstance(3, Vector3.zero);
      location.localAngles = Vector.getInstance(3, Vector3.zero);
      return location;
    }
  }
}
