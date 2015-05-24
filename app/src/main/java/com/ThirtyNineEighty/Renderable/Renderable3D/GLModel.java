package com.ThirtyNineEighty.Renderable.Renderable3D;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Resources.Sources.FileGeometrySource;
import com.ThirtyNineEighty.Resources.Sources.FileTextureSource;
import com.ThirtyNineEighty.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Renderable.Shader;
import com.ThirtyNineEighty.Renderable.Shader3D;
import com.ThirtyNineEighty.Resources.Entities.Texture;
import com.ThirtyNineEighty.System.GameContext;

public class GLModel
  implements I3DRenderable
{
  private float[] modelProjectionViewMatrix;
  private float[] modelMatrix;
  private boolean matrixInitialized;

  private Texture textureData;
  private Geometry geometryData;

  private Vector3 globalPosition;
  private Vector3 globalAngles;

  private Vector3 localPosition;
  private Vector3 localAngles;

  private float scale;

  public GLModel(String geometryName, String textureName)
  {
    modelMatrix = new float[16];
    modelProjectionViewMatrix = new float[16];
    scale = 1f;

    globalPosition = Vector.getInstance(3);
    globalAngles = Vector.getInstance(3);

    localPosition = Vector.getInstance(3);
    localAngles = Vector.getInstance(3);

    geometryData = GameContext.resources.getGeometry(new FileGeometrySource(geometryName));
    textureData = GameContext.resources.getTexture(new FileTextureSource(textureName, true));
  }

  @Override
  public void draw(float[] projectionViewMatrix, Vector3 lightPosition)
  {
    Shader3D shader = (Shader3D) Shader.getCurrent();

    if (!matrixInitialized)
    {
      setModelMatrix();
      matrixInitialized = true;
    }

    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, projectionViewMatrix, 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send uniform data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixProjectionHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelMatrix, 0);
    GLES20.glUniform3fv(shader.uniformLightVectorHandle, 1, lightPosition.getRaw(), 0);

    // bind data buffer
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.getHandle());

    // set offsets to arrays for buffer
    GLES20.glVertexAttribPointer(shader.attributePositionHandle, 3, GLES20.GL_FLOAT, false, 32, 0);
    GLES20.glVertexAttribPointer(shader.attributeNormalHandle, 3, GLES20.GL_FLOAT, false, 32, 12);
    GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 32, 24);

    // enable attribute arrays
    GLES20.glEnableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeNormalHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeTexCoordHandle);

    // validating if debug
    shader.validate();
    geometryData.validate();
    textureData.validate();

    // draw
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometryData.getTrianglesCount() * 3);

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeNormalHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeTexCoordHandle);
  }

  public void setGlobal(Vector3 position, Vector3 angles)
  {
    if (globalPosition.equals(position) && globalAngles.equals(angles))
      return;

    globalPosition.setFrom(position);
    globalAngles.setFrom(angles);
    setModelMatrix();
  }

  public void setLocal(Vector3 position, Vector3 angles)
  {
    if (localPosition.equals(position) && localAngles.equals(angles))
      return;

    localPosition.setFrom(position);
    localAngles.setFrom(angles);
    setModelMatrix();
  }

  private void setModelMatrix()
  {
    // calculate local
    Vector3 resultLocalPosition = Vector.getInstance(3, localPosition);
    resultLocalPosition.add(geometryData.getPosition());

    Vector3 resultLocalAngles = Vector.getInstance(3, localAngles);
    resultLocalAngles.add(geometryData.getAngles());

    // reset matrix
    Matrix.setIdentityM(modelMatrix, 0);

    // set global
    Matrix.translateM(modelMatrix, 0, globalPosition.getX(), globalPosition.getY(), globalPosition.getZ());

    Matrix.rotateM(modelMatrix, 0, globalAngles.getX(), 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(modelMatrix, 0, globalAngles.getY(), 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(modelMatrix, 0, globalAngles.getZ(), 0.0f, 0.0f, 1.0f);

    // set local
    Matrix.translateM(modelMatrix, 0, resultLocalPosition.getX(), resultLocalPosition.getY(), resultLocalPosition.getZ());

    Matrix.rotateM(modelMatrix, 0, resultLocalAngles.getX(), 1.0f, 0.0f, 0.0f);
    Matrix.rotateM(modelMatrix, 0, resultLocalAngles.getY(), 0.0f, 1.0f, 0.0f);
    Matrix.rotateM(modelMatrix, 0, resultLocalAngles.getZ(), 0.0f, 0.0f, 1.0f);

    // set scale
    Matrix.scaleM(modelMatrix, 0, scale, scale, scale);

    // release vectors
    Vector.release(resultLocalPosition);
    Vector.release(resultLocalAngles);
  }

  public void setScale(float value)
  {
    scale = value;
  }
}
