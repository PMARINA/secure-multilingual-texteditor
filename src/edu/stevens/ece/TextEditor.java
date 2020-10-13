package edu.stevens.ece;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import gutil.*;

public class TextEditor extends App {
	private JTextPane editor;
	private DefaultStyledDocument doc;
	public TextEditor() throws Exception {
		super(new Conf("conf/stex.conf")); // TODO: get conf file from home directory
		setSize(1000,800);
		editor = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor);
		editor.setDocument(doc = new DefaultStyledDocument());
		add(editorScrollPane, BorderLayout.CENTER);
		setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		new TextEditor();
	}
}
