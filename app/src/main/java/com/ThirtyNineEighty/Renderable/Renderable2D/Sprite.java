package com.ThirtyNineEighty.Renderable.Renderable2D;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix3f;

import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Renderable.Renderable;
import com.ThirtyNineEighty.Renderable.Shader;
import com.ThirtyNineEighty.Renderable.Shader2D;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Sprite implements I2DRenderable
{
  private static float[] bufferData = new float[]
  {
    -1,  1, 0, 0,
    -1, -1, 0, 1,
     1,  1, 1, 0,
    -1, -1, 0, 1,
     1, -1, 1, 1,
     1,  1, 1, 0,
  };

  private float[] modelMatrix;
  private float[] modelViewMatrix;

  private Matrix3f textureMatrix;

  private boolean needBuildMatrix;

  private Vector2 position;
  private float angle;
  private float zIndex;
  private float width;
  private float height;

  private int textureHandle;
  private int bufferHandle;

  private boolean disposed;

  public Sprite(String textureName)
  {
    textureHandle = Renderable.loadTexture(String.format("Textures/%s.png", textureName), false);
    bufferHandle = setBuffer();

    modelMatrix = new float[16];
    modelViewMatrix = new float[16];
    textureMatrix = new Matrix3f();
    position = new Vector2(0f, 0f);

    setSize(1, 1);
    setTextureCoordinates(0f, 0f, 1f, 1f);

    needBuildMatrix = true;
  }

  public void dispose()
  {
    if (disposed)
      return;

    disposed = true;

    GLES20.glDeleteTextures(1, new int[] { textureHandle }, 0);
    GLES20.glDeleteBuffers(1, new int[] { bufferHandle }, 0);
  }

  @Override
  public void finalize() throws Throwable
  {
    super.finalize();

    dispose();
  }

  @Override
  public void draw(float[] viewMatrix)
  {
    tryBuildMatrix();

    // build result matrix
    Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferHandle);

    Shader2D shader = (Shader2D)Shader.getCurrent();

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix3fv(shader.uniformTextureMatrixHandle, 1, false, textureMatrix.getArray(), 0);
    GLES20.glUniformMatrix4fv(shader.uniformModelViewMatrixHandle, 1, false, modelViewMatrix, 0);

    GLES20.glVertexAttribPointer(shader.attributePositionHandle, 2, GLES20.GL_FLOAT, false, 16, 0);
    GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 16, 8);

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
    Matrix.rotateM(modelMatrix, 0, angle, 0.0f, 0.0f, 1);
    Matrix.scaleM(modelMatrix, 0, width / 2, height / 2, 1);

    needBuildMatrix = false;
  }

  private int setBuffer()
  {
    Buffer data = ByteBuffer.allocateDirect(bufferData.length * 4)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer()
                            .put(bufferData)
                            .position(0);

    int[] buffers = new int[1];
    GLES20.glGenBuffers(1, buffers, 0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.capacity() * 4, data, GLES20.GL_STATIC_DRAW);

    return buffers[0];
  }

  public void setSize(float width, float height)
  {
    this.width = width;
    this.height = height;

    needBuildMatrix = true;
  }

  public void setTextureCoordinates(float x, float y, float width, float height)
  {
    textureMatrix.loadIdentity();
    textureMatrix.translate(x, y);
    textureMatrix.scale(width, height);
  }

  public void setPosition(float x, float y)
  {
    position.setFrom(x, y);

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
