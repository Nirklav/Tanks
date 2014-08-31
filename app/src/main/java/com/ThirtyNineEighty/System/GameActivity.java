package com.ThirtyNineEighty.System;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity
  extends Activity
  implements View.OnTouchListener
{
  private static final int FPS = 30;

  private boolean pause;
  private Handler handler;

  private GLSurfaceView view;
  private Content renderer;
  private MotionEvent event;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);

    ActivityContext.setContext(this);

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

    //renderer init
    renderer = new Content();
    view.setRenderer(renderer);
    view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    setContentView(view);
    
    //Bind listener and set view
    view.setOnTouchListener(this);
  }
  
  @Override
  public boolean onTouch(View v, MotionEvent event)
  {
    this.event = event;
    return true;
  }

  private void requestRenderer()
  {
    handler.removeCallbacks(drawRunnable);
    if(!pause)
    {
      handler.postDelayed(drawRunnable, 1000 / FPS);
      renderer.Update(event);
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
