package sql.usc.OutputGraph.copy;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;

import soot.SootMethod;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

public class OutputNode {
	private Automaton Value;
	private int id;
	private static int next_id;
	 String defig;
	String Methodsig;
	int offset;
	//Set<OutputNode> predecessor=new HashSet<OutputNode>();
	//Set<OutputNode> succesors=new HashSet<OutputNode>();
	Set<Edge> inegdes=new HashSet<Edge>();
	Set<Edge> outegdes=new HashSet<Edge>();	
	public int getOffset()
	{
		return offset;
	}
	public Set<Edge> getOutedge()
	{
		return outegdes;
	}
	public Set<Edge> getInedge()
	{
		return inegdes;
	}
	public String MethodSig()
	{
		return Methodsig;
	}

	public Automaton getValue()
	{
		return Value;
	}
	public boolean equals(Object o)
	{
		if(!(o instanceof OutputNode))
			return false;
		OutputNode other=(OutputNode)o;
		return other.id==this.id;
	}
	public int hashCode() {
	    int hash =id;
	    
	    return hash;
	}	
	
	
	public OutputNode(Automaton v, SootMethod m, int offset)
	{
		Value=v;
		Methodsig=m.getSignature();
		defig=Methodsig+";"+offset;
		this.offset=offset;
		 //predecessor=new HashSet<OutputNode>();
		 //succesors=new HashSet<OutputNode>();
		Set<Edge> inegdes=new HashSet<Edge>();
		Set<Edge> outegdes=new HashSet<Edge>();
		 next_id++;
		 id=next_id;
	}
	public String GetSampleString()
	{
		Set<String> set=Value.getFiniteStrings();
		if(set==null)
		{
			return "null";
		}
		if(set.isEmpty())
			return "[]";
		String r=(String)set.toArray()[0];
		r=r.replaceAll("\"", "");
		return r;
	}
	public int getId()
	{
		return id;
	}

}
