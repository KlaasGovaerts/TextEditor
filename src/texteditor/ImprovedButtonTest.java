/**
 * 
 */
package texteditor;

import static org.junit.Assert.*;

import javax.swing.JButton;
import javax.swing.JTextArea;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Klaas Govaerts
 * Test class for {@link texteditor.ImprovedButton}.
 */
public class ImprovedButtonTest{
	JTextArea textArea;
	Texed texed;
	ImprovedButton button;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		texed=new Texed();
		textArea=texed.getTextArea();
		button=new ImprovedButton("test",textArea,texed);
	}

	/**
	 * Test method for {@link texteditor.ImprovedButton#getTextArea()}.
	 */
	@Test
	public void testGetTextArea() {
		assertEquals(textArea,button.getTextArea());
	}

	/**
	 * Test method for {@link texteditor.ImprovedButton#getTexed()}.
	 */
	@Test
	public void testGetTexed() {
		assertEquals(texed,button.getTexed());
	}

}
