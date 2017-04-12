/**
 * 
 */
package texteditor;

/**
 * @author klaas
 *
 */
public class DocumentEdit{
	private int location;
	private String edit;
	private boolean insert; //true if insert, false if delete
	
	public DocumentEdit(int location,String edit,boolean insert){
		this.location=location;
		this.edit=edit;
		this.insert=insert;
	}
	
	public int getLocation(){return location;}
	public String getEdit(){return edit;}
	public boolean isInsert(){return insert;}
}
