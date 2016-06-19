package com.ThirtyNineEighty.Base.Renderable;

import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;

import com.ThirtyNineEighty.Base.Providers.IDataProvider;
import com.ThirtyNineEighty.Base.Renderable.Common.Camera;
import com.ThirtyNineEighty.Base.Renderable.Common.Light;
import com.ThirtyNineEighty.Base.Renderable.Shaders.Shader;
import com.ThirtyNineEighty.Base.GameContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer
  implements GLSurfaceView.Renderer,
             IRenderer
{
  public static final float EtalonHeight = 1080f;
  public static final float EtalonWidth  = 1920f;
  public static final float EtalonAspect = EtalonWidth / EtalonHeight;

  public static final float Left = EtalonWidth / -2f;
  public static final float Right = EtalonWidth / 2f;
  public static final float Bottom = EtalonHeight / -2f;
  public static final float Top = EtalonHeight / 2f;

  private static float width;
  private static float height;

  private volatile boolean initialized = false;

  private final Object syncObject = new Object();

  private final ArrayList<IRenderable> objects = new ArrayList<>();
  private final ArrayList<IRenderable> menus = new ArrayList<>();
  private final ArrayList<IRenderable> particles = new ArrayList<>();

  private final RendererContext context = new RendererContext();
  private IDataProvider<Camera.Data> cameraProvider;
  private IDataProvider<Light.Data> lightProvider;

  public static float getAspect() { return width / height; }
  public static float getWidth() { return width; }
  public static float getHeight() { return height; }

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
  public void setCameraProvider(IDataProvider<Camera.Data> provider)
  {
    if (cameraProvider != null && provider != null)
      throw new IllegalStateException("Camera provider already set");

    cameraProvider = provider;
  }

  @Override
  public void setLightProvider(IDataProvider<Light.Data> provider)
  {
    if (lightProvider != null && provider != null)
      throw new IllegalStateException("Light provider already set");

    lightProvider = provider;
  }

  private ArrayList<IRenderable> getCollection(int shaderId)
  {
    switch (shaderId)
    {
    case Shader.ShaderSprite:
    case Shader.ShaderLabel:
      return menus;
    case Shader.ShaderModel:
    case Shader.ShaderSkyBox:
    case Shader.ShaderPolyLine:
      return objects;
    case Shader.ShaderExplosionParticles:
    case Shader.ShaderParticles:
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
      Camera.Data data = cameraProvider.get();
      context.setCamera(data);
    }

    if (lightProvider != null)
    {
      Light.Data data = lightProvider.get();
      context.setLight(data);
    }

    synchronized (syncObject)
    {
      // Objects
      draw(context, objects);

      // Particles
      GLES20.glDepthMask(false);
      GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

      draw(context, particles);

      GLES20.glDepthMask(true);
      GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

      // Menus
      GLES20.glDisable(GLES20.GL_DEPTH_TEST);
      draw(context, menus);
      GLES20.glEnable(GLES20.GL_DEPTH_TEST);
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

      int error;
      if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
      {
        Class<?> renderableClass = renderable.getClass();
        String message = String.format("Error after %s draw call", renderableClass.getName());
        throw new GLException(error, message);
      }
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int w, int h)
  {
    width = w;
    height = h;

    GLES20.glEnable(GLES20.GL_CULL_FACE);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glDepthFunc(GLES20.GL_LEQUAL);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    GLES20.glViewport(0, 0, w, h);

    Shader.initShaders();

    GameContext.resources.reloadCache();
    GameContext.resources.preload();

    initialized = true;

    GameContext.content.start();
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config)
  {
    GameContext.setGlThread();
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
