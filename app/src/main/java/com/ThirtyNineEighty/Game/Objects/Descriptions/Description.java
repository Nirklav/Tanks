package com.ThirtyNineEighty.Game.Objects.Descriptions;

import java.io.Serializable;

public class Description
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  protected String name;
  protected String objectType;

  protected VisualDescription[] visuals;
  protected PhysicalDescription physical;

  protected boolean removeOnCollide;
  protected boolean openedOnStart;

  public Description(VisualDescription[] visuals, PhysicalDescription physical)
  {
    this.visuals = visuals;
    this.physical = physical;
  }

  public String getName() { return name; }
  public String getObjectType() { return objectType; }

  public VisualDescription[] getVisuals() { return visuals; }
  public PhysicalDescription getPhysical() { return physical; }

  public boolean removeOnCollide() { return removeOnCollide; }
  public boolean openedOnStart() { return openedOnStart; }
}
