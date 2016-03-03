package com.ThirtyNineEighty.Base.Objects;

import com.ThirtyNineEighty.Base.Bindable;
import com.ThirtyNineEighty.Base.Collisions.Collidable;
import com.ThirtyNineEighty.Base.Common.Math.Angle;
import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Objects.Descriptions.*;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.IRenderable;
import com.ThirtyNineEighty.Base.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.Base.Worlds.IWorld;

public abstract class WorldObject<TDescription extends Description, TProperties extends Properties>
  extends Bindable
{
  private static final long serialVersionUID = 1L;

  protected String descriptionName;

  protected transient TDescription description;
  protected TProperties properties;

  protected Vector3 position;
  protected Vector3 angles;

  public Collidable collidable;

  @SuppressWarnings("unchecked")
  protected WorldObject(String descriptionName, TProperties properties)
  {
    this.descriptionName = descriptionName;
    this.properties = properties;
    this.position = new Vector3();
    this.angles = new Vector3();

    TDescription description = (TDescription) GameContext.resources.getDescription(new FileDescriptionSource(descriptionName));
    create(description, properties);
    GameContext.resources.release(description);
  }

  protected void create(TDescription description, TProperties properties)
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
      collidable = new Collidable(physical.modelName);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void initialize()
  {
    description = (TDescription) GameContext.resources.getDescription(new FileDescriptionSource(descriptionName));

    if (collidable != null)
    {
      collidable.initialize();
      collidable.setLocation(position, angles);
    }

    super.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    if (collidable != null)
      collidable.uninitialize();

    GameContext.resources.release(description);
  }

  @Override
  public void enable()
  {
    super.enable();

    if (collidable != null)
      collidable.enable();
  }

  @Override
  public void disable()
  {
    super.disable();

    if (collidable != null)
      collidable.disable();
  }

  public void setCollidableLocation()
  {
    if (collidable != null)
      collidable.setLocation(position, angles);
  }

  public void collide(WorldObject<?, ?> object)
  {

  }

  public void rotate(Vector3 deltaAngles)
  {
    float x = deltaAngles.getX();
    float y = deltaAngles.getY();
    float z = deltaAngles.getZ();

    rotate(x, y, z);
  }

  public void rotate(float dX, float dY, float dZ)
  {
    angles.addToX(dX);
    angles.addToY(dY);
    angles.addToZ(dZ);
    angles.correctAngles();

    GameContext.collisions.addToResolving(this);
  }

  public void rotateTo(Vector3 targetAngles)
  {
    Vector3 corrected = Vector3.getInstance(targetAngles);
    corrected.correctAngles();

    float speed = description.getRotationSpeed() * DeltaTime.get();

    for (int i = 0; i < corrected.getSize(); i++)
    {
      float angle = angles.get(i);
      float targetAngle = corrected.get(i);
      float delta = Math.abs(targetAngle - angle);
      float direction = Angle.getDirection(angle, targetAngle);
      float addedValue = Math.min(delta, speed) * direction;

      angles.add(i, addedValue);
    }

    Vector3.release(corrected);
    angles.correctAngles();
    GameContext.collisions.addToResolving(this);
  }

  public void move()
  {
    move(getSpeed());
  }

  public void moveBack()
  {
    move(- getSpeed());
  }

  private float getSpeed()
  {
    return description.getSpeed() * DeltaTime.get();
  }

  public void move(float length)
  {
    position.move(length, angles);

    normalizePosition();

    GameContext.collisions.addToResolving(this);
  }

  public void move(float length, Vector3 vector)
  {
    vector.normalize();
    vector.multiply(length);
    position.add(vector);

    normalizePosition();

    GameContext.collisions.addToResolving(this);
  }

  public TDescription getDescription()
  {
    return description;
  }

  public TProperties getProperties()
  {
    return properties;
  }

  public Vector3 getPosition()
  {
    return position;
  }

  public void setPosition(Vector3 value)
  {
    position.setFrom(value);

    normalizePosition();
  }

  public Vector3 getAngles()
  {
    return angles;
  }

  public void setAngles(Vector3 value)
  {
    angles.setFrom(value);
    angles.correctAngles();
  }

  private void normalizePosition()
  {
    IWorld world = GameContext.content.getWorld();
    IMap map = world.getMap();
    if (map == null)
      return;

    float mapSize = map.size();
    Vector3 position = getPosition();
    int vecSize = position.getSize();

    for (int i = 0; i < vecSize; i++)
    {
      float value = position.get(i);
      if (Math.abs(value) >= mapSize)
        position.set(i, mapSize * Math.signum(value));
    }
  }
}
