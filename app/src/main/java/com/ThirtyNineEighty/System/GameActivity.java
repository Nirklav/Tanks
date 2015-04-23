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

import com.ThirtyNineEighty.Game.Menu.MainMenu;

public class GameActivity
  extends Activity
  implements View.OnTouchListener
{
  private static final int FPS = 30;

  private boolean pause;
  private Handler handler;

  private Content content;

  private GLSurfaceView glView;

  private Runnable updateRunnable;
  private Runnable drawRunnable;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    handler = new Handler();

    // If game resumed content has been already created
    boolean contentCreated = false;
    content = (Content)GameContext.getContent();
    if (content == null)
    {
      content = new Content();
      contentCreated = true;
    }

    // OpenGL init
    glView = new GLSurfaceView(this);
    glView.getHolder().setFormat(PixelFormat.RGBA_8888);
    glView.setEGLContextClientVersion(2);
    glView.setEGLConfigChooser(new ConfigChooser());
    glView.setRenderer(content);
    glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    // Bind listener
    glView.setOnTouchListener(this);

    // Set view
    setContentView(glView);

    // Set game context data
    GameContext.setAppContext(this);
    GameContext.setContent(content);
    GameContext.mapLoader.initialize();

    // If first load set main menu
    if (contentCreated)
      content.setMenu(new MainMenu());
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

    if (updateRunnable == null)
    {
      updateRunnable = new Runnable()
      {
        @Override
        public void run() { content.onUpdate(); }
      };
    }

    handler.removeCallbacks(drawRunnable);
    if (!pause)
    {
      handler.postDelayed(drawRunnable, 1000 / FPS);
      GameContext.updateTime();
      glView.queueEvent(updateRunnable);
      glView.requestRender();
    }
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent)
  {
    content.onTouch(motionEvent);
    return true;
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    glView.onPause();
    content.onPause();
    pause = true;
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
