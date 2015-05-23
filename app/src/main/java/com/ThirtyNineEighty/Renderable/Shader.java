package com.ThirtyNineEighty.Renderable;

import android.opengl.GLES20;
import android.opengl.GLException;
import android.util.Log;

import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.io.InputStream;

public abstract class Shader
{
  protected static Shader current;
  private static Shader shader2D;
  private static Shader shader3D;

  protected int vertexShaderHandle;
  protected int fragmentShaderHandle;
  protected int programHandle;

  public abstract void compile();
  protected abstract void getLocations();

  public static Shader getCurrent()
  {
    return current;
  }

  public static void initShader3D()
  {
    if (shader3D != null)
      shader3D.deleteProgram();

    shader3D = new Shader3D();
    shader3D.compile();
  }

  public static void initShader2D()
  {
    if (shader2D != null)
      shader2D.deleteProgram();

    shader2D = new Shader2D();
    shader2D.compile();
  }

  public static void setShader3D()
  {
    if (current == shader3D)
      return;

    current = shader3D;
    GLES20.glUseProgram(shader3D.programHandle);
  }

  public static void setShader2D()
  {
    if (current == shader2D)
      return;

    current = shader2D;
    GLES20.glUseProgram(shader2D.programHandle);
  }

  public void validate()
  {
    if (!GameContext.isDebuggable())
      return;

    int[] result = new int[1];

    GLES20.glValidateProgram(programHandle);
    GLES20.glGetProgramiv(programHandle, GLES20.GL_VALIDATE_STATUS, result, 0);

    boolean validated = result[0] != 0;
    if (!validated)
    {
      String message = !GLES20.glIsProgram(programHandle)
        ? "Program handle deprecated!"
        : "Program do not validated!";

      throw new GLException(result[0], message);
    }
  }

  protected void compile(String vertexFileName, String fragmentFileName)
  {
    Log.d("Shader", String.format("compiler called for [vertex: %s, fragment: %s]", vertexFileName, fragmentFileName));

    vertexShaderHandle    = compileShader(GLES20.GL_VERTEX_SHADER, vertexFileName);
    fragmentShaderHandle  = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentFileName);
    programHandle = GLES20.glCreateProgram();

    GLES20.glAttachShader(programHandle, vertexShaderHandle);
    GLES20.glAttachShader(programHandle, fragmentShaderHandle);
    GLES20.glLinkProgram(programHandle);

    GLES20.glReleaseShaderCompiler();

    getLocations();
  }

  private int compileShader(int type, String path)
  {
    try
    {
      // Load shader source
      InputStream stream = GameContext.activity.getAssets().open(path);
      int size = stream.available();
      byte[] buffer = new byte[size];
      int readCount = stream.read(buffer);
      if (readCount != size)
        throw new IOException("File has been read not fully: " + path);

      String source = new String(buffer);
      stream.close();

      // Compile shader
      int shaderHandle = GLES20.glCreateShader(type);
      GLES20.glShaderSource(shaderHandle, source);
      GLES20.glCompileShader(shaderHandle);

      // Check for errors
      int[] compiled = new int[1];
      GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compiled, 0);
      if (compiled[0] == 0)
      {
        String error = GLES20.glGetShaderInfoLog(shaderHandle);
        GLES20.glDeleteShader(shaderHandle);
        throw new GLException(compiled[0], error);
      }

      return shaderHandle;
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  private void deleteProgram()
  {
    if (GLES20.glIsShader(vertexShaderHandle))
      GLES20.glDeleteShader(vertexShaderHandle);

    if (GLES20.glIsShader(fragmentShaderHandle))
      GLES20.glDeleteShader(fragmentShaderHandle);

    if (GLES20.glIsProgram(programHandle))
      GLES20.glDeleteProgram(programHandle);
  }
}
