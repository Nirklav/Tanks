package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Game.Gameplay.Characteristics.Characteristic;
import com.ThirtyNineEighty.Game.Gameplay.Land;
import com.ThirtyNineEighty.Game.Gameplay.MapDescription;
import com.ThirtyNineEighty.Game.Gameplay.Tank;
import com.ThirtyNineEighty.Game.Menu.GameMenu;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.ISubprogram;
import com.ThirtyNineEighty.System.Subprogram;

public class GameWorld
  extends BaseWorld
{
  private ISubprogram worldProgram;
  private ISubprogram collideProgram;

  @Override
  public void initialize(Object obj)
  {
    if (!(obj instanceof GameStartArgs))
      throw new IllegalArgumentException("Illegal args type");

    GameStartArgs args = (GameStartArgs) obj;
    MapDescription map = GameContext.mapManager.load(args.getMapName());

    player = new Tank(args.getTankName());
    player.setPosition(map.player.getPosition());
    player.setAngles(map.player.getAngles());

    add(player);
    add(new Land());

    GameContext.content.setMenu(new GameMenu());
    GameContext.content.bindProgram(worldProgram = new Subprogram() // TODO: move this code in button callbacks
    {
      @Override
      public void onUpdate()
      {
        GameMenu menu = (GameMenu) GameContext.content.getMenu();
        Tank playerTank = (Tank) player;

        Vector3 vector = Vector.getInstance(3);

        float joyAngle = menu.getJoystickAngle();
        float playerAngle = playerTank.getAngles().getZ();

        if (Math.abs(joyAngle - playerAngle) < 90)
          GameContext.collisionManager.move(playerTank);

        if (Math.abs(joyAngle - playerAngle) > 3)
          GameContext.collisionManager.rotate(playerTank, joyAngle);

        Characteristic playerCh = playerTank.getCharacteristics();
        if (menu.getLeftTurretState())
          playerTank.addTurretAngle(playerCh.getTurretRotationSpeed() * GameContext.getDelta());

        if (menu.getRightTurretState())
          playerTank.addTurretAngle(-playerCh.getTurretRotationSpeed() * GameContext.getDelta());

        Vector.release(vector);
      }
    });

    GameContext.content.bindLastProgram(collideProgram = new Subprogram()
    {
      @Override
      public void onUpdate()
      {
        // resolve all collisions
        GameContext.collisionManager.resolve();
      }
    });

    super.initialize(args);
  }

  @Override
  public void uninitialize()
  {
    GameContext.content.unbindProgram(worldProgram);
    GameContext.content.unbindLastProgram();

    super.uninitialize();
  }

  @Override
  public void enable()
  {
    worldProgram.enable();
    collideProgram.enable();

    super.enable();
  }

  @Override
  public void disable()
  {
    worldProgram.disable();
    collideProgram.disable();

    super.disable();
  }

  @Override
  public void setCamera(Camera camera)
  {
    camera.target.setFrom(player.getPosition());
    camera.eye.setFrom(player.getPosition());

    camera.eye.addToY(14);
    camera.eye.addToZ(35);
  }

  @Override
  public void setLight(Vector3 lightPosition)
  {
    lightPosition.setFrom(player.getPosition());
    lightPosition.setZ(30);
  }
}
