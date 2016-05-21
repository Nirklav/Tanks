package com.ThirtyNineEighty.Base;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ThirtyNineEighty.Base.Menus.IMenu;
import com.ThirtyNineEighty.Base.Renderable.*;
import com.ThirtyNineEighty.Base.Worlds.IWorld;

import java.util.concurrent.Executors;

public class GameActivity
  extends Activity
  implements View.OnTouchListener,
             IGlThread
{
  public static final String SavedWorld = "savedWorld";
  public static final int FPS = 30;

  private boolean pause;
  private Handler handler;
  private Renderer renderer;
  private GLSurfaceView glView;
  private Runnable drawRunnable;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    handler = new Handler();
    renderer = new Renderer();

    initializeContext();
    initializeFactory();

    // OpenGL init
    glView = new GLSurfaceView(this);
    glView.getHolder().setFormat(PixelFormat.RGBA_8888);
    glView.setEGLContextClientVersion(2);
    glView.setEGLConfigChooser(new ConfigChooser());
    glView.setRenderer(renderer);
    glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    // Bind listener
    glView.setOnTouchListener(this);

    // Set view
    setContentView(glView);

    // Set menu and world
    initializeContent();
  }

  protected void initializeContext()
  {
    ApplicationInfo info = getApplicationInfo();

    GameContext.setMainThread();
    GameContext.debuggable = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

    GameContext.context = this;
    GameContext.glThread = this;
    GameContext.renderer = renderer;
    GameContext.threadPool = Executors.newCachedThreadPool();
  }

  protected void initializeFactory()
  {

  }

  protected void initializeContent()
  {

  }

  protected void uninitialize()
  {
    GameContext.context = null;
    GameContext.renderer = null;
    GameContext.glThread = null;
  }

  @Override
  public void postEvent(final Runnable r)
  {
    if (GameContext.isGlThread())
    {
      r.run();
      return;
    }

    glView.queueEvent(r);
  }

  @Override
  @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
  public void sendEvent(final Runnable r)
  {
    if (GameContext.isMainThread())
      throw new IllegalStateException("can't stop main thread (use post)");

    if (GameContext.isGlThread())
    {
      r.run();
      return;
    }

    try
    {
      final Object waitObj = new Object();
      synchronized (waitObj)
      {
        glView.queueEvent(new Runnable()
        {
          @Override
          public void run()
          {
            r.run();
            synchronized (waitObj)
            {
              waitObj.notifyAll();
            }
          }
        });

        waitObj.wait();
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private void requestRenderer()
  {
    if (drawRunnable == null)
    {
      drawRunnable = new Runnable()
      {
        @Override
        public void run() { requestRenderer(); }
      };
    }

    handler.removeCallbacks(drawRunnable);
    if (!pause)
    {
      handler.postDelayed(drawRunnable, 1000 / FPS);
      glView.requestRender();
    }
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent)
  {
    IMenu menu = GameContext.content.getMenu();
    if (menu == null)
      return false;

    menu.processEvent(motionEvent);
    return true;
  }

  @Override
  public void onLowMemory()
  {
    super.onLowMemory();

    int clearedResources = GameContext.resources.clearUnusedCache();
    Log.d("GameActivity", String.format("OnLowMemory: Cleared %d resources", clearedResources));
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    glView.onPause();
    pause = true;

    // get world
    IWorld world = GameContext.content.getWorld();

    // stop the world
    GameContext.content.stop();
    GameContext.content.reset();

    // save game world if need
    if (world != null && world.needSave())
      GameContext.data.saveWorld(SavedWorld, world);
    else
      GameContext.data.deleteWorld(SavedWorld);

    // reset other managers
    uninitialize();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    glView.onResume();
    pause = false;
    requestRenderer();
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    pause = true;
    this.finish();
  }
}
