package com.ThirtyNineEighty.Renderable.Renderable2D;

import com.ThirtyNineEighty.Renderable.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;

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
    super(fontTexture, getGeometryName(value), buildGeometry(value, charWidth, charHeight), mode);

    this.charWidth = charWidth;
    this.charHeight = charHeight;
  }

  public void setValue(String value)
  {
    MeshMode mode = geometryData.getMode();

    float[] bufferData = buildGeometry(value, charWidth, charHeight);
    String geometryName = getGeometryName(value);
    geometryData = GameContext.renderableResources.getGeometry(geometryName, bufferData.length / 12, bufferData, mode);
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
