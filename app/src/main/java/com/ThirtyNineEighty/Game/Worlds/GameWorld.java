package com.ThirtyNineEighty.Game.Worlds;

import com.ThirtyNineEighty.Resources.Entities.Characteristic;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Game.Menu.GameMenu;
import com.ThirtyNineEighty.Helpers.Vector;
import com.ThirtyNineEighty.Helpers.Vector3;
import com.ThirtyNineEighty.Renderable.Camera;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

public class GameWorld
  extends BaseWorld
{
  @Override
  public void initialize(Object obj)
  {
    if (!(obj instanceof GameStartArgs))
      throw new IllegalArgumentException("Illegal args type");

    GameStartArgs args = (GameStartArgs) obj;
    player = GameContext.mapManager.load(args);

    GameContext.content.setMenu(new GameMenu());
    bindProgram(new Subprogram() // TODO: move this code in button callbacks
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
          GameContext.collisions.move(playerTank);

        if (Math.abs(joyAngle - playerAngle) > 3)
          GameContext.collisions.rotate(playerTank, joyAngle);

        Characteristic playerCh = playerTank.getCharacteristic();
        if (menu.getLeftTurretState())
          playerTank.addTurretAngle(playerCh.getTurretRotationSpeed() * GameContext.getDelta());

        if (menu.getRightTurretState())
          playerTank.addTurretAngle(-playerCh.getTurretRotationSpeed() * GameContext.getDelta());

        Vector.release(vector);
      }
    });

    GameContext.content.bindLastProgram(new Subprogram()
    {
      @Override
      public void onUpdate()
      {
        // resolve all collisions
        GameContext.collisions.resolve();
      }
    });

    super.initialize(args);
  }

  @Override
  public void uninitialize()
  {
    GameContext.content.unbindLastProgram();
    super.uninitialize();
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
