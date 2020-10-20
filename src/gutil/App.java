package gutil;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.net.URI;
import javax.swing.*;
import java.util.Properties;
import java.util.*;
import javax.swing.filechooser.FileFilter;

/*
 * @author: Dov Kruger
 * Wrapper and convenience class for building Java apps under Swing
 * without having to tie actions directly to input events.
 * The programmer defines named actions, and named buttons and menus
 * automatically trigger. Also provides internationalization in a 
 * cleaner way.
 */
public abstract class App extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Style defaultToolStyle;
    
	static {
		defaultToolStyle = new Style("Arial", 12, 1, Color.WHITE, Color.BLUE);
	}
    
	private Conf conf;
	private Prefs prefs;
	private boolean dirty; // when true, the app needs to be saved
	private String docsURL;
	private String localdocsURL;
	private String language;
	private HashMap<String,String> languageTranslate;
	private NamedActionListener listener;
    
	public void setDocsURL(String url) { docsURL = url; }
	public void setLocalDocsURL(String url) { localdocsURL = url; }
	public Conf getConf() { return conf; }
	public Prefs getPrefs() { return prefs; }
  
	public void setLanguage(String lang) {
		language = lang;
		try {
			languageTranslate = conf.defaulted(language, new HashMap<String, String>());
			buildMenu();
			buildToolBar();
			doLayout();
			repaint();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open menu for current language" + e);
		}
	}
	public String lookupText(String name) {
		if (name == null)
			return null;
		String text = languageTranslate.get(name);
		return text != null ? text : name;
	}
  /*
  TODO: This should provide 3 ways of an application alerting the user
  1. console, ugly
  2. status bar
  3. popup request
  4. Possible flash of screen to highlight?
  */
 	public void statusAlert(Exception e) {
		e.printStackTrace(); // for now, just dump to console
	}

	public JFileChooser buildFileDialog(String title, String suffix) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(lookupText(title)); 
    //fc.setFont();
    if (suffix != null) {
  		fc.setAcceptAllFileFilterUsed(false);
  		String explanation = lookupText(suffix);
  		FileFilter f = new FileTypeFilter(suffix, explanation);
  		fc.addChoosableFileFilter(f);
    }
    return fc;
	}
  
  
	// Post a file dialog and return the file selected, null if none
	public String createFileDialog(String title, String suffix) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(lookupText(title)); 
		fileChooser.setAcceptAllFileFilterUsed(false);
		String explanation = lookupText(suffix);
		FileFilter f = new FileTypeFilter(suffix, explanation);
		fileChooser.addChoosableFileFilter(f);
		int userSelection = fileChooser.showSaveDialog(App.this);
        
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			String filename = fileChooser.getSelectedFile().toPath().toString();
			System.out.println(filename);
			return filename;
            
		}
		return null;
	}

	/*
		File dialog to filter out files with known extension
	*/
	public String showSaveDialog(String extension) {
		FileDialog fd = new FileDialog(this);
		if (extension != null)
			fd.setFilenameFilter(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(extension);
					}
				});

		fd.setTitle("Save File");
		fd.pack();
		fd.setLocationRelativeTo(this);
		fd.setVisible(true);
		String filepath = fd.getDirectory() + fd.getFile();
		return filepath;
	}
    
	protected void buildMenu() throws FileNotFoundException {
		String[][] defaultMenus = {
			{"FILE", "PREFERENCES", "QUIT"},
			{"LANGUAGE", "English", "CHINESE"},
			{"HELP", "ABOUT", "WEBDOCS", "LOCALDOCS"}
		};
        
		String[][] menus = conf.defaulted("menu", defaultMenus);
		JMenuBar mb = new JMenuBar();
		prefs.getMenuStyle().set(mb);
		for (int i = 0; i < menus.length; i++) {
			JMenu m = new JMenu(lookupText(menus[i][0]));
			prefs.getMenuStyle().set(m);
			mb.add(m);
			for (int j = 1; j < menus[i].length; j++) {
				JMenuItem mi = new NamedMenuItem(lookupText(menus[i][j]), menus[i][j], listener);
				prefs.getMenuStyle().set(mi);
				m.add(mi);
			}
		}
		setJMenuBar(mb);
	}
    
	private void buildToolBar() {
		Container c = getContentPane();
		String toolbarPos  = conf.defaulted("toolbarPos", BorderLayout.WEST);
		String[][] defaultToolNames = {{},{}}; //NEW,  OPEN, SAVE, QUIT};
		//String[] toolNames = conf.defaulted("toolnames", defaultToolNames);
		Properties lang = prefs.getMessages();
		Style toolbarStyle = conf.defaulted("toolbarStyle", prefs.getMenuStyle());
		String[][] toolNames = conf.defaulted("toolbarNames", defaultToolNames);
        
        
		// debugging
		System.out.println("---------------------");
		for (String[] toolNameBar : toolNames){
			for (String ANAME : toolNameBar){
				System.out.println(ANAME);
			}
		}
		System.out.println("---------------------");
        
        
		int iconSize = conf.defaulted("iconsize", 32);
		final String prefix = "img/" + iconSize + "/";
		try {
			Toolbar toolbar = new Toolbar(this, toolNames, iconSize, listener);
			toolbarStyle.set(toolbar);
			/*
				toolbar.setLayout(new GridLayout(toolNames.length, 2));
				JButton b;
				for (int i = 0; i < toolNames.length; ++i)
				for (int j = 0; j < toolNames[i].length; ++j) {
				String name = toolNames[i][j];
				String toolText = lookupText(name);
				String imgName = prefix + name + ".png";
				ImageIcon img = new ImageIcon(imgName);
				if (img == null)
				b = new NamedButton(toolText, name, listener);
				else
				b = new NamedButton(img, name, listener);
				b.setToolTipText(toolText);
				toolbar.add(b);
				}
				toolbarStyle.set(toolbar);
			*/
			c.add(toolbar, toolbarPos);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to load toolbar");
		}     
	}
    
	/**
	 * Build standard commands that many gui applications would like to have, such as:
	 * 
	 * QUIT (just get out)
	 * CHECK_SAVE_AND_QUIT (first pop up a dialog box if the dirty bit is set, and ask whether to save before quitting
	 * JUMPTO_WEBDOCS   (jump to the link for the online help for this project)
	 * JUMPT_LOCALWEBDOCS (jump to a local file with a local copy of the online help so it works even without internet
	 */
	public void buildStandardCommands() {
		new IrreversibleAction("QUIT") {
			@Override public void doIt(ActionEvent e) {
				System.exit(0);
			}
		};
		new IrreversibleAction("WEBDOCS") {
			@Override public void doIt(ActionEvent e) throws Exception {
				URI uri = new URI(docsURL);
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(uri);
				}
			}      
		};        
		new IrreversibleAction("LOCALDOCS") {
			@Override public void doIt(ActionEvent e) throws Exception {
				URI uri = new URI(localdocsURL);
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(uri);
				}
			}      
		};
    new IrreversibleAction("ENGLISH") {
      @Override
      public void doIt(ActionEvent e) throws Exception {
        setLanguage("en");
      }
    };
    new IrreversibleAction("CHINESE") {
			@Override public void doIt(ActionEvent e) throws Exception {
        setLanguage("cn");
      }
    };
    // TODO: it shouldn't be this way. The internal name should be used to trigger the action
    new IrreversibleAction("English") {
      @Override
      public void doIt(ActionEvent e) throws Exception {
        setLanguage("en");
      }
    };
    new IrreversibleAction("中文") {
			@Override public void doIt(ActionEvent e) throws Exception {
        setLanguage("cn");
      }
    };
	}
    
	public App(Conf conf) throws FileNotFoundException {
		super(conf.defaulted("title", "App Title"));
		this.conf = conf;
		prefs = new Prefs(conf);
		setSize(conf.defaulted("w", 400), conf.defaulted("h", 768));
		Container c = getContentPane();
		c.setForeground(conf.defaulted("fg", Color.WHITE));
        
		listener = new NamedActionListener(this);
		language = prefs.getLanguage();
		setLanguage(language);
    buildStandardCommands();
		addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
				quit();
			}
		});
		setVisible(true);
	}

	public void setDirty() {
		dirty = true;
	}
	public void quit() {
		if (dirty) {
			// TODO: popup dialog to ask whether to save before quit
		}
		System.exit(0); // and then just quit
	}
}
