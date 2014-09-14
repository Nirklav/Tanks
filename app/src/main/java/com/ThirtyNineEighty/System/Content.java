package com.ThirtyNineEighty.System;

import java.util.Collection;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ThirtyNineEighty.Game.World;
import com.ThirtyNineEighty.Helpers.Vector2;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.I2DRenderable;
import com.ThirtyNineEighty.Renderable.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Shader;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

public class Content
  implements GLSurfaceView.Renderer
{
  private boolean initialized = false;

  private World world;

  private float width;
  private float height;

  private float[] viewMatrix;
  private float[] projectionMatrix;
  private float[] projectionViewMatrix;
  private float[] lightPosition;

  private Vector<I3DRenderable> renderable3DObjects;
  private Vector<I2DRenderable> renderable2DObjects;

  private MotionEvent.PointerProperties properties;

  public Content()
  {
    lightPosition = new float[] { 0.0f, 0.0f, 12.0f };

    viewMatrix = new float[16];
    projectionMatrix = new float[16];
    projectionViewMatrix = new float[16];
    properties = new MotionEvent.PointerProperties();

    world = new World();

    renderable3DObjects = new Vector<I3DRenderable>();
    renderable2DObjects = new Vector<I2DRenderable>();
  }

  public void Update(MotionEvent event)
  {
    if (!initialized || event == null)
      return;

    world.updatePlayer(event, width, height);
    world.update();
  }

  @Override
  public void onDrawFrame(GL10 egl)
  {
    if (!initialized)
      return;

    DeltaTime.UpdateTime();

    GLES20.glClearColor(0.0f, 0.0f, 0.0f ,1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    I3DRenderable target = world.getCameraTarget();
    Vector3 center = target.getPosition();
    Vector3 eye = new Vector3(target.getPosition());
    eye.addToX(-8.0f * (float)Math.cos(Math.toRadians(target.getZAngle() + 90)));
    eye.addToY(-8.0f * (float)Math.sin(Math.toRadians(target.getZAngle() + 90)));
    eye.addToZ(6);

    Matrix.setLookAtM(viewMatrix, 0, eye.getX(), eye.getY(), eye.getZ(), center.getX(), center.getY(), center.getZ(), 0.0f, 0.0f, 1.0f);
    Matrix.perspectiveM(projectionMatrix, 0, 60.0f, width / height, 0.1f, 40.0f);
    Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

    if (renderable3DObjects.size() != 0)
    {
      Shader.setShader3D();

      for (I3DRenderable renderable : renderable3DObjects)
        renderable.draw(projectionViewMatrix, lightPosition);
    }

    if (renderable2DObjects.size() != 0)
    {
      Shader.setShader2D();

      for (I2DRenderable renderable : renderable2DObjects)
        renderable.draw();
    }
  }
  
  @Override
  public void onSurfaceChanged(GL10 egl, int width, int height)
  {
    egl.glViewport(0, 0, width, height);

    GLES20.glEnable(GLES20.GL_CULL_FACE);
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glDepthFunc(GLES20.GL_LEQUAL);
    GLES20.glEnable(GLES20.GL_ALPHA);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    this.width = width;
    this.height = height;

    world.initialize(null);

    Collection<I3DRenderable> i3DRenderable = world.get3DRenderable();
    Collection<I2DRenderable> i2DRenderable = world.get2DRenderable();

    if (i3DRenderable != null)
      renderable3DObjects.addAll(i3DRenderable);

    if (i2DRenderable != null)
      renderable2DObjects.addAll(i2DRenderable);

    initialized = true;
  }

  @Override
  public void onSurfaceCreated(GL10 egl, EGLConfig config)
  {
    Shader.setShader3D();
  }
}
