package com.ThirtyNineEighty.Renderable.GL;

import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Resources.Sources.GeometrySource;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;

import java.nio.FloatBuffer;

// Can process only \n and \t spec symbols
// TODO: serializable
public class GLLabel
  extends GLSprite
{
  private static final int tabLength = 3;

  private static final float TextureCellWidth = 0.0625f;
  private static final float TextureCellHeight = 0.125f;

  private float charWidth;
  private float charHeight;
  private String value;

  private AlignType alignType;
  private Vector2 originalPosition;

  public GLLabel(String value) { this(value, "simpleFont", 30, 40, MeshMode.Static); }
  public GLLabel(String value, String fontTexture, float charWidth, float chatHeight,  MeshMode mode)
  {
    super(fontTexture, new LabelGeometrySource(value, mode, charWidth, chatHeight));
    this.charWidth = charWidth;
    this.charHeight = chatHeight;
    this.value = value;

    alignType = AlignType.Center;
    originalPosition = Vector.getInstance(2, position);
    setPosition();
  }

  public void setValue(String value)
  {
    if (value == null)
      value = "";

    if (value.equals(this.value))
      return;

    this.value = value;

    setPosition();
    rebuild();
  }

  public void setCharSize(float width, float height)
  {
    charWidth = width;
    charHeight = height;

    setPosition();
    rebuild();
  }

  public void setAlign(AlignType value)
  {
    alignType = value;
    setPosition();
  }

  @Override
  public void setPosition(Vector2 value)
  {
    originalPosition.setFrom(value);
    setPosition();
  }

  @Override
  public void setPosition(float x, float y)
  {
    originalPosition.setFrom(x, y);
    setPosition();
  }

  private void setPosition()
  {
    Vector2 shift;

    switch (alignType)
    {
    case Center:
      shift = getCenterShift();
      break;

    case TopRight:
      shift = getCenterShift();
      shift.multiplyToX(2);
      shift.multiplyToY(0);
      break;

    case TopLeft:
    default:
      shift = Vector.getInstance(2);
      break;

    case TopCenter:
      shift = getCenterShift();
      shift.multiplyToY(2);
      break;

    case BottomRight:
      shift = getCenterShift();
      shift.multiplyToX(2);
      shift.multiplyToY(2);
      break;

    case BottomLeft:
      shift = getCenterShift();
      shift.multiplyToX(0);
      shift.multiplyToY(2);
      break;

    case BottomCenter:
      shift = getCenterShift();
      shift.setY(0);
      break;
    }

    shift.addToX(-charWidth / 2);
    shift.addToY(-charHeight / 2);

    super.setPosition(originalPosition.getSubtract(shift));
    Vector.release(shift);
  }

  private Vector2 getCenterShift()
  {
    int widthLength = 0;
    int heightLength = 1;

    int currentLineLength = 0;

    // calculate mesh size
    int length = value.length();
    for (int i = 0; i < length; i++)
    {
      char ch = value.charAt(i);

      switch (ch)
      {
      case '\n':
        heightLength++;

        if (widthLength < currentLineLength)
          widthLength = currentLineLength;

        currentLineLength = 0;
        continue;
      case '\t':
        currentLineLength += tabLength;
        continue;
      default:
        currentLineLength++;
      }
    }

    // check last line width
    if (widthLength < currentLineLength)
      widthLength = currentLineLength;

    Vector2 shifts = Vector2.getInstance(2);
    shifts.addToX((widthLength * charWidth) / 2);
    shifts.addToY((heightLength * charHeight) / 2);
    return shifts;
  }

  private void rebuild()
  {
    MeshMode mode = geometryData.getMode();
    geometryData = GameContext.resources.getGeometry(new LabelGeometrySource(value, mode, charWidth, charHeight));
  }

  public enum AlignType
  {
    Center,
    TopRight,
    TopLeft,
    TopCenter,
    BottomRight,
    BottomLeft,
    BottomCenter
  }

  private static class LabelGeometrySource
    extends GeometrySource
  {
    private String value;
    private float charWidth;
    private float charHeight;

    public LabelGeometrySource(String value, MeshMode mode, float charWidth, float charHeight)
    {
      super(String.format("String: %s Width: %d Height %d", value, (int)charWidth, (int)charHeight), mode);

      this.value = value;
      this.charWidth = charWidth;
      this.charHeight = charHeight;
    }

    @Override
    public String getName() // Disable cache for dynamic label.
    {
      if (mode == MeshMode.Dynamic)
        return null;

      return super.getName();
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
          carriagePosition += tabLength;
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

      FloatBuffer buffer = loadGeometry(geometry);
      return new LoadResult(buffer, geometry.length / 12, Vector3.zero, Vector3.zero);
    }
  }
}
