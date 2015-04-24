package com.ThirtyNineEighty.Renderable.Renderable2D;

import com.ThirtyNineEighty.Renderable.Resources.Geometry;
import com.ThirtyNineEighty.Renderable.Resources.GeometrySource;
import com.ThirtyNineEighty.Renderable.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;

import java.nio.FloatBuffer;

public class GLLabel
  extends GLSprite
{
  private static final float TextureCellWidth = 0.0625f;
  private static final float TextureCellHeight = 0.125f;

  private float charWidth;
  private float charHeight;

  public GLLabel(String value, String fontTexture, float charWidth, float charHeight) { this(value, fontTexture, charWidth, charHeight, MeshMode.Static); }
  public GLLabel(String value, String fontTexture, float charWidth, float charHeight, MeshMode mode)
  {
    super(fontTexture, new LabelGeometrySource(value, mode, charWidth, charHeight));
    this.charWidth = charWidth;
    this.charHeight = charHeight;
  }

  public void setValue(String value) { setValue(value, geometryData.getMode()); }
  public void setValue(String value, MeshMode mode)
  {
    LabelGeometrySource source = new LabelGeometrySource(value, mode, charWidth, charHeight);
    geometryData = GameContext.renderableResources.getGeometry(source);
  }

  private static class LabelGeometrySource
    extends GeometrySource
  {
    private String value;
    private float charWidth;
    private float charHeight;

    public LabelGeometrySource(String value, MeshMode mode, float charWidth, float charHeight)
    {
      super(getGeometryName(value), mode);

      this.value = value;
      this.charWidth = charWidth;
      this.charHeight = charHeight;
    }

    @Override
    public Geometry load()
    {
      float[] bufferData = buildGeometry(value, charWidth, charHeight);
      FloatBuffer buffer = loadGeometry(bufferData);

      switch (mode)
      {
      case Dynamic:
        return new Geometry(buffer, bufferData.length / 12);

      case Static:
        int handle = loadGeometry(buffer);
        return new Geometry(handle, bufferData.length / 12);
      }

      throw new IllegalArgumentException("Invalid mesh mode");
    }

    @Override
    public void reload(Geometry geometry)
    {
      release(geometry);

      float[] bufferData = buildGeometry(value, charWidth, charHeight);
      FloatBuffer buffer = loadGeometry(bufferData);

      switch (mode)
      {
      case Dynamic:
        geometry.updateData(buffer, bufferData.length / 12);
        return;

      case Static:
        int handle = loadGeometry(buffer);
        geometry.updateData(handle, bufferData.length / 12);
        return;
      }

      throw new IllegalArgumentException("Invalid mesh mode");
    }

    private static String getGeometryName(String value)
    {
      return String.format("String: %s", value);
    }

    private static float[] buildGeometry(String value, float charWidth, float charHeight)
    {
      int carriagePosition = 0;
      int lineNumber = 0;
      int counter = 0;
      int strLength = 0;
      int strRealLength = value.length();

      for (int i = 0; i < strRealLength; i++)
      {
        char ch = value.charAt(i);

        switch (ch)
        {
        case '\n':
        case '\t':
          continue;

        default:
          strLength++;
          break;
        }
      }

      float[] geometry = new float[strLength * 24];

      for (int i = 0; i < strRealLength; i++)
      {
        char ch = value.charAt(i);

        if (ch == '\n')
        {
          lineNumber++;
          carriagePosition = 0;
          continue;
        }
        if (ch == '\t')
        {
          carriagePosition += 3;
          continue;
        }

        int chInt = (int) ch;
        int numOfTextureCellX = (chInt - 32) % 16;
        int numOfTextureCellY = (chInt - 32) / 16;

        float charViewOffsetX = carriagePosition * charWidth;
        float charViewOffsetY = lineNumber * charHeight;

        // 1 point
        // vertices
        geometry[counter++] = - charWidth / 2 + charViewOffsetX;
        geometry[counter++] =   charHeight / 2 - charViewOffsetY;
        // texture
        geometry[counter++] = numOfTextureCellX * TextureCellWidth;
        geometry[counter++] = numOfTextureCellY * TextureCellHeight;

        // 2 point
        // vertices
        geometry[counter++] = - charWidth / 2 + charViewOffsetX;
        geometry[counter++] = - charHeight / 2 - charViewOffsetY;
        // texture
        geometry[counter++] = numOfTextureCellX * TextureCellWidth;
        geometry[counter++] = numOfTextureCellY * TextureCellHeight + TextureCellHeight;

        // 3 point
        // vertices
        geometry[counter++] =   charWidth / 2 + charViewOffsetX;
        geometry[counter++] =   charHeight / 2 - charViewOffsetY;
        // texture
        geometry[counter++] = numOfTextureCellX * TextureCellWidth + TextureCellWidth;
        geometry[counter++] = numOfTextureCellY * TextureCellHeight;

        // 4 point
        // vertices
        geometry[counter++] = - charWidth / 2 + charViewOffsetX;
        geometry[counter++] = - charHeight / 2 - charViewOffsetY;
        // texture
        geometry[counter++] = numOfTextureCellX * TextureCellWidth;
        geometry[counter++] = numOfTextureCellY * TextureCellHeight + TextureCellHeight;

        // 5 point
        // vertices
        geometry[counter++] =   charWidth / 2 + charViewOffsetX;
        geometry[counter++] = - charHeight / 2 - charViewOffsetY;
        // texture
        geometry[counter++] = numOfTextureCellX * TextureCellWidth + TextureCellWidth;
        geometry[counter++] = numOfTextureCellY * TextureCellHeight + TextureCellHeight;

        // 6 point
        // vertices
        geometry[counter++] =   charWidth / 2 + charViewOffsetX;
        geometry[counter++] =   charHeight / 2 - charViewOffsetY;
        // texture
        geometry[counter++] = numOfTextureCellX * TextureCellWidth + TextureCellWidth;
        geometry[counter++] = numOfTextureCellY * TextureCellHeight;

        carriagePosition++;
      }

      return geometry;
    }
  }
}
