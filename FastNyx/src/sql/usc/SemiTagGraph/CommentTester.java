package sql.usc.SemiTagGraph;

import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import sql.usc.SemiTagGraph.SemiTagNode;

public class CommentTester {
	public static boolean IsOpenCommentTag(SemiTagNode n)
	{
		//tags in form of <!--.....>
		String prefix;
		Automaton a=n.getValue();
		prefix=a.getCommonPrefix();
		if(prefix.startsWith("<!--"))
			return true;
		else if(prefix.startsWith("<"))
			return false;
		else
			throw new Error("illegal html tag "+a.getCommonPrefix()+"...."+n.sigs);
		
	}
	public static boolean IsEndCommentTag(SemiTagNode n)
	{
		//the tag is in form of <.....-->
		Automaton a=n.getValue();
		Set<State> simiendtags=new HashSet<State>();
		Set<State> secondendtags=new HashSet<State>();

		for(State s:a.getStates())
			for(Transition tr: s.getTransitions())
			{
				if(tr.getDest().isAccept() && tr.getMin() ==tr.getMax() &&tr.getMin()=='>')
					simiendtags.add(s);
				else if(tr.getDest().isAccept())
					throw new Error("unclosed html tag "+a.getCommonPrefix()+"...."+n.sigs);
			}
		for(State s:a.getStates())
			for(Transition tr: s.getTransitions())
			{
				if(simiendtags.contains(tr.getDest()) && tr.getMin() ==tr.getMax() &&tr.getMin()=='-')
					secondendtags.add(s);
			}
		boolean flag=false;
		for(State s:a.getStates())
			for(Transition tr: s.getTransitions())
			{
				if(secondendtags.contains(tr.getDest()) && tr.getMin() ==tr.getMax() &&tr.getMin()=='-')
					flag=true;
				else if(secondendtags.contains(tr.getDest()) && flag==true)
					throw new Error("unclosed html tag "+a.getCommonPrefix()+"...."+n.sigs);
			}
		return flag;
	}
}
