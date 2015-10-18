package sql.usc.HTMLContentGraph;

import java.util.HashSet;
import java.util.Set;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import sql.usc.SemiTagGraph.SemiTagNode;

public class TagTester {
	public static String getTagName(SemiTagNode n)
	{
		//Set<String> asa=["dasaad"];
		if(IsOpenCommentTag(n) || IsEndCommentTag(n))
			return "!--";
		Automaton a=n.getValue();
		Set<String> strings=a.getFiniteStrings();
		if(strings==null)
			throw new Error("n has loop, please remove it first");
		String prefix=n.getValue().getCommonPrefix();
		prefix=prefix.replaceAll("\\\\n", " ");
		prefix=prefix.replaceAll("\\\\t", " ");
		prefix=prefix.replaceAll("\\\\", "");

		prefix=prefix.replaceAll("<", "");
		prefix=prefix.replaceAll(">", "");
		prefix=prefix.replaceAll("/", "");
		//prefix=prefix.replaceAll("\", "");

		prefix=prefix.trim();
		String[] allstirng=prefix.split(" +");

		return allstirng[0].toLowerCase();
	}
	public static boolean IsOpenTag(SemiTagNode n)
	{
		if(IsEndTag(n))
			return false;
		if(IsSelfEndTag(n))
			return false;
		if(IsEndCommentTag(n))
			return false;
		if(IsOpenCommentTag(n))
			return true;//is in form of <!-->

		return true;
	}
	public static boolean IsEndTag(SemiTagNode n)
	{
		//the tag is in form of </.....> or <...-->
		if(IsEndCommentTag(n) && !IsOpenCommentTag(n))
			return true;
		String prefix;
		Automaton a=n.getValue();
		prefix=a.getCommonPrefix();
		if(prefix.startsWith("</"))
			return true;
		else if(prefix.startsWith("<"))
			return false;
		else
			throw new Error("illegal html tag "+a.getCommonPrefix()+"...."+n.sigs);
	}
	
	public static boolean IsOpenCommentTag(SemiTagNode n)
	{
		//tags in form of <!--.....
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
	public static boolean IsSelfEndTag(SemiTagNode n)
	{
		//tags in form of <..../>
		if(IsEndCommentTag(n)&&IsOpenCommentTag(n))
			return true;//in form of <!--....-->
		HTMLTagTable self=new HTMLTagTable();
		String tagname=getTagName(n);
		if(self.selfContain(tagname))
			return true;
		Automaton a=n.getValue();
		Set<State> simiendtags=new HashSet<State>();
		for(State s:a.getStates())
			for(Transition tr: s.getTransitions())
			{
				if(tr.getDest().isAccept() && tr.getMin() ==tr.getMax() &&tr.getMin()=='>')
					simiendtags.add(s);
				else if(tr.getDest().isAccept())
					throw new Error("unclosed html tag "+a.getCommonPrefix()+"...."+n.sigs);
			}
		boolean flag=false;
		for(State s:a.getStates())
			for(Transition tr: s.getTransitions())
			{
				if(simiendtags.contains(tr.getDest()) && tr.getMin() ==tr.getMax() &&tr.getMin()=='/')
					flag=true;
				else if(simiendtags.contains(tr.getDest()) && flag==true)
					throw new Error("unclosed html tag "+a.getCommonPrefix()+"...."+n.sigs);
			}
		return flag;
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
