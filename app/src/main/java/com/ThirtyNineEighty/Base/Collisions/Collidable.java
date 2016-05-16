package com.ThirtyNineEighty.Base.Collisions;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Base.EngineObject;
import com.ThirtyNineEighty.Base.Resources.Sources.FilePhGeometrySource;
import com.ThirtyNineEighty.Base.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.Base.GameContext;

import java.io.Serializable;
import java.util.ArrayList;

public class Collidable
  extends EngineObject
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String name;
  private Vector3 position;
  private Vector3 lastInputPosition;
  private Vector3 lastInputAngles;

  private float[] verticesMatrix;
  private float[] normalsMatrix;
  private ArrayList<Vector3> normals;
  private ArrayList<Vector3> vertices;

  private transient PhGeometry geometryData;

  public Collidable(String name)
  {
    this.name = name;

    verticesMatrix = new float[16];
    normalsMatrix = new float[16];
    position = new Vector3();
    normals = new ArrayList<>();
    vertices = new ArrayList<>();
  }

  @Override
  public void initialize()
  {
    geometryData = GameContext.resources.getPhGeometry(new FilePhGeometrySource(name));

    lastInputPosition = null;
    lastInputAngles = null;

    fill(normals, geometryData.normals.size());
    fill(vertices, geometryData.vertices.size());

    super.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    clear(normals);
    clear(vertices);

    GameContext.resources.release(geometryData);
  }

  public Vector3 getPosition()
  {
    return position;
  }

  public Vector3 getAngles()
  {
    return lastInputAngles;
  }

  public ArrayList<Vector3> getVertices()
  {
    return vertices;
  }

  public ArrayList<Vector3> getNormals()
  {
    return normals;
  }

  public float getRadius()
  {
    return geometryData.radius;
  }

  public void setLocation(Vector3 inputPosition, Vector3 inputAngles)
  {
    // check position or angles is changed
    if (!isLocationChanged(inputPosition, inputAngles))
      return;

    // matrix
    setMatrix(inputPosition, inputAngles);

    // position
    Matrix.multiplyMV(position.getRaw(), 0, verticesMatrix, 0, Vector3.zero.getRaw(), 0);

    // vertices
    for (int i = 0; i < geometryData.vertices.size(); i++)
    {
      Vector3 local = geometryData.vertices.get(i);
      Vector3 global = vertices.get(i);

      Matrix.multiplyMV(global.getRaw(), 0, verticesMatrix, 0, local.getRaw(), 0);
    }

    // normals
    for (int i = 0; i < geometryData.normals.size(); i++)
    {
      Vector3 local = geometryData.normals.get(i);
      Vector3 global = normals.get(i);

      Matrix.multiplyMV(global.getRaw(), 0, normalsMatrix, 0, local.getRaw(), 0);
      global.normalize();
    }
  }

  private boolean isLocationChanged(Vector3 inputPosition, Vector3 inputAngles)
  {
    if (lastInputPosition == null || lastInputAngles == null)
    {
      lastInputPosition = new Vector3(inputPosition);
      lastInputAngles = new Vector3(inputAngles);
    }
    else
    {
      if (inputPosition.equals(lastInputPosition) && inputAngles.equals(lastInputAngles))
        return false;

      lastInputPosition.setFrom(inputPosition);
      lastInputAngles.setFrom(inputAngles);
    }

    return true;
  }

  private void setMatrix(Vector3 inputPosition, Vector3 inputAngles)
  {
    // Reset vertices matrix
    Matrix.setIdentityM(verticesMatrix, 0);

    // Set global
    Matrix.translateM(verticesMatrix, 0, inputPosition.getX(), inputPosition.getY(), inputPosition.getZ());
    Matrix.rotateM(verticesMatrix, 0, inputAngles.getX(), 1, 0, 0);
    Matrix.rotateM(verticesMatrix, 0, inputAngles.getY(), 0, 1, 0);
    Matrix.rotateM(verticesMatrix, 0, inputAngles.getZ(), 0, 0, 1);

    // Set local
    Matrix.translateM(verticesMatrix, 0, geometryData.position.getX(), geometryData.position.getY(), geometryData.position.getZ());
    Matrix.rotateM(verticesMatrix, 0, geometryData.angles.getX(), 1, 0, 0);
    Matrix.rotateM(verticesMatrix, 0, geometryData.angles.getY(), 0, 1, 0);
    Matrix.rotateM(verticesMatrix, 0, geometryData.angles.getZ(), 0, 0, 1);

    // Normals matrix
    System.arraycopy(verticesMatrix, 0, normalsMatrix, 0, normalsMatrix.length);

    // Reset translate
    normalsMatrix[12] = 0;
    normalsMatrix[13] = 0;
    normalsMatrix[14] = 0;
  }

  private static void fill(ArrayList<Vector3> list, int count)
  {
    for (int i = 0; i < count; i++)
    {
      Vector3 vec = Vector3.getInstance();
      list.add(vec);
    }
  }

  private static void clear(ArrayList<Vector3> list)
  {
    Vector3.release(list);
    list.clear();
  }
}
