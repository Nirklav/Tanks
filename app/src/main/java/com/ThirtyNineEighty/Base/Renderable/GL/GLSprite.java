package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix3f;

import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.RendererContext;
import com.ThirtyNineEighty.Base.Renderable.Shaders.*;
import com.ThirtyNineEighty.Base.Resources.Entities.Texture;
import com.ThirtyNineEighty.Base.Resources.Sources.*;
import com.ThirtyNineEighty.Base.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Base.Resources.Sources.ISource;
import com.ThirtyNineEighty.Base.Resources.Entities.Image;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.Resources.Sources.StaticGeometrySource;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Renderable.Renderable;

import java.nio.FloatBuffer;

public class GLSprite
  extends Renderable
{
  private static final long serialVersionUID = 1L;

  private final static float[] quadMeshData = new float[]
  {
    -0.5f,  0.5f, 0, 0,
    -0.5f, -0.5f, 0, 1,
     0.5f,  0.5f, 1, 0,
    -0.5f, -0.5f, 0, 1,
     0.5f, -0.5f, 1, 1,
     0.5f,  0.5f, 1, 0,
  };

  private final static ISource<Geometry> geometrySource = new StaticGeometrySource("GLSpriteMesh", quadMeshData, quadMeshData.length / 12, MeshMode.Static);

  private IDataProvider<Data> dataProvider;
  private float[] modelViewMatrix;
  private Matrix3f textureMatrix;

  private transient Geometry geometryData;

  public GLSprite(IDataProvider<Data> provider)
  {
    this.modelViewMatrix = new float[16];
    this.textureMatrix = new Matrix3f();
    this.dataProvider = provider;
  }

  @Override
  public void initialize()
  {
    geometryData = GameContext.resources.getGeometry(geometrySource);

    super.initialize();
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    GameContext.resources.release(geometryData);
  }

  @Override
  public boolean isVisible()
  {
    Data data = dataProvider.get();
    return data.visible;
  }

  @Override
  public int getShaderId()
  {
    return Shader.ShaderSprite;
  }

  @Override
  public IDataProvider getProvider()
  {
    return dataProvider;
  }

  @Override
  public void draw(RendererContext context)
  {
    Data data = dataProvider.get();
    Vector3 color = data.colorCoefficients;
    ShaderSprite shader = (ShaderSprite)Shader.getCurrent();

    // get dynamic resources
    Image image = GameContext.resources.getImage(new FileImageSource(data.imageName));
    Texture textureData = GameContext.resources.getTexture(new FileTextureSource(image.getTextureName(), false));

    // set texture matrix
    setTextureCoordinates(image.getCoordinates());

    // build result matrix
    Matrix.multiplyMM(modelViewMatrix, 0, context.getOrthoMatrix(), 0, data.modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix3fv(shader.uniformTextureMatrixHandle, 1, false, textureMatrix.getArray(), 0);
    GLES20.glUniformMatrix4fv(shader.uniformModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
    GLES20.glUniform4f(shader.uniformColorCoefficients, color.getX(), color.getY(), color.getZ(), 1);

    // set buffer or reset if dynamic
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.getHandle());

    switch (geometryData.getMode())
    {
    case Static:
      GLES20.glVertexAttribPointer(shader.attributePositionHandle, 2, GLES20.GL_FLOAT, false, 16, 0);
      GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 16, 8);
      break;

    case Dynamic:
      FloatBuffer buffer = geometryData.getData();
      buffer.position(0);
      GLES20.glVertexAttribPointer(shader.attributePositionHandle, 2, GLES20.GL_FLOAT, false, 16, buffer);
      buffer.position(2);
      GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 16, buffer);
      break;
    }

    // enable arrays
    GLES20.glEnableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeTexCoordHandle);

    // validating if debug
    shader.validate();
    geometryData.validate();
    textureData.validate();

    // draw
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometryData.getPointsCount());

    // disable arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeTexCoordHandle);

    // release dynamic resources
    GameContext.resources.release(image);
    GameContext.resources.release(textureData);
  }

  private void setTextureCoordinates(float[] coords)
  {
    float x = coords[0];
    float y = coords[1];
    float width = coords[2];
    float height = coords[3];

    textureMatrix.loadIdentity();
    textureMatrix.translate(x, y);
    textureMatrix.scale(width, height);
  }

  public static class Data
    extends Renderable.Data
  {
    private static final long serialVersionUID = 1L;

    public String imageName;
    public Vector3 colorCoefficients;
    public float[] modelMatrix;

    public Data()
    {
      modelMatrix = new float[16];
      colorCoefficients = Vector3.getInstance();
    }
  }
}
