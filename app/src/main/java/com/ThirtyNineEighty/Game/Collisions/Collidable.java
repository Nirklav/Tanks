package com.ThirtyNineEighty.Game.Collisions;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Common.Math.*;
import com.ThirtyNineEighty.Resources.Sources.FilePhGeometrySource;
import com.ThirtyNineEighty.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Collidable
  implements ICollidable,
             Serializable
{
  private static final long serialVersionUID = 1L;

  private String name;
  private IDataProvider<Data> dataProvider;

  private Vector3 position;
  private float[] verticesMatrix;
  private float[] normalsMatrix;
  private ArrayList<Vector3> normals;
  private ArrayList<Vector3> vertices;

  private transient PhGeometry geometryData;

  public Collidable(String name, IDataProvider<Data> provider)
  {
    this.name = name;

    verticesMatrix = new float[16];
    normalsMatrix = new float[16];
    dataProvider = provider;
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

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();

    geometryData = GameContext.resources.getPhGeometry(new FilePhGeometrySource(name));
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
    // matrix
    setMatrix(dataProvider.get());

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

  private void setMatrix(Data data)
  {
    // Reset vertices matrix
    Matrix.setIdentityM(verticesMatrix, 0);

    // Set global
    Matrix.translateM(verticesMatrix, 0, data.position.getX(), data.position.getY(), data.position.getZ());
    Matrix.rotateM(verticesMatrix, 0, data.angles.getX(), 1, 0, 0);
    Matrix.rotateM(verticesMatrix, 0, data.angles.getY(), 0, 1, 0);
    Matrix.rotateM(verticesMatrix, 0, data.angles.getZ(), 0, 0, 1);

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

  public static class Data
    implements Serializable
  {
    private static final long serialVersionUID = 1L;

    public Vector3 position;
    public Vector3 angles;

    public Data()
    {
      position = Vector.getInstance(3);
      angles = Vector.getInstance(3);
    }
  }
}
