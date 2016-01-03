package com.ThirtyNineEighty.Game.Map;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;

class PathNode
{
  public PathNode from;
  public Vector2 position;
  public float lengthFromStart;
  public float estimateLeftLength;

  public PathNode(Vector2 position, float lengthFromStart, float estimateLeftLength)
  {
    this(null, position, lengthFromStart, estimateLeftLength);
  }

  public PathNode(PathNode from, Vector2 position, float lengthFromStart, float estimateLeftLength)
  {
    this.from = from;
    this.position = position;
    this.lengthFromStart = lengthFromStart;
    this.estimateLeftLength = estimateLeftLength;
  }

  public float getFullLength()
  {
    return lengthFromStart + estimateLeftLength;
  }
}
