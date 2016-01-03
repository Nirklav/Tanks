package com.ThirtyNineEighty.Base.Collisions;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Base.Resources.Sources.FilePhGeometrySource;
import com.ThirtyNineEighty.Base.Resources.Entities.PhGeometry;
import com.ThirtyNineEighty.Base.GameContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Collidable
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String name;
  private Vector3 position;
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
    geometryData = GameContext.resources.getPhGeometry(new FilePhGeometrySource(name));
    position = new Vector3();

    normals = new ArrayList<>();
    for (int i = 0; i < geometryData.normals.size(); i++)
      normals.add(new Vector3());

    vertices = new ArrayList<>();
    for (int i = 0; i < geometryData.vertices.size(); i++)
      vertices.add(new Vector3());
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();

    geometryData = GameContext.resources.getPhGeometry(new FilePhGeometrySource(name));
  }

  public Vector3 getPosition() { return position; }

  public ArrayList<Vector3> getVertices() { return vertices; }
  public ArrayList<Vector3> getNormals() { return normals; }

  public float getRadius() { return geometryData.radius; }

  public void setLocation(Vector3 inputPosition, Vector3 inputAngles)
  {
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
