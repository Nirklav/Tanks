package com.ThirtyNineEighty.System;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ThirtyNineEighty.Game.Menu.IMenu;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.Renderable;
import com.ThirtyNineEighty.Renderable.Renderable2D.I2DRenderable;
import com.ThirtyNineEighty.Renderable.Renderable3D.I3DRenderable;
import com.ThirtyNineEighty.Renderable.Shader;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Content
  implements IContent,
             GLSurfaceView.Renderer,
             View.OnTouchListener
{
  private boolean initialized = false;

  private float[] viewMatrix;
  private float[] projectionMatrix;
  private float[] projectionViewMatrix;
  private float[] lightPosition;

  private float[] orthoMatrix;

  private IWorld world;
  private IMenu menu;

  private final ArrayList<ISubprogram> subprograms;
  private final ArrayList<SubprogramAction> subprogramActions;

  private final ArrayList<I3DRenderable> renderable3DObjects;
  private final ArrayList<I2DRenderable> renderable2DObjects;

  public Content()
  {
    lightPosition = new float[] { 0.0f, 0.0f, 12.0f };

    viewMatrix = new float[16];
    projectionMatrix = new float[16];
    projectionViewMatrix = new float[16];

    orthoMatrix = new float[16];

    subprograms = new ArrayList<ISubprogram>();
    subprogramActions = new ArrayList<SubprogramAction>();
    renderable3DObjects = new ArrayList<I3DRenderable>();
    renderable2DObjects = new ArrayList<I2DRenderable>();
  }

  @Override
  public void setWorld(IWorld value) { setWorld(value, null); }

  @Override
  public void setWorld(IWorld value, Object args)
  {
    if (world != null)
      world.uninitialize();

    world = value;
    world.initialize(args);
  }

  @Override
  public IWorld getWorld() { return world; }

  @Override
  public void setMenu(IMenu value) { setMenu(value, null); }

  @Override
  public void setMenu(IMenu value, Object args)
  {
    if (menu != null)
      menu.uninitialize();

    menu = value;
    menu.initialize(args);
  }

  @Override
  public IMenu getMenu() { return menu; }

  @Override
  public void bindProgram(ISubprogram subprogram)
  {
    subprogramActions.add(new SubprogramAction(subprogram, SubprogramAction.ADD_ACTION));
  }

  @Override
  public void unbindProgram(ISubprogram subprogram)
  {
    subprogramActions.add(new SubprogramAction(subprogram, SubprogramAction.REMOVE_ACTION));
  }

  public void onUpdate()
  {
    if (!initialized)
      return;

    for (ISubprogram subprogram : subprograms)
      subprogram.update();

    // update can change subprograms
    for (SubprogramAction action : subprogramActions)
    {
      switch (action.type)
      {
      case SubprogramAction.ADD_ACTION:
        subprograms.add(action.subprogram);
        break;

      case SubprogramAction.REMOVE_ACTION:
        subprograms.remove(action.subprogram);
        break;
      }
    }

    subprogramActions.clear();
  }

  @Override
  public boolean onTouch(View v, MotionEvent event)
  {
    return initialized && menu.processEvent(event);
  }

  @Override
  public void onDrawFrame(GL10 gl)
  {
    if (!initialized)
      return;

    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    renderable3DObjects.clear();

    if (world != null)
      world.fillRenderable(renderable3DObjects);

    if (renderable3DObjects.size() != 0)
    {
      world.setViewMatrix(viewMatrix);
      Matrix.perspectiveM(projectionMatrix, 0, 60.0f, GameContext.getAspect(), 0.1f, 50.0f);
      Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

      Shader.setShader3D();

      for (I3DRenderable renderable : renderable3DObjects)
        renderable.draw(projectionViewMatrix, lightPosition);
    }

    renderable2DObjects.clear();

    if (menu != null)
      menu.fillRenderable(renderable2DObjects);

    if (renderable2DObjects.size() != 0)
    {
      Matrix.setIdentityM(orthoMatrix, 0);
      Matrix.orthoM(orthoMatrix, 0, GameContext.Left, GameContext.Right, GameContext.Bottom, GameContext.Top, -1, 1);

      Shader.setShader2D();

      for (I2DRenderable renderable : renderable2DObjects)
        renderable.draw(orthoMatrix);
    }

    int error = GLES20.glGetError();
    if (error != GLES20.GL_NO_ERROR)
      Log.e("Error", "OpenGL error. Code: " + Integer.toString(error));
  }
  
  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height)
  {
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

    Renderable.clearCache();

    setWorld(new GameWorld());

    initialized = true;

    int error = GLES20.glGetError();
    if (error != GLES20.GL_NO_ERROR)
      Log.e("Error", "OpenGL error. Code: " + Integer.toString(error));
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config)
  {

  }

  private static final class SubprogramAction
  {
    public static final int ADD_ACTION = 0;
    public static final int REMOVE_ACTION = 1;

    public final ISubprogram subprogram;
    public final int type;

    public SubprogramAction(ISubprogram subprogram, int type)
    {
      this.subprogram = subprogram;
      this.type = type;
    }
  }
}
