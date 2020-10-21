package gutil;

import java.awt.event.*;

/**
 * @author yangbai
 */
public class NamedActionListener implements ActionListener {
  private App app;
  public NamedActionListener(App app) {
    this.app = app;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    NamedMenuItem m = (NamedMenuItem)e.getSource();
    Action.execute(app, m.getInternalName());
  }
}
