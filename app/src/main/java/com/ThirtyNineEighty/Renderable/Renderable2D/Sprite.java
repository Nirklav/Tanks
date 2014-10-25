package com.ThirtyNineEighty.Renderable.Renderable2D;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Renderable.Renderable;
import com.ThirtyNineEighty.Renderable.Shader;
import com.ThirtyNineEighty.Renderable.Shader2D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Sprite implements I2DRenderable
{
  private static final int numIndices = 6;
  private static ShortBuffer indices;

  static
  {
    indices = ByteBuffer.allocateDirect(numIndices * 2)
                        .order(ByteOrder.nativeOrder())
                        .asShortBuffer();

    indices.put(0, (short) 0)
           .put(1, (short) 2)
           .put(2, (short) 1)
           .put(3, (short) 0)
           .put(4, (short) 3)
           .put(5, (short) 2);
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

  public Sprite(String atlasName)
  {
    textureHandle = Renderable.loadTexture(String.format("Textures/%s.png", atlasName), false);

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
    Matrix.multiplyMM(modelOrthoViewMatrix, 0, modelMatrix, 0, orthoViewMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

    Shader2D shader = (Shader2D)Shader.getCurrent();

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, textureHandle);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelOrthoViewMatrix, 0);

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
      vertices = ByteBuffer.allocateDirect(12 * 4)
                           .order(ByteOrder.nativeOrder())
                           .asFloatBuffer();

    vertices.put(0, width / 2)
            .put(1, height / 2)
            .put(2, 0.0f)
            .put(3, width / 2)
            .put(4, -height / 2)
            .put(5, 0.0f)
            .put(6, -width / 2)
            .put(7, -height / 2)
            .put(8, 0.0f)
            .put(9, -width / 2)
            .put(10, height / 2)
            .put(11, 0.0f);
  }

  public void setTextureCoordinates(float x, float y, float width, float height)
  {
    if (textureCoordinates == null)
      textureCoordinates = ByteBuffer.allocateDirect(8 * 4)
                                     .order(ByteOrder.nativeOrder())
                                     .asFloatBuffer();

    textureCoordinates.put(0, x + width)
                      .put(1, y)
                      .put(2, x + width)
                      .put(3, y + height)
                      .put(4, x)
                      .put(5, y + height)
                      .put(6, x)
                      .put(7, y);
  }

  public void setPosition(float x, float y)
  {
    if (position != null)
    {
      position.setFrom(x, y);
      return;
    }

    position = new Vector2(x, y);
  }

  public void setPosition(Vector2 value)
  {
    position = value;
  }

  public Vector2 getPosition()
  {
    return position;
  }

  public void setAngle(float value)
  {
    angle = value;
  }

  public float getAngle()
  {
    return angle;
  }

  public void setZIndex(float value)
  {
    zIndex = value;
  }

  public float getZIndex()
  {
    return zIndex;
  }
}
