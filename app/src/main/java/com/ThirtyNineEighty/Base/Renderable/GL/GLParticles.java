package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.RendererContext;
import com.ThirtyNineEighty.Base.Renderable.Shaders.Shader;
import com.ThirtyNineEighty.Base.Renderable.Shaders.ShaderParticles;
import com.ThirtyNineEighty.Base.Resources.Entities.Texture;
import com.ThirtyNineEighty.Base.Resources.Sources.FileTextureSource;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

public class GLParticles
  extends GLRenderable<GLRenderable.Data>
{
  private static final long serialVersionUID = 1L;

  private transient Texture textureData;
  private transient Particles particles;

  private int count;
  private float updateTimeout;
  private float startPointSize;
  private float endPointSize;
  private Vector3 startColor;
  private Vector3 endColor;
  private float size;
  private float angleVariance;
  private float maxTime;
  private boolean unbinded;

  public GLParticles(IDataProvider<Data> provider)
  {
    super(provider);

    count = 200;
    updateTimeout = 15;
    startPointSize = 20;
    endPointSize = 70;
    startColor = new Vector3(1.4f, 0.6f, 0.0f, 1.0f);
    endColor = new Vector3(0.3f, 0.3f, 0.3f, 0.1f);
    size = 8;
    angleVariance = 60;
    maxTime = -1;
  }

  public GLParticles setCount(int value)
  {
    count = value;
    return this;
  }

  public GLParticles setUpdateTimeout(float value)
  {
    updateTimeout = value;
    return this;
  }

  public GLParticles setColor(Vector3 start, Vector3 end)
  {
    startColor.setFrom(start);
    endColor.setFrom(end);
    return this;
  }

  public GLParticles setPointSize(float start, float end)
  {
    startPointSize = start;
    endPointSize = end;
    return this;
  }

  public GLParticles setSize(float value)
  {
    size = value;
    return this;
  }

  public GLParticles setAngleVariance(float value)
  {
    angleVariance = value;
    return this;
  }

  public GLParticles setMaxTime(float value)
  {
    maxTime = value;
    return this;
  }

  @Override
  public void initialize()
  {
    textureData = GameContext.resources.getTexture(new FileTextureSource(ParticleTexture, false));
    particles = new Particles(count, updateTimeout, size, angleVariance);

    super.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    particles = null;
    GameContext.resources.release(textureData);
  }

  @Override
  public int getShaderId()
  {
    return Shader.ShaderParticles;
  }

  @Override
  protected void draw(RendererContext context, Data data)
  {
    if (isEnabled())
      particles.update();

    ShaderParticles shader = (ShaderParticles) Shader.getCurrent();

    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send data to shader
    float life = count * updateTimeout;
    GLES20.glUniform2f(shader.uniformLifeTimeHandle, life, particles.getTime());
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniform2f(shader.uniformPointSize, startPointSize, endPointSize);
    GLES20.glUniform4fv(shader.uniformStartColor, 1, startColor.getRaw(), 0);
    GLES20.glUniform4fv(shader.uniformEndColor, 1, endColor.getRaw(), 0);

    // reset array buffer
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    // set offsets to arrays for buffer
    FloatBuffer buffer = particles.getBuffer();
    buffer.position(0);
    GLES20.glVertexAttribPointer(shader.attributeDirectionVectorHandle, 3, GLES20.GL_FLOAT, false, 16, buffer);
    buffer.position(3);
    GLES20.glVertexAttribPointer(shader.attributeStartTimeHandle, 1, GLES20.GL_FLOAT, false, 16, buffer);

    // enable attribute arrays
    GLES20.glEnableVertexAttribArray(shader.attributeDirectionVectorHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeStartTimeHandle);

    // validating if debug
    shader.validate();

    // draw
    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, particles.getCount());

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributeDirectionVectorHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeStartTimeHandle);

    // auto unbind if max time reached
    if (maxTime > 0 && particles.getTime() > maxTime && !unbinded)
    {
      unbinded = true;
      unbind();
    }
  }
}

class Particles
{
  public static final int normalsSize = 3;
  public static final int startTimeSize = 1;
  public static final int packSize = normalsSize + startTimeSize;

  private final FloatBuffer buffer;
  private final int count;
  private final float timeout;

  private int nextParticleIndex;
  private float time;

  public Particles(int particlesCount, float updateTimeout, float size, float angleVariance)
  {
    count = particlesCount;
    timeout = updateTimeout;
    buffer = ByteBuffer
      .allocateDirect(packSize * 4 * count)
      .order(ByteOrder.nativeOrder())
      .asFloatBuffer();

    build(buffer, size, angleVariance);
  }

  public int getCount()
  {
    return count;
  }

  public float getTime()
  {
    return time;
  }

  public void update()
  {
    float currentTime = time + DeltaTime.get() * 1000;
    float left = currentTime - time + time % timeout;
    float processed = 0;

    while (left >= timeout)
    {
      left -= timeout;
      processed += timeout;

      int index = nextParticleIndex++;
      if (index >= count)
      {
        nextParticleIndex = 1;
        index = 0;
      }

      buffer.put(index * packSize + normalsSize, time + processed);
    }

    time = currentTime;
  }

  public FloatBuffer getBuffer()
  {
    return buffer;
  }

  private static void build(FloatBuffer buffer, float size, float angleVariance)
  {
    int limit = buffer.limit() / packSize;
    float[] matrix = new float[16];
    Random rnd = new Random();
    Vector3 vec = new Vector3();

    buffer.position(0);
    for (int i = 0; i < limit; i++)
    {
      vec.setFrom(0, 0, rnd.nextFloat() * 0.3f + 0.7f);

      Matrix.setRotateM(matrix, 0, angleVariance
        , rnd.nextFloat() - 0.5f
        , rnd.nextFloat() - 0.5f
        , rnd.nextFloat() - 0.5f);

      Matrix.multiplyMV(vec.getRaw(), 0, matrix, 0, vec.getRaw(), 0);

      buffer.put(size * vec.getX());
      buffer.put(size * vec.getY());
      buffer.put(size * vec.getZ());
      buffer.put(0);
    }
  }
}
