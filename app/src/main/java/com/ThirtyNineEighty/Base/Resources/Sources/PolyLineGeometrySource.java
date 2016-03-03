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
    super(String.format("Hash: %d", getHash(polyLine)), MeshMode.Dynamic);

    this.polyLine = polyLine;
  }

  @Override
  protected LoadResult buildGeometry()
  {
    int size = polyLine.size();
    int pointsSize = (size - 1) * 6; //( 0 --- 0 --- 0 ) size 3, lines 2. line - 6 point.
    float[] data = new float[pointsSize * 3];

    for (int i = 1; i < size; i++)
    {
      Vector3 start = polyLine.get(i - 1);
      Vector3 end = polyLine.get(i);

      write(data, i - 1, start, end);
    }

    FloatBuffer buffer = loadGeometry(data);
    return new LoadResult(buffer, pointsSize, Vector3.zero, Vector3.zero);
  }

  private static void write(float[] data, int num, Vector3 start, Vector3 end)
  {
    Vector3 vector = end.getSubtract(start);

    vector.normalize();
    vector.orthogonal();
    vector.multiply(0.25f);

    Vector3 leftTop = start.getSum(vector);
    Vector3 rightTop = start.getSubtract(vector);
    Vector3 leftBottom = end.getSum(vector);
    Vector3 rightBottom = end.getSubtract(vector);

    data[num * 18     ] = leftTop.getX();
    data[num * 18 + 1 ] = leftTop.getY();
    data[num * 18 + 2 ] = leftTop.getZ();

    data[num * 18 + 3 ] = rightTop.getX();
    data[num * 18 + 4 ] = rightTop.getY();
    data[num * 18 + 5 ] = rightTop.getZ();

    data[num * 18 + 6 ] = rightBottom.getX();
    data[num * 18 + 7 ] = rightBottom.getY();
    data[num * 18 + 8 ] = rightBottom.getZ();

    data[num * 18 + 9 ] = leftTop.getX();
    data[num * 18 + 10] = leftTop.getY();
    data[num * 18 + 11] = leftTop.getZ();

    data[num * 18 + 12] = rightBottom.getX();
    data[num * 18 + 13] = rightBottom.getY();
    data[num * 18 + 14] = rightBottom.getZ();

    data[num * 18 + 15] = leftBottom.getX();
    data[num * 18 + 16] = leftBottom.getY();
    data[num * 18 + 17] = leftBottom.getZ();

    Vector3.release(vector);
    Vector3.release(leftTop);
    Vector3.release(rightTop);
    Vector3.release(leftBottom);
    Vector3.release(rightBottom);
  }

  private static int getHash(ArrayList<Vector3> polyLine)
  {
    int hashCode = 0;
    for (Vector3 vec : polyLine)
      hashCode = (hashCode * 397) ^ vec.hashCode();
    return hashCode;
  }
}
