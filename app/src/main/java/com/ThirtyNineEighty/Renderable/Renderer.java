package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ThirtyNineEighty.Providers.IDataProvider;
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

  private final Object syncObject = new Object();

  private final ArrayList<IRenderable> objects = new ArrayList<>();
  private final ArrayList<IRenderable> menus = new ArrayList<>();
  private final ArrayList<IRenderable> particles = new ArrayList<>();

  private IDataProvider<Camera> cameraProvider;
  private IDataProvider<Light> lightProvider;
  private final RendererContext context = new RendererContext();

  @Override
  public void add(IRenderable renderable)
  {
    synchronized (syncObject)
    {
      ArrayList<IRenderable> collection = getCollection(renderable.getShaderId());

      collection.add(renderable);
      Collections.sort(collection, new RenderableComparator());
    }
  }

  @Override
  public void remove(IRenderable renderable)
  {
    synchronized (syncObject)
    {
      ArrayList<IRenderable> collection = getCollection(renderable.getShaderId());

      collection.remove(renderable);
    }
  }

  @Override
  public void setCameraProvider(IDataProvider<Camera> provider)
  {
    if (cameraProvider != null && provider != null)
      throw new IllegalStateException("Camera provider already set");

    cameraProvider = provider;
  }

  @Override
  public void setLightProvider(IDataProvider<Light> provider)
  {
    if (lightProvider != null && provider != null)
      throw new IllegalStateException("Light provider already set");

    lightProvider = provider;
  }

  private ArrayList<IRenderable> getCollection(int shaderId)
  {
    switch (shaderId)
    {
    case Shader.Shader2D:
      return menus;
    case Shader.Shader3D:
    case Shader.ShaderSkyBox:
      return objects;
    case Shader.ShaderExplosionParticles:
      return particles;
    default:
      throw new IllegalStateException("unknown shader id");
    }
  }

  @Override
  public void onDrawFrame(GL10 gl)
  {
    if (!initialized)
      return;

    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    if (cameraProvider != null)
    {
      Camera camera = cameraProvider.get();
      context.setCamera(camera);
    }

    if (lightProvider != null)
    {
      Light light = lightProvider.get();
      context.setLight(light);
    }

    synchronized (syncObject)
    {
      // Objects
      GLES20.glEnable(GLES20.GL_DEPTH_TEST);

      draw(context, objects);

      GLES20.glDisable(GLES20.GL_DEPTH_TEST);

      // Particles
      GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

      draw(context, particles);

      GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

      // Menus
      draw(context, menus);
    }
  }

  private static void draw(RendererContext context, ArrayList<IRenderable> renderables)
  {
    for (IRenderable renderable : renderables)
    {
      if (!renderable.isVisible())
        continue;

      Shader.setShader(renderable.getShaderId());
      renderable.draw(context);
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height)
  {
    GameContext.setWidth(width);
    GameContext.setHeight(height);

    GLES20.glEnable(GLES20.GL_CULL_FACE);
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
    GameContext.setGLThread();
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
