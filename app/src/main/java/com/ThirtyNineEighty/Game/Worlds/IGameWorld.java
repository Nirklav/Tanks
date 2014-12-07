package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.IEngineObject;
import com.ThirtyNineEighty.Helpers.Vector3;

public interface IGameWorld extends IWorld
{
  void addObject(IEngineObject object);

  void move(IEngineObject engineObject, float length);
  void rotate(IEngineObject engineObject, Vector3 angles);
}
