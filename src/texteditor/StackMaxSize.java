/**
 * 
 */
package texteditor;

/**
 * @author Klaas Govaerts
 *
 */
public class StackMaxSize<T> extends StackLL<T>{
	private int maxSize;
	public StackMaxSize(int maxSize){
		super();
		this.maxSize=maxSize;
	}
	
	public void push(T element){
		super.push(element);
		if(super.size()>=maxSize){
			ll.
		}
	}
}
