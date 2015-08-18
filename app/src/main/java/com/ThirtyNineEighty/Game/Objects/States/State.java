package com.ThirtyNineEighty.Game.Objects.States;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Game.Objects.Properties.Properties;

import java.io.Serializable;

public class State
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  protected String type;
  protected String name;
  protected Properties properties;

  protected Vector3 position;
  protected Vector3 angles;

  public String getType() { return type; }
  public void setType(String value) { type = value; }

  public String getName() { return name; }
  public void setName(String value) { name = value; }

  public Properties getProperties() { return properties; }
  public void setProperties(Properties value) { properties = value; }

  public Vector3 getPosition() { return position; }
  public void setPosition(Vector3 value) { position = value; }

  public Vector3 getAngles() { return angles; }
  public void setAngles(Vector3 value) { angles = value; }
}