package sql.usc.SemiTagGraph;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;

import sql.usc.OutputGraph.Edge;


import dk.brics.automaton.*;

public class SemiTagNode {
public Automaton Value;
private int id;
long gene;
int status=0;//0:complete, 1: upper incomplete, 2: lower incomplete 3. plain string
private static int next_id;
//Set<HTMLNode> predecessor=new HashSet<HTMLNode>();
//Set<HTMLNode> succesors=new HashSet<HTMLNode>();
Set<SemiTagEdge> inegdes=new HashSet<SemiTagEdge>();
Set<SemiTagEdge> outegdes=new HashSet<SemiTagEdge>();
public String  Methodsig="";
public Set<String> sigs=new HashSet<String>();
int position=0; //0: inside a method. 1, entrance of a method 2. exit of a method
static final int INMETHOD=0;
static final int ENTRYMETHOD=0;
static final int EXITMETHOD=0;
boolean incomment=false;
public int getId()
{
	return id;
}
public Set<SemiTagEdge> getInEdges()
{
	return inegdes;
}
public Set<SemiTagEdge> getOutEdges()
{
	return outegdes;
}
public Automaton getValue()
{
	return Value;
}

public void Acyclize()
{
	Set<State> visited=new HashSet<State>();
	Hashtable<State,State> copytable=new Hashtable<State,State>();
	Automaton newvalue=new Automaton();
	for(State s:Value.getStates())
		copytable.put(s, new State());
	Stack<State> worklist=new Stack<State>();
	worklist.push(Value.getInitialState());
	newvalue.setInitialState(copytable.get(Value.getInitialState()));
	visited.add(Value.getInitialState());
	while(!worklist.isEmpty())
	{
		State top=worklist.pop();
		State shadow=copytable.get(top);
		//visited.add(top);
		if(top.isAccept())
			shadow.setAccept(true);
		for(Transition tr:top.getTransitions())
		{
			State target=tr.getDest();
			if(!visited.contains(target))
			{
				shadow.addTransition(new Transition(tr.getMin(),tr.getMax(),copytable.get(target)));
				visited.add(target);
				worklist.push(target);
			}
			else if(target==Value.getInitialState() && tr.getMin()=='>')
			{
				State news=new State();
				news.setAccept(true);
				shadow.addTransition(new Transition(tr.getMin(),tr.getMax(),news));

			}
		}
	}
	Value=newvalue;
}
public void setStatus()
{
	Acyclize();
	State s=Value.getInitialState();
	boolean upperflag=false, nonupperflag=false;
	if(Value.isEmptyString())
		throw new Error("the tag cannot be empty");
	for(Transition st:s.getTransitions())
	{
		if(st.getMin()==st.getMin() && st.getMin()=='<')
			upperflag=true;
		else
			nonupperflag=true;
	}
	if(upperflag && nonupperflag)
		throw new Error("the html tag is not legel "+Methodsig+ " "+Value.toDot());
	boolean lowerflag=false, nonlowerflag=false;
	for(State s2 :Value.getStates())
		for(Transition st:s2.getTransitions())
		{
			if(st.getMin()==st.getMin() && st.getMin()=='>' && st.getDest().isAccept())
				lowerflag=true;
			else if(st.getDest().isAccept())
				nonlowerflag=true;
		}
	if(lowerflag && nonlowerflag)
		throw new Error("the html tag is not legel "+Methodsig+ " "+Value.toDot());
	if(nonupperflag && nonlowerflag)
		status=3;
	else if(nonupperflag)
		status=1;
	else if(nonlowerflag)
		status=2;
	else 
		status=0;
}
public boolean IsComplete()
{
	return status==0;
}
public boolean IsUpperIncomplete()
{
	return status==1;
}
public boolean IsLowerIncomplete()
{
	return status==2;
}
public boolean IsDualIncomplete()
{
	return status==3;
}
public SemiTagNode()
{
	Value=new Automaton();
	inegdes=new HashSet<SemiTagEdge>();
	outegdes=new HashSet<SemiTagEdge>();
	position=INMETHOD;
	status=3;
	 next_id++;
	 id=next_id;
	 gene=id;
}
public String toString()
{
	if(Value.isFinite())
	{
		//Set<String> ss=Value.getCommonPrefix()
		//ss.iterator().next();
		String pre=Value.getCommonPrefix();
		if(pre.length()>=6)
			return 	pre.substring(0, 5);
		return pre;
	}
	else
		return "NULL";
}
public String getTagname()
{
	if(Value.isFinite())
	{
		String s0=sigs.toString();
		//System.out.println(s0);
		s0=s0.replace("[", "");
		s0=s0.replace("]", "");
		//System.out.println(s0);

		String s=Value.getFiniteStrings().toString();
		s=s.replace("[", "\""+s0+" "+gene+","+status);
		s=s.replace("]", "\"");
		return s;
	}
	return "NULL";
}
}
