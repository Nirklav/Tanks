package com.ThirtyNineEighty.Game.Gameplay.Subprograms;

import com.ThirtyNineEighty.Game.Gameplay.GameObject;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;

public class BotSubprogram
  extends GameSubprogram
{
  private final static float minDistance = 30;
  private final static float maxDistance = 150;

  private Tank bot;

  public BotSubprogram(GameObject bot)
  {
    super();
    this.bot = (Tank)bot;
  }

  @Override
  protected void onUpdate()
  {
    Tank player = (Tank)world.getPlayer();

    Vector3 playerPosition = player.getPosition();
    Vector3 botPosition = bot.getPosition();

    Vector3 resultVec3 = playerPosition.getSubtract(botPosition);
    Vector2 resultVec2 = Vector.getInstance(2);
    resultVec2.setFrom(resultVec3); // Set without z coordinate

    float distance = resultVec2.getLength();
    if (distance > maxDistance)
      return;

    float targetAngle = Vector2.xAxis.getAngle(resultVec2);
    float botAngle = bot.getAngles().getZ();

    if (distance > minDistance)
    {
      world.collisionManager.rotate(bot, targetAngle);

      if (Math.abs(botAngle - targetAngle) < 30)
        world.collisionManager.move(bot);
    }

    if (Math.abs(bot.getTurretAngle() - targetAngle) < 3)
      bot.fire();
    else
      bot.turnTurret(targetAngle);

    Vector.release(resultVec3);
    Vector.release(resultVec2);
  }
}
