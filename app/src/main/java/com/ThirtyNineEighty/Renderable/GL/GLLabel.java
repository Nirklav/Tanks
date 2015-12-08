package com.ThirtyNineEighty.Renderable.GL;

import com.ThirtyNineEighty.Common.Math.Vector;
import com.ThirtyNineEighty.Common.Math.Vector2;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.Resources.Sources.LabelGeometrySource;
import com.ThirtyNineEighty.System.GameContext;

// Can process only \n and \t spec symbols
// TODO: serializable
public class GLLabel
  extends GLSprite
{
  public static final int tabLength = 3;

  public static final String FontSimple = "SimpleFont";
  private static final int StandardWidth = 30;
  private static final int StandardHeight = 40;

  private int charWidth;
  private int charHeight;
  private String value;

  private AlignType alignType;
  private Vector2 originalPosition;

  public GLLabel(String value) { this(value, FontSimple, StandardWidth, StandardHeight, MeshMode.Static); }
  public GLLabel(String value, MeshMode mode) { this(value, FontSimple, StandardWidth, StandardHeight, mode); }
  public GLLabel(String value, String fontTexture, int charWidth, int chatHeight,  MeshMode mode)
  {
    super(fontTexture, new LabelGeometrySource(value, mode, charWidth, chatHeight));

    this.charWidth = charWidth;
    this.charHeight = chatHeight;
    this.value = value;

    colorCoefficients.setFrom(1, 1, 0);

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

  public void setCharSize(int width, int height)
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
}
