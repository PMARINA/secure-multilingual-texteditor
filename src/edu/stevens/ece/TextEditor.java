package edu.stevens.ece;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import gutil.*;
import java.util.zip.*;
import java.io.*;

public class TextEditor extends App {
	private JTextPane editor;
	private DefaultStyledDocument doc;
	private static JFileChooser fileChooser;
	private static DefaultEditorKit editorKit;
	static {
		editorKit = new DefaultEditorKit();
		fileChooser = new JFileChooser();
	}
	public void statusAlert(Exception e) {
		e.printStackTrace(); // for now, just dump to console
	}
	public TextEditor() throws Exception {
		super(new Conf("conf/stex.conf")); // TODO: get conf file from home directory
		setSize(1000,800);
		editor = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor);
		editor.setDocument(doc = new DefaultStyledDocument());
		add(editorScrollPane, BorderLayout.CENTER);
		setVisible(true);
	}
	public void addActions() {
		
	}
	public void open() {
	}
	public void save() {
		try {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				GZIPOutputStream gz = new GZIPOutputStream(new FileOutputStream(file));
				Writer writer = new BufferedWriter(new OutputStreamWriter(gz));
				editorKit.write(writer, doc, 0, doc.getLength());
				writer.close();
			}
		} catch(Exception e) { // if anything goes wrong, alert in the status bar
			statusAlert(e);
		}
	}
	public static void main(String[] args) throws Exception {
		new TextEditor();
	}
}
