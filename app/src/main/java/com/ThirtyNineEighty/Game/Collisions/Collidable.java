package com.ThirtyNineEighty.Game.Collisions;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Common.Math.Plane;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Resources.Sources.FilePhGeometrySource;
import com.ThirtyNineEighty.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Collidable
  implements ICollidable
{
  private float[] verticesMatrix;
  private float[] normalsMatrix;
  private ILocationProvider<Vector3> locationProvider;
  private PhGeometry geometryData;
  private Location<Vector3> lastLocation;
  private ArrayList<Vector3> normals;
  private ArrayList<Vector3> vertices;
  private Vector3 position;

  public Collidable(String name, ILocationProvider<Vector3> provider)
  {
    verticesMatrix = new float[16];
    normalsMatrix = new float[16];
    locationProvider = provider;
    geometryData = GameContext.resources.getPhGeometry(new FilePhGeometrySource(name));

    position = new Vector3();

    normals = new ArrayList<>();
    for (int i = 0; i < geometryData.normals.size(); i++)
      normals.add(new Vector3());

    vertices = new ArrayList<>();
    for (int i = 0; i < geometryData.vertices.size(); i++)
      vertices.add(new Vector3());

    normalizeLocation();
  }

  @Override
  public ArrayList<Vector2> getConvexHull(Plane plane)
  {
    ArrayList<Vector2> projection = getDistinctProjection(plane);
    ArrayList<Vector2> convexHull = new ArrayList<>(projection.size());
    if (projection.size() < 2)
      throw new IllegalStateException("projection size less than 2");

    int firstIndex = getFirstPointIndex(projection);
    Vector2 first = projection.remove(firstIndex);
    convexHull.add(first);

    Collections.sort(projection, new AngleComparator(first));

    Vector2 second = projection.remove(0);
    convexHull.add(second);

    Vector2 prevVector = Vector.getInstance(2);
    Vector2 currentVector = Vector.getInstance(2);

    for(Vector2 current : projection)
    {
      Vector2 firstPrevPoint = convexHull.get(convexHull.size() - 1);
      Vector2 secondPrevPoint = convexHull.get(convexHull.size() - 2);

      prevVector.setFrom(firstPrevPoint);
      prevVector.subtract(secondPrevPoint);

      currentVector.setFrom(current);
      currentVector.subtract(firstPrevPoint);

      float angle = prevVector.getAngle(currentVector);
      if (angle >= 180 && angle < 360)
        convexHull.remove(convexHull.size() - 1);

      convexHull.add(current);
    }

    Vector.release(prevVector);
    Vector.release(currentVector);

    return convexHull;
  }

  private static int getFirstPointIndex(ArrayList<Vector2> projection)
  {
    Vector2 minVector = null;
    int minVectorIndex = 0;

    int size = projection.size();
    for (int i = 0; i < size; i++)
    {
      Vector2 current = projection.get(i);

      if (minVector == null)
      {
        minVector = current;
        continue;
      }

      int compareX = Float.compare(current.getX(), minVector.getX());
      if (compareX < 0)
      {
        minVector = current;
        minVectorIndex = i;
      }

      if (compareX == 0)
      {
        int compareY = Float.compare(current.getY(), minVector.getY());
        if (compareY == 0)
          throw new IllegalArgumentException("projection has the same points");

        if (compareY > 0)
        {
          minVector = current;
          minVectorIndex = i;
        }
      }
    }

    return minVectorIndex;
  }

  private ArrayList<Vector2> getDistinctProjection(Plane plane)
  {
    Vector2 vector = Vector.getInstance(2);
    ArrayList<Vector2> result = new ArrayList<>();

    for (Vector3 current : vertices)
    {
      plane.getProjection(vector, current);
      if (!result.contains(vector))
      {
        Vector2 copy = Vector.getInstance(2, vector);
        result.add(copy);
      }
    }

    Vector.release(vector);
    return result;
  }

  private static class AngleComparator implements Comparator<Vector2>
  {
    private Vector2 first;
    private Vector2 left;
    private Vector2 right;

    public AngleComparator(Vector2 first)
    {
      this.first = first;
      left = Vector.getInstance(2);
      right = Vector.getInstance(2);
    }

    @Override
    public int compare(Vector2 lhs, Vector2 rhs)
    {
      // sort angles counterclockwise

      // shift vectors to center
      left.setFrom(lhs);
      left.subtract(first);

      right.setFrom(rhs);
      right.subtract(first);

      // find angles
      float firstAngle = Vector2.xAxis.getAngle(left);
      float secondAngle = Vector2.xAxis.getAngle(right);

      // normalize angles for correct sorting
      // Example: 15, 45, 315, 345 => -45, -15, 15, 45
      if (firstAngle > 90)
        firstAngle -= 360;

      if (secondAngle > 90)
        secondAngle -= 360;

      // if angles equals compare by length
      if (Math.abs(firstAngle - secondAngle) <= Vector.epsilon)
      {
        float leftLength = left.getLength();
        float rightLength = right.getLength();

        // if angle > 0 sort by desc
        if (firstAngle >= 0)
          return Float.compare(rightLength, leftLength);

        return Float.compare(leftLength, rightLength);
      }

      // compare angles
      return Float.compare(firstAngle, secondAngle);
    }
  }

  @Override
  public Vector3 getPosition()
  {
    return position;
  }

  @Override
  public ArrayList<Vector3> getNormals()
  {
    return normals;
  }

  @Override
  public float getRadius()
  {
    return geometryData.radius;
  }

  @Override
  public void normalizeLocation()
  {
    Location<Vector3> loc = locationProvider.getLocation();
    if (lastLocation != null && lastLocation.equals(loc))
      return;

    lastLocation = loc;

    // matrix
    setVerticesMatrix(loc);
    setNormalsMatrix(loc);

    // vertices
    for (int i = 0; i < geometryData.vertices.size(); i++)
    {
      Vector3 local = geometryData.vertices.get(i);
      Vector3 global = vertices.get(i);

      Matrix.multiplyMV(global.getRaw(), 0, verticesMatrix, 0, local.getRaw(), 0);
    }

    // position
    Matrix.multiplyMV(position.getRaw(), 0, verticesMatrix, 0, Vector3.zero.getRaw(), 0);

    // normals
    for (int i = 0; i < geometryData.normals.size(); i++)
    {
      Vector3 local = geometryData.normals.get(i);
      Vector3 global = normals.get(i);

      Matrix.multiplyMV(global.getRaw(), 0, normalsMatrix, 0, local.getRaw(), 0);
      global.normalize();
    }
  }

  private void setVerticesMatrix(Location<Vector3> loc)
  {
    Vector3 resultPos = loc.position.getSum(loc.localPosition); // TODO: fix (can't just add)
    resultPos.add(geometryData.position); // TODO: fix (can't just add)

    Vector3 resultAng = loc.angles.getSum(loc.localAngles); // TODO: fix (can't just add)
    resultAng.add(geometryData.angles); // TODO: fix (can't just add)

    Matrix.setIdentityM(verticesMatrix, 0);
    Matrix.translateM(verticesMatrix, 0, resultPos.getX(), resultPos.getY(), resultPos.getZ());
    Matrix.rotateM(verticesMatrix, 0, resultAng.getX(), 1, 0, 0);
    Matrix.rotateM(verticesMatrix, 0, resultAng.getY(), 0, 1, 0);
    Matrix.rotateM(verticesMatrix, 0, resultAng.getZ(), 0, 0, 1);

    Vector.release(resultPos);
    Vector.release(resultAng);
  }

  private void setNormalsMatrix(Location<Vector3> loc)
  {
    Vector3 resultAng = loc.angles.getSum(loc.localAngles); // TODO: fix (can't just add)
    resultAng.add(geometryData.angles); // TODO: fix (can't just add)

    Matrix.setIdentityM(normalsMatrix, 0);
    Matrix.rotateM(normalsMatrix, 0, resultAng.getX(), 1, 0, 0);
    Matrix.rotateM(normalsMatrix, 0, resultAng.getY(), 0, 1, 0);
    Matrix.rotateM(normalsMatrix, 0, resultAng.getZ(), 0, 0, 1);
    Vector.release(resultAng);
  }
}
