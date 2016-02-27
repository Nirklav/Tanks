package com.ThirtyNineEighty.Base.Resources.Sources;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

import java.util.Random;

public class RandomParticlesSource
  extends GeometrySource
{
  private static final int size = 10000;
  private static final int partSize = 12 + 12 + 16;
  private static final int defaultAngleVariance = 120;

  private Random random;
  private float[] matrix;
  private float angleVariance;

  public RandomParticlesSource()
  {
    this(defaultAngleVariance);
  }

  public RandomParticlesSource(float angleVariance)
  {
    super(String.format("Sphere_particles_%s", angleVariance), MeshMode.Static);

    this.angleVariance = angleVariance;
  }

  @Override
  protected LoadResult buildGeometry()
  {
    random = new Random();
    matrix = new float[16];

    float[] data = new float[size * partSize];

    for (int i = 0; i < size; i++)
      writePart(data, i, Vector3.zero, createVector(), createColor());

    return new LoadResult(loadGeometry(data), size, new Vector3(), new Vector3());
  }

  private Vector3 createVector()
  {
    Vector3 vector = new Vector3(0, 0, random.nextFloat() * 10 + 10);

    Matrix.setRotateEulerM(matrix, 0,
      (random.nextFloat() - 0.5f) * angleVariance,
      (random.nextFloat() - 0.5f) * angleVariance,
      (random.nextFloat() - 0.5f) * angleVariance);

    Matrix.multiplyMV(vector.getRaw(), 0, matrix, 0, vector.getRaw(), 0);
    return vector;
  }

  private Vector3 createColor()
  {
    return new Vector3(1.4f, 0.6f, 0.0f);
  }

  private void writePart(float[] data, int index, Vector3 position, Vector3 vector, Vector3 color)
  {
    data[index * 10     ] = position.getX();
    data[index * 10 + 1 ] = position.getY();
    data[index * 10 + 2 ] = position.getZ();

    data[index * 10 + 3 ] = vector.getX();
    data[index * 10 + 4 ] = vector.getY();
    data[index * 10 + 5 ] = vector.getZ();

    data[index * 10 + 6 ] = color.getX();
    data[index * 10 + 7 ] = color.getY();
    data[index * 10 + 8 ] = color.getZ();
    data[index * 10 + 9 ] = 1.0f;
  }
}
