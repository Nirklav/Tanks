package com.ThirtyNineEighty.Game.Gameplay;

import com.ThirtyNineEighty.Game.EngineObject;
import com.ThirtyNineEighty.Game.EngineObjectDescription;

public class Land extends EngineObject
{
  private static final EngineObjectDescription initializer;

  static
  {
    initializer = new EngineObjectDescription();
    initializer.VisualModels.add(new EngineObjectDescription.VisualModelDescription("land", "land"));
    initializer.PhysicalModel = new EngineObjectDescription.PhysicalModelDescription("land");
  }

  public Land()
  {
    super(initializer);
  }
}
