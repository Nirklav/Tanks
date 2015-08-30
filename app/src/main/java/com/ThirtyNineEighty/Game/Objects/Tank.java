package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Common.*;
import com.ThirtyNineEighty.Game.Objects.Descriptions.*;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Subprograms.RechargeSubprogram;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Common.Math.*;
import com.ThirtyNineEighty.System.*;

public class Tank
  extends GameObject
{
  private float turretAngle;
  private float rechargeProgress;

  public Tank(State s)
  {
    super(s);

    TankState state = (TankState) s;
    turretAngle = state.turretAngle;
    rechargeProgress = state.rechargeProgress;
  }

  public Tank(String type) { this(type, new GameProperties()); }
  public Tank(String type, GameProperties properties)
  {
    super(type, properties);
  }

  @Override
  public void initialize()
  {
    bind(new RechargeSubprogram(this));

    super.initialize();
  }

  @Override
  protected State createState()
  {
    return new TankState();
  }

  @Override
  public State getState()
  {
    TankState state = (TankState) super.getState();
    state.turretAngle = turretAngle;
    state.rechargeProgress = rechargeProgress;
    return state;
  }

  public void fire()
  {
    if (rechargeProgress < GameDescription.maxRechargeLevel)
      return;

    IWorld world = GameContext.content.getWorld();
    rechargeProgress = 0;

    GameProperties properties = getProperties();
    Bullet bullet = new Bullet(properties.getBullet());

    Vector3 bulletAngles = Vector.getInstance(3, angles);
    bulletAngles.addToZ(turretAngle);
    bullet.setAngles(bulletAngles);

    bullet.setPosition(position);
    bullet.move(bullet.collidable.getRadius() + collidable.getRadius());

    world.add(bullet);

    Vector.release(bulletAngles);
  }

  public float getTurretAngle()
  {
    return Angle.correct(turretAngle + angles.getZ());
  }

  public void addTurretAngle(float delta)
  {
    turretAngle += delta;
  }

  public void turnTurret(float targetAngle)
  {
    GameDescription description = getDescription();
    turretAngle += Angle.getDirection(getTurretAngle(), targetAngle) * description.getTurretRotationSpeed() * GameContext.getDelta();
  }

  public float getRechargeProgress()
  {
    return rechargeProgress;
  }

  public void setRechargeProgress(float value)
  {
    rechargeProgress = value;
  }

  @Override
  protected ILocationProvider<Vector3> createPositionProvider(VisualDescription visual)
  {
    if (visual.id == 1)
      return new LocationProvider(this);
    return super.createPositionProvider(visual);
  }

  private static class LocationProvider
    implements ILocationProvider<Vector3>
  {
    private Tank tank;

    public LocationProvider(Tank tank)
    {
      this.tank = tank;
    }

    @Override
    public Location<Vector3> getLocation()
    {
      Location<Vector3> location = new Location<>(3);
      location.position.setFrom(tank.position);
      location.angles.setFrom(tank.angles);
      location.localAngles.setFrom(0, 0, tank.turretAngle);
      return location;
    }
  }

  protected static class TankState
    extends GameState
  {
    private static final long serialVersionUID = 1L;

    protected float turretAngle;
    protected float rechargeProgress;
  }
}