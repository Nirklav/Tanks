package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.Subprograms.Subprogram;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;

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
      rechargeProgress += description.getRechargeSpeed() * DeltaTime.get();

    tank.setRechargeProgress(rechargeProgress);
  }
}
