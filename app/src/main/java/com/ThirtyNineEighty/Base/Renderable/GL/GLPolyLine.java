package com.ThirtyNineEighty.Base.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.GameContext;
import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.RendererContext;
import com.ThirtyNineEighty.Base.Renderable.Shaders.Shader;
import com.ThirtyNineEighty.Base.Renderable.Shaders.ShaderPolyLine;
import com.ThirtyNineEighty.Base.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Base.Resources.Sources.PolyLineGeometrySource;

import java.util.ArrayList;

public class GLPolyLine
  extends GLRenderable<GLPolyLine.Data>
{
  private static final long serialVersionUID = 1L;

  public GLPolyLine(IDataProvider<GLPolyLine.Data> provider)
  {
    super(provider);
  }

  @Override
  public int getShaderId()
  {
    return Shader.ShaderPolyLine;
  }

  @Override
  protected void draw(RendererContext context, Data data)
  {
    ShaderPolyLine shader = (ShaderPolyLine) Shader.getCurrent();
    Geometry geometryData = GameContext.resources.getGeometry(new PolyLineGeometrySource(data.points));

    // build PVM matrix
    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    GLES20.glUniform4fv(shader.uniformColorHandle, 1,  data.color.getRaw(), 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixProjectionHandle, 1, false, modelProjectionViewMatrix, 0);

    GLES20.glVertexAttribPointer(shader.attributePositionHandle, 3, GLES20.GL_FLOAT, false, 12, 0);

    GLES20.glEnableVertexAttribArray(shader.attributePositionHandle);

    // validating if debug
    shader.validate();
    geometryData.validate();

    // draw
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, geometryData.getPointsCount());

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionHandle);

    GameContext.resources.release(geometryData);
  }

  public static class Data
    extends GLRenderable.Data
  {
    private static final long serialVersionUID = 1L;

    public ArrayList<Vector3> points = new ArrayList<>();
    public Vector3 color = new Vector3();
    public float lineSize = 1;
  }
}
