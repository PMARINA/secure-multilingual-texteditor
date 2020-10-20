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
  private String saveFile;
	private File currentDir;
	private JFileChooser openChooser;
	private JFileChooser saveChooser;
	private static DefaultEditorKit editorKit;
	private static Font defaultFont;
  JFileChooser buildFileChooser(File dir, Font f, String title) {
    JFileChooser fc = new JFileChooser(dir);
    fc.setDialogTitle(lookupText(title));
    fc.setFont(f);
    return fc;
  }
  
  File selectFile(JFileChooser fc) {
    int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
      return fc.getSelectedFile();
    }
    return null;
  }
	static {
		editorKit = new DefaultEditorKit();
		defaultFont = new Font("Courier", Font.BOLD, 30);
	}
  private void buildDialogs() {
    saveFile = null;
    currentDir = new File(".");
    openChooser = buildFileChooser(currentDir, defaultFont, "OPEN");
    saveChooser = buildFileChooser(currentDir, defaultFont, "SAVEAS");
  }
  public TextEditor() throws Exception {
		super(new Conf("conf/stex.conf")); // TODO: get conf file from home directory
		setSize(1000,800);
		editor = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor);
		editor.setDocument(doc = new DefaultStyledDocument());
		editor.setFont(defaultFont);
		add(editorScrollPane, BorderLayout.CENTER);
    buildDialogs();
		addActions();
		setVisible(true);
	}
	public void addActions() {
		new IrreversibleAction("SAVE") {
			@Override public void doIt(ActionEvent e) {
        if (saveFile == null) {
          //execute saveas
        } else {
          // do a save  here
        }
			}
		};
		new IrreversibleAction("SAVEAS") {
			@Override public void doIt(ActionEvent e) throws Exception {
				File file = selectFile(saveChooser);
        if (file == null)
          return; // user cancelled
				Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				editorKit.write(writer, doc, 0, doc.getLength());
				writer.close();
			}
		};
    new IrreversibleAction("OPEN") {
      @Override
      public void doIt(ActionEvent e) throws Exception {
        File file = selectFile(openChooser);
        if (file == null)
          return; // user cancelled
        Reader reader = new BufferedReader(new FileReader(file));
        editorKit.read(reader, doc, 0);
        reader.close();
      }
    };
    new IrreversibleAction("OPEN_COMPRESSED") {
      @Override public void doIt(ActionEvent e) throws Exception {
        File file = selectFile(openChooser);
        if (file == null)
          return; // user cancelled
        GZIPInputStream gz = new GZIPInputStream(new FileInputStream(file));
        Reader reader = new BufferedReader(new InputStreamReader(gz));
        editorKit.read(reader, doc, 0);
        reader.close();
      }
    };
  
    new IrreversibleAction("SAVEAS_COMPRESSED") {
      @Override public void doIt(ActionEvent e) throws Exception {
        File file = selectFile(openChooser);
        if (file == null)
          return; // user cancelled
        GZIPOutputStream gz = new GZIPOutputStream(new FileOutputStream(file));
        Writer writer = new BufferedWriter(new OutputStreamWriter(gz));
  			editorKit.write(writer, doc, 0, doc.getLength());
  			writer.close();
      }
    };
  }
     
  public static void main(String[] args) throws Exception {
		new TextEditor();
	}
}
