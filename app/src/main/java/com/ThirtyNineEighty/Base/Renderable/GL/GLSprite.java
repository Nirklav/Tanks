package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix3f;

import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.RendererContext;
import com.ThirtyNineEighty.Base.Renderable.Shaders.*;
import com.ThirtyNineEighty.Base.Resources.Sources.*;
import com.ThirtyNineEighty.Base.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Base.Resources.Sources.ISource;
import com.ThirtyNineEighty.Base.Resources.Entities.Image;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.Resources.Sources.StaticGeometrySource;
import com.ThirtyNineEighty.Base.Resources.Entities.Texture;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Renderable.Renderable;

import java.nio.FloatBuffer;

// TODO: serializable
public class GLSprite
  extends Renderable
{
  private final static float[] quadMeshData = new float[]
  {
    -0.5f,  0.5f, 0, 0,
    -0.5f, -0.5f, 0, 1,
     0.5f,  0.5f, 1, 0,
    -0.5f, -0.5f, 0, 1,
     0.5f, -0.5f, 1, 1,
     0.5f,  0.5f, 1, 0,
  };

  private final static StaticGeometrySource geometrySource = new StaticGeometrySource("GLSpriteMesh", quadMeshData, quadMeshData.length / 12, MeshMode.Static);

  private float[] modelMatrix;
  private float[] modelViewMatrix;

  private Matrix3f textureMatrix;

  private boolean needBuildMatrix;

  protected Vector2 position;
  protected Vector3 colorCoefficients;
  protected float angle;
  protected float zIndex;
  protected float width;
  protected float height;

  protected transient Texture textureData;
  protected transient Geometry geometryData;

  public GLSprite(String imageName) { this(GameContext.resources.getImage(new FileImageSource(imageName))); }
  public GLSprite(Image image)
  {
    this();

    geometryData = GameContext.resources.getGeometry(geometrySource);
    setImage(image);
    needBuildMatrix = true;
  }

  protected GLSprite(String textureName, ISource<Geometry> geometrySource)
  {
    this();

    textureData = GameContext.resources.getTexture(new FileTextureSource(textureName, false));
    geometryData = GameContext.resources.getGeometry(geometrySource);

    setTextureCoordinates(0, 0, 1, 1);
    needBuildMatrix = true;
  }

  private GLSprite()
  {
    modelMatrix = new float[16];
    modelViewMatrix = new float[16];
    textureMatrix = new Matrix3f();
    position = Vector.getInstance(2);
    colorCoefficients = Vector.getInstance(3, 1, 1, 1);

    setSize(1, 1);
  }

  @Override
  public int getShaderId()
  {
    return Shader.Shader2D;
  }

  @Override
  public IDataProvider getProvider()
  {
    return null;
  }

  @Override
  public void draw(RendererContext context)
  {
    tryBuildMatrix();

    Shader2D shader = (Shader2D)Shader.getCurrent();

    // build result matrix
    Matrix.multiplyMM(modelViewMatrix, 0, context.getOrthoMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix3fv(shader.uniformTextureMatrixHandle, 1, false, textureMatrix.getArray(), 0);
    GLES20.glUniformMatrix4fv(shader.uniformModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
    GLES20.glUniform4f(shader.uniformColorCoefficients, colorCoefficients.getX(), colorCoefficients.getY(), colorCoefficients.getZ(), 1);

    // set buffer or reset if dynamic
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.getHandle());

    switch (geometryData.getMode())
    {
    case Static:
      GLES20.glVertexAttribPointer(shader.attributePositionHandle, 2, GLES20.GL_FLOAT, false, 16, 0);
      GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 16, 8);
      break;

    case Dynamic:
      FloatBuffer data = geometryData.getData();
      data.position(0);
      GLES20.glVertexAttribPointer(shader.attributePositionHandle, 2, GLES20.GL_FLOAT, false, 16, data);
      data.position(2);
      GLES20.glVertexAttribPointer(shader.attributeTexCoordHandle, 2, GLES20.GL_FLOAT, false, 16, data);
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
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometryData.getTrianglesCount() * 3);

    // disable arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeTexCoordHandle);
  }

  private void tryBuildMatrix()
  {
    if (!needBuildMatrix)
      return;

    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.translateM(modelMatrix, 0, position.getX(), position.getY(), zIndex);
    Matrix.rotateM(modelMatrix, 0, angle, 0, 0, 1);
    Matrix.scaleM(modelMatrix, 0, width, height, 1);

    needBuildMatrix = false;
  }

  private void setTextureCoordinates(float[] coords) { setTextureCoordinates(coords[0], coords[1], coords[2], coords[3]); }
  private void setTextureCoordinates(float x, float y, float width, float height)
  {
    textureMatrix.loadIdentity();
    textureMatrix.translate(x, y);
    textureMatrix.scale(width, height);
  }

  public void setImage(Image imageData)
  {
    textureData = imageData.getTexture();
    setTextureCoordinates(imageData.getCoordinates());
  }

  public void setSize(float width, float height)
  {
    this.width = width;
    this.height = height;
    needBuildMatrix = true;
  }

  public void setPosition(float x, float y)
  {
    position.setFrom(x, y);
    needBuildMatrix = true;
  }

  public void setPosition(Vector2 value)
  {
    position.setFrom(value);
    needBuildMatrix = true;
  }

  public Vector2 getPosition()
  {
    return position;
  }

  public void setColorCoefficients(Vector3 coefficients)
  {
    colorCoefficients.setFrom(coefficients);
  }

  public void setAngle(float value)
  {
    angle = value;
    needBuildMatrix = true;
  }

  public float getAngle()
  {
    return angle;
  }

  public float getWidth()
  {
    return width;
  }

  public float getHeight()
  {
    return height;
  }

  public void setZIndex(float value)
  {
    zIndex = value;
    needBuildMatrix = true;
  }

  public float getZIndex()
  {
    return zIndex;
  }
}
