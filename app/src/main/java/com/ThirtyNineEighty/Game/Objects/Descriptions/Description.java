package com.ThirtyNineEighty.Game.Objects.Descriptions;

import java.io.Serializable;

public class Description
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  protected VisualDescription[] visuals;
  protected PhysicalDescription physical;
  protected boolean removeOnCollide;

  public Description(VisualDescription[] visuals, PhysicalDescription physical)
  {
    this.visuals = visuals;
    this.physical = physical;
  }

  public VisualDescription[] getVisuals() { return visuals; }
  public PhysicalDescription getPhysical() { return physical; }
  public boolean removeOnCollide() { return removeOnCollide; }
}
