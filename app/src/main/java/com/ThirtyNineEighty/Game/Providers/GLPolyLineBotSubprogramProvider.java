package com.ThirtyNineEighty.Game.Providers;

import com.ThirtyNineEighty.Base.Common.Math.Vector;
import com.ThirtyNineEighty.Base.Common.Math.Vector2;
import com.ThirtyNineEighty.Base.Common.Math.Vector3;
import com.ThirtyNineEighty.Base.Map.IPath;
import com.ThirtyNineEighty.Base.Providers.RenderableDataProvider;
import com.ThirtyNineEighty.Base.Renderable.GL.GLPolyLine;
import com.ThirtyNineEighty.Game.Map.Path;
import com.ThirtyNineEighty.Game.Subprograms.BotSubprogram;

import java.util.ArrayList;

public class GLPolyLineBotSubprogramProvider
  extends RenderableDataProvider<GLPolyLine.Data>
{
  private final BotSubprogram bot;

  public GLPolyLineBotSubprogramProvider(BotSubprogram bot)
  {
    super(GLPolyLine.Data.class);

    this.bot = bot;
  }

  @Override
  public void set(GLPolyLine.Data data)
  {
    super.set(data);

    IPath iPath = bot.getPath();
    if (iPath == null)
      return;

    if (!(iPath instanceof Path))
      return;

    Path path = (Path) iPath;
    ArrayList<Vector2> rawPath = path.getPath();

    int index = 0;
    for (Vector2 point : rawPath)
    {
      Vector3 resultPoint;
      if (index >= data.points.size())
      {
        resultPoint = Vector.getInstance(3, point);
        data.points.add(resultPoint);
      }
      else
      {
        resultPoint = data.points.get(index);
        resultPoint.setFrom(point);
      }

      index++;
    }

    data.color.setFrom(1, 1, 0);
    data.lineSize = 3;
  }
}
