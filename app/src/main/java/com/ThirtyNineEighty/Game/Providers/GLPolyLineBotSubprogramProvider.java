package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Map.IPath;
import com.ThirtyNineEighty.Base.Providers.RenderableDataProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLPolyLine;
import com.ThirtyNineEighty.Game.Map.Path;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Subprograms.BotSubprogram;

import java.util.ArrayList;

public class GLPolyLineBotSubprogramProvider
  extends RenderableDataProvider<GLPolyLine.Data>
{
  private static final long serialVersionUID = 1L;

  private final GameObject<?, ?> bot;
  private final BotSubprogram botSubprogram;

  public GLPolyLineBotSubprogramProvider(GameObject<?, ?> bot, BotSubprogram botSubprogram)
  {
    super(GLPolyLine.Data.class);

    this.bot = bot;
    this.botSubprogram = botSubprogram;
  }

  @Override
  public void set(GLPolyLine.Data data)
  {
    if (bot.getHealth() <= 0)
    {
      setVisible(false);
      super.set(data);
      return;
    }

    IPath iPath = botSubprogram.getPath();
    if (iPath == null)
    {
      setVisible(false);
      super.set(data);
      return;
    }

    if (!(iPath instanceof Path))
    {
      setVisible(false);
      super.set(data);
      return;
    }

    setVisible(true);
    super.set(data);

    Path path = (Path) iPath;
    ArrayList<Vector2> rawPath = path.getPath();

    Vector3.release(data.points);
    data.points.clear();

    for (Vector2 point : rawPath)
    {
      Vector3 resultPoint = Vector3.getInstance(point);
      resultPoint.setZ(0.5f);
      data.points.add(resultPoint);
    }

    data.color.setFrom(1, 1, 0);
    data.lineSize = 3;
  }
}
