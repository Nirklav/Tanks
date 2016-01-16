package com.ThirtyNineEighty.Base.Objects;

import com.ThirtyNineEighty.Base.Bindable;
import com.ThirtyNineEighty.Base.Collisions.Collidable;
import com.ThirtyNineEighty.Base.Common.Math.Angle;
import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Objects.Descriptions.*;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Properties.Properties;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.IRenderable;
import com.ThirtyNineEighty.Base.Resources.Sources.FileDescriptionSource;

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

    description = (TDescription) GameContext.resources.getDescription(new FileDescriptionSource(descriptionName));

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
    {
      collidable = new Collidable(physical.modelName);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void initialize()
  {
    if (description == null)
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
    rotate(deltaAngles.getX(), deltaAngles.getY(), deltaAngles.getZ());
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
    Vector3 corrected = Vector.getInstance(3, targetAngles);
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

    Vector.release(corrected);
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

    GameContext.collisions.addToResolving(this);
  }

  public void move(float length, Vector3 vector)
  {
    vector.normalize();
    vector.multiply(length);
    position.add(vector);

    GameContext.collisions.addToResolving(this);
  }

  public TDescription getDescription() { return description; }
  public TProperties getProperties() { return properties; }

  public Vector3 getPosition() { return position; }
  public void setPosition(Vector3 value) { position.setFrom(value); }

  public Vector3 getAngles() { return angles; }
  public void setAngles(Vector3 value)
  {
    angles.setFrom(value);
    angles.correctAngles();
  }
}
