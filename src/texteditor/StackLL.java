package texteditor;

/**
 * @author P. Cordemans
 * 
 * Implements a Stack based on a Linked List
 *
 * @param <T> type of the elements on the stack
 */
public class StackLL<T> implements Stack<T> {
	protected LinkedList<T> ll;
	
	/**
	 * Creates an empty stack
	 */
	public StackLL(){
		ll = new LinkedList<T>();
	}
	
	@Override
	public boolean isEmpty() {
		return ll.isEmpty();
	}

	@Override
	public int size() {
		return ll.size();
	}

	@Override
	public void push(T element) {
		ll.prepend(element);

	}

	@Override
	public T pop()  {
		if (size() == 0) throw new StackEmptyException();
		T element = ll.first();
		ll = ll.tail();
		return element;
	}

	@Override
	public T top() {
		return ll.first();
	}
	
	/**
	 * Removes all elements from the stack
	 */
	public void clear(){
		while(!isEmpty()){
			pop();
		}
	}

}
