package com.ThirtyNineEighty.Renderable.Renderable2D;

public class GLLabel
  extends GLSprite
{
  private static final float TextureCellWidth = 0.0625f;
  private static final float TextureCellHeight = 0.125f;

  public GLLabel(String value, String fontTexture, float charWidth, float charHeight)
  {
    super(fontTexture, String.format("String: %s", value), buildGeometry(value, charWidth, charHeight));
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
