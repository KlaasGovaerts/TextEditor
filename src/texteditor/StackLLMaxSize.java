
/**
 * 
 */
package texteditor;

/**
 * @author Klaas Govaerts
 *
 */
public class StackLLMaxSize<T> extends StackLL<T>{
	private int maxSize;
	
	/**
	 * 
	 * @param maxSize The maximal size of the array. If this size is exceeded previous elements will get removed.
	 */
	public StackLLMaxSize(int maxSize){
		super();
		this.maxSize=maxSize;
	}
	
	/**
	 * Add an element to the stack, shortens the stack if necessary.
	 */
	public void push(T element){
		super.push(element);
		if(size()>maxSize){
			ll.deleteLast();
		}
	}
}