package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.Objects.Descriptions.RenderableDescription;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.RendererContext;
import com.ThirtyNineEighty.Base.Renderable.Shaders.*;
import com.ThirtyNineEighty.Base.Resources.Entities.*;
import com.ThirtyNineEighty.Base.Resources.Sources.*;
import com.ThirtyNineEighty.Base.GameContext;

public class GLExplosionParticles
  extends GLRenderable<GLRenderable.Data>
{
  private static final long serialVersionUID = 1L;

  private static final RenderableDescription Description = new RenderableDescription("sphere", "particle");

  private transient Texture textureData;
  private transient Geometry geometryData;

  private int count;
  private float time;
  private float life;
  private float angleVariance;

  public GLExplosionParticles(float lifeMs, int count, float angleVariance, IDataProvider<Data> provider)
  {
    super(provider);

    this.count = count;
    this.time = 0;
    this.life = lifeMs;
    this.angleVariance = angleVariance;
  }

  @Override
  public void initialize()
  {
    this.geometryData = GameContext.resources.getGeometry(new RandomParticlesSource(angleVariance));
    this.textureData = GameContext.resources.getTexture(new FileTextureSource(Description.textureName, false));

    super.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    GameContext.resources.release(geometryData);
    GameContext.resources.release(textureData);
  }

  @Override
  public int getShaderId()
  {
    return Shader.ShaderExplosionParticles;
  }

  @Override
  public void draw(RendererContext context, Data data)
  {
    ShaderExplosionParticles shader = (ShaderExplosionParticles) Shader.getCurrent();

    if (isEnabled())
    {
      time += 1000 * DeltaTime.get();
      if (time > life)
        unbind();
    }

    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniform2f(shader.uniformLifeTimeHandle, life, time);

    // bind data buffer
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.getHandle());

    // set offsets to arrays for buffer
    GLES20.glVertexAttribPointer(shader.attributePositionStartHandle, 3, GLES20.GL_FLOAT, false, 40, 0);
    GLES20.glVertexAttribPointer(shader.attributeDirectionVectorHandle, 3, GLES20.GL_FLOAT, false, 40, 12);
    GLES20.glVertexAttribPointer(shader.attributeColorHandle, 4, GLES20.GL_FLOAT, false, 40, 24);

    // enable attribute arrays
    GLES20.glEnableVertexAttribArray(shader.attributePositionStartHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeDirectionVectorHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeColorHandle);

    // validating if debug
    shader.validate();
    geometryData.validate();

    // draw
    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, count);

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionStartHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeDirectionVectorHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeColorHandle);
  }
}
