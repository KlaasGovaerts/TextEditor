package texteditor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class improvedButton extends JButton {
	protected JTextArea textArea;
	protected Texed texed;
	public improvedButton(String text,JTextArea textArea,Texed texed){
		super(text);
		this.textArea=textArea;
		this.texed=texed;
	}
	
	public JTextArea getTextArea(){
		return textArea;
	}
	
	public Texed getTexed(){return texed;}
}
