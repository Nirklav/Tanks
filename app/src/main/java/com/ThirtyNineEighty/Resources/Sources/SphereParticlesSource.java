package com.ThirtyNineEighty.Resources.Sources;

import android.opengl.GLES20;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Resources.MeshMode;

import java.util.Random;

public class SphereParticlesSource
  extends GeometrySource
{
  private static final int size = 1000;
  private static final int partSize = 44;

  public SphereParticlesSource()
  {
    super("Sphere_particles", MeshMode.Static);
  }

  @Override
  protected LoadResult buildGeometry()
  {
    float[] data = new float[size * partSize];
    Vector3 color = new Vector3(1, 0, 0);
    Random rnd = new Random();

    float[] pointSizes = new float[2];
    GLES20.glGetFloatv(GLES20.GL_ALIASED_POINT_SIZE_RANGE, pointSizes, 0);

    for (int i = 0; i < size; i++)
      writePart(data, i, Vector3.zero, getVector(rnd), color, 1, pointSizes[1]);

    return new LoadResult(loadGeometry(data), size, new Vector3(), new Vector3());
  }

  private Vector3 getVector(Random rnd)
  {
    Vector3 vector = new Vector3(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
    vector.normalize();
    return vector;
  }

  private void writePart(float[] data, int index, Vector3 startPos, Vector3 vector, Vector3 color, float speed, float size)
  {
    data[index     ] = startPos.getX();
    data[index + 1 ] = startPos.getY();
    data[index + 2 ] = startPos.getZ();

    data[index + 3 ] = vector.getX();
    data[index + 4 ] = vector.getY();
    data[index + 5 ] = vector.getZ();

    data[index + 6 ] = color.getX();
    data[index + 7 ] = color.getY();
    data[index + 8 ] = color.getZ();

    data[index + 9 ] = speed;
    data[index + 10] = size;
  }
}
