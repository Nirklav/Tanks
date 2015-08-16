package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.Shaders.Shader;
import com.ThirtyNineEighty.System.GameContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer
  implements GLSurfaceView.Renderer, IRenderer
{
  private volatile boolean initialized = false;

  private final ArrayList<IRenderable> renderables = new ArrayList<>();
  private final Camera camera;
  private final Light light;
  private final RendererContext context;

  public Renderer()
  {
    camera = new Camera();
    light = new Light();
    context = new RendererContext();
  }

  @Override
  public void add(IRenderable renderable)
  {
    synchronized (renderables)
    {
      renderables.add(renderable);
      Collections.sort(renderables, new RenderableComparator());
    }
  }

  @Override
  public void remove(IRenderable renderable)
  {
    synchronized (renderables)
    {
      renderables.remove(renderable);
    }
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
      world.setCamera(camera);
      world.setLight(light);
    }

    context.setCamera(camera);
    context.setLight(light);

    synchronized (renderables)
    {
      for (IRenderable renderable : renderables)
      {
        if (!renderable.isVisible())
          continue;

        Shader.setShader(renderable.getShaderId());
        renderable.draw(context);
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

    Shader.initShaders();

    GameContext.resources.reloadCache();
    GameContext.resources.preload();

    initialized = true;

    GameContext.content.start();
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config)
  {

  }

  static class RenderableComparator
    implements Comparator<IRenderable>
  {
    @Override
    public int compare(IRenderable left, IRenderable right)
    {
      int leftShaderId = left.getShaderId();
      int rightShaderId = right.getShaderId();
      return leftShaderId - rightShaderId;
    }
  }
}
