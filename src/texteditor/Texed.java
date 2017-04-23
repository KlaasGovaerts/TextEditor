package texteditor;

import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
//import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent.EventType;

/**
 * @author P. Cordemans
 * @author Klaas Govaerts
 * 
 * Simple GUI for a text editor.
 *
 */
//source used:http://stackoverflow.com/questions/25276020/listen-to-the-paste-events-jtextarea

public class Texed extends JFrame implements DocumentListener{
	private JTextArea textArea;
	private JLabel info;
	private boolean automaticEdit=false;
	private ImprovedButton undoButton;
	private ImprovedButton redoButton;
	private ImprovedButton closeButton;
	private StackLLMaxSize<Edit> undoStack=new StackLLMaxSize<Edit>(50);
	private StackLL<Edit> redoStack=new StackLL<Edit>();
	private String previousText="";
	
	private static final long serialVersionUID = 5514566716849599754L;
	
	/**
	 * Constructs a new GUI: A TextArea on a ScrollPane
	 * The GUI also contains a menu
	 */
	public Texed() {
		super();
		setTitle("Texed: simple text editor");
		setBounds(800, 800, 600, 600);
				
		setLayout(new BorderLayout());	
		textArea = new JTextArea(30, 80);
		Action action = textArea.getActionMap().get("paste-from-clipboard");
		textArea.getActionMap().put("paste-from-clipboard", new ProxyAction(action));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		//Registration of the callback
		textArea.getDocument().addDocumentListener(this);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane,BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		undoButton=new ImprovedButton("undo",textArea,this);
		redoButton=new ImprovedButton("redo",textArea,this);
		closeButton=new ImprovedButton("close tag",textArea,this);
		info=new JLabel();
		JPanel menu=new JPanel();
		menu.add(undoButton);
		menu.add(redoButton);
		menu.add(closeButton);
		menu.add(info);
		add(menu,BorderLayout.PAGE_START);
		undoButton.addActionListener(new ButtonHandler());
		undoButton.setEnabled(false);
		redoButton.addActionListener(new ButtonHandler());
		redoButton.setEnabled(false);
		closeButton.addActionListener(new ButtonHandler());
		closeButton.setEnabled(false);
		showUnclosedTags();
		repaint();
		revalidate();
		doLayout();
	}

	/**
	 * 
	 * @return The text in the textArea
	 */
	public String getText(){
		return textArea.getText();
	}
	
	/**
	 * 
	 * @return A reference to the textArea
	 */
	public JTextArea getTextArea(){
		return textArea;
	}
	
	/**
	 * Callback when changing an element
	 */
	public void changedUpdate(DocumentEvent ev) {
		showUnclosedTags();
		saveDocumentEdit();
	}
	

	/**
	 * 
	 * @return The stack of all unclosed HTML tags
	 */
	public StackLL<String> generateStack(){
		StackLL<String> tagStack=new StackLL<String>();
		char[] charArray = textArea.getText().toCharArray();
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
					if(currentTag.equals(tagStack.top())){
						tagStack.pop();
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
		return tagStack;
	}
	
	/**
	 * Automatically closes the last unclosed HTML tag
	 */
	public void closeTag(){
		StackLL<String> tagStack=generateStack();
		if(!tagStack.isEmpty()){
			String closeTag="</"+tagStack.top()+">";
			textArea.append(closeTag);
			saveDocumentEdit();
		}
	}
	
	/**
	 * Updates the number of unclosed tags on the screen
	 */
	public void showUnclosedTags(){
		StackLL<String> tagStack=generateStack();
		int size=tagStack.size();
		if(size==1){
			info.setText(size+" Unclosed tag");
		}else{
			info.setText(size+" Unclosed tags");
		}
		closeButton.setEnabled(size!=0);
	}
	
	/**
	 * 
	 * @return The text in the infobox on the upper right of the screen.
	 */
	public String getInfoBoxText(){
		return info.getText();
	}

	/**
	 * Callback when deleting an element
	 */
	public void removeUpdate(DocumentEvent ev) {
		showUnclosedTags();
		saveDocumentEdit();
	}

	/**
	 * Callback when inserting an element
	 */
	public void insertUpdate(DocumentEvent ev) {
		//Check if the change is only a single character, otherwise return so it does not go in an infinite loop
		if(ev.getLength() != 1) return;
		
		// In the callback you cannot change UI elements, you need to start a new Runnable
		//SwingUtilities.invokeLater(new Task("foo"));
		updateButtons();
		saveDocumentEdit();
	}
	
	/**
	 * Undo the last action
	 */
	public void undo(){
		if(!undoStack.isEmpty()){
			Edit edit=undoStack.pop();
			redoStack.push(edit);
			setText(edit.getOldText());
		}
	}
	
	/**
	 * Redo the last undone action (if any).
	 */
	public void redo(){
		if(!redoStack.isEmpty()){
			Edit edit=redoStack.pop();
			undoStack.push(edit);
			setText(edit.getNewText());
		}
	}
	
	
	public void setText(String text){
		automaticEdit=true;
		textArea.setText(text);
		automaticEdit=false;
		updateButtons();
	}

	/**
	 * 
	 * @param ev A documentEvent generated by the DocumentListener.
	 */
	private void saveDocumentEdit(){
		if(!automaticEdit){
		undoStack.push(new Edit(previousText,textArea.getText()));
		redoStack.clear();
		updateButtons();
		previousText=textArea.getText();
		}
	}
	
	/**
	 * Disables buttons if they can't do anything.
	 */
	public void updateButtons(){
		undoButton.setEnabled(!undoStack.isEmpty());
		redoButton.setEnabled(!redoStack.isEmpty());
		closeButton.setEnabled(!generateStack().isEmpty());
		showUnclosedTags();
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
	
	
	
	/**
	 * 
	 * @author Klaas Govaerts
	 *	Handles all 3 buttons.
	 */
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			ImprovedButton source=(ImprovedButton) e.getSource();
			Texed texed=source.getTexed();
			String buttonText=source.getText();
			switch(buttonText){
				case "close tag":
					texed.closeTag();
				break;
				case "undo":
					texed.undo();
				break;
				case "redo":
					texed.redo();
				break;
			}
			showUnclosedTags();
	    }
	}
	
	private class ProxyAction extends AbstractAction{

	    private Action action;

	    public ProxyAction(Action action) {
	        this.action = action;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        action.actionPerformed(e);   
			saveDocumentEdit();
	    }
	}
}