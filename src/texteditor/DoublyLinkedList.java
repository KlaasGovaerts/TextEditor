package texteditor;

public class DoublyLinkedList<T> {
	private int maxSize;
	private Node current;
	private Node first;
	private Node last;
	private boolean isFirst;
	private boolean empty=true;
	
	public DoublyLinkedList(int maxSize){
		this.maxSize=maxSize;
	}
	
	public DoublyLinkedList(int maxSize,T element){
		this.maxSize=maxSize;
		initialise(element);
	}
	
	public void initialise(T element){
		current=new Node(element);
		first=current;
		last=current;
		empty=false;
	}
	
	public void addBeforeCurrent(T element){
		if(current==null){
			initialise(element);
		} else {
		Node newNode=new Node(element);
		current.setPrevious(newNode);
		newNode.setNext(current);
		first=newNode;
		current=newNode;
		chop();
		}
	}
	
	public T getCurrent(){
		if(current!=null){
		return current.getElement();
		} else {
		return null;
		}
	}
	
	public void previous(){
		current=current.getPrevious();
		/*if(!empty){
			if(current!=null){
				if(current.getPrevious()!=null){
					
				}
			}else{
				current=last;
			}*/
		
		//}
		/*
		if(current!=null){
		current=current.getPrevious();
		} else {
			if(current=null)
		}
		if(current==null){
			isFirst=true;
		}*/
	}
	
	public void next(){
		/*
		current=current.getNext();	
		if(current==null){
			isFirst=false;
		}
		*/
		if(current==null){
			current=first;
		} else {
			current=current.getNext();
		}
	}
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
	
	public void chop(){
		if(getSize()>maxSize){
			removeLast();
		}
	}
	
	public void removeLast(){
		last.getPrevious().setNext(null);
		last=last.getPrevious();
	}
	
	public T getPrevious(){
		if(current!=null&&current.getPrevious()!=null){
			return current.getPrevious().getElement();
		} else {
			return null;
		}
	}
	
	public T getFirst(){
		if(current!=null){
			return first.getElement();
		} else {
			return null;
		}
	}
	
	//TODO check if all methods use T element
	
	
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
