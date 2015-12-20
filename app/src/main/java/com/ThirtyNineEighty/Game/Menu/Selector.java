package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Resources.Sources.FileContentSource;
import com.ThirtyNineEighty.System.GameContext;

import java.util.List;

public class Selector
{
  private List<String> values;
  private int selectedIndex;
  private Callback onChange;

  public Selector(String resourceContentName, Callback onChange)
  {
    this.values = GameContext.resources.getContent(new FileContentSource(resourceContentName));
    this.onChange = onChange;
  }

  public Selector(List<String> values, Callback onChange)
  {
    this.values = values;
    this.onChange = onChange;
  }

  public String getCurrent()
  {
    return values.get(selectedIndex);
  }

  public void Next()
  {
    selectedIndex++;
    if (selectedIndex >= values.size())
      selectedIndex = 0;

    if (onChange != null)
      onChange.onChange(values.get(selectedIndex));
  }

  public void Prev()
  {
    selectedIndex--;
    if (selectedIndex < 0)
      selectedIndex = values.size() - 1;

    if (onChange != null)
      onChange.onChange(values.get(selectedIndex));
  }

  public interface Callback
  {
    void onChange(String current);
  }
}
