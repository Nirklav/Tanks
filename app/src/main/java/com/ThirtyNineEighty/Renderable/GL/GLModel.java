package com.ThirtyNineEighty.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.Descriptions.RenderableDescription;
import com.ThirtyNineEighty.Providers.IDataProvider;
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
  extends GLRenderable<GLModel.Data>
{
  private Texture textureData;
  private Geometry geometryData;

  public GLModel(RenderableDescription description, IDataProvider<Data> provider)
  {
    super(provider);

    this.geometryData = GameContext.resources.getGeometry(new FileGeometrySource(description.modelName));
    this.textureData = GameContext.resources.getTexture(new FileTextureSource(description.textureName, true));
  }

  @Override
  public int getShaderId()
  {
    return Shader.Shader3D;
  }

  @Override
  protected Vector3 getLocalPosition()
  {
    return geometryData.getPosition();
  }

  @Override
  protected Vector3 getLocalAngles()
  {
    return geometryData.getAngles();
  }

  @Override
  public void draw(RendererContext context, Data data)
  {
    Shader3D shader = (Shader3D) Shader.getCurrent();
    Light light = context.getLight();

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
    GLES20.glUniform4f(shader.uniformColorCoefficients, data.RedCoeff, data.GreenCoeff, data.BlueCoeff, 1.0f);

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

  public static class Data
    extends GLRenderable.Data
  {
    public float RedCoeff = 1;
    public float GreenCoeff = 1;
    public float BlueCoeff = 1;
  }
}
