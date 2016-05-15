package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Base.Subprograms.Subprogram;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Map.Descriptions.MapObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Game.TanksContext;

import java.util.ArrayList;

public class RespawnSubprogram
  extends Subprogram
{
  private static final long serialVersionUID = 1L;

  private int count;
  private MapObject object;
  private GameObject<?, ?> respawned;

  public RespawnSubprogram() // TODO: subprogram parameters system
  {
    count = 3;

    object = new MapObject(new Vector3(50, 50, 0), new Vector3());
    object.properties = new GameProperties("armor piercing", new ArrayList<String>());
    object.description = "botTank";
    object.subprograms = new String[] { "bot", "rechargeSubprogram" };
  }

  @Override
  protected void onUpdate()
  {
    // if object still alive
    if (respawned != null && respawned.getHealth() > 0)
      return;

    IWorld world = TanksContext.content.getWorld();

    if (count > 0)
      world.add(respawned = create(object));
    else
      unbind();

    count--;
  }

  private static GameObject<?, ?> create(MapObject mapObj)
  {
    GameObject<?, ?> object = (GameObject<?, ?>) TanksContext.factory.createObject(mapObj.description, mapObj.properties);

    object.setPosition(mapObj.getPosition());
    object.setAngles(mapObj.getAngles());

    for (String subprogramName : mapObj.subprograms)
      object.bind(TanksContext.factory.createSubprogram(subprogramName, object));

    return object;
  }
}
