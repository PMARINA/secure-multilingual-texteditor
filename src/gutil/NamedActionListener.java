package gutil;

import java.awt.event.*;

/**
 * @author yangbai
 */
public class NamedActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        NamedMenuItem m = (NamedMenuItem)e.getSource();
        Action.execute(m.getInternalName());
    }
}
