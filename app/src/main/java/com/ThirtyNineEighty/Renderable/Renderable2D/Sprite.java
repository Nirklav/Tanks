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

public class Sprite implements I2DRenderable
{
  private FloatBuffer vertices;
  private FloatBuffer texCoords;

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
    Matrix.multiplyMM(modelOrthoViewMatrix, 0, orthoViewMatrix, 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

    Shader2D shader = (Shader2D)Shader.getCurrent();

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelOrthoViewMatrix, 0);

    GLES20.glVertexAttribPointer(shader.attributePositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertices);
    GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 8, texCoords);

    // enable arrays
    GLES20.glEnableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeTexCoordHandle);

    // validating if debug
    shader.validateProgram();

    // draw
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

    // disable arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeTexCoordHandle);
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
      vertices = ByteBuffer.allocateDirect(18 * 4)
                           .order(ByteOrder.nativeOrder())
                           .asFloatBuffer();

    vertices.put(width / 2)   // 0
            .put(height / 2)
            .put(0.0f)
            .put(-width / 2)  // 2
            .put(-height / 2)
            .put(0.0f)
            .put(width / 2)   // 1
            .put(-height / 2)
            .put(0.0f)
            .put(width / 2)   // 0
            .put(height / 2)
            .put(0.0f)
            .put(-width / 2)  // 3
            .put(height / 2)
            .put(0.0f)
            .put(-width / 2)  // 2
            .put(-height / 2)
            .put(0.0f);

    vertices.position(0);
  }

  public void setTextureCoordinates(float x, float y, float width, float height)
  {
    if (texCoords == null)
      texCoords = ByteBuffer.allocateDirect(12 * 4)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer();

    texCoords.put(x + width) // 0
             .put(y)
             .put(x)         // 2
             .put(y + height)
             .put(x + width) // 1
             .put(y + height)
             .put(x + width) // 0
             .put(y)
             .put(x)         // 3
             .put(y)
             .put(x)         // 2
             .put(y + height);

    texCoords.position(0);
  }

  public void setPosition(float x, float y)
  {
    if (position != null)
    {
      position.setFrom(x, y);
      needBuildMatrix = true;
      return;
    }

    position = new Vector2(x, y);
    needBuildMatrix = true;
  }

  public void setPosition(Vector2 value)
  {
    position = value;

    needBuildMatrix = true;
  }

  public Vector2 getPosition()
  {
    return position;
  }

  public void setAngle(float value)
  {
    angle = value;

    needBuildMatrix = true;
  }

  public float getAngle()
  {
    return angle;
  }

  public void setZIndex(float value)
  {
    zIndex = value;

    needBuildMatrix = true;
  }

  public float getZIndex()
  {
    return zIndex;
  }
}
