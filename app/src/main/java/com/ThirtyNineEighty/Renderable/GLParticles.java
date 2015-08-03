package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Resources.Entities.Texture;
import com.ThirtyNineEighty.Resources.Sources.FileTextureSource;
import com.ThirtyNineEighty.Resources.Sources.ISource;
import com.ThirtyNineEighty.Resources.Sources.SphereParticlesSource;
import com.ThirtyNineEighty.System.GameContext;

public class GLParticles
  extends GLBase
{
  public static final int Sphere = 0;
  public static final int Hemisphere = 1;

  private Geometry geometryData;
  private Texture textureData;
  private int count;
  private float time;

  public GLParticles(int type, int count, String textureName, ILocationProvider<Vector3> provider)
  {
    super(provider);

    this.geometryData = GameContext.resources.getGeometry(getSource(type));
    this.textureData = GameContext.resources.getTexture(new FileTextureSource(textureName, true));
    this.count = count;
  }

  private static ISource<Geometry> getSource(int type)
  {
    if (type == Sphere)
      return new SphereParticlesSource();
    return null;
  }

  @Override
  public int getShaderId()
  {
    return Shader.ShaderParticles;
  }

  @Override
  public void draw(RendererContext context)
  {
    ShaderParticles shader = (ShaderParticles) Shader.getCurrent();
    time += GameContext.getDelta() / 1000;

    setModelMatrix();

    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send uniform data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniform1f(shader.uniformTimeHandle, time);

    // bind data buffer
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.getHandle());

    // set offsets to arrays for buffer
    GLES20.glVertexAttribPointer(shader.attributePositionStartHandle, 3, GLES20.GL_FLOAT, false, 44, 0);
    GLES20.glVertexAttribPointer(shader.attributeVectorHandle, 3, GLES20.GL_FLOAT, false, 44, 12);
    GLES20.glVertexAttribPointer(shader.attributeColorHandle, 3, GLES20.GL_FLOAT, false, 44, 24);
    GLES20.glVertexAttribPointer(shader.attributeSpeedSizeHandle, 2, GLES20.GL_FLOAT, false, 44, 36);

    // enable attribute arrays
    GLES20.glEnableVertexAttribArray(shader.attributePositionStartHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeVectorHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeColorHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeSpeedSizeHandle);

    // validating if debug
    shader.validate();
    geometryData.validate();
    textureData.validate();

    // draw
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, count * 3);

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionStartHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeVectorHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeColorHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeSpeedSizeHandle);
  }
}
