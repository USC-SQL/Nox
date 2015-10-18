package sql.usc.ColorConflictGraph;

import sql.usc.Color.Color;

public class ConflictPair {
	CCGNode background;
	CCGNode text;
	public ConflictPair(CCGNode bg, CCGNode ctext)	
	{
		background=bg;
		text=ctext;
	}
	public boolean equals(Object o)
	{
		if(!(o instanceof ConflictPair))
			return false;
		ConflictPair input=(ConflictPair)o;
		if(this.background==input.background && this.text==input.text) 
			return true;
		return false;
	}
	public int hashCode() 
	{
    	int hash = background.hashCode()*10+text.hashCode();
    
    	return hash;
	}	
}
