package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;

class PathNode
{
  public PathNode from;
  public Vector2 position;
  public float lengthFromStart;
  public float estimatedLeftLength;

  public PathNode(Vector2 position, Vector2 end)
  {
    this(null, position, end);
  }

  public PathNode(PathNode from, Vector2 position, Vector2 end)
  {
    this.from = from;
    this.position = position;
    this.estimatedLeftLength = getPathLength(position, end);

    if (from != null)
      this.lengthFromStart = from.lengthFromStart + Map.stepSize;
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private static float getPathLength(Vector2 start, Vector2 end)
  {
    float x = Math.abs(start.getX() - end.getX());
    float y = Math.abs(start.getY() - end.getY());
    return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
  }

  public float getFullLength()
  {
    return lengthFromStart + estimatedLeftLength;
  }
}
