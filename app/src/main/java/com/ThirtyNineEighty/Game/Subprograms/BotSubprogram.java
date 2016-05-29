package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.Map.IMap;
import com.ThirtyNineEighty.Base.Map.IPath;
import com.ThirtyNineEighty.Base.Subprograms.ITask;
import com.ThirtyNineEighty.Base.Subprograms.ITaskAdder;
import com.ThirtyNineEighty.Base.Subprograms.Subprogram;
import com.ThirtyNineEighty.Base.Collisions.Tracer;
import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Subprograms.TaskPriority;
import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Game.TanksContext;

public class BotSubprogram
  extends Subprogram
{
  private static final long serialVersionUID = 1L;

  private final static float minDistance = 20;
  private final static float minPathRebuildDistance = 25;
  private final static float maxDistance = 150;
  private final static float maxPathNotFoundDelay = 5;

  private Tank bot;

  private IPath path;
  private transient ITask pathTask;
  private boolean findPath;
  private float pathNotFoundDelay;

  public BotSubprogram(Tank bot)
  {
    this.bot = bot;
  }

  public IPath getPath()
  {
    return path;
  }

  @Override
  protected void onPrepare(ITaskAdder adder)
  {
    // Not need path
    if (!findPath)
      return;

    // If path find delay
    if (pathNotFoundDelay > 0)
    {
      pathNotFoundDelay -= DeltaTime.get();
      return;
    }

    findPath = false;
    pathTask = adder.schedule(TaskPriority.Low, new Runnable()
    {
      @Override
      public void run()
      {
        // Find path
        IWorld world = TanksContext.content.getWorld();
        IMap map = world.getMap();
        path = map.findPath(bot, world.getPlayer());

        // Path not found, set delay
        if (path == null)
          pathNotFoundDelay = maxPathNotFoundDelay;
      }
    });
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

    Vector2 playerPosition = Vector2.getInstance(player.getPosition());
    Vector2 botPosition = Vector2.getInstance(bot.getPosition());
    Vector2 targetVector = playerPosition.getSubtract(botPosition);

    float distance = targetVector.getLength();
    if (distance < maxDistance)
    {
      tryFire(player, targetVector);

      if (distance > minDistance)
        tryMove(player);
    }

    Vector2.release(playerPosition);
    Vector2.release(botPosition);
    Vector2.release(targetVector);
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
    if (pathTask != null)
    {
      if (!pathTask.isCompleted())
        return;

      pathTask = null;
    }

    if (path == null)
    {
      findPath = true;
      return;
    }

    // If player drove away from path end
    Vector3 pathEnd = path.end();
    Vector3 targetPosition = target.getPosition();
    Vector3 vector = targetPosition.getSubtract(pathEnd);

    if (vector.getLength() > minPathRebuildDistance)
    {
      path.release();
      path = null;
      findPath = true;
      return;
    }

    // Move
    boolean moved = path.moveObject();
    if (!moved)
    {
      path.release();
      path = null;
      findPath = true;
    }
  }
}
