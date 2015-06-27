package com.ThirtyNineEighty.System;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ThirtyNineEighty.Game.Menu.IMenu;
import com.ThirtyNineEighty.Game.Menu.MainMenu;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.ConfigChooser;
import com.ThirtyNineEighty.Renderable.Renderer;

public class GameActivity
  extends Activity
  implements View.OnTouchListener
{
  public static final int FPS = 30;

  private boolean pause;
  private Handler handler;
  private GLSurfaceView glView;
  private Runnable drawRunnable;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    GameContext.setMainThread();

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    handler = new Handler();

    // OpenGL init
    glView = new GLSurfaceView(this);
    glView.getHolder().setFormat(PixelFormat.RGBA_8888);
    glView.setEGLContextClientVersion(2);
    glView.setEGLConfigChooser(new ConfigChooser());
    glView.setRenderer(new Renderer());
    glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    // Bind listener
    glView.setOnTouchListener(this);

    // Set view
    setContentView(glView);

    // Initialize game context
    GameContext.setActivity(this);
    GameContext.mapManager.initialize();
    GameContext.gameProgress.initialize(this);

    if (GameContext.content == null)
      GameContext.content = new Content();
    else
    {
      IWorld world = GameContext.content.getWorld();
      if (world != null)
        world.disable();
    }

    GameContext.content.setMenuAsync(new MainMenu(), null);
  }

  public void postEvent(final Runnable r)
  {
    if (GameContext.isGLThread())
    {
      r.run();
      return;
    }

    glView.queueEvent(r);
  }

  @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
  public void sendEvent(final Runnable r)
  {
    if (GameContext.isMainThread())
      throw new IllegalStateException("can't stop main thread (use post)");

    if (GameContext.isGLThread())
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
          @Override public void run()
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
  protected void onPause()
  {
    super.onPause();
    glView.onPause();
    pause = true;

    // reset
    GameContext.content.stop();
    GameContext.setActivity(null);
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
