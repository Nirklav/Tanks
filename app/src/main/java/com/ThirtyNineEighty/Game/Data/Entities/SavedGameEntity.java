package com.ThirtyNineEighty.Game.Data.Entities;

import com.ThirtyNineEighty.Game.Objects.States.State;

import java.io.Serializable;
import java.util.ArrayList;

public class SavedGameEntity
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public State player;
  public ArrayList<State> objects;

  public SavedGameEntity(State player, ArrayList<State> objects)
  {
    this.player = player;
    this.objects = objects;
  }
}
