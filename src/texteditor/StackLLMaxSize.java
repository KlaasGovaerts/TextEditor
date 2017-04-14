
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
	public StackLLMaxSize(int maxSize){
		super();
		this.maxSize=maxSize;
	}
	
	public void push(T element){
		super.push(element);
		if(super.size()>=maxSize){
			ll.deleteLast();
		}
	}
}