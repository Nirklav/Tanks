package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.DeltaTime;
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

  private transient Texture textureData;
  private transient Geometry geometryData;

  private int count;
  private float time;
  private float life;
  private float angleVariance;
  private float explosionSize;
  private float startPointSize;
  private float endPointSize;
  private Vector3 startColor;
  private Vector3 endColor;
  private boolean unbinded;

  public GLExplosionParticles(IDataProvider<Data> provider)
  {
    super(provider);

    count = 1000;
    time = 0;
    life = 1000;
    angleVariance = 60;
    explosionSize = 8;
    startPointSize = 5;
    endPointSize = 5;
    startColor = new Vector3(1.4f, 0.6f, 0.0f, 1.0f);
    endColor = new Vector3(0.3f, 0.3f, 0.3f, 0.1f);
  }

  public GLExplosionParticles setCount(int value)
  {
    if (value > RandomParticlesSource.size)
      throw new IllegalArgumentException("value must be bigger than 0 and less than RandomParticlesSource.size");

    count = value;
    return this;
  }

  public GLExplosionParticles setLife(float value)
  {
    life = value;
    return this;
  }

  public GLExplosionParticles setAngleVariance(float value)
  {
    angleVariance = value;
    return this;
  }

  public GLExplosionParticles setColor(Vector3 start, Vector3 end)
  {
    startColor.setFrom(start);
    endColor.setFrom(end);
    return this;
  }

  public GLExplosionParticles setExplosionSize(float value)
  {
    explosionSize = value;
    return this;
  }

  public GLExplosionParticles setPointSize(float start, float end)
  {
    startPointSize = start;
    endPointSize = end;
    return this;
  }

  @Override
  public void initialize()
  {
    this.geometryData = GameContext.resources.getGeometry(new RandomParticlesSource(angleVariance));
    this.textureData = GameContext.resources.getTexture(new FileTextureSource(ParticleTexture, false));

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

    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send data to shader
    GLES20.glUniform2f(shader.uniformLifeTimeHandle, life, time);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniform2f(shader.uniformPointSize, startPointSize, endPointSize);
    GLES20.glUniform1f(shader.uniformExplosionSize, explosionSize);
    GLES20.glUniform4fv(shader.uniformStartColor, 1, startColor.getRaw(), 0);
    GLES20.glUniform4fv(shader.uniformEndColor, 1, endColor.getRaw(), 0);

    // bind data buffer
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.getHandle());

    // set offsets to arrays for buffer
    GLES20.glVertexAttribPointer(shader.attributeDirectionVectorHandle, 3, GLES20.GL_FLOAT, false, 12, 0);

    // enable attribute arrays
    GLES20.glEnableVertexAttribArray(shader.attributeDirectionVectorHandle);

    // validating if debug
    shader.validate();
    geometryData.validate();

    // draw
    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, count);

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributeDirectionVectorHandle);

    // update time
    if (isEnabled())
    {
      time += 1000 * DeltaTime.get();

      // auto unbind if explosion is over
      if (time > life && !unbinded)
      {
        unbinded = true;
        unbind();
      }
    }
  }
}
