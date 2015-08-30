package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.System.*;

public class RechargeSubprogram
  extends Subprogram
{
  private Tank tank;

  public RechargeSubprogram(State s)
  {
    super(s);
  }

  public RechargeSubprogram(Tank tank)
  {
    super(String.format("RechargeSubprogram_%s", tank.getName()));

    this.tank = tank;
  }

  @Override
  protected void onUpdate()
  {
    GameDescription description = tank.getDescription();
    float rechargeProgress = tank.getRechargeProgress();

    if (rechargeProgress >= GameDescription.maxRechargeLevel)
      rechargeProgress = GameDescription.maxRechargeLevel;
    else
      rechargeProgress += description.getRechargeSpeed() * GameContext.getDelta();

    tank.setRechargeProgress(rechargeProgress);
  }
}
