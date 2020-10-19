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
	private static File currentDir;
	private static JFileChooser fileChooser;
	private static DefaultEditorKit editorKit;
	private static Font defaultFont;
	static {
		editorKit = new DefaultEditorKit();
		currentDir = new File(".");
		fileChooser = new JFileChooser(currentDir);
		defaultFont = new Font("Courier", Font.BOLD, 30);
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
		editor.setFont(defaultFont);
		add(editorScrollPane, BorderLayout.CENTER);
		addActions();
		setVisible(true);
	}
	public void addActions() {
		new IrreversibleAction("SAVE") {
			@Override public void doIt(ActionEvent e) {
				save();
			}
		};
		new IrreversibleAction("SAVEAS") {
			@Override public void doIt(ActionEvent e) {
				saveas();
			}
		};
		new IrreversibleAction("OPEN") {
			@Override public void doIt(ActionEvent e) {
				open();
			}
		};
	}
	public void open() {
		try {
			fileChooser.setDialogTitle(lookupText("OPEN"));
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Reader reader = new BufferedReader(new FileReader(file));
				editorKit.read(reader, doc, 0);
				reader.close();
			}
		} catch(Exception e) {
			statusAlert(e);
		}			
	}
	public void save() {
		try {

		} catch(Exception e) { // if anything goes wrong, alert in the status bar
			statusAlert(e);
		}
	}
	public void saveas() {
		try {
			fileChooser.setDialogTitle(lookupText("SAVEAS..."));
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				editorKit.write(writer, doc, 0, doc.getLength());
				writer.close();
			}
		} catch(Exception e) { // if anything goes wrong, alert in the status bar
			statusAlert(e);
		}
	}
	public void openCompressed() {
		try {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				GZIPInputStream gz = new GZIPInputStream(new FileInputStream(file));
				Reader reader = new BufferedReader(new InputStreamReader(gz));
				editorKit.read(reader, doc, 0);
				reader.close();
			}
		} catch(Exception e) {
			statusAlert(e);
		}			
	}

	public void saveasCompressed() {
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
