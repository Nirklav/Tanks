package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class PolyLineGeometrySource
  extends GeometrySource
{
  private ArrayList<Vector3> polyLine;

  public PolyLineGeometrySource(ArrayList<Vector3> polyLine)
  {
    super(null, MeshMode.Dynamic);

    this.polyLine = polyLine;
  }

  @Override
  protected LoadResult buildGeometry()
  {
    int size = polyLine.size();
    float[] data = new float[size * 3];

    for (int i = 0; i < size; i++)
    {
      Vector3 vector = polyLine.get(i);
      write(data, i, vector);
    }

    FloatBuffer buffer = loadGeometry(data);
    return new LoadResult(buffer, size, Vector3.zero, Vector3.zero);
  }

  private static void write(float[] data, int num, Vector3 vector)
  {
    data[num    ] = vector.getX();
    data[num + 1] = vector.getY();
    data[num + 2] = vector.getZ();
  }
}
