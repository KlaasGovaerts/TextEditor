package texteditor;

/**
 * @author Klaas Govaerts
 * Contains information about an edit in the document, used to undo and redo an action.
 * 
 */
public class DocumentEdit{
	private int location;
	private String edit;
	private boolean insert; //true if insert, false if delete
	
	/**
	 * Constructor for a document edit
	 * @param location where the edit happened, counted from the start of the document
	 * @param edit The text that was modified
	 * @param insert true if insert, false if deletion
	 */
	public DocumentEdit(int location,String edit,boolean insert){
		this.location=location;
		this.edit=edit;
		this.insert=insert;
	}
	
	public int getLocation(){return location;}
	public String getEdit(){return edit;}
	public boolean isInsert(){return insert;}
}
