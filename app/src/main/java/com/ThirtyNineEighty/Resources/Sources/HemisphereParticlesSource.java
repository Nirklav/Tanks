package com.ThirtyNineEighty.Resources.Sources;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Resources.MeshMode;

import java.util.Random;

public class HemisphereParticlesSource
  extends GeometrySource
{
  private static final int size = 10000;
  private static final int partSize = 12 + 12 + 12;
  private static final float angleVariance = 180f;

  private Random random;
  private float[] matrix;

  public HemisphereParticlesSource()
  {
    super("Sphere_particles", MeshMode.Static);
  }

  @Override
  protected LoadResult buildGeometry()
  {
    random = new Random();
    matrix = new float[16];

    float[] data = new float[size * partSize];
    Vector3 color = new Vector3(1, 0.2f, 0);

    for (int i = 0; i < size; i++)
      writePart(data, i * 9, Vector3.zero, createVector(), color);

    return new LoadResult(loadGeometry(data), size, new Vector3(), new Vector3());
  }

  private Vector3 createVector()
  {
    Vector3 vector = new Vector3(0, 0, random.nextFloat() * 7 + 4);

    Matrix.setRotateEulerM(matrix, 0,
      (random.nextFloat() - 0.5f) * angleVariance,
      (random.nextFloat() - 0.5f) * angleVariance,
      (random.nextFloat() - 0.5f) * angleVariance);

    Matrix.multiplyMV(vector.getRaw(), 0, matrix, 0, vector.getRaw(), 0);
    return vector;
  }

  private void writePart(float[] data, int index, Vector3 position, Vector3 vector, Vector3 color)
  {
    data[index     ] = position.getX();
    data[index + 1 ] = position.getY();
    data[index + 2 ] = position.getZ();

    data[index + 3 ] = vector.getX();
    data[index + 4 ] = vector.getY();
    data[index + 5 ] = vector.getZ();

    data[index + 6 ] = color.getX();
    data[index + 7 ] = color.getY();
    data[index + 8 ] = color.getZ();
  }
}
