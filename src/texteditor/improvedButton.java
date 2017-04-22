package texteditor;

import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 * 
 * @author Klaas Govaerts
 *	A JButton with a reference to the text editor, and the textArea as well
 *
 */
public class ImprovedButton extends JButton {
	protected JTextArea textArea;
	protected Texed texed;
	
	/**
	 *
	 * @param text The text of the button
	 * @param textArea The JTextArea where the button will have effect.
	 * @param texed The texteditor the button is located in.
	 */
	public ImprovedButton(String text,JTextArea textArea,Texed texed){
		super(text);
		this.textArea=textArea;
		this.texed=texed;
	}
	
	public JTextArea getTextArea(){return textArea;}
	public Texed getTexed(){return texed;}
}
