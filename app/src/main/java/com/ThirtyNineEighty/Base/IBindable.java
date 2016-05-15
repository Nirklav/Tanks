package com.ThirtyNineEighty.Base;

import com.ThirtyNineEighty.Base.Renderable.IView;
import com.ThirtyNineEighty.Base.Subprograms.ISubprogram;

import java.util.List;

public interface IBindable
  extends IEngineObject
{
  List<ISubprogram> getSubprograms();
  List<IView> getViews();

  void bind(ISubprogram subprogram);
  void unbind(ISubprogram subprogram);

  void bind(IView view);
  void unbind(IView view);

  void setViews();
}
