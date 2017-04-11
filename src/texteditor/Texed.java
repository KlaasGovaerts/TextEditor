package texteditor;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;

/**
 * @author P. Cordemans
 * 
 * Simple GUI for a text editor.
 *
 */
public class Texed extends JFrame implements DocumentListener {
	private JTextArea textArea;
	private StackLL<String> undoStack;

	private static final long serialVersionUID = 5514566716849599754L;
	/**
	 * Constructs a new GUI: A TextArea on a ScrollPane
	 */
	public Texed() {
		super();
		setTitle("Texed: simple text editor");
		setBounds(800, 800, 600, 600);
		
		setLayout(new BorderLayout());	
		textArea = new JTextArea(30, 80);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		//Registration of the callback
		textArea.getDocument().addDocumentListener(this);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane,BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		improvedButton undoButton=new improvedButton("undo",textArea);
		improvedButton redoButton=new improvedButton("redo",textArea);
		improvedButton closeButton=new improvedButton("close tag",textArea);
		improvedButton checkButton=new improvedButton("check tags",textArea);
		/*JButton undoButton=new JButton("undo");
		JButton redoButton=new JButton("redo");
		JButton closeButton=new JButton("close tag");*/
		JPanel menu=new JPanel();
		menu.add(undoButton);
		menu.add(redoButton);
		menu.add(closeButton);
		menu.add(checkButton);
		add(menu,BorderLayout.PAGE_START);
		closeButton.addActionListener(new closeButtonHandler());
		
		undoStack=new StackLL<String>();
	}

	/**
	 * Callback when changing an element
	 */
	public void changedUpdate(DocumentEvent ev) {
	}
	
	/**
	 * Seaches the text for tags.
	 */
	public static String closeTag(String str){
		StackLL<String> tagStack=new StackLL<String>();
		char[] charArray = str.toCharArray();
		boolean inTag=false;//set to true if a '<' is found.
		boolean closingTag=false;
		String currentTag="";
		for(char c:charArray){
			if(inTag&&c=='/'){
				closingTag=true;
			}
			if(c=='>'){
				inTag=false;
				if(!closingTag){
					tagStack.push(currentTag+"");
				}else{
					/*if(tagStack.size()!=0){
						
					}*/
					if(currentTag.equals(tagStack.top())){
						tagStack.pop();
					} else {
						//TODO tag closed that isn't opened
					}
					
				}
				currentTag="";
				closingTag=false;
			}
			if(inTag&&c!='/'){
				currentTag+=c;
			}
			if(c=='<'){
				inTag=true;
			}
		}
		return "</"+tagStack.top()+">";
	}

	/**
	 * Callback when deleting an element
	 */
	public void removeUpdate(DocumentEvent ev) {
	}

	/**
	 * Callback when inserting an element
	 */
	public void insertUpdate(DocumentEvent ev) {
		//Check if the change is only a single character, otherwise return so it does not go in an infinite loop
		if(ev.getLength() != 1) return;
		
		// In the callback you cannot change UI elements, you need to start a new Runnable
		//SwingUtilities.invokeLater(new Task("foo"));
	}

	/**
	 * Runnable: change UI elements as a result of a callback
	 * Start a new Task by invoking it through SwingUtilities.invokeLater
	 */
	private class Task implements Runnable {
		private String text;
		
		/**
		 * Pass parameters in the Runnable constructor to pass data from the callback 
		 * @param text which will be appended with every character
		 */
		Task(String text) {
			this.text = text;
		}

		/**
		 * The entry point of the runnable
		 */
		public void run() {
			textArea.append(text);
		}
	}

	/**
	 * Entry point of the application: starts a GUI
	 */
	public static void main(String[] args) {
		new Texed();

	}
	
	private class closeButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			/*SwingUtilities.invokeLater(new Task("foo"));*/
			improvedButton source=(improvedButton) e.getSource();
			textArea=source.getTextArea();
			String text=textArea.getText();
			textArea.append(Texed.closeTag(text));
			//TODO actual insert, not at end of document
	    }
	}

}