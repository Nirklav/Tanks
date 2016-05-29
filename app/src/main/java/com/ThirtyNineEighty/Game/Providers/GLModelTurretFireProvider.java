package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Objects.Descriptions.PhysicalDescription;
import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Providers.GLRenderableWorldObjectProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLExplosionParticles;
import com.ThirtyNineEighty.Base.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Base.Resources.Sources.FileGeometrySource;
import com.ThirtyNineEighty.Game.Objects.Tank;

public class GLModelTurretFireProvider
  extends GLRenderableWorldObjectProvider<GLExplosionParticles.Data, Tank>
{
  private static final long serialVersionUID = 1L;

  private boolean setted;
  private Vector3 position;
  private Vector3 localPosition;
  private Vector3 angles;
  private Tank tank;

  public GLModelTurretFireProvider(Tank object)
  {
    super(object, GLExplosionParticles.Data.class, null);

    tank = object;
    position = Vector3.getInstance();
    localPosition = Vector3.getInstance();
    angles = Vector3.getInstance();
  }

  @Override
  protected void set(GLExplosionParticles.Data data, VisualDescription description)
  {
    if (setted)
    {
      // Load saved values
      data.position.setFrom(position);
      data.localPosition.setFrom(localPosition);
      data.angles.setFrom(angles);
    }
    else
    {
      PhysicalDescription physical = tank
        .getDescription()
        .getPhysical();
      Geometry geometry = GameContext.resources.getGeometry(new FileGeometrySource(physical.modelName));

      // Set values from object
      float angle = object.getRelativeTurretAngle();
      float radius = object.collidable.getRadius();

      data.angles.addToZ(angle);
      data.position.move(radius, data.angles);
      data.localPosition.setFrom(geometry.getPosition());

      // Save values
      position.setFrom(data.position);
      localPosition.setFrom(data.localPosition);
      angles.setFrom(data.angles);
      setted = true;

      GameContext.resources.release(geometry);
    }

    data.localAngles.setY(90);
  }
}
