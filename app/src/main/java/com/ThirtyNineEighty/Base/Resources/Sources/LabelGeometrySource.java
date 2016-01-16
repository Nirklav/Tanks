package com.ThirtyNineEighty.Base.Resources.Sources;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

import java.nio.FloatBuffer;

public class LabelGeometrySource
  extends GeometrySource
{
  private static final float TextureCellWidth = 0.0625f;
  private static final float TextureCellHeight = 0.125f;

  private String value;
  private int charWidth;
  private int charHeight;

  public LabelGeometrySource(String value, MeshMode mode, int charWidth, int charHeight)
  {
    super(String.format("String: %d (hash) Width: %d Height %d", value.hashCode(), charWidth, charHeight), mode);

    this.value = value;
    this.charWidth = charWidth;
    this.charHeight = charHeight;
  }

  @Override
  protected LoadResult buildGeometry()
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

      switch (ch)
      {
      case '\n':
        lineNumber++;
        carriagePosition = 0;
        continue;
      case '\t':
        carriagePosition += GLLabel.tabLength;
        continue;
      }

      int chInt = (int) ch;
      int numOfTextureCellX = (chInt - 32) % 16;
      int numOfTextureCellY = (chInt - 32) / 16;

      float charViewOffsetX = carriagePosition * charWidth;
      float charViewOffsetY = lineNumber * charHeight;

      // 1 point
      // vertices
      geometry[counter++] = -charWidth / 2f + charViewOffsetX;
      geometry[counter++] = charHeight / 2f - charViewOffsetY;
      // texture
      geometry[counter++] = numOfTextureCellX * TextureCellWidth;
      geometry[counter++] = numOfTextureCellY * TextureCellHeight;

      // 2 point
      // vertices
      geometry[counter++] = -charWidth / 2f + charViewOffsetX;
      geometry[counter++] = -charHeight / 2f - charViewOffsetY;
      // texture
      geometry[counter++] = numOfTextureCellX * TextureCellWidth;
      geometry[counter++] = numOfTextureCellY * TextureCellHeight + TextureCellHeight;

      // 3 point
      // vertices
      geometry[counter++] = charWidth / 2f + charViewOffsetX;
      geometry[counter++] = charHeight / 2f - charViewOffsetY;
      // texture
      geometry[counter++] = numOfTextureCellX * TextureCellWidth + TextureCellWidth;
      geometry[counter++] = numOfTextureCellY * TextureCellHeight;

      // 4 point
      // vertices
      geometry[counter++] = -charWidth / 2f + charViewOffsetX;
      geometry[counter++] = -charHeight / 2f - charViewOffsetY;
      // texture
      geometry[counter++] = numOfTextureCellX * TextureCellWidth;
      geometry[counter++] = numOfTextureCellY * TextureCellHeight + TextureCellHeight;

      // 5 point
      // vertices
      geometry[counter++] = charWidth / 2f + charViewOffsetX;
      geometry[counter++] = -charHeight / 2f - charViewOffsetY;
      // texture
      geometry[counter++] = numOfTextureCellX * TextureCellWidth + TextureCellWidth;
      geometry[counter++] = numOfTextureCellY * TextureCellHeight + TextureCellHeight;

      // 6 point
      // vertices
      geometry[counter++] = charWidth / 2f + charViewOffsetX;
      geometry[counter++] = charHeight / 2f - charViewOffsetY;
      // texture
      geometry[counter++] = numOfTextureCellX * TextureCellWidth + TextureCellWidth;
      geometry[counter++] = numOfTextureCellY * TextureCellHeight;

      carriagePosition++;
    }

    FloatBuffer buffer = loadGeometry(geometry);
    return new LoadResult(buffer, geometry.length / 4, Vector3.zero, Vector3.zero);
  }
}
