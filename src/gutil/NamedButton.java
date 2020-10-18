package gutil;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author dkruger
 * JButton with an embedded name that automatically triggers actions with the right name
 */
public class NamedButton extends JButton {
    private String name;
    public String getInternalName() { return name; }
    public NamedButton(String text, String name, NamedActionListener listener) {
        super(text);
        this.name = name;
        addActionListener(listener);
    }
    public NamedButton(ImageIcon img, String name, NamedActionListener listener) {
        super(img);
        this.name = name;
        addActionListener(listener);
    }
}
