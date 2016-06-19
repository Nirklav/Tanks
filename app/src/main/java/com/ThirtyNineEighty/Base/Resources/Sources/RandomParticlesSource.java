package com.ThirtyNineEighty.Base.Resources.Sources;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

import java.util.Random;

public class RandomParticlesSource
  extends GeometrySource
{
  public static final int size = 10000;
  private static final int partSize = 12;

  private Random random;
  private float[] matrix;
  private float angleVariance;

  public RandomParticlesSource(float angleVariance)
  {
    super(String.format("Random_sphere_particles_%f", angleVariance), MeshMode.Static);

    this.angleVariance = angleVariance;
  }

  @Override
  protected LoadResult buildGeometry()
  {
    random = new Random();
    matrix = new float[16];

    Vector3 cached = new Vector3();
    float[] data = new float[size * partSize];

    for (int i = 0; i < size; i++)
      writePart(data, i, setVector(cached));

    return new LoadResult(loadGeometry(data), size, new Vector3(), new Vector3());
  }

  private Vector3 setVector(Vector3 cached)
  {
    cached.setFrom(0, 0, random.nextFloat() * 0.8f + 0.2f);

    Matrix.setRotateM(matrix, 0, angleVariance
      , random.nextFloat() - 0.5f
      , random.nextFloat() - 0.5f
      , random.nextFloat() - 0.5f);

    Matrix.multiplyMV(cached.getRaw(), 0, matrix, 0, cached.getRaw(), 0);
    return cached;
  }

  private void writePart(float[] data, int index, Vector3 vector)
  {
    final int floatsCount = 3;

    data[index * floatsCount    ] = vector.getX();
    data[index * floatsCount + 1] = vector.getY();
    data[index * floatsCount + 2] = vector.getZ();
  }
}
