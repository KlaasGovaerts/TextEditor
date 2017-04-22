/**
 * 
 */
package texteditor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Klaas Govaerts
 * Contains the tests for deleteLast of the linkedlist
 * Tests for the other methods were already written in the lecture.
 */
public class LinkedListTest {
private LinkedList ll;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception{
		ll=new LinkedList<Integer>();
	}

	/**
	 * Test method for {@link texteditor.LinkedList#deleteLast()}.
	 */
	@Test
	public void testDeleteLast() {
		assertEquals(ll.size(),0);
		for(int i=0;i<5;i++){
			assertEquals(ll.size(),i);
			ll.prepend((Integer) i);
		}
		assertEquals((int) ll.last(),0);
		ll.deleteLast();
		assertEquals((int) ll.last(),1);
	}
}
