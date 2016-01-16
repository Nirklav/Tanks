package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Objects.Descriptions.RenderableDescription;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.Common.Light;
import com.ThirtyNineEighty.Base.Renderable.RendererContext;
import com.ThirtyNineEighty.Base.Renderable.Shaders.*;
import com.ThirtyNineEighty.Base.Resources.Sources.*;
import com.ThirtyNineEighty.Base.Resources.Entities.*;
import com.ThirtyNineEighty.Base.GameContext;

public class GLModel
  extends GLRenderable<GLModel.Data>
{
  private static final long serialVersionUID = 1L;

  private transient Texture textureData;
  private transient Geometry geometryData;
  private RenderableDescription description;

  public GLModel(RenderableDescription description, IDataProvider<Data> provider)
  {
    super(provider);

    this.description = description;
  }

  @Override
  public void initialize()
  {
    geometryData = GameContext.resources.getGeometry(new FileGeometrySource(description.modelName));
    textureData = GameContext.resources.getTexture(new FileTextureSource(description.textureName, true));

    super.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    GameContext.resources.release(textureData);
    GameContext.resources.release(geometryData);
  }

  @Override
  public int getShaderId()
  {
    return Shader.ShaderModel;
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
    ShaderModel shader = (ShaderModel) Shader.getCurrent();
    Light.Data light = context.getLight();

    // build PVM matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send uniform data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixProjectionHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniformMatrix3fv(shader.uniformNormalMatrix, 1, false, modelNormalMatrix, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelMatrix, 0);
    GLES20.glUniform3fv(shader.uniformLightVectorHandle, 1, light.Position.getRaw(), 0);
    GLES20.glUniform4f(shader.uniformColorCoefficients, data.redCoeff, data.greenCoeff, data.blueCoeff, 1.0f);

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
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometryData.getPointsCount());

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeNormalHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeTexCoordHandle);
  }

  public static class Data
    extends GLRenderable.Data
  {
    private static final long serialVersionUID = 1L;

    public float redCoeff = 1;
    public float greenCoeff = 1;
    public float blueCoeff = 1;
  }
}
