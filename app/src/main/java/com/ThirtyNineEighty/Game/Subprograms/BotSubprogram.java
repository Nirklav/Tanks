package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Map.IPath;
import com.ThirtyNineEighty.Base.Subprogram;
import com.ThirtyNineEighty.Base.Collisions.Tracer;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Game.TanksContext;

public class BotSubprogram
  extends Subprogram
{
  private static final long serialVersionUID = 1L;

  private final static float minDistance = 10;
  private final static float minPathRebuildDistance = 50;
  private final static float maxDistance = 150;
  private final static float maxPathTimeMissedSec = 5;

  private Tank bot;
  private IPath path;
  private float pathTimeMissedSec;

  public BotSubprogram(Tank bot)
  {
    this.bot = bot;
  }

  @Override
  protected void onUpdate()
  {
    if (bot.getHealth() <= 0)
    {
      unbind();
      return;
    }

    IWorld world = TanksContext.content.getWorld();
    WorldObject<?, ?> player = world.getPlayer();

    Vector2 playerPosition = Vector.getInstance(2, player.getPosition());
    Vector2 botPosition = Vector.getInstance(2, bot.getPosition());
    Vector2 targetVector = playerPosition.getSubtract(botPosition);

    float distance = targetVector.getLength();
    if (distance < maxDistance)
    {
      tryFire(player, targetVector);
      tryMove(player);
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

  private void tryMove(WorldObject<?, ?> target)
  {
    IWorld world = TanksContext.content.getWorld();
    IMap map = world.getMap();

    // Find path
    if (path == null)
      path = map.findPath(bot, target);

    if (path == null)
      return;

    // If player drove away from path end
    Vector3 pathEnd = path.end();
    Vector3 targetPosition = target.getPosition();
    Vector3 vector = targetPosition.getSubtract(pathEnd);

    if (vector.getLength() > minPathRebuildDistance)
    {
      path.release();
      path = null;
      return;
    }

    // We arrived
    if (path.distance() < minDistance)
      return;

    // Move
    boolean moved = path.moveObject();
    if (!moved)
    {
      // Sum pass time
      pathTimeMissedSec += DeltaTime.get();

      // Reset path, if we waiting pass too long
      if (pathTimeMissedSec > maxPathTimeMissedSec)
      {
        pathTimeMissedSec = 0.0f;
        path.release();
        path = null;
      }
    }
  }
}
