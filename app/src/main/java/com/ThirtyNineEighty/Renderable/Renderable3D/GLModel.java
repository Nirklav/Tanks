package com.ThirtyNineEighty.Renderable.Renderable3D;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable;
import com.ThirtyNineEighty.Renderable.Shader;
import com.ThirtyNineEighty.Renderable.Shader3D;

public class GLModel
  implements I3DRenderable
{
  private float[] modelProjectionViewMatrix;
  private float[] modelMatrix;

  private Renderable.TextureData textureData;
  private Renderable.GeometryData geometryData;

  private Vector3 position;
  private Vector3 angles;
  private float scale;

  private boolean disposed;

  public GLModel(String geometryName, String textureName)
  {
    modelMatrix = new float[16];
    modelProjectionViewMatrix = new float[16];
    scale = 1f;

    position = Vector3.getInstance(3);
    angles = Vector3.getInstance(3);

    geometryData = Renderable.load3DGeometry(geometryName);
    textureData = Renderable.loadTexture(textureName, true);
  }

  public void dispose()
  {
    if (disposed)
      return;

    disposed = true;
  }

  @Override
  public void finalize() throws Throwable
  {
    super.finalize();

    dispose();
  }

  @Override
  public void draw(float[] projectionViewMatrix, float[] lightPosition)
  {
    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, projectionViewMatrix, 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.handle);

    // bind data buffer
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.handle);

    Shader3D shader = (Shader3D) Shader.getCurrent();

    // send uniform data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixProjectionHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelMatrix, 0);
    GLES20.glUniform3fv(shader.uniformLightVectorHandle, 1, lightPosition, 0);

    // set offsets to arrays for buffer
    GLES20.glVertexAttribPointer(shader.attributePositionHandle, 3, GLES20.GL_FLOAT, false, 32, 0);
    GLES20.glVertexAttribPointer(shader.attributeNormalHandle, 3, GLES20.GL_FLOAT, false, 32, 12);
    GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 32, 24);

    // enable attribute arrays
    GLES20.glEnableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeNormalHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeTexCoordHandle);

    // validating if debug
    shader.validateProgram();

    // draw
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometryData.numOfTriangles * 3);

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeNormalHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeTexCoordHandle);
  }

  public void setGlobal(Vector3 position, Vector3 angles)
  {
    if (position.equals(this.position) && angles.equals(this.angles))
      return;

    this.position.setFrom(position);
    this.angles.setFrom(angles);

    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.translateM(modelMatrix, 0, position.getX(), position.getY(), position.getZ());

    Matrix.rotateM(modelMatrix, 0, angles.getX(), 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(modelMatrix, 0, angles.getY(), 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(modelMatrix, 0, angles.getZ(), 0.0f, 0.0f, 1.0f);

    Matrix.scaleM(modelMatrix, 0, scale, scale, scale);
  }

  public void setScale(float value)
  {
    scale = value;
  }
}
