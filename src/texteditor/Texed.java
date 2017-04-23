package texteditor;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
//import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
//import java.awt.*;
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
public class Texed extends JFrame implements DocumentListener{
	private JTextArea textArea;
	private JLabel info;
	private String previousText="";
	private boolean automaticEdit=false;
	private ImprovedButton undoButton;
	private ImprovedButton redoButton;
	private ImprovedButton closeButton;
	private StackLLMaxSize<DocumentEdit> undoStack=new StackLLMaxSize<DocumentEdit>(50);
	private StackLL<DocumentEdit> redoStack=new StackLL<DocumentEdit>();

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
		saveDocumentEdit(ev);
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
			int position=textArea.getText().length();
			String closeTag="</"+tagStack.top()+">";
			textArea.append(closeTag);
			DocumentEdit documentedit=new DocumentEdit(position,closeTag,true);
			undoStack.push(documentedit);
			redoStack.clear();
			previousText=textArea.getText();
			updateButtons();
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
		saveDocumentEdit(ev);
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
		saveDocumentEdit(ev);
	}
	
	/**
	 * Undo the last action
	 */
	public void undo(){
		if(!undoStack.isEmpty()){
		DocumentEdit documentedit=undoStack.pop();
		redoStack.push(documentedit);
		undoredo(true,documentedit);
		}
	}
	
	/**
	 * Redo the last undone action (if any).
	 */
	public void redo(){
		if(!redoStack.isEmpty()){
		DocumentEdit documentedit=redoStack.pop();
		undoStack.push(documentedit);
		undoredo(false,documentedit);
		}
	}
	
	/**
	 * Written to avoid copying of code.
	 */
	public void undoredo(boolean undo,DocumentEdit documentedit){
		automaticEdit=true;
		int location=documentedit.getLocation();
		String edit=documentedit.getEdit();
		boolean insert=documentedit.isInsert();
		if(insert^undo){
			textArea.insert(edit,location);
		} else {
			textArea.replaceRange("",location,location+edit.length());
		}
		automaticEdit=false;
		updateButtons();
	}

	/**
	 * 
	 * @param ev A documentEvent generated by the DocumentListener.
	 */
	private void saveDocumentEdit(DocumentEvent ev){
		if(!automaticEdit){
		String text=textArea.getText();
		EventType event=ev.getType();
		int location=ev.getOffset();
		int length=ev.getLength();
		String edit="";
		boolean insert;
		
		if(event==EventType.INSERT){
			insert=true;
			for(int i=0;i<length;i++){
				edit+=text.charAt(location+i);
			}
		} else {
			insert=false;
			for(int i=0;i<length;i++){
				edit+=previousText.charAt(location+i);
			}
		}
		DocumentEdit documentedit=new DocumentEdit(location,edit,insert);
		undoStack.push(documentedit);
		redoStack.clear();
		previousText=textArea.getText();
		updateButtons();
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
}