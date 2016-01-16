package com.ThirtyNineEighty.Base.Objects.Descriptions;

import com.ThirtyNineEighty.Base.Resources.Entities.Resource;

public class Description
  extends Resource
{
  private static final long serialVersionUID = 1L;

  protected String objectType;

  protected VisualDescription[] visuals;
  protected PhysicalDescription physical;

  protected float speed; // meters per seconds
  protected float rotationSpeed; // degree per seconds
  protected boolean removeOnCollide;
  protected boolean openedOnStart;

  public Description(String name)
  {
    super(name);
  }

  public String getObjectType() { return objectType; }

  public VisualDescription[] getVisuals() { return visuals; }
  public PhysicalDescription getPhysical() { return physical; }

  public float getSpeed() { return speed; }
  public float getRotationSpeed() { return rotationSpeed; }
  public boolean removeOnCollide() { return removeOnCollide; }
  public boolean openedOnStart() { return openedOnStart; }
}
