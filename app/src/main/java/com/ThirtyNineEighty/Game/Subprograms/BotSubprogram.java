package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Game.Collisions.Tracer;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

import java.util.ArrayList;

public class BotSubprogram
  extends Subprogram
{
  private static final long serialVersionUID = 1L;

  private final static float minDistance = 20;
  private final static float maxDistance = 150;
  private final static float maxPathTimeMissedSec = 5;

  private Tank bot;
  private ArrayList<Vector2> path;
  private int currentPathStep;
  private float stepCompletion;
  private float pathTimeMissedSec;

  public BotSubprogram(Tank bot)
  {
    this.bot = bot;
    stepCompletion = bot.collidable.getRadius();
  }

  @Override
  protected void onUpdate()
  {
    if (bot.getHealth() <= 0)
    {
      unbind();
      return;
    }

    IWorld world = GameContext.content.getWorld();
    WorldObject<?, ?> player = world.getPlayer();

    Vector2 playerPosition = Vector.getInstance(2, player.getPosition());
    Vector2 botPosition = Vector.getInstance(2, bot.getPosition());
    Vector2 targetVector = playerPosition.getSubtract(botPosition);

    float distance = targetVector.getLength();
    if (distance < maxDistance)
    {
      tryFire(player, targetVector);

      Vector2 movingVector = getMovingVector(player);
      if (movingVector != null && distance > minDistance)
        move(movingVector);

      Vector.release(movingVector);
    }

    Vector.release(playerPosition);
    Vector.release(botPosition);
    Vector.release(targetVector);
  }

  private void tryFire(WorldObject<?, ?> target, Vector2 targetVector)
  {
    float targetAngle = Vector2.xAxis.getAngle(targetVector);

    if (Math.abs(bot.getTurretAngle() - targetAngle) >= 3)
    {
      bot.turnTurret(targetAngle);
      return;
    }

    if (bot.getRechargeProgress() >= GameDescription.maxRechargeLevel)
    {
      Tracer tracer = new Tracer(bot, target);
      if (!tracer.intersect())
        bot.fire();
    }
  }

  private Vector2 getMovingVector(WorldObject<?, ?> target)
  {
    Map map = GameContext.mapManager.getMap();
    Vector2 botPosition = Vector.getInstance(2, bot.getPosition());

    // Find path
    if (path == null)
    {
      path = map.findPath(bot, target);
      if (path == null)
        return null;

      currentPathStep = 0;
    }

    // Select next step from path
    Vector2 nextStepVector = Vector.getInstance(2);
    while (true)
    {
      // If we arrived, reset path
      if (currentPathStep >= path.size())
      {
        Vector.release(path);
        path = null;
        return null;
      }

      Vector2 nextStep = path.get(currentPathStep);
      nextStepVector.setFrom(nextStep);
      nextStepVector.subtract(botPosition);

      if (nextStepVector.getLength() > stepCompletion)
        return nextStepVector;

      currentPathStep++;
    }
  }

  private void move(Vector2 movingVector)
  {
    Map map = GameContext.mapManager.getMap();
    float movingAngle = Vector2.xAxis.getAngle(movingVector);
    float botAngle = bot.getAngles().getZ();

    GameContext.collisions.rotate(bot, movingAngle);

    if (Math.abs(botAngle - movingAngle) >= 15)
      return;

    // Try move
    if (map.canMove(bot))
    {
      pathTimeMissedSec = 0.0f;
      GameContext.collisions.move(bot);
    }
    else
    {
      // Sum pass time
      pathTimeMissedSec += GameContext.getDelta();

      // Reset path, if we waiting pass too long
      if (pathTimeMissedSec > maxPathTimeMissedSec)
      {
        pathTimeMissedSec = 0.0f;
        Vector.release(path);
        path = null;
      }
    }
  }
}
