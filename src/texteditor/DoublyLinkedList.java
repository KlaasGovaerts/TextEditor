package texteditor;

/**
 * 
 * @author Klaas Govaerts
 *
 * @param <T> The element you want to store in the doubly linked list.
 * 
 * A doubly linked list, with a maximal size.
 * There is also a cursor that can move trough the 
 */
public class DoublyLinkedList<T> {
	private int maxSize;
	private Node cursor;
	private Node first;
	private Node last;
	private boolean empty=true;
	
	/**
	 * Constructor for an empty doubly linked list
	 * @param maxSize Maximal size of the list
	 */
	public DoublyLinkedList(int maxSize){
		this.maxSize=maxSize;
	}
	
	/**
	 * 
	 * @param maxSize Maximal size of the list
	 * @param element First (and last) element in the list
	 */
	public DoublyLinkedList(int maxSize,T element){
		this.maxSize=maxSize;
		initialise(element);
	}
	
	/**
	 * 
	 * @return true if the list is empty
	 */
	public boolean isEmpty(){
		return empty;
	}
	
	private void initialise(T element){
		cursor=new Node(element);
		first=cursor;
		last=cursor;
		empty=false;
	}
	
	/**
	 * Add an elements before the location of the cursor, and removes all other elements before it as well.
	 * @param element
	 */
	public void addBeforeCursor(T element){
		if(cursor==null){
			initialise(element);
		} else {
		Node newNode=new Node(element);
		cursor.setPrevious(newNode);
		newNode.setNext(cursor);
		first=newNode;
		cursor=newNode;
		chop();
		}
	}
	
	/**
	 * 
	 * @return The element at the current location of the cursor
	 */
	public T getCursor(){
		if(cursor!=null){
		return cursor.getElement();
		} else {
		return null;
		}
	}
	
	/**
	 * Moves the cursor back
	 */
	public void previous(){
		if(cursor==null){
			cursor=last;
		} else {
			cursor=cursor.getPrevious();
		}
	}
	
	/**
	 * Moves the cursor to the front
	 */
	public void next(){
		if(cursor==null){
			cursor=first;
		} else {
			cursor=cursor.getNext();
		}
	}
	
	/**
	 * 
	 * @return The size of the list
	 */
	public int getSize(){
		Node iterator=first;
		int size=0;
		if(first!=null){
			size=1;
			while(iterator.getNext()!=null){
				size+=1;
				iterator=iterator.getNext();
			}
		}
		return size;
	}
	
	/**
	 * Removes the last element, only if the list is too big
	 */
	private void chop(){
		if(getSize()>maxSize){
			removeLast();
		}
	}
	
	/**
	 * Removes the last element
	 */
	public void removeLast(){
		last.getPrevious().setNext(null);
		last=last.getPrevious();
	}
	
	/**
	 * 
	 * @return true if the cursor is located at the first element
	 */
	public boolean cursorAtFirst(){
		if(cursor!=null){
			return cursor.getPrevious()==null;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return The element before the cursor, without moving the cursor.
	 */
	public T getPrevious(){
		if(cursor==null){
			return last.getElement();
		} else {
			return cursor.getPrevious().getElement();
		}		
	}
	
	/**
	 * 
	 * @return The first element of the array
	 */
	public T getFirst(){
		if(cursor!=null){
			return first.getElement();
		} else {
			return null;
		}
	}
	
	private class Node{
		Node next;
		Node previous;
		T element;
		public Node(T element){
			this(null,null,element);
		}
		
		public Node(Node next,Node previous,T element){
			this.next=next;
			this.previous=previous;
			this.element=element;
		}

		public void setNext(Node next){
			this.next=next;
		}
		
		public Node getNext(){
			return next;
		}

		public void setPrevious(Node previous){
			this.previous=previous;
		}
		
		public Node getPrevious(){
			return previous;
		}		

		public T getElement(){
			return element;
		}

		public void setElement(T element){
			this.element=element;
		}	
	}
}
