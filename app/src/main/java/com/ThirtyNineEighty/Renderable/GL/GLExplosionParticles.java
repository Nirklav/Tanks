package com.ThirtyNineEighty.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.Objects.Descriptions.RenderableDescription;
import com.ThirtyNineEighty.Providers.IDataProvider;
import com.ThirtyNineEighty.Renderable.RendererContext;
import com.ThirtyNineEighty.Renderable.Shaders.*;
import com.ThirtyNineEighty.Resources.Entities.*;
import com.ThirtyNineEighty.Resources.Sources.*;
import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.ObjectInputStream;

public class GLExplosionParticles
  extends GLRenderable<GLRenderable.Data>
{
  private static final long serialVersionUID = 1L;

  public static final RenderableDescription Sphere = new RenderableDescription("sphere", "particle");
  public static final RenderableDescription Hemisphere = new RenderableDescription("hemisphere", "particle");

  private transient Texture textureData;
  private transient Geometry geometryData;

  private int count;
  private float time;
  private float life;

  public GLExplosionParticles(float lifeMs, int count, RenderableDescription description, IDataProvider<Data> provider)
  {
    super(description, provider);

    this.count = count;
    this.time = 0;
    this.life = lifeMs;

    this.geometryData = GameContext.resources.getGeometry(getSource(description.modelName));
    this.textureData = GameContext.resources.getTexture(new FileTextureSource(description.textureName, false));
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();

    geometryData = GameContext.resources.getGeometry(getSource(description.modelName));
    textureData = GameContext.resources.getTexture(new FileTextureSource(description.textureName, false));
  }

  private static ISource<Geometry> getSource(String type)
  {
    if (type.equals(Hemisphere.modelName))
      return new HemisphereParticlesSource();
    if (type.equals(Sphere.modelName))
      return null; // TODO:
    return null;
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
      time += 1000 * GameContext.getDelta();
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
