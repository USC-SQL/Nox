package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;



import sql.usc.SemiTagGraph.SemiTagNode;
import sql.usc.SemiTagGraph.SemiTagTree;
import sql.usc.SemiTagGraph.TagAbstractor;
import sql.usc.StringResolver.ExtendedOperation;
import dk.brics.automaton.Automaton;

public class HTMLtester {
	public static void main(String[] argv) throws FileNotFoundException
	{
		PrintWriter pw=new PrintWriter("/home/dingli/graph1.gv");
		String seperator=""+ExtendedOperation.parameter_seperator;
		String input=seperator+"parameter0"+seperator;
		Automaton a1=Automaton.makeString("aa><t>a</t>");
		Automaton a2=Automaton.makeString("at><t>a</k></t>");
		Automaton a3=Automaton.makeString("asdasad <body>dasdas ");
		Automaton a4=Automaton.makeString("asdas").concatenate(a3);
		a4.reduce();
		a4.reduce();
		a4.minimize();
		a4.minimize();
		Automaton a0=Automaton.makeEmptyString();
		a4=a0.concatenate(a1);

		pw.println(a4.toDot());
		//pw.close();	
		pw.close();
		//TagAbstractor ta=new TagAbstractor(a3);
		//TagAbstractor ta2=new TagAbstractor(a2);
		//ta2.tagtree.union(ta.tagtree);
		//HTMLTree c=ta.tagtree.clone();
		//pw.println(ta.tagtree.clone().ToDot());
		//pw.println(ta2.tagtree.clone().ToDot());
		//pw.println(newt.ToDot());



		//System.out.println(ta.tagtree.Getallnodes());
		//System.out.println(ta.tagtree.GetTails());

		//System.out.println(ta.uppertable);
		/*Hashtable<State,Set<State>> table=calreach(M3);
		
		for (State s : M3.getStates()) {

			Set<State> reachset=table.get(s);
			System.out.println(s+" aa"+reachset);
		}*/
	}
}
