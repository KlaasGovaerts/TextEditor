package texteditor;

import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 * 
 * @author Klaas Govaerts
 *	A JButton with a reference to the text editor, and the textArea as well
 *
 */
public class improvedButton extends JButton {
	protected JTextArea textArea;
	protected Texed texed;
	
	/**
	 *
	 * @param text
	 * @param textArea
	 * @param texed
	 */
	public improvedButton(String text,JTextArea textArea,Texed texed){
		super(text);
		this.textArea=textArea;
		this.texed=texed;
	}
	

	public JTextArea getTextArea(){
		return textArea;
	}
	
	/**
	 * 
	 * @return
	 */
	public Texed getTexed(){return texed;}
}
