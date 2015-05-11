package com.ThirtyNineEighty.System;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Game.Menu.IMenu;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Shader;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer
  implements GLSurfaceView.Renderer
{
  private volatile boolean initialized = false;

  private float[] viewMatrix;
  private float[] projectionMatrix;
  private float[] projectionViewMatrix;

  private float[] orthoMatrix;

  private Vector3 lightPosition;
  private Camera camera;

  private final ArrayList<I3DRenderable> renderable3DObjects;
  private final ArrayList<I2DRenderable> renderable2DObjects;

  public Renderer()
  {
    lightPosition = new Vector3();
    viewMatrix = new float[16];
    projectionMatrix = new float[16];
    projectionViewMatrix = new float[16];

    orthoMatrix = new float[16];

    renderable3DObjects = new ArrayList<>();
    renderable2DObjects = new ArrayList<>();

    camera = new Camera();
  }

  @Override
  public void onDrawFrame(GL10 gl)
  {
    if (!initialized)
      return;

    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    IWorld world = GameContext.content.getWorld();
    if (world != null && world.isInitialized())
    {
      renderable3DObjects.clear();
      world.fillRenderable(renderable3DObjects);

      if (renderable3DObjects.size() != 0)
      {
        world.setCamera(camera);
        world.setLight(lightPosition);

        float eyeX = camera.eye.getX();
        float eyeY = camera.eye.getY();
        float eyeZ = camera.eye.getZ();

        float targetX = camera.target.getX();
        float targetY = camera.target.getY();
        float targetZ = camera.target.getZ();

        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, targetX, targetY, targetZ, 0.0f, 0.0f, 1.0f);
        Matrix.perspectiveM(projectionMatrix, 0, 60.0f, GameContext.getAspect(), 0.1f, 60.0f);
        Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        Shader.setShader3D();

        for (I3DRenderable renderable : renderable3DObjects)
          renderable.draw(projectionViewMatrix, lightPosition);
      }
    }

    IMenu menu = GameContext.content.getMenu();
    if (menu != null && menu.isInitialized())
    {
      renderable2DObjects.clear();
      menu.fillRenderable(renderable2DObjects);

      if (renderable2DObjects.size() != 0)
      {
        Matrix.setIdentityM(orthoMatrix, 0);
        Matrix.orthoM(orthoMatrix, 0, GameContext.Left, GameContext.Right, GameContext.Bottom, GameContext.Top, -1, 1);

        Shader.setShader2D();

        for (I2DRenderable renderable : renderable2DObjects)
          renderable.draw(orthoMatrix);
      }
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height)
  {
    GameContext.setGLThread();
    GameContext.setWidth(width);
    GameContext.setHeight(height);

    GLES20.glEnable(GLES20.GL_CULL_FACE);
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glDepthFunc(GLES20.GL_LEQUAL);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    GLES20.glViewport(0, 0, width, height);

    Shader.initShader3D();
    Shader.initShader2D();

    GameContext.renderableResources.reloadCache();
    initialized = true;

    GameContext.content.start();
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config)
  {

  }
}
