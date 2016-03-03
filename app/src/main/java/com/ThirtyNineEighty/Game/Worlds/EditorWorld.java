package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Base.Objects.WorldObject;
import com.ThirtyNineEighty.Base.Worlds.BaseWorld;
import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Game.Objects.*;
import com.ThirtyNineEighty.Base.Objects.Descriptions.Description;
import com.ThirtyNineEighty.Base.Renderable.Common.*;
import com.ThirtyNineEighty.Game.TanksContext;

public class EditorWorld
  extends BaseWorld
{
  private static final long serialVersionUID = 1L;

  private static final float lightHeight = 100;
  private static final float lightX = 50;
  private static final float lightY = 50;

  private Vector3 cameraPosition;
  private Land land;
  private WorldObject<?, ?> currentObject;

  public EditorWorld()
  {
    cameraPosition = Vector3.getInstance();

    add(land = new Land(Land.sand));

    bind(new Camera(new Camera.Setter()
    {
      @Override
      public void set(Camera.Data camera)
      {
        camera.target.setFrom(cameraPosition);
        camera.eye.setFrom(cameraPosition);

        camera.eye.addToY(14);
        camera.eye.addToZ(35);
      }
    }));

    bind(new Light(new Light.Setter()
    {
      @Override
      public void set(Light.Data light)
      {
        light.Position.setFrom(lightX, lightY, lightHeight);
      }
    }));
  }

  public boolean isObjectCreated()
  {
    return currentObject != null;
  }

  public void create(String descriptionStr)
  {
    currentObject = TanksContext.factory.createObject(descriptionStr);
    currentObject.setPosition(cameraPosition);

    add(currentObject);
  }

  public void apply()
  {
    if (currentObject == null)
      throw new IllegalStateException("object not created");

    Description description = currentObject.getDescription();
    Vector3 position = currentObject.getPosition();
    Vector3 angles = currentObject.getAngles();

    // create new
    currentObject = TanksContext.factory.createObject(description.getName());
    currentObject.setPosition(position);
    currentObject.setAngles(angles);
    add(currentObject);
  }

  public void destroy()
  {
    if (currentObject == null)
      throw new IllegalStateException("object not created");

    remove(currentObject);
  }

  public void addToObjectAngles(Vector3 delta)
  {
    if (currentObject == null)
      throw new IllegalStateException("object not created");

    Vector3 angles = currentObject.getAngles();
    angles.add(delta);
  }

  public void addToCameraPosition(Vector3 delta)
  {
    cameraPosition.add(delta);

    Vector3 landPosition = land.getPosition();
    landPosition.add(delta);

    if (currentObject != null)
    {
      Vector3 position = currentObject.getPosition();
      position.add(delta);
    }
  }
}
