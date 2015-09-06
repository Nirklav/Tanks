package com.ThirtyNineEighty.Game.Collisions;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Resources.Sources.FilePhGeometrySource;
import com.ThirtyNineEighty.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;

public class Collidable
  implements ICollidable
{
  private ILocationProvider<Vector3> locationProvider;
  private Location<Vector3> lastLocation;

  private Vector3 position;

  private float[] verticesMatrix;
  private float[] normalsMatrix;

  private PhGeometry geometryData;

  private ArrayList<Vector3> normals;
  private ArrayList<Vector3> vertices;

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
  public Vector3 getPosition()
  {
    return position;
  }

  @Override
  public ArrayList<Vector3> getVertices()
  {
    return vertices;
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
