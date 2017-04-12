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
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent.EventType;

/**
 * @author P. Cordemans
 * 
 * Simple GUI for a text editor.
 *
 */
public class Texed extends JFrame implements DocumentListener {
	private JTextArea textArea;
	private JLabel info;
	private DoublyLinkedList<DocumentEdit> editList=new DoublyLinkedList<DocumentEdit>(50);
	private String previousText="";
	private boolean automaticEdit=false;

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
		
		improvedButton undoButton=new improvedButton("undo",textArea,this);
		improvedButton redoButton=new improvedButton("redo",textArea,this);
		improvedButton closeButton=new improvedButton("close tag",textArea,this);
		improvedButton checkButton=new improvedButton("check tags",textArea,this);
		info=new JLabel();
		/*JButton undoButton=new JButton("undo");
		JButton redoButton=new JButton("redo");
		JButton closeButton=new JButton("close tag");*/
		JPanel menu=new JPanel();
		menu.add(undoButton);
		menu.add(redoButton);
		menu.add(closeButton);
		menu.add(checkButton);
		menu.add(info);
		add(menu,BorderLayout.PAGE_START);
		undoButton.addActionListener(new ButtonHandler());
		redoButton.addActionListener(new ButtonHandler());
		closeButton.addActionListener(new ButtonHandler());
		checkButton.addActionListener(new ButtonHandler());
		showUnclosedTags();
		repaint();
		revalidate();
		doLayout();
	}

	/**
	 * Callback when changing an element
	 */
	public void changedUpdate(DocumentEvent ev) {
		showUnclosedTags();
	}
	
	/**
	 * Seaches the text for tags.
	 */

	public static StackLL<String> generateStack(String str){
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
		return tagStack;
	}
	
	public static String closeTag(String str){
		StackLL<String> tagStack=Texed.generateStack(str);
		return "</"+tagStack.top()+">";
	}
	
	public void showUnclosedTags(){
		StackLL<String> tagStack=Texed.generateStack(textArea.getText());
		int size=tagStack.size();
		if(size==1){
			info.setText(size+" Unclosed tag");
		}else{
			info.setText(size+" Unclosed tags");
		}
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
	
	public void undo(){
		if(editList.getCurrent()!=null){
		automaticEdit=true;
		DocumentEdit documentedit=editList.getCurrent();
		int location=documentedit.getLocation();
		String edit=documentedit.getEdit();
		boolean insert=documentedit.isInsert();
		
		if(insert){
			textArea.replaceRange("",location,location+edit.length());
		} else {
			textArea.insert(edit,location);
		}
		editList.next();
		automaticEdit=false;
		}
	}
	
	public void redo(){
		automaticEdit=true;
		
		automaticEdit=false;
	}
	
	public void saveDocumentEdit(DocumentEvent ev){
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
		editList.addBeforeCurrent(documentedit);
		previousText=textArea.getText();
		}
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
	
	//TODO replace code only used for close tags
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			/*SwingUtilities.invokeLater(new Task("foo"));*/
			improvedButton source=(improvedButton) e.getSource();
			textArea=source.getTextArea();
			Texed texed=source.getTexed();
			String text=textArea.getText();
			String buttonText=source.getText();
			switch(buttonText){
				case "close tag":
					if(!closeTag(text).equals("</null>")){
						int position=textArea.getText().length();
						String closeTag=Texed.closeTag(text);
						textArea.append(closeTag);
						DocumentEdit documentedit=new DocumentEdit(position,closeTag,true);
						editList.addBeforeCurrent(documentedit);
					}
				break;
				case "undo":
					texed.undo();
				break;
				case "redo":
					texed.redo();
				break;
			}
			showUnclosedTags();
			//TODO actual insert, not at end of document
	    }
	}
	
	private class textFieldHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
		}
	}

}