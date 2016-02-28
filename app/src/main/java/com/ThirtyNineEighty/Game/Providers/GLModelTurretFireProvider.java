package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Base.Providers.GLRenderableWorldObjectProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLExplosionParticles;
import com.ThirtyNineEighty.Game.Objects.Tank;

public class GLModelTurretFireProvider
  extends GLRenderableWorldObjectProvider<GLExplosionParticles.Data, Tank>
{
  private boolean setted;
  private Vector3 position;
  private Vector3 angles;

  public GLModelTurretFireProvider(Tank object)
  {
    super(object, GLExplosionParticles.Data.class, null);

    position = Vector.getInstance(3);
    angles = Vector3.getInstance(3);
  }

  @Override
  protected void set(GLExplosionParticles.Data data, VisualDescription description)
  {
    if (setted)
    {
      // Load saved values
      data.position.setFrom(position);
      data.angles.setFrom(angles);
    }
    else
    {
      // Set values from object
      super.set(data, description);

      float angle = object.getRelativeTurretAngle();
      float radius = object.collidable.getRadius();

      data.angles.addToZ(angle);
      data.position.move(radius, data.angles);

      // Save values
      position.setFrom(data.position);
      angles.setFrom(data.angles);
      setted = true;
    }

    data.localAngles.setY(90);
  }
}
