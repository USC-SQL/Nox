package sql.usc.OutputGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import sql.usc.MIF.MIF;
import sql.usc.StringResolver.MethodSummary;
import sql.usc.StringResolver.StringDatabase;
import sql.usc.StringResolver.StringResolver;
import sql.usc.StringResolver.Utils.Configer;
import sql.usc.StringResolver.Utils.Utils;
import wam.configuration.exceptions.WAMConfigurationException;

public class OutputGraph {
	Metainfor meta;
	public  OutPutTree globletree=new OutPutTree();
	private void visit(OutputNode n,Set<OutputNode> temp,Set<OutputNode> perm, LinkedList<OutputNode> sortlist)
	{
		if(perm.contains(n))
			return;
		if(temp.contains(n))
			return;
		temp.add(n);
		for (Edge e : n.outegdes) {
			OutputNode suc=e.target;
			visit(suc,temp,perm,sortlist);
		}
		perm.add(n);
		temp.remove(n);
		sortlist.addFirst(n);
	}
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
	public String dumpToHtml()
	{

		String r="";
		Set<OutputNode> visited=new HashSet<OutputNode>();
		Queue<OutputNode> worklist=new LinkedList<OutputNode>();
		worklist.addAll(globletree.heads);
		visited.addAll(globletree.heads);
		LinkedList<OutputNode> sortlist=new LinkedList<OutputNode>();
		Set<OutputNode> temp=new HashSet<OutputNode>();
		Set<OutputNode> perm=new HashSet<OutputNode>();
		int count=0;
		for(OutputNode n:worklist)
		{
			visit(n,temp,perm,sortlist);
		}
		for(OutputNode n:sortlist)
		{
			r+=n.GetSampleString()+"\n";
		}
		return r;
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
			String tagname="\""+""+current.getId()+";"+current.getPrefix()+"\"";
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
				b.append(tagname+" -> \""+""+suc.getId()+";"+suc.getPrefix()+"\"");
				//b.append("[label = \""+e.methodsig+"\"]");
				//b.append("  ").append(current.Value.getFiniteStrings());
				
				
			}
			count++;
		}
		return b.append("}\n").toString();
	}
	public OutputGraph(File template) throws IOException
	{
		System.out.println(template.getAbsolutePath());
		BufferedReader br = new BufferedReader(new FileReader(template));
		String line;
		int counter=0;
		OutputNode current=null,last=null;
		while ((line = br.readLine()) != null) {
		   // process the line.
			//'System.out.println(line);
			current=new OutputNode(line);
			globletree.allnodes.add(current);
			if(counter==0)
			{
				//System.out.println(current.getValue().getCommonPrefix());
				globletree.heads.add(current);
			}
			else
			{
				//System.out.println("last "+last.getValue().getCommonPrefix());

				//System.out.println("current "+current.getValue().getCommonPrefix());

				last.outegdes.add(new Edge(last, current,""));
			}
			counter++;
			last=current;
				
				
		}
		br.close();

	}
	public OutputGraph(MIF mif) throws ClassFormatException, WAMConfigurationException, IOException
	{
		meta=new Metainfor(mif);
		Queue<SootMethod> sorted=meta.sortMethodsRTO();
		for(SootMethod m:meta.allmethods)
		{
			 System.out.println(m.getName());
			OutputSummary os=new OutputSummary(m,meta.strdb);
			meta.SummaryTable.put(m.getSignature(), os);
			//globletree.allnodes.addAll(os.ot.allnodes);
		}
		boolean flag=true;
		Set<SootMethod> unsolved =new HashSet<SootMethod>();
		unsolved.addAll(sorted);
		while(flag)
		{
			flag=false;
			int oldcnt=unsolved.size();
			sorted=meta.sortMethodsRTO();

			while(!sorted.isEmpty())
			{
				SootMethod m=sorted.poll();
				OutputSummary os=meta.SummaryTable.get(m.getSignature());
				if(!os.solved)
				{
					System.out.println(m.getSignature());
					OutputSummary nos=new OutputSummary(m,meta.strdb);
					nos.parsing(meta);
					if(nos.solved)
					{
						meta.SummaryTable.put(m.getSignature(), nos);
						unsolved.remove(m);
					}
				}
				//System.out.println(m);
			}


			if(oldcnt!=unsolved.size())
				flag=true;
		}
		if(unsolved.size()!=0)
		{
			//handling strong connected components;
			String err="";
			for(SootMethod sm:unsolved)
			 err+=sm.toString();
			throw new Error("There is a string connected component! "+err);
		}
		for(SootMethod m:meta.entries)
		{
			OutputSummary os=meta.SummaryTable.get(m.getSignature());
			System.out.println(m);
			globletree.allnodes.addAll(os.ot.allnodes);
			globletree.heads.addAll(os.ot.heads);
			globletree.tails.addAll(os.ot.tails);

		}
		/*for(SootMethod m:meta.allmethods)
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
		}*/
		Utils.Log("Output Graph is built");
	}
	public OutputGraph(OutPutTree it)
	{
		globletree=it;
	}
}
