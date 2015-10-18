package sql.usc.ColorConflictGraph;

import java.util.HashSet;
import java.util.Set;

import sql.usc.Color.Color;


public class CCGNode {
	Color color;
	Set<String> sigs=new HashSet<String>();
	//private static int next_id;
	private int id;
	boolean root=false;
	public Color getColor()
	{
		return color;
	}
	public void makeRoot()
	{
		this.root=true;
	}
	public CCGNode(Color c,int next_id)
	{
		this.id=next_id;
		this.color=c;
	}
	public void AddSigs(String sig)
	{
		sigs.add(sig);
	}
	public void AddSigs(Set<String> sig)
	{
		sigs.addAll(sig);
	}
	public int getID()
	{
		return id;
	}
	public boolean equals(Object o)
	{
		if(!(o instanceof CCGNode))
			return false;
		CCGNode input=(CCGNode)o;
		if(this.id==input.id) 
			return true;
		return false;
	}
	public int hashCode() 
	{
    	int hash =id;
    
    	return hash;
	}	

}
