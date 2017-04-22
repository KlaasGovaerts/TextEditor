package texteditor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test method for a stack with a maximal size
 * @author Klaas Govaerts
 *
 */
public class StackLLMaxSizeTest {
	private StackLLMaxSize<Integer> stack;
	
	@Before
	public void setUp() throws Exception {
		stack=new StackLLMaxSize<Integer>(50);
	}
	
	/**
	 * Test for push method, to see if maximal size isn't exceeded.
	 */
	@Test
	public void testPush() {
		assertEquals(stack.size(),0);
		for(int i=0;i<60;i++){
			stack.push((Integer) i);
		}
		assertEquals(stack.size(),50);
		
		for(int i=59;i>9;i--){
			assertEquals((int) stack.pop(),i);
			assertEquals(stack.size(),i-10);
		}
		
		assertEquals(stack.size(),0);
	}
	
	/**
	 * If you pop more elements than the maximal size, there should be an empty exception.
	 */
	@Test(expected=StackEmptyException.class)
	public void testEmptyStack() {
		testPush();
		stack.pop();
	}
	
}
