package texteditor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Klaas Govaerts
 * Test class for {@link texteditor.DocumentEdit}
 */
public class DocumentEditTest {
	private DocumentEdit edit;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		edit=new DocumentEdit(1,"test",true);
	}

	/**
	 * Test method for {@link texteditor.DocumentEdit#getLocation()}.
	 */
	@Test
	public void testGetLocation() {
		assertEquals(1,edit.getLocation());
	}

	/**
	 * Test method for {@link texteditor.DocumentEdit#getEdit()}.
	 */
	@Test
	public void testGetEdit() {
		assertTrue(edit.getEdit().equals("test"));
	}

	/**
	 * Test method for {@link texteditor.DocumentEdit#isInsert()}.
	 */
	@Test
	public void testIsInsert() {
		assertTrue(edit.isInsert());
	}

}
