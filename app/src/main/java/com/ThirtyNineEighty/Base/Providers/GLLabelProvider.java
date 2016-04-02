package com.ThirtyNineEighty.Base.Providers;

import android.opengl.Matrix;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;

// Can process only \n and \t spec symbols
public class GLLabelProvider
  extends RenderableDataProvider<GLLabel.Data>
{
  private static final long serialVersionUID = 1L;

  private static final int StandardWidth = 30;
  private static final int StandardHeight = 40;

  private float[] modelMatrix;
  private boolean needRebuildMatrix;

  private int charWidth;
  private int charHeight;
  private String value;
  private MeshMode mode;
  private float angle;
  private AlignType alignType;
  private Vector3 position;
  private Vector3 originalPosition;
  private Vector3 colorCoefficients;

  public GLLabelProvider(String value)
  {
    this(value, StandardWidth, StandardHeight, MeshMode.Static);
  }

  public GLLabelProvider(String value, MeshMode mode)
  {
    this(value, StandardWidth, StandardHeight, mode);
  }

  public GLLabelProvider(String value, int charWidth, int charHeight, MeshMode mode)
  {
    super(GLLabel.Data.class);

    this.charWidth = charWidth;
    this.charHeight = charHeight;
    this.mode = mode;
    this.colorCoefficients = Vector3.getInstance(1, 1, 0);
    this.modelMatrix = new float[16];
    this.alignType = AlignType.Center;
    this.position = Vector3.getInstance();
    this.originalPosition = Vector3.getInstance();
    this.needRebuildMatrix = true;

    setValue(value);
  }

  @Override
  public void set(GLLabel.Data data)
  {
    super.set(data);

    tryRebuildMatrix();

    data.value = value;
    data.mode = mode;
    data.charWidth = charWidth;
    data.charHeight = charHeight;
    data.colorCoefficients.setFrom(colorCoefficients);
    System.arraycopy(modelMatrix, 0, data.modelMatrix, 0, 16);
  }

  private void tryRebuildMatrix()
  {
    if (!needRebuildMatrix)
      return;

    Matrix.setIdentityM(modelMatrix, 0);
    Matrix.translateM(modelMatrix, 0, position.getX(), position.getY(), position.getZ());
    Matrix.rotateM(modelMatrix, 0, angle, 0, 0, 1);

    needRebuildMatrix = false;
  }

  public void setCharSize(int width, int height)
  {
    charWidth = width;
    charHeight = height;

    setPosition();
  }

  public void setPosition(float x, float y)
  {
    originalPosition.setX(x);
    originalPosition.setY(y);

    setPosition();
  }

  public void setPosition(Vector2 value)
  {
    originalPosition.setFrom(value);

    setPosition();
  }

  public void setColorCoefficients(Vector3 coefficients)
  {
    colorCoefficients.setFrom(coefficients);
  }

  public void setAngle(float value)
  {
    angle = value;
    needRebuildMatrix = true;
  }

  public void setZIndex(float value)
  {
    position.setY(value);
    needRebuildMatrix = true;
  }

  public void setValue(String str)
  {
    if (str == null)
      str = "";

    if (str.equals(value))
      return;

    value = str;
    setPosition();
  }

  public void setAlign(AlignType value)
  {
    alignType = value;
    setPosition();
  }

  private void setPosition()
  {
    needRebuildMatrix = true;
    Vector3 shift;

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
      shift = Vector3.getInstance();
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

    position.setFrom(originalPosition);
    position.subtract(shift);
    Vector3.release(shift);
  }

  private Vector3 getCenterShift()
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
        currentLineLength += GLLabel.tabLength;
        continue;
      default:
        currentLineLength++;
      }
    }

    // check last line width
    if (widthLength < currentLineLength)
      widthLength = currentLineLength;

    Vector3 shifts = Vector3.getInstance();
    shifts.addToX((widthLength * charWidth) / 2);
    shifts.addToY((heightLength * charHeight) / 2);
    return shifts;
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
