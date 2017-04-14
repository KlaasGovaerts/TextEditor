package texteditor;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
public class Texed extends JFrame implements DocumentListener {
	private JTextArea textArea;
	private JLabel info;
	//private DoublyLinkedList<DocumentEdit> editList=new DoublyLinkedList<DocumentEdit>(50);
	private String previousText="";
	private boolean automaticEdit=false;
	private improvedButton undoButton;
	private improvedButton redoButton;
	private improvedButton closeButton;
	private StackLLMaxSize<DocumentEdit> undoStack=new StackLLMaxSize<DocumentEdit>(50);
	private StackLL<DocumentEdit> redoStack=new StackLL<DocumentEdit>();

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
		
		undoButton=new improvedButton("undo",textArea,this);
		redoButton=new improvedButton("redo",textArea,this);
		closeButton=new improvedButton("close tag",textArea,this);
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
	 * @return The text in the textarea of the Text editor
	 */
	public String getText(){
		return textArea.getText();
	}
	
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
	 * @return The stack of all unclosed html tags
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
			//editList.addBeforeCursor(documentedit);
			previousText=textArea.getText();
			//redoButton.setEnabled(false);
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
		showUnclosedTags();
		saveDocumentEdit(ev);
	}
	
	/**
	 * Undo the last action
	 */
	public void undo(){
		if(!undoStack.isEmpty()){
		automaticEdit=true;
		DocumentEdit documentedit=undoStack.pop();
		redoStack.push(documentedit);	
		int location=documentedit.getLocation();
		String edit=documentedit.getEdit();
		boolean insert=documentedit.isInsert();
		
		if(insert){
			textArea.replaceRange("",location,location+edit.length());
		} else {
			textArea.insert(edit,location);
		}
		//editList.next();
		automaticEdit=false;
		/*
		if(undoStack.isEmpty()){
			undoButton.setEnabled(false);
		}
		}
		redoButton.setEnabled(true);
		*/
		updateButtons();
		}
	}
	
	/**
	 * Redo the last undone action (if any).
	 */
	public void redo(){
		if(!redoStack.isEmpty()){
		DocumentEdit documentedit=redoStack.pop();
		undoStack.push(documentedit);
		automaticEdit=true;
		int location=documentedit.getLocation();
		String edit=documentedit.getEdit();
		boolean insert=documentedit.isInsert();
		if(insert){
			textArea.insert(edit,location);
		} else {
			textArea.replaceRange("",location,location+edit.length());
		}
		automaticEdit=false;
		/*if(redoStack.isEmpty()){
			redoButton.setEnabled(false);
		}
		undoButton.setEnabled(true);*/
		updateButtons();
		}
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
		//editList.addBeforeCursor(documentedit);
		undoStack.push(documentedit);
		redoStack.clear();
		previousText=textArea.getText();
		//undoButton.setEnabled(true);
		//redoButton.setEnabled(false);
		updateButtons();
		}
	}
	
	private void updateButtons(){
		undoButton.setEnabled(!undoStack.isEmpty());
		redoButton.setEnabled(!redoStack.isEmpty());
		closeButton.setEnabled(!generateStack().isEmpty());
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
			improvedButton source=(improvedButton) e.getSource();
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