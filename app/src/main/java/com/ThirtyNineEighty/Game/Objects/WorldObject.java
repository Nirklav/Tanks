package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Collisions.*;
import com.ThirtyNineEighty.Game.Objects.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;
import com.ThirtyNineEighty.Providers.CollidableTankProvider;
import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Renderable.IRenderable;
import com.ThirtyNineEighty.Resources.Sources.FileDescriptionSource;
import com.ThirtyNineEighty.System.*;

import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class WorldObject<TDescription extends Description, TProperties extends Properties>
  extends Bindable
{
  private static final long serialVersionUID = 1L;

  protected String descriptionName;

  protected transient TDescription description;
  protected TProperties properties;

  protected Vector3 position;
  protected Vector3 angles;

  public ICollidable collidable;

  protected WorldObject(String descriptionName, TProperties properties)
  {
    this.descriptionName = descriptionName;

    this.description = getDescription(descriptionName);
    this.properties = properties;
    this.position = new Vector3();
    this.angles = new Vector3();

    build();
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();

    description = getDescription(descriptionName);
  }

  private TDescription getDescription(String descriptionName)
  {
    return (TDescription) GameContext.resources.getDescription(new FileDescriptionSource(descriptionName));
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

  public void collide(WorldObject<?, ?> object) { }

  public void rotate(float x, float y, float z)
  {
    angles.addToX(x);
    angles.addToY(y);
    angles.addToZ(z);
    angles.correctAngles();

    GameContext.collisions.addToResolving(this);
  }

  public void rotate(Vector3 value)
  {
    angles.add(value);
    angles.correctAngles();

    GameContext.collisions.addToResolving(this);
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
