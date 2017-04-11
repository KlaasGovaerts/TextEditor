/**
 * 
 */
package texteditor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author klaas
 *
 */
public class TexedTest {
	private Texed texed;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		texed=new Texed();
	}

	/**
	 * Test method for {@link texteditor.Texed#Texed()}.
	 */
	@Test
	public void testTexed() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link texteditor.Texed#changedUpdate(javax.swing.event.DocumentEvent)}.
	 */
	@Test
	public void testChangedUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link texteditor.Texed#closeTag(java.lang.String)}.
	 */
	@Test
	public void testCloseTag() {
		String html="<tag1>tekst / teskt <tag2> teskt </tag2>";
		assertEquals("</tag1>",texed.closeTag(html));
	}

	/**
	 * Test method for {@link texteditor.Texed#removeUpdate(javax.swing.event.DocumentEvent)}.
	 */
	@Test
	public void testRemoveUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link texteditor.Texed#insertUpdate(javax.swing.event.DocumentEvent)}.
	 */
	@Test
	public void testInsertUpdate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link texteditor.Texed#main(java.lang.String[])}.
	 */
	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

}
