package com.ThirtyNineEighty.Game.Menu;


import com.ThirtyNineEighty.Common.EditorExporter;
import com.ThirtyNineEighty.Common.Math.*;
import com.ThirtyNineEighty.Game.Menu.Controls.*;
import com.ThirtyNineEighty.Game.Subprograms.Subprogram;
import com.ThirtyNineEighty.Game.Worlds.EditorWorld;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.System.GameContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditorMenu
  extends BaseMenu
{
  private static final float cameraSpeed = 20;
  private static final float smallCameraSpeed = 5;
  private static final float rotationSpeed = 45;
  private static final float smallRotationSpeed = 12.5f;

  private static final ArrayList<String> categories;
  static
  {
    categories = new ArrayList<>();
    categories.add(FileContentSource.decors);
    categories.add(FileContentSource.bots);
  }

  private Joystick joystick;
  private GLLabel categoryLabel;
  private GLLabel objectLabel;
  private GLLabel messageLabel;
  private Selector categorySelector;
  private Selector objectSelector;
  private Button rightRotate;
  private Button leftRotate;
  private ToggleButton smallSpeed;

  @Override
  public void initialize()
  {
    super.initialize();

    bind(new Subprogram()
    {
      @Override
      public void onUpdate()
      {
        EditorWorld world = (EditorWorld) GameContext.content.getWorld();

        processCamera(world);
        processRotation(world);
      }
    });

    add(joystick = new Joystick(-710, -290, 150));

    messageLabel = new GLLabel(" ", MeshMode.Dynamic);
    messageLabel.setVisible(false);
    bind(messageLabel);

    Button menuButton = new Button("Menu");
    menuButton.setPosition(-710, 465);
    menuButton.setSize(500, 150);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        GameContext.content.setWorld(null);
        GameContext.content.setMenu(new MainMenu());
      }
    });
    add(menuButton);

    categoryLabel = new GLLabel(FileContentSource.decors, MeshMode.Dynamic);
    categoryLabel.setPosition(-960, 310);
    categoryLabel.setAlign(GLLabel.AlignType.TopLeft);
    bind(categoryLabel);

    Button nextCategory = new Button("Next category");
    nextCategory.setPosition(-710, 210);
    nextCategory.setSize(500, 150);
    nextCategory.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        categorySelector.Next();
      }
    });
    add(nextCategory);

    objectLabel = new GLLabel(" ", MeshMode.Dynamic);
    objectLabel.setPosition(-960, 40);
    objectLabel.setAlign(GLLabel.AlignType.TopLeft);
    bind(objectLabel);

    Button nextObject = new Button("Next object");
    nextObject.setPosition(-710, -60);
    nextObject.setSize(500, 150);
    nextObject.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        objectSelector.Next();
      }
    });
    add(nextObject);

    rightRotate = new Button("Turn right");
    rightRotate.setPosition(785, -465);
    rightRotate.setSize(350, 150);
    add(rightRotate);

    leftRotate = new Button("Turn left");
    leftRotate.setPosition(420, -465);
    leftRotate.setSize(350, 150);
    add(leftRotate);

    Button apply = new Button("Apply");
    apply.setPosition(710, 465);
    apply.setSize(500, 150);
    apply.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        EditorWorld world = (EditorWorld) GameContext.content.getWorld();
        if (world.isObjectCreated())
          world.apply();
      }
    });
    add(apply);

    smallSpeed = new ToggleButton("Small speed");
    smallSpeed.setPosition(710, 310);
    smallSpeed.setSize(500, 150);
    add(smallSpeed);

    Button exportBtn = new Button("Export");
    exportBtn.setPosition(710, 0);
    exportBtn.setSize(500, 150);
    exportBtn.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          IWorld world = GameContext.content.getWorld();
          EditorExporter.exportMap(world);
          showMessage("Exported");
        }
        catch (IOException e)
        {
          showMessage(e.getMessage());
        }
      }
    });
    add(exportBtn);

    Button importBtn = new Button("Import");
    importBtn.setPosition(710, -170);
    importBtn.setSize(500, 150);
    importBtn.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        List<String> maps = EditorExporter.getMaps();
        if (maps == null || maps.size() == 0)
        {
          showMessage("Maps not found");
          return;
        }

        GameContext.content.setMenu(new EditorImportMenu());
      }
    });
    add(importBtn);

    setCategorySelector();
    setObjectSelector(FileContentSource.decors);
  }

  private void processCamera(EditorWorld world)
  {
    Vector2 joyVector = joystick.getVector();
    Vector3 delta = Vector.getInstance(3);

    float speed = smallSpeed.getState()
      ? smallCameraSpeed
      : cameraSpeed;

    delta.setFrom(joyVector);
    delta.normalize();
    delta.multiply(speed * GameContext.getDelta());

    world.addToCameraPosition(delta);

    Vector.release(delta);
  }

  private void processRotation(EditorWorld world)
  {
    if (!world.isObjectCreated())
      return;

    float speed = smallSpeed.getState()
      ? smallRotationSpeed
      : rotationSpeed;

    if (rightRotate.getState())
    {
      Vector3 delta = Vector.getInstance(3, 0, 0, -speed * GameContext.getDelta());
      world.addToObjectAngles(delta);
      Vector.release(delta);
    }

    if (leftRotate.getState())
    {
      Vector3 delta = Vector.getInstance(3, 0, 0, speed * GameContext.getDelta());
      world.addToObjectAngles(delta);
      Vector.release(delta);
    }
  }

  private void setCategorySelector()
  {
    categorySelector = new Selector(categories, new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        setObjectSelector(current);
        categoryLabel.setValue(current);
      }
    });
  }

  private void setObjectSelector(String category)
  {
    objectSelector = new Selector(category, new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        EditorWorld world = (EditorWorld) GameContext.content.getWorld();

        if (world.isObjectCreated())
          world.destroy();

        world.create(current);

        objectLabel.setValue(current);
      }
    });
  }

  private void showMessage(String message)
  {
    messageLabel.setVisible(true);
    messageLabel.setValue(message);

    bind(new Subprogram()
    {
      boolean delayed;

      @Override
      protected void onUpdate()
      {
        if (!delayed)
        {
          delay(5000);
          delayed = true;
          return;
        }

        messageLabel.setVisible(false);
        unbind();
      }
    });
  }
}
