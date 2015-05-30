package com.ThirtyNineEighty.Game.Gameplay.Subprograms;

import com.ThirtyNineEighty.Game.Gameplay.GameObject;
import com.ThirtyNineEighty.Game.Gameplay.Map;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

import java.util.ArrayList;

public class BotSubprogram
  extends Subprogram
{
  private final static float minDistance = 30;
  private final static float maxDistance = 150;
  private final static float stepCompletion = Map.stepSize / 10f;

  private Tank bot;
  private ArrayList<Vector2> path;
  private int currentPathStep;

  public BotSubprogram(GameObject bot)
  {
    super();
    this.bot = (Tank)bot;
  }

  @Override
  protected void onUpdate()
  {
    IWorld world = GameContext.content.getWorld();
    Tank player = (Tank) world.getPlayer();
    Map map = GameContext.mapManager.getMap();

    Vector2 playerPosition = Vector.getInstance(2);
    playerPosition.setFrom(player.getPosition());

    Vector2 botPosition = Vector.getInstance(2);
    botPosition.setFrom(bot.getPosition());

    Vector2 targetVector = playerPosition.getSubtract(botPosition);
    float distance = targetVector.getLength();
    if (distance > maxDistance)
      return;

    if (path == null)
    {
      path = map.findPath(botPosition, playerPosition, null);
      if (path == null)
        return;

      currentPathStep = 0;
    }

    Vector2 nextStepVector = Vector.getInstance(2);
    while (true)
    {
      if (currentPathStep >= path.size())
      {
        Vector.release(path);
        path = null;
        return; // skip update (wait next)
      }

      Vector2 nextStep = path.get(currentPathStep);
      nextStepVector.setFrom(nextStep);
      nextStepVector.subtract(botPosition);

      if (nextStepVector.getLength() <= stepCompletion)
        currentPathStep++;
      else
        break;
    }

    float targetAngle = Vector2.xAxis.getAngle(targetVector); // For turret
    float nextStepAngle = Vector2.xAxis.getAngle(nextStepVector); // For body
    float botAngle = bot.getAngles().getZ();

    // Move/Turn body
    if (distance > minDistance)
    {
      GameContext.collisionManager.rotate(bot, nextStepAngle);

      if (Math.abs(botAngle - nextStepAngle) < 30)
        GameContext.collisionManager.move(bot);
    }

    // Turn turret (or fire)
    if (Math.abs(bot.getTurretAngle() - targetAngle) < 3)
      bot.fire();
    else
      bot.turnTurret(targetAngle);

    Vector.release(playerPosition);
    Vector.release(botPosition);
    Vector.release(targetVector);
    Vector.release(nextStepVector);
  }
}
