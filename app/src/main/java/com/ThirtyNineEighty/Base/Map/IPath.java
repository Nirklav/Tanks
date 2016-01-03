package com.ThirtyNineEighty.Base.Map;

import com.ThirtyNineEighty.Base.Common.Math.Vector3;

import java.io.Serializable;

public interface IPath
  extends Serializable
{
  float distance();

  Vector3 start();
  Vector3 end();

  boolean moveObject();
  void release();
}
