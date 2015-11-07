package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.System.*;

public class RechargeSubprogram
  extends Subprogram
{
  private static final long serialVersionUID = 1L;

  private Tank tank;

  public RechargeSubprogram(Tank tank)
  {
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
