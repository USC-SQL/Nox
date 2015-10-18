package sql.usc.OutputGraph.copy;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.bcel.classfile.ClassFormatException;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import sql.usc.StringResolver.MethodSummary;
import sql.usc.StringResolver.StringDatabase;
import sql.usc.StringResolver.StringResolver;
import sql.usc.StringResolver.Utils.Configer;
import sql.usc.StringResolver.Utils.Utils;
import wam.configuration.exceptions.WAMConfigurationException;

public class OutputGraph {
	Metainfor meta;
	 OutPutTree globletree=new OutPutTree();
	public Set<OutputNode> getAllNodes()
	{
		return globletree.allnodes;
	}
	public Set<OutputNode> getHeads()
	{
		return globletree.heads;
	}
	public Set<OutputNode> getTails()
	{
		return globletree.tails;
	}
	private void trim()
	{
		String r="";
		Set<OutputNode> visited=new HashSet<OutputNode>();
		Queue<OutputNode> worklist=new LinkedList<OutputNode>();
		worklist.addAll(globletree.heads);
		visited.addAll(globletree.heads);
		int count=0;
		while(!worklist.isEmpty())
		{
			
			OutputNode current=worklist.poll();


			for (Edge e : current.outegdes) {
				OutputNode suc=e.target;
				if(!visited.contains(suc))
				{
					visited.add(suc);
					worklist.add(suc);
				}
				//b.append("  ").append(current.Value.getFiniteStrings());
				
				
			}
			count++;
		}
		globletree.allnodes.retainAll(visited);
	}
	public String toDot()
	{
		String r="";
		Set<OutputNode> visited=new HashSet<OutputNode>();
		Queue<OutputNode> worklist=new LinkedList<OutputNode>();
		worklist.addAll(globletree.heads);
		visited.addAll(globletree.heads);
		StringBuilder b = new StringBuilder("digraph HtmlTree {\n");
		b.append("  rankdir = LR;\n");
		int count=0;
		while(!worklist.isEmpty())
		{
			
			OutputNode current=worklist.poll();
			//if(count>20)
			//	continue;
			String tagname="\""+current.defig+""+current.GetSampleString()+"\"";
			b.append("  ").append(tagname);
			b.append("[shape=box];\n");
			for (Edge e : current.outegdes) {
				OutputNode suc=e.target;
				if(!visited.contains(suc))
				{
					visited.add(suc);
					worklist.add(suc);
				}
				b.append("  ");
				//System.out.println(tagname);
				b.append(tagname+" -> \""+suc.defig+""+suc.GetSampleString()+"\"");
				b.append("[label = \""+e.methodsig+"\"]");
				//b.append("  ").append(current.Value.getFiniteStrings());
				
				
			}
			count++;
		}
		return b.append("}\n").toString();
	}
	public OutputGraph(String path) throws ClassFormatException, WAMConfigurationException, IOException
	{
		meta=new Metainfor(path);
		for(SootMethod m:meta.allmethods)
		{
			OutputSummary os=new OutputSummary(m,meta.strdb);
			//System.out.println(m.getSignature());
			meta.SummaryTable.put(m.getSignature(), os);
			globletree.allnodes.addAll(os.ot.allnodes);
		}
		
		for(String msig:meta.SummaryTable.keySet())
		{
			OutputSummary os=meta.SummaryTable.get(msig);
			os.parsing(meta);
		}
		for(SootMethod sm:meta.entries)
		{
			String sig=sm.getSignature();
			OutputSummary os=meta.SummaryTable.get(sig);
			globletree.heads.add(os.START);
		}
		trim();
		for(OutputNode on:globletree.allnodes)
		{
			on.getValue().removeDeadTransitions();
		}
		Utils.Log("Output Graph is built");
	}
	public OutputGraph(OutPutTree it)
	{
		globletree=it;
	}
}
