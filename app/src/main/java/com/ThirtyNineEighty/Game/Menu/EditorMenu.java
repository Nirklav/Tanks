package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.DeltaTime;
import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Menus.Selector;
import com.ThirtyNineEighty.Base.Providers.GLLabelProvider;
import com.ThirtyNineEighty.Base.Renderable.Subprograms.DelayedRenderableSubprogram;
import com.ThirtyNineEighty.Base.Resources.Entities.ContentNames;
import com.ThirtyNineEighty.Game.Common.EditorExporter;
import com.ThirtyNineEighty.Game.Common.LoadException;
import com.ThirtyNineEighty.Base.Common.Math.*;
import com.ThirtyNineEighty.Base.Menus.Controls.*;
import com.ThirtyNineEighty.Base.Subprograms.Subprogram;
import com.ThirtyNineEighty.Game.ContentState.States.EditorImportState;
import com.ThirtyNineEighty.Game.ContentState.States.MainState;
import com.ThirtyNineEighty.Game.TanksContext;
import com.ThirtyNineEighty.Game.Worlds.EditorWorld;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Base.Resources.Sources.FileContentSource;

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
    categories.add(FileContentSource.bonuses);
  }

  private Joystick joystick;
  private GLLabelProvider categoryLabel;
  private GLLabelProvider objectLabel;
  private GLLabelProvider messageLabel;
  private Selector categorySelector;
  private Selector objectSelector;
  private Button rightRotate;
  private Button leftRotate;
  private ToggleButton smallSpeed;

  private transient ContentNames objects;

  @Override
  public void initialize()
  {
    super.initialize();

    bind(new Subprogram()
    {
      @Override
      public void onUpdate()
      {
        EditorWorld world = (EditorWorld) TanksContext.content.getWorld();

        processCamera(world);
        processRotation(world);
      }
    });

    add(joystick = new Joystick(-710, -290, 150));

    messageLabel = new GLLabelProvider(" ", MeshMode.Dynamic);
    messageLabel.setVisible(false);
    bind(new GLLabel(messageLabel));

    Button menuButton = new Button("Menu");
    menuButton.setPosition(-710, 465);
    menuButton.setSize(500, 150);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TanksContext.contentState.set(new MainState(true));
      }
    });
    add(menuButton);

    categoryLabel = new GLLabelProvider(FileContentSource.decors, MeshMode.Dynamic);
    categoryLabel.setPosition(-960, 310);
    categoryLabel.setAlign(GLLabelProvider.AlignType.TopLeft);
    bind(new GLLabel(categoryLabel));

    Button nextCategory = new Button("Next category");
    nextCategory.setPosition(-710, 210);
    nextCategory.setSize(500, 150);
    nextCategory.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        categorySelector.next();
      }
    });
    add(nextCategory);

    objectLabel = new GLLabelProvider(" ", MeshMode.Dynamic);
    objectLabel.setPosition(-960, 40);
    objectLabel.setAlign(GLLabelProvider.AlignType.TopLeft);
    bind(new GLLabel(objectLabel));

    Button nextObject = new Button("Next object");
    nextObject.setPosition(-710, -60);
    nextObject.setSize(500, 150);
    nextObject.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        objectSelector.next();
      }
    });
    add(nextObject);

    rightRotate = new Button("Turn right");
    rightRotate.setPosition(760, -465);
    rightRotate.setSize(400, 150);
    add(rightRotate);

    leftRotate = new Button("Turn left");
    leftRotate.setPosition(350, -465);
    leftRotate.setSize(400, 150);
    add(leftRotate);

    Button apply = new Button("Apply");
    apply.setPosition(760, 465);
    apply.setSize(400, 150);
    apply.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        EditorWorld world = (EditorWorld) TanksContext.content.getWorld();
        if (world.isObjectCreated())
          world.apply();
      }
    });
    add(apply);

    Button destroy = new Button("Destroy");
    destroy.setPosition(760, 310);
    destroy.setSize(400, 150);
    destroy.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        EditorWorld world = (EditorWorld) TanksContext.content.getWorld();
        if (world.isObjectCreated())
          world.destroy();
      }
    });
    add(destroy);

    smallSpeed = new ToggleButton("Small speed");
    smallSpeed.setPosition(760, 155);
    smallSpeed.setSize(400, 150);
    add(smallSpeed);

    Button exportBtn = new Button("Export");
    exportBtn.setPosition(760, -50);
    exportBtn.setSize(400, 150);
    exportBtn.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          IWorld world = TanksContext.content.getWorld();
          EditorExporter.exportMap(world);
          showMessage("Exported");
        }
        catch (LoadException e)
        {
          showMessage(e.getMessage());
        }
      }
    });
    add(exportBtn);

    Button importBtn = new Button("Import");
    importBtn.setPosition(760, -220);
    importBtn.setSize(400, 150);
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

        TanksContext.contentState.set(new EditorImportState());
      }
    });
    add(importBtn);

    setCategorySelector();
    setObjectSelector(FileContentSource.decors);
  }

  @Override
  public void uninitialize()
  {
    super.uninitialize();

    TanksContext.resources.release(objects);
  }

  private void processCamera(EditorWorld world)
  {
    Vector2 joyVector = joystick.getVector();
    Vector3 delta = Vector3.getInstance();

    float speed = smallSpeed.getState()
      ? smallCameraSpeed
      : cameraSpeed;

    delta.setFrom(joyVector);
    delta.normalize();
    delta.multiply(speed * DeltaTime.get());

    world.addToCameraPosition(delta);

    Vector3.release(delta);
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
      Vector3 delta = Vector3.getInstance(0, 0, -speed * DeltaTime.get());
      world.addToObjectAngles(delta);
      Vector3.release(delta);
    }

    if (leftRotate.getState())
    {
      Vector3 delta = Vector3.getInstance(0, 0, speed * DeltaTime.get());
      world.addToObjectAngles(delta);
      Vector3.release(delta);
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
    objects = TanksContext.resources.getContent(new FileContentSource(category));
    objectSelector = new Selector(objects.names, new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        EditorWorld world = (EditorWorld) TanksContext.content.getWorld();

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
    bind(new DelayedRenderableSubprogram(messageLabel, 5000));
  }
}
