package com.ThirtyNineEighty.System;

import java.util.ArrayList;
import java.util.Collection;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ThirtyNineEighty.Game.World;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Shader;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.view.View;

public class Content
  implements GLSurfaceView.Renderer,
             View.OnTouchListener
{
  private boolean initialized = false;

  private World world;

  private float[] viewMatrix;
  private float[] projectionMatrix;
  private float[] projectionViewMatrix;
  private float[] lightPosition;

  private float[] orthoMatrix;

  private ArrayList<I3DRenderable> renderable3DObjects;
  private ArrayList<I2DRenderable> renderable2DObjects;

  public Content()
  {
    lightPosition = new float[] { 0.0f, 0.0f, 12.0f };

    viewMatrix = new float[16];
    projectionMatrix = new float[16];
    projectionViewMatrix = new float[16];

    orthoMatrix = new float[16];

    world = new World();

    renderable3DObjects = new ArrayList<I3DRenderable>();
    renderable2DObjects = new ArrayList<I2DRenderable>();
  }

  @Override
  public boolean onTouch(View v, MotionEvent event)
  {
    return initialized && world.processEvent(event);
  }

  public void onUpdate()
  {
    if (!initialized)
      return;

    world.update();
  }

  @Override
  public void onDrawFrame(GL10 gl)
  {
    if (!initialized)
      return;

    GLES20.glClearColor(0.0f, 0.0f, 0.0f ,1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    if (renderable3DObjects.size() != 0)
    {
      I3DRenderable target = world.getCameraTarget();
      Vector3 center = target.getPosition();
      Vector3 eye = new Vector3(target.getPosition());
      eye.addToX(-8.0f * (float)Math.cos(Math.toRadians(target.getZAngle())));
      eye.addToY(-8.0f * (float)Math.sin(Math.toRadians(target.getZAngle())));
      eye.addToZ(6);

      Matrix.setLookAtM(viewMatrix, 0, eye.getX(), eye.getY(), eye.getZ(), center.getX(), center.getY(), center.getZ(), 0.0f, 0.0f, 1.0f);
      Matrix.perspectiveM(projectionMatrix, 0, 60.0f, GameContext.getAspect(), 0.1f, 40.0f);
      Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

      Shader.setShader3D();

      for (I3DRenderable renderable : renderable3DObjects)
        renderable.draw(projectionViewMatrix, lightPosition);
    }

    if (renderable2DObjects.size() != 0)
    {
      float width = GameContext.getWidth();
      float left = width / -2f;
      float right = width / 2f;
      float top = width / (2f * GameContext.getAspect());
      float bottom = width / (-2f * GameContext.getAspect());

      Matrix.orthoM(orthoMatrix, 0, left, right, bottom, top, -1, 1);

      Shader.setShader2D();

      for (I2DRenderable renderable : renderable2DObjects)
        renderable.draw(orthoMatrix);
    }
  }
  
  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height)
  {
    GameContext.setWidth(width);
    GameContext.setHeight(height);

    GLES20.glEnable(GLES20.GL_CULL_FACE);
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glDepthFunc(GLES20.GL_LEQUAL);
    GLES20.glEnable(GLES20.GL_ALPHA);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    GLES20.glViewport(0, 0, width, height);

    Shader.initShader3D();
    Shader.initShader2D();
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config)
  {
    world.initialize(null);

    Collection<I3DRenderable> i3DRenderable = world.get3DRenderable();
    Collection<I2DRenderable> i2DRenderable = world.get2DRenderable();

    if (i3DRenderable != null)
      renderable3DObjects.addAll(i3DRenderable);

    if (i2DRenderable != null)
      renderable2DObjects.addAll(i2DRenderable);

    initialized = true;
  }
}
