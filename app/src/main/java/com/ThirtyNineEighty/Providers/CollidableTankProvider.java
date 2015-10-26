package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Game.Collisions.Collidable;
import com.ThirtyNineEighty.Game.Objects.Descriptions.PhysicalDescription;
import com.ThirtyNineEighty.Game.Objects.WorldObject;

public class CollidableTankProvider
  extends DataProvider<Collidable.Data, PhysicalDescription>
{
  private final WorldObject object;

  public CollidableTankProvider(WorldObject object, PhysicalDescription description)
  {
    super(new Collidable.Data(), description);
    this.object = object;
  }

  @Override
  public void set(Collidable.Data data, PhysicalDescription description)
  {
    data.angles.setFrom(object.getAngles());
    data.position.setFrom(object.getPosition());
  }
}
