package com.ThirtyNineEighty.Base.Menus;

import java.util.List;

public class Selector
{
  private List<String> values;
  private int selectedIndex;
  private Callback onChange;

  public Selector(List<String> values, Callback onChange)
  {
    this.values = values;
    this.onChange = onChange;
  }

  public String getCurrent()
  {
    return values.get(selectedIndex);
  }

  public void next()
  {
    selectedIndex++;
    if (selectedIndex >= values.size())
      selectedIndex = 0;

    if (onChange != null)
      onChange.onChange(values.get(selectedIndex));
  }

  public void prev()
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
