package texteditor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class improvedButton extends JButton {
	protected JTextArea textArea;
	public improvedButton(String text,JTextArea textArea){
		super(text);
		this.textArea=textArea;
	}
	
	public JTextArea getTextArea(){
		return textArea;
	}
}
