package texteditor;

/**
 * Queue with a fixed size, circular behavior
 *
 * @param <T> 
 */
public class CircularQueue<T> {
	Node current;
	int size;
	int maxSize;
	
	/**
	 * 
	 */
	public CircularQueue(int maxSize){
		this.maxSize=maxSize;
		current = null;
		size = 0;
	}
	
	/**
	 * 
	 * @return number of elements
	 */
	public int size(){
		return size;
	}
	
	/**
	 * 
	 * @return true if size == 0 otherwise false
	 */
	public boolean isEmpty(){
		return size == 0;
	}
	
	/**
	 * 	
	 * @return the first element without removing it from the queue
	 */
	public T current(){
		return current.get();
	}
	
	/**
	 * Add element to the queue
	 * @param element
	 */
	public void enqueue(T element){
		//TODO resolve adding more than 1 element
		Node node = new Node(element);
		current = node;
	}
	
	/**
	 * Remove element from the queue
	 * @return element
	 */
	public T dequeue(){
		//TODO resolve queue with single element
		T result = current.get();
		current = current.next();
		return result;
	}
	
	private class Node{
		private T element;
		private Node next;
		private Node previous;
		
		public Node(T element){
			this.next = this;
			this.previous=this;
			this.element = element;
		}
		
		public Node(T element, Node next){
			this.element = element;
			this.next = next;
		}
		
		public T get(){
			return element;
		}
		
		public Node next(){
			return next;
		}
	}
}
