package com.ThirtyNineEighty.Helpers;

public class VectorUtils
{
  public static float[] xAxis = { 1.0f, 0.0f, 0.0f, 0.0f };
  public static float[] yAxis = { 0.0f, 1.0f, 0.0f, 0.0f };
  public static float[] zAxis = { 0.0f, 0.0f, 1.0f, 0.0f };

  public static float getLength3(float[] vector, int offset)
  {
    double powX = Math.pow(vector[0 + offset], 2);
    double powY = Math.pow(vector[1 + offset], 2);
    double powZ = Math.pow(vector[2 + offset], 2);

    return (float)Math.sqrt(powX + powY + powZ);
  }

  public static float getLength2(float[] vector, int offset)
  {
    double powX = Math.pow(vector[0 + offset], 2);
    double powY = Math.pow(vector[1 + offset], 2);

    return (float)Math.sqrt(powX + powY);
  }

  public static void normalize3(float[] vector, int offset)
  {
    float length = getLength3(vector, offset);

    vector[0] /= length;
    vector[1] /= length;
    vector[2] /= length;
  }

  public static void normalize2(float[] vector, int offset)
  {
    float length = getLength2(vector, offset);

    vector[0] /= length;
    vector[1] /= length;
  }

  public static float getAngle3(float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    float scalar = getScalar3(vectorOne, offsetOne, vectorTwo, offsetTwo);
    float lengthOne = getLength3(vectorOne, offsetOne);
    float lengthTwo = getLength3(vectorTwo, offsetTwo);

    return (float)Math.toDegrees(Math.acos(scalar / (lengthOne * lengthTwo)));
  }

  public static float getAngle2(float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    float scalar = getScalar2(vectorOne, offsetOne, vectorTwo, offsetTwo);
    float lengthOne = getLength2(vectorOne, offsetOne);
    float lengthTwo = getLength2(vectorTwo, offsetTwo);
    float angle = (float)Math.toDegrees(Math.acos(scalar / (lengthOne * lengthTwo)));

    return getCross2(vectorOne, offsetOne, vectorTwo, offsetTwo) > 0 ? angle : angle - 360;
  }

  public static float getScalar3(float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    float multOne   = vectorOne[0 + offsetOne] * vectorTwo[0 + offsetTwo];
    float multTwo   = vectorOne[1 + offsetOne] * vectorTwo[1 + offsetTwo];
    float multThree = vectorOne[2 + offsetOne] * vectorTwo[2 + offsetTwo];

    return multOne + multTwo + multThree;
  }

  public static float getScalar2(float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    float multOne   = vectorOne[0 + offsetOne] * vectorTwo[0 + offsetTwo];
    float multTwo   = vectorOne[1 + offsetOne] * vectorTwo[1 + offsetTwo];

    return multOne + multTwo;
  }

  public static void getCross3(float[] result, int resultOffset, float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    float result1 =      vectorOne[1 + offsetOne] * vectorTwo[2 + offsetTwo];
    float result2 = -1 * vectorOne[2 + offsetOne] * vectorTwo[1 + offsetTwo];
    float result3 = -1 * vectorOne[0 + offsetOne] * vectorTwo[2 + offsetTwo];
    float result4 =      vectorOne[2 + offsetOne] * vectorTwo[0 + offsetTwo];
    float result5 =      vectorOne[0 + offsetOne] * vectorTwo[1 + offsetTwo];
    float result6 = -1 * vectorOne[1 + offsetOne] * vectorTwo[0 + offsetTwo];

    result[0 + resultOffset] = result1 + result2;
    result[1 + resultOffset] = result3 + result4;
    result[2 + resultOffset] = result5 + result6;
  }

  public static float getCross2(float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    return vectorOne[0 + offsetOne] * vectorTwo[1 + offsetTwo] - vectorOne[1 + offsetOne] * vectorTwo[0 + offsetTwo];
  }

  public static void getVector3(float[] result, int resultOffset, float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    result[0 + resultOffset] = vectorOne[0 + offsetOne] - vectorTwo[0 - offsetTwo];
    result[1 + resultOffset] = vectorOne[1 + offsetOne] - vectorTwo[1 - offsetTwo];
    result[2 + resultOffset] = vectorOne[2 + offsetOne] - vectorTwo[2 - offsetTwo];
  }

  public static void getVector2(float[] result, int resultOffset, float[] vectorOne, int offsetOne, float[] vectorTwo, int offsetTwo)
  {
    result[0 + resultOffset] = vectorOne[0 + offsetOne] - vectorTwo[0 - offsetTwo];
    result[1 + resultOffset] = vectorOne[1 + offsetOne] - vectorTwo[1 - offsetTwo];
  }
}
