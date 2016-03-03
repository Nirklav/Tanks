package com.ThirtyNineEighty.Base.Common.Math;

public class Spiral
{
  public static Vector2 get(int index)
  {
    Vector2 result = Vector2.getInstance();

    int direction = 0; // Right
    int currentPosition = 0;
    int linePosition = 0;
    int lineLength = 1;
    int lineIteration = 0;

    while (true)
    {
      if (currentPosition == index)
        return result;

      if (linePosition == lineLength)
      {
        lineIteration++;
        if (lineIteration == 2)
        {
          lineLength++;
          lineIteration = 0;
        }

        direction = getNextDirection(direction);
        linePosition = 0;
      }

      getNextPoint(direction, result);

      linePosition++;
      currentPosition++;
    }
  }

  private static void getNextPoint(int direction, Vector2 vector)
  {
    switch (direction)
    {
    case 0: // Right
      vector.addToX(1);
      return;
    case 1: // Up
      vector.addToY(1);
      return;
    case 2: // Left
      vector.addToX(-1);
      return;
    case 3: // Down
      vector.addToY(-1);
      return;
    }
  }

  private static int getNextDirection(int current)
  {
    current += 1;
    if (current > 3)
      return 0;
    return current;
  }
}
