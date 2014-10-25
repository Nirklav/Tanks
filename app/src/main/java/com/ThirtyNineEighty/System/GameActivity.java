package com.ThirtyNineEighty.System;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity
  extends Activity
{
  private static final int FPS = 30;

  private boolean pause;
  private Handler handler;

  private GLSurfaceView view;

  @SuppressWarnings("FieldCanBeLocal")
  private Content content;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    GameContext.setAppContext(this);

    handler = new Handler();

    //OpenGL init
    view = new GLSurfaceView(this);
    view.setPreserveEGLContextOnPause(true);
    SurfaceHolder holder = view.getHolder();

    if (holder != null)
      holder.setFormat(PixelFormat.RGBA_8888);

    view.setEGLContextClientVersion(2);
    view.setEGLConfigChooser(new ConfigChooser());
    view.setEGLConfigChooser(true);

    // Content init
    content = new Content();
    view.setRenderer(content);
    view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    // Set view
    setContentView(view);
    
    // Bind listener
    view.setOnTouchListener(content);
  }

  private void requestRenderer()
  {
    handler.removeCallbacks(drawRunnable);
    if(!pause)
    {
      handler.postDelayed(drawRunnable, 1000 / FPS);
      GameContext.updateTime();
      content.onUpdate();
      view.requestRender();
    }
  }

  private final Runnable drawRunnable = new Runnable()
  {
    @Override
    public void run()
    {
      requestRenderer();
    }
  };

  @Override
  protected void onPause()
  {
    super.onPause();
    view.onPause();
    pause = true;
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    view.onResume();
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
