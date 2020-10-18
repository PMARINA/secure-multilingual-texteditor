package gutil;

import javax.swing.JMenuItem;

/**
 * @author dkruger
 */
public class NamedMenuItem extends JMenuItem {
    private String name;
    public String getInternalName() { return name; }
    public NamedMenuItem(String text, String name, NamedActionListener listener) {
        super(text);
        this.name = name;
        addActionListener(listener);
    }
}
