package com.ThirtyNineEighty.Renderable.Renderable2D;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Renderable.Renderable;
import com.ThirtyNineEighty.Renderable.Shader;
import com.ThirtyNineEighty.Renderable.Shader2D;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Sprite implements I2DRenderable
{
  public static final float left = -960f;
  public static final float right = 960f;

  public static final float top = 640;
  public static final float bottom = -640;

  private static final int numIndices = 6;
  private static ShortBuffer indices;

  static
  {
    ByteBuffer buffer = ByteBuffer.allocateDirect(numIndices * 2);
    buffer.putShort((short)0);
    buffer.putShort((short)2);
    buffer.putShort((short)1);
    buffer.putShort((short)0);
    buffer.putShort((short)3);
    buffer.putShort((short)2);
    indices = buffer.asShortBuffer();
  }

  private FloatBuffer vertices;
  private FloatBuffer textureCoordinates;

  private float[] modelMatrix;
  private float[] modelOrthoViewMatrix;

  private boolean needBuildMatrix;

  private Vector2 position;
  private float angle;
  private float zIndex;

  private int textureHandle;

  public Sprite(String fileName)
  {
    textureHandle = Renderable.loadTexture(fileName, false);

    modelMatrix = new float[16];
    modelOrthoViewMatrix = new float[16];

    setSize(1.0f, 1.0f);
    setTextureCoordinates(0.0f, 0.0f, 1.0f, 1.0f);

    needBuildMatrix = true;
  }

  @Override
  public void finalize() throws Throwable
  {
    super.finalize();

    GLES20.glDeleteTextures(1, new int[] { textureHandle }, 0);
  }

  @Override
  public void draw(float[] orthoViewMatrix)
  {
    tryBuildMatrix();

    // build result matrix
    Matrix.multiplyMM(modelOrthoViewMatrix, 0, orthoViewMatrix, 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

    Shader2D shader = (Shader2D)Shader.getCurrent();

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, textureHandle);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelMatrix, 0);

    GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureCoordinates);
    GLES20.glVertexAttribPointer(shader.attributePositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertices);

    // enable arrays
    GLES20.glEnableVertexAttribArray(shader.attributeTexCoordHandle);
    GLES20.glEnableVertexAttribArray(shader.attributePositionHandle);

    // draw
    GLES20.glDrawElements(GLES20.GL_TRIANGLES, numIndices, GLES20.GL_UNSIGNED_SHORT, indices);

    // disable arrays
    GLES20.glDisableVertexAttribArray(shader.attributeTexCoordHandle);
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);
  }

  private void tryBuildMatrix()
  {
    if (!needBuildMatrix)
      return;

    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.translateM(modelMatrix, 0, position.getX(), position.getY(), zIndex);

    Matrix.rotateM(modelMatrix, 0, angle, 0.0f, 0.0f, 1.0f);

    needBuildMatrix = false;
  }

  public void setSize(float width, float height)
  {
    if (vertices == null)
      vertices = FloatBuffer.allocate(12);

    vertices.put(0, width / 2);
    vertices.put(1, height / 2);
    vertices.put(2, 0.0f);
    vertices.put(3, width / 2);
    vertices.put(4, -height / 2);
    vertices.put(5, 0.0f);
    vertices.put(6, -width / 2);
    vertices.put(7, -height / 2);
    vertices.put(8, 0.0f);
    vertices.put(9, -width / 2);
    vertices.put(10, height / 2);
    vertices.put(11, 0.0f);
  }

  public void setTextureCoordinates(float x, float y, float width, float height)
  {
    if (textureCoordinates == null)
      textureCoordinates = FloatBuffer.allocate(8);

    textureCoordinates.put(0, x + width);
    textureCoordinates.put(1, y);

    textureCoordinates.put(2, x + width);
    textureCoordinates.put(3, y + height);

    textureCoordinates.put(4, x);
    textureCoordinates.put(5, y + height);

    textureCoordinates.put(6, x);
    textureCoordinates.put(7, y);
  }
}
