package edu.stevens.ece;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;

public class TextEditor extends JFrame {
	private JTextPane editor;
	private DefaultStyledDocument doc;
	public TextEditor() {
		super("Text Editor");
		setSize(1000,800);
		editor = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor);
		editor.setDocument(doc = new DefaultStyledDocument());
		add(editorScrollPane, BorderLayout.CENTER);
		setVisible(true);
	}
	public static void main(String[] args) {
		new TextEditor();
	}
}
