package sql.usc.SemiTagGraph;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import sql.usc.OutputGraph.OutputNode;



import dk.brics.automaton.*;

/*
 * Abstract HTML tags from an Automaton. A HTML tag is defined as <html_attributes ......>
 * The algorithm is an iterative algorithm. Iteratively bread first travel all states in Automaton, 
 * for a state, if it has an transition with "<" or it is initial state we call it upper_state. If there is a transition with ">" to the 
 * state or it is accept state we call it lower_state.
 * For each state, we calculate the set of upper_satates, and lower_states which can reach it. here reach for a upper_state means that
 * there is a path from the upper_state to current state. Reach for a lower_state means there is a path from current state to the 
 * lower_state
 * 
 * To calculate the structure of HTMLnodes, we defined the anchor points as 1. initial state, 2 accept states,3 states have in-edge
 * with ">"
 */
class StateGroup
{
	State start;
	State end;
	Set<State> allstates=new HashSet<State>();
	public String toString()
	{
		return "=="+start.toString()+" "+end.toString()+"==\n";
	}
	public boolean equals(Object obj) {
		if(!(obj instanceof StateGroup))
			throw new Error("need StateGroup");
		StateGroup in=(StateGroup)obj;
		if(start.equals(in.start))
			return end.equals(in.end);
		return false;
	}

	public int hashCode() {
		return 30*start.hashCode()+end.hashCode();
	}
}
public class TagAbstractor {
	public Automaton auto;
	public SemiTagTree tagtree=new SemiTagTree();
	public Hashtable<State,Set<State>> predisessor=new Hashtable<State,Set<State>>();
	public Set<State> anchorpoints=new HashSet<State>();//anchorpoint are defined as 1, initial states, accept states
	public Hashtable<State,Set<State>> uppertable=new Hashtable<State,Set<State>>();//upper_states for all states
	public Hashtable<State,Set<State>> lowertable=new Hashtable<State,Set<State>>();//lower_states for all states
	public Hashtable<State,Set<State>> lastanchorrtable=new Hashtable<State,Set<State>>();//lower_states for all states
	public Set<State> endingset=new HashSet<State>();

	//public	Automaton opentail;
	//public 
	private void initilize()
	{

		Automaton a=auto;
		Set<State> upper_state=new HashSet<State>();
		Set<State> lower_state=new HashSet<State>();
		Queue<State> worklist=new LinkedList<State>();

		Set<State> visited=new HashSet<State>();

		//initialization
		for(State s:a.getStates())
		{
			uppertable.put(s, new HashSet<State>());
			lowertable.put(s, new HashSet<State>());
			predisessor.put(s, new HashSet<State>());
			lastanchorrtable.put(s, new HashSet<State>());


		}
		Set<State> firstanchor=lastanchorrtable.get(a.getInitialState());
		firstanchor.add(a.getInitialState());
		lastanchorrtable.put(a.getInitialState(), firstanchor);
		anchorpoints.add(a.getInitialState());
		//anchorpoints.addAll(a.getAcceptStates());
		visited=new HashSet<State>();
		worklist=new LinkedList<State>();
		State init=a.getInitialState();
		worklist.add(init);
		visited.add(init);
		while(!worklist.isEmpty())
		{
			State current=worklist.poll();
			for(Transition st:current.getTransitions())
			{
				State to=st.getDest();
				if(st.getMax()==st.getMin()&& st.getMin()=='>')
					anchorpoints.add(to);
				predisessor.get(to).add(current);

				if(!visited.contains(to))
				{
					worklist.add(to);
					visited.add(current);
				}
			}
		}

		/////////////////////////////////
		//to calculate the uppertable
		boolean flag=true;
		while(flag)
		{
			flag=false;
			visited=new HashSet<State>();
			worklist=new LinkedList<State>();
			init=a.getInitialState();
			worklist.add(init);
			visited.add(init);
			while(!worklist.isEmpty())
			{
				State current=worklist.poll();
				for(Transition st:current.getTransitions())
				{
					//System.out.println("aa");
					State to=st.getDest();
					if(!visited.contains(to))
					{
						worklist.add(to);
						visited.add(to);
					}
					//if(st.getMax()==st.getMin()&& st.getMin()=='>')
					//	continue;
					//System.out.println("here");
					if(st.getMax()==st.getMin()&& st.getMin()=='<')
					{
						Set<State> toupperset=uppertable.get(to);
						if(!toupperset.contains(current))
							flag=true;
						toupperset.add(current);
						uppertable.put(to, toupperset);
					}
					else if(current==init)
					{
						Set<State> toupperset=uppertable.get(to);
						if(!toupperset.contains(current))
							flag=true;
						toupperset.add(current);
						uppertable.put(to, toupperset);
					}
					else
					{
						Set<State> toupperset=uppertable.get(to);
						Set<State> upperset=uppertable.get(current);
						if(!toupperset.containsAll(upperset))
							flag=true;
						toupperset.addAll(upperset);
						uppertable.put(to, toupperset);
					}

				}

					
							
			}
		}
		//////////////////////////////////////////////////////////////////
		//to calculate lowertable
		flag=true;
		while(flag)
		{
			flag=false;
			visited=new HashSet<State>();
			worklist=new LinkedList<State>();
			init=a.getInitialState();
			worklist.add(init);
			visited.add(init);
			while(!worklist.isEmpty())
			{
				State current=worklist.poll();
				for(Transition st:current.getTransitions())
				{
					State to=st.getDest();
					if(!visited.contains(to))
					{
						worklist.add(to);
						visited.add(to);
					}
					//if(st.getMax()==st.getMin()&& st.getMin()=='<')
					//	continue;

					if(st.getMax()==st.getMin()&& st.getMin()=='>')
					{
						Set<State> lowerset=lowertable.get(current);
						if(!lowerset.contains(to))
							flag=true;
						lowerset.add(to);
						lowertable.put(current, lowerset);
	
					}
					/*else if(to.isAccept())
					{
						Set<State> lowerset=lowertable.get(current);
						Set<State> tolowerset=lowertable.get(to);

						if(!lowerset.contains(to))
							flag=true;
						if(!lowerset.containsAll(tolowerset))
							flag=true;
						lowerset.addAll(tolowerset);
						lowerset.add(to);
						lowertable.put(current, lowerset);
					}*/
					else
					{
						
						Set<State> tolowerset=lowertable.get(to);
						Set<State> lowerset=lowertable.get(current);
						if(!lowerset.containsAll(tolowerset))
							flag=true;
						lowerset.addAll(tolowerset);
						if(to.isAccept())
						{
							if(!lowerset.contains(to))
								flag=true;
							lowerset.add(to);
						}

						lowertable.put(current, lowerset);
					}

				}

					
							
			}
		}
		////////////////////////////////////////////////////
		//to calculate the anchropoint
				 flag=true;
				while(flag)
				{
					flag=false;
					visited=new HashSet<State>();
					worklist=new LinkedList<State>();
					init=a.getInitialState();
					worklist.add(init);
					visited.add(init);
					while(!worklist.isEmpty())
					{
						State current=worklist.poll();
						for(Transition st:current.getTransitions())
						{
							State to=st.getDest();
							if(!visited.contains(to))
							{
								worklist.add(to);
								visited.add(to);
							}
							if(st.getMax()==st.getMin()&& st.getMin()=='>')
							{
								Set<State> toanchorset=lastanchorrtable.get(to);
								if(!toanchorset.contains(to))
									flag=true;
								toanchorset.add(to);
								lastanchorrtable.put(to, toanchorset);
							}
							else if(current==a.getInitialState())
							{
								Set<State> toanchorset=lastanchorrtable.get(to);
								if(!toanchorset.contains(current))
									flag=true;
								toanchorset.add(current);
								lastanchorrtable.put(to, toanchorset);
							}
							else
							{
								Set<State> toanchorset=lastanchorrtable.get(to);
								Set<State> anchorset=lastanchorrtable.get(current);
								if(!toanchorset.containsAll(anchorset))
									flag=true;
								toanchorset.addAll(anchorset);
								lastanchorrtable.put(to, toanchorset);
							}

						}

							
									
					}
				}
				/*for(State s:lastanchorrtable.keySet())
				{
					System.out.println(s+ "==>"+lastanchorrtable.get(s));
				}*/
				//////////////////////////////////////////////////////////////////
				//to calculate which states are in the set of tail
				 flag=true;
				 endingset.addAll(auto.getAcceptStates());
					while(flag)
					{
						flag=false;
						for(State s: auto.getStates())
							for(Transition tr:s.getTransitions())
							{
								State to = tr.getDest();
								if(endingset.contains(to) && !endingset.contains(s) && tr.getMin()!= '>' && tr.getMin() != '<')
									{
											endingset.add(s);
											flag=true;
									}
							}
						
					}
	
	}
	public TagAbstractor(Automaton a)
	{
		auto=a;
		String mark="";
		initilize();//obtain necessary information
		Set<StateGroup> SGset=new HashSet<StateGroup>();
		Set<State> alluppernode=new HashSet<State>();
		//System.out.println(lowertable);
		/* 
		 * handle all html tags, we say an html complete, if it start with a "<" and end with a ">", we say it is incomplete if an html
		 * tag end with a ">" but start from the initial state or it start with "<" and end with an accept state
		 */
		for(State s:auto.getStates())
			alluppernode.addAll(uppertable.get(s));
		//System.out.println(alluppernode);

		for(State us:alluppernode)
		{
			Set<State> lowerset=lowertable.get(us);

			
			for(State ls:lowerset )
			{
				//System.out.println("here");
				Set<State> checkset=uppertable.get(ls);
				if(!checkset.contains(us))//check if it is a incomplete tag
					throw new Error("tags are not match"+us.toString()+" "+ls.toString());
				StateGroup sg=new StateGroup();
				sg.allstates.add(us);
				sg.allstates.add(ls);
				sg.start=us;
				sg.end=ls;
				SGset.add(sg);
			}
		}
		//System.out.println(SGset.size());
		//System.out.println(SGset);
		for(State s:auto.getStates())
		{
			Set<State> upperset=uppertable.get(s);

			Set<State> lowerset=lowertable.get(s);
			//System.out.println(upperset);
			//System.out.println(lowerset);
			for(StateGroup sg:SGset)
			{
				if(upperset.contains(sg.start)&&lowerset.contains(sg.end))
					sg.allstates.add(s);
			}

		}

		Hashtable<SemiTagNode, StateGroup> HTMLNodeToStateGroup=new Hashtable< SemiTagNode,StateGroup>();
		Hashtable<StateGroup, SemiTagNode> StateGroupToHTMLNode=new Hashtable< StateGroup,SemiTagNode>();
		for(StateGroup sg:SGset)
		{
			SemiTagNode n=new SemiTagNode();
			HTMLNodeToStateGroup.put(n, sg);
			StateGroupToHTMLNode.put(sg, n);
		}
		for(SemiTagNode htmlnode:HTMLNodeToStateGroup.keySet())
		{
			StateGroup sg=HTMLNodeToStateGroup.get(htmlnode);
			Hashtable<State,State> copytable=new Hashtable<State,State>();
			for(State s:sg.allstates)
				copytable.put(s, new State());
			Automaton v=new Automaton();
			for(State s:sg.allstates)
			{
				State p=copytable.get(s);
				if(s==sg.start)
				{
					v.setInitialState(p);
				}
				if(s==sg.end)
				{
					p.setAccept(true);
				}
				for(Transition st:s.getTransitions())
				{
					State to=st.getDest();
					if(sg.allstates.contains(to))
					{
						if(s!=sg.start && st.getMax()==st.getMin()&& st.getMin()=='<' )
							continue;
						if(to !=sg.end && st.getMax()==st.getMin()&& st.getMin()=='>' )
							continue;
						p.addTransition(new Transition(st.getMin(),st.getMax(),copytable.get(to)));
					}
				}
			}
			htmlnode.Value=v;	
			//System.out.println("ff"+v.getFiniteStrings());
		}
		//System.out.println(lastanchorrtable);
		//tagtree

		for(SemiTagNode htmlnode:HTMLNodeToStateGroup.keySet())
		{
			//System.out.println("old"+htmlnode);
			tagtree.allnodes.add(htmlnode);
			StateGroup sg=HTMLNodeToStateGroup.get(htmlnode);
			Set<State> anchorset;
			/*if(sg.start==auto.getInitialState())
				 anchorset=lastanchorrtable.get(sg.start);
			else
				anchorset=lastanchorrtable.get(sg.start.step('<'));*/
			anchorset=lastanchorrtable.get(sg.start);
			if(anchorset.isEmpty())
				throw new Error("unhooked node"+ sg.start);
			//System.out.println(sg.allstates);

			if(anchorset.contains(auto.getInitialState()))//those hooked to the initial state
				tagtree.heads.add(htmlnode);
			if(sg.end.isAccept())
				tagtree.tails.add(htmlnode);//those are end of this sentence

			for(SemiTagNode p:HTMLNodeToStateGroup.keySet())
			{
				StateGroup sgp=HTMLNodeToStateGroup.get(p);
				//System.out.println(p);
				//System.out.println(anchorset);
				if(anchorset.contains(sgp.end))
				{
					SemiTagEdge e=new SemiTagEdge(p,htmlnode, mark);
					p.outegdes.add(e);
					htmlnode.inegdes.add(e);

				}
			}
		}
		for(SemiTagNode htmlnode:HTMLNodeToStateGroup.keySet())
		{
			htmlnode.setStatus();
			if(htmlnode.IsUpperIncomplete() && !tagtree.heads.contains(htmlnode))
				throw new Error("upper incomplete must in the head set");
			if(htmlnode.IsLowerIncomplete() && !tagtree.tails.contains(htmlnode))
				throw new Error("lower incomplete must in the tail set");
		}
		//System.out.println(tagtree.heads);
	}
	public TagAbstractor(OutputNode on)
	{
		auto=on.getValue();
		String mark=on.MethodSig();
		initilize();//obtain necessary information
		Set<StateGroup> SGset=new HashSet<StateGroup>();
		Set<State> alluppernode=new HashSet<State>();
		if(on.getOffset()==-1)
		{
			SemiTagNode se=new SemiTagNode();
			se.Value=Automaton.makeEmptyString();
			se.Methodsig=on.MethodSig();
			se.position=SemiTagNode.ENTRYMETHOD;
			se.status=3;
			tagtree.allnodes.add(se);
			tagtree.heads.add(se);
			tagtree.tails.add(se);
			return;
			
		}
		if(on.getOffset()==Integer.MAX_VALUE)
		{
			SemiTagNode se=new SemiTagNode();
			se.Value=Automaton.makeEmptyString();
			se.Methodsig=on.MethodSig();
			se.position=SemiTagNode.EXITMETHOD;
			se.status=3;

			tagtree.allnodes.add(se);
			tagtree.heads.add(se);
			tagtree.tails.add(se);
			return;
			
		}
		if(on.getValue().isEmptyString())
		{
			SemiTagNode se=new SemiTagNode();
			se.Value=Automaton.makeEmptyString();
			se.Methodsig=on.MethodSig();
			se.position=SemiTagNode.EXITMETHOD;
			se.status=3;

			tagtree.allnodes.add(se);
			tagtree.heads.add(se);
			tagtree.tails.add(se);
			return;
			
		}
		if(on.getValue().isEmpty())
		{
			SemiTagNode se=new SemiTagNode();
			se.Value=Automaton.makeEmptyString();
			se.Methodsig=on.MethodSig();
			se.position=SemiTagNode.EXITMETHOD;
			se.status=3;

			tagtree.allnodes.add(se);
			tagtree.heads.add(se);
			tagtree.tails.add(se);
			return;
			
		}
		//System.out.println(lowertable);
		/* 
		 * handle all html tags, we say an html complete, if it start with a "<" and end with a ">", we say it is incomplete if an html
		 * tag end with a ">" but start from the initial state or it start with "<" and end with an accept state
		 */
		for(State s:auto.getStates())
			alluppernode.addAll(uppertable.get(s));
		//System.out.println(alluppernode);

		for(State us:alluppernode)
		{
			Set<State> lowerset=lowertable.get(us);

			
			for(State ls:lowerset )
			{
				//System.out.println("here");
				Set<State> checkset=uppertable.get(ls);
				if(!checkset.contains(us) && us!=auto.getInitialState())//check if it is a incomplete tag
				{
					try{
						throw new SemiTagException("May have errors, tags are not match"+" "+on.MethodSig()+on.getOffset()+" "+on.getValue().toDot()+" "+us.toString()+" "+ls.toString());
					}
					catch(SemiTagException e)
					{
						//e.printStackTrace();
					}
					continue;
					//System.out.println();
					//throw new Error("tags are not match"+" "+on.MethodSig()+on.getOffset()+" "+on.getValue().toDot()+" "+us.toString()+" "+ls.toString());
				}
				else if(!checkset.contains(us) && us==auto.getInitialState())
					continue;
				StateGroup sg=new StateGroup();
				sg.allstates.add(us);
				sg.allstates.add(ls);
				sg.start=us;
				sg.end=ls;
				SGset.add(sg);
			}
		}
		//System.out.println(SGset.size());
		//System.out.println(SGset);
		for(State s:auto.getStates())
		{
			Set<State> upperset=uppertable.get(s);

			Set<State> lowerset=lowertable.get(s);
			//System.out.println(upperset);
			//System.out.println(lowerset);
			for(StateGroup sg:SGset)
			{
				if(upperset.contains(sg.start)&&lowerset.contains(sg.end))
					sg.allstates.add(s);
			}

		}

		Hashtable<SemiTagNode, StateGroup> HTMLNodeToStateGroup=new Hashtable< SemiTagNode,StateGroup>();
		Hashtable<StateGroup, SemiTagNode> StateGroupToHTMLNode=new Hashtable< StateGroup,SemiTagNode>();
		for(StateGroup sg:SGset)
		{
			SemiTagNode n=new SemiTagNode();
			n.Methodsig=on.MethodSig();
			HTMLNodeToStateGroup.put(n, sg);
			StateGroupToHTMLNode.put(sg, n);
		}
		for(SemiTagNode htmlnode:HTMLNodeToStateGroup.keySet())
		{
			StateGroup sg=HTMLNodeToStateGroup.get(htmlnode);
			Hashtable<State,State> copytable=new Hashtable<State,State>();
			for(State s:sg.allstates)
				copytable.put(s, new State());
			Automaton v=new Automaton();
			for(State s:sg.allstates)
			{
				State p=copytable.get(s);
				if(s==sg.start)
				{
					v.setInitialState(p);
				}
				if(s==sg.end)
				{
					p.setAccept(true);
				}
				for(Transition st:s.getTransitions())
				{
					State to=st.getDest();
					if(sg.allstates.contains(to))
					{
						if(s!=sg.start && st.getMax()==st.getMin()&& st.getMin()=='<' )
							continue;
						if(to !=sg.end && st.getMax()==st.getMin()&& st.getMin()=='>' )
							continue;
						p.addTransition(new Transition(st.getMin(),st.getMax(),copytable.get(to)));
					}
				}
			}
			htmlnode.Value=v;	
			//System.out.println("ff"+v.getFiniteStrings());
		}
		//System.out.println(lastanchorrtable);
		//tagtree

		for(SemiTagNode htmlnode:HTMLNodeToStateGroup.keySet())
		{
			//System.out.println("old"+htmlnode);
			tagtree.allnodes.add(htmlnode);
			StateGroup sg=HTMLNodeToStateGroup.get(htmlnode);
			Set<State> anchorset;
			/*if(sg.start==auto.getInitialState())
				 anchorset=lastanchorrtable.get(sg.start);
			else
				anchorset=lastanchorrtable.get(sg.start.step('<'));*/
			anchorset=lastanchorrtable.get(sg.start);
			if(anchorset.isEmpty())
				throw new Error("unhooked node"+ sg.start);
			//System.out.println(sg.allstates);

			if(anchorset.contains(auto.getInitialState()))//those hooked to the initial state
				tagtree.heads.add(htmlnode);
			//if(sg.end.isAccept())
			if(endingset.contains(sg.end))
				tagtree.tails.add(htmlnode);//those are end of this sentence

			for(SemiTagNode p:HTMLNodeToStateGroup.keySet())
			{
				StateGroup sgp=HTMLNodeToStateGroup.get(p);
				//System.out.println(p);
				//System.out.println(anchorset);
				if(anchorset.contains(sgp.end))
				{
					SemiTagEdge e=new SemiTagEdge(p,htmlnode, mark);
					p.outegdes.add(e);
					htmlnode.inegdes.add(e);

				}
			}
		}
		for(SemiTagNode htmlnode:HTMLNodeToStateGroup.keySet())
		{
			htmlnode.setStatus();
			if(htmlnode.IsUpperIncomplete() && !tagtree.heads.contains(htmlnode))
				throw new Error("upper incomplete must in the head set");
			if(htmlnode.IsLowerIncomplete() && !tagtree.tails.contains(htmlnode))
				throw new Error("lower incomplete must in the tail set");
		}
		//System.out.println(tagtree.heads);

	}

		


}
