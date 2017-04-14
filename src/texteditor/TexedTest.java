package texteditor;

import static org.junit.Assert.*;

import javax.swing.JTextArea;

import org.junit.Before;
import org.junit.Test;

public class TexedTest {
	private Texed texed;
	private JTextArea textArea;
	
	@Before
	public void setUp() throws Exception {
		texed=new Texed();
		textArea=texed.getTextArea();
		textArea.setText("<tag1>tekst / teskt <tag2> teskt </tag2>");
	}

	@Test
	public void testGenerateStack() {
		fail("Not yet implemented");
	}

	@Test
	public void testCloseTag() {
		texed.closeTag();
		assertEquals("<tag1>tekst / teskt <tag2> teskt </tag2></tag1>",textArea.getText());
	}

	@Test
	public void testShowUnclosedTags() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testUndo() {
		textArea.append("nog meer tekst");
		texed.undo();
		assertEquals("<tag1>tekst / teskt <tag2> teskt </tag2>",textArea.getText());
		/*Texed.undo();
		assertEquals(texArea.getText(),);*/
	}

	@Test
	public void testRedo() {
		
	}

	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

}
