package com.ThirtyNineEighty.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Location;
import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Renderable.Light;
import com.ThirtyNineEighty.Renderable.RendererContext;
import com.ThirtyNineEighty.Renderable.Shaders.Shader;
import com.ThirtyNineEighty.Renderable.Shaders.Shader3D;
import com.ThirtyNineEighty.Resources.Sources.FileGeometrySource;
import com.ThirtyNineEighty.Resources.Sources.FileTextureSource;
import com.ThirtyNineEighty.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Resources.Entities.Texture;
import com.ThirtyNineEighty.System.GameContext;

public class GLModel
  extends GLBase
{
  private Texture textureData;
  private Geometry geometryData;

  public GLModel(String geometryName, String textureName, ILocationProvider<Vector3> provider)
  {
    super(provider);

    geometryData = GameContext.resources.getGeometry(new FileGeometrySource(geometryName));
    textureData = GameContext.resources.getTexture(new FileTextureSource(textureName, true));
  }

  @Override
  public int getShaderId()
  {
    return Shader.Shader3D;
  }

  @Override
  public void draw(RendererContext context)
  {
    Shader3D shader = (Shader3D) Shader.getCurrent();
    Light light = context.getLight();

    setModelMatrix();

    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send uniform data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixProjectionHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelMatrix, 0);
    GLES20.glUniform3fv(shader.uniformLightVectorHandle, 1, light.Position.getRaw(), 0);

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

  @Override
  protected void setModelMatrix()
  {
    Location<Vector3> loc = locationProvider.getLocation();
    if (lastLocation != null && lastLocation.equals(loc))
      return;

    lastLocation = loc;

    // calculate local
    Vector3 resultLocalPosition = loc.localPosition.getSum(geometryData.getPosition());
    Vector3 resultLocalAngles = loc.localAngles.getSum(geometryData.getAngles());

    // reset matrix
    Matrix.setIdentityM(modelMatrix, 0);

    // set global
    Matrix.translateM(modelMatrix, 0, loc.position.getX(), loc.position.getY(), loc.position.getZ());
    Matrix.rotateM(modelMatrix, 0, loc.angles.getX(), 1, 0, 0);
    Matrix.rotateM(modelMatrix, 0, loc.angles.getY(), 0, 1, 0);
    Matrix.rotateM(modelMatrix, 0, loc.angles.getZ(), 0, 0, 1);

    // set local
    Matrix.translateM(modelMatrix, 0, resultLocalPosition.getX(), resultLocalPosition.getY(), resultLocalPosition.getZ());
    Matrix.rotateM(modelMatrix, 0, resultLocalAngles.getX(), 1, 0, 0);
    Matrix.rotateM(modelMatrix, 0, resultLocalAngles.getY(), 0, 1, 0);
    Matrix.rotateM(modelMatrix, 0, resultLocalAngles.getZ(), 0, 0, 1);

    // set scale
    Matrix.scaleM(modelMatrix, 0, scale, scale, scale);

    // release vectors
    Vector.release(resultLocalPosition);
    Vector.release(resultLocalAngles);
  }
}
