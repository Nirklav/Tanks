package com.ThirtyNineEighty.Base.Renderable;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.opengl.GLSurfaceView.EGLConfigChooser;

public class ConfigChooser implements EGLConfigChooser
{
  public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
  {
    int[] value = new int[1];
    int[] configSpec = 
    {
      EGL10.EGL_RED_SIZE,        8,
      EGL10.EGL_GREEN_SIZE,      8,
      EGL10.EGL_BLUE_SIZE,       8,
      EGL10.EGL_ALPHA_SIZE,      8,
      EGL10.EGL_DEPTH_SIZE,      16,
      EGL10.EGL_RENDERABLE_TYPE, 4,
      EGL10.EGL_NONE
    };
    
    if (!egl.eglChooseConfig(display, configSpec, null, 0, value))
      throw new IllegalArgumentException("RGB888 eglChooseConfig failed");
    
    int numConfigs = value[0];
    if (numConfigs <= 0)
    {
      configSpec = new int[] 
      { 
        EGL10.EGL_RED_SIZE, 5, 
        EGL10.EGL_GREEN_SIZE, 6,
        EGL10.EGL_BLUE_SIZE, 5, 
        EGL10.EGL_RENDERABLE_TYPE, 4, 
        EGL10.EGL_NONE
      };
      
      if (!egl.eglChooseConfig(display, configSpec, null, 0, value))
        throw new IllegalArgumentException("RGB565 eglChooseConfig failed");

      numConfigs = value[0];
      if (numConfigs <= 0)
        throw new IllegalArgumentException("No configs match configSpec RGB565");
    }
    
    EGLConfig[] configs = new EGLConfig[numConfigs];
    egl.eglChooseConfig(display, configSpec, configs, numConfigs, value);
    return configs[0];
  }
}
