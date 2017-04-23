package texteditor;

/**
 * Contains information about the text before and after the edit.
 * @author Klaas Govaerts
 */
public class Edit {
	private String oldText;
	private String newText;
	
	/**
	 * 
	 * @param oldText Text before the edit
	 * @param newText Text after the edit
	 */
	public Edit(String oldText,String newText){
		this.oldText=oldText;
		this.newText=newText;
	}
	
	/**
	 * 
	 * @return Text before the edit
	 */
	public String getOldText(){return oldText;}
	
	/**
	 * 
	 * @return Text after the edit
	 */
	public String getNewText(){return newText;}

}
