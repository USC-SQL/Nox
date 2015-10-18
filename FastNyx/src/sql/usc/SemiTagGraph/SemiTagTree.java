package sql.usc.SemiTagGraph;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import sql.usc.HTMLContentGraph.*;

import sql.usc.OutputGraph.Edge;
import sql.usc.OutputGraph.OutputNode;


import dk.brics.automaton.*;

public class SemiTagTree {
	Set<SemiTagNode> heads=new HashSet<SemiTagNode>();
	Set<SemiTagNode> allnodes=new HashSet<SemiTagNode>();
	Set<SemiTagNode> tails=new HashSet<SemiTagNode>();
	PrintWriter pw;
	public boolean Changed(SemiTagTree n)
	{
		if(this.allnodes.size()!=n.allnodes.size())
			return true;
		int edge1=0,edge2=0;
		for(SemiTagNode tn:this.allnodes)
			edge1+=tn.outegdes.size();
		for(SemiTagNode tn:n.allnodes)
			edge2+=tn.outegdes.size();
		if(edge1!=edge2)
			return true;
		return false;
	}
	public void mark(String s)
	{
		for(SemiTagNode n:allnodes)
		{
			n.sigs=new HashSet<String>();
			n.sigs.add(s);
		}
	}
	public boolean isEmpty()
	{
		return allnodes.isEmpty();
	}
	public int GetNodeNum()
	{
		return allnodes.size();
	}
	public Set<SemiTagNode> GetHeads()
	{
		return heads;
	}
	public Set<SemiTagNode> GetTails()
	{
		return tails;
	}
	public Set<SemiTagNode> Getallnodes()
	{
		return allnodes;
	}
	public void AcyclizeAll()
	{
		for(SemiTagNode node:allnodes)
			node.Acyclize();
	}
	public String ToDot()
	{
		String r="";
		Set<SemiTagNode> visited=new HashSet<SemiTagNode>();
		Queue<SemiTagNode> worklist=new LinkedList<SemiTagNode>();
		worklist.addAll(heads);
		visited.addAll(heads);
		StringBuilder b = new StringBuilder("digraph HtmlTree {\n");
		b.append("  rankdir = LR;\n");
		int count=0;
		for(SemiTagNode node:allnodes)
		{
			SemiTagNode current=node;
			String tagname="\""+""+current.getId()+current.toString()+"\"";
			b.append("  ").append(tagname);
			b.append("[shape=box];\n");
			for (SemiTagEdge e : current.outegdes) {
				SemiTagNode suc=e.target;
				b.append("  ");
				//System.out.println(tagname);
				b.append(tagname+" -> \""+""+suc.getId()+suc.toString()+"\"");
				String cutmethodsig;
				if(e.methodsig.length()>26)
					cutmethodsig=e.methodsig.substring(16, 25);
				else
					cutmethodsig=e.methodsig;
				b.append("[label = \""+cutmethodsig+"\"]");
				//b.append("  ").append(current.Value.getFiniteStrings());
				
				
			}
		}
		/*while(!worklist.isEmpty())
		{
			
			SemiTagNode current=worklist.poll();
			String tagname="\""+""+current.getId()+current.toString()+"\"";
			b.append("  ").append(tagname);
			b.append("[shape=box];\n");
			for (SemiTagEdge e : current.outegdes) {
				SemiTagNode suc=e.target;
				if(!visited.contains(suc))
				{
					visited.add(suc);
					worklist.add(suc);
				}
				b.append("  ");
				//System.out.println(tagname);
				b.append(tagname+" -> \""+""+suc.getId()+suc.toString()+"\"");
				String cutmethodsig;
				if(e.methodsig.length()>26)
					cutmethodsig=e.methodsig.substring(16, 25);
				else
					cutmethodsig=e.methodsig;
				b.append("[label = \""+cutmethodsig+"\"]");
				//b.append("  ").append(current.Value.getFiniteStrings());
				
				
			}
			count++;
		}*/
		return b.append("}\n").toString();
	}
	public void union(SemiTagTree tree)
	{
		
		this.heads.addAll(tree.heads);
		this.allnodes.addAll(tree.allnodes);
		this.tails.addAll(tree.tails);
		//this.MergeSameGene();
	}
	public SemiTagTree clone()
	{
		SemiTagTree newtree=new SemiTagTree();
		Hashtable<SemiTagNode, SemiTagNode> dup=new Hashtable<SemiTagNode, SemiTagNode>();
		for(SemiTagNode hn:allnodes)
			dup.put(hn, new SemiTagNode());
		for(SemiTagNode hn:allnodes)
		{
			SemiTagNode shadow=dup.get(hn);
			for(SemiTagEdge p:hn.inegdes)				
				shadow.inegdes.add(new SemiTagEdge(dup.get(p.source),dup.get(p.target),p.methodsig));
			for(SemiTagEdge s:hn.outegdes)
				shadow.outegdes.add(new SemiTagEdge(dup.get(s.source),dup.get(s.target),s.methodsig));
			shadow.Value=hn.Value.clone();
			shadow.status=hn.status;
			shadow.gene=hn.gene;
			shadow.sigs=hn.sigs;
		}
		for(SemiTagNode hn:heads)
			newtree.heads.add(dup.get(hn));
		for(SemiTagNode hn:tails)
			newtree.tails.add(dup.get(hn));
		for(SemiTagNode hn:allnodes)
			newtree.allnodes.add(dup.get(hn));
		return newtree;
		
	}
	public void Merge()
	{
		Queue<SemiTagNode> openlist=new LinkedList<SemiTagNode>();
		for(SemiTagNode n:allnodes)
		{
			if(n.IsLowerIncomplete())
			{
				openlist.add(n);
			}
		}
		while(!openlist.isEmpty())
		{
			SemiTagNode head=openlist.poll();
			//merge to successors
			for(SemiTagEdge oe:head.outegdes)
			{
				SemiTagNode t=oe.target;
				//if(t.IsComplete() || t.IsLowerIncomplete())
				//	throw new Error("the tag is illegal"+head.Value.getFiniteStrings()+"+++"+t.Value.getFiniteStrings());
				SemiTagNode nn=new SemiTagNode();
				nn.sigs.addAll(head.sigs);
				nn.sigs.addAll(t.sigs);
				nn.Value=head.Value.concatenate(t.Value);
				nn.setStatus();
				//set the relationship to predecessors;
				for(SemiTagEdge ie:head.inegdes)
				{
					SemiTagEdge nie=new SemiTagEdge(ie.source,nn,ie.methodsig);
					ie.source.outegdes.remove(ie);
					ie.source.outegdes.add(nie);
					nn.inegdes.add(nie);
					nn.inegdes.remove(ie);

				}
				//set the relationship to successors;
				for(SemiTagEdge toe:t.outegdes)
				{
					SemiTagEdge noe=new SemiTagEdge(nn,toe.target,toe.methodsig);
					toe.target.inegdes.remove(toe);
					toe.target.inegdes.add(noe);
					nn.outegdes.add(noe);
					nn.outegdes.remove(toe);


				}
				allnodes.add(nn);
				if(heads.contains(head))
					heads.add(nn);
				if(tails.contains(t))
					tails.add(nn);
				if(!nn.IsComplete())
					openlist.add(nn);
				allnodes.remove(t);
				heads.remove(t);
				tails.remove(t);
				//if(!nn.IsComplete())
				//	openlist.add(nn);

					
			}
			allnodes.remove(head);
			heads.remove(head);
			tails.remove(head);
		}

	}
	public void Trim()
	{
		Set<SemiTagNode> toremove=new HashSet<SemiTagNode>();
		for(SemiTagNode n:allnodes)
		{
			if(n.IsDualIncomplete())
			{
				for(SemiTagEdge ie:n.inegdes)
				{
					if(ie.source==n)
						continue;
					ie.source.outegdes.remove(ie);
					for(SemiTagEdge oe:n.outegdes)
					{
						if(oe.target==n)
							continue;
						SemiTagEdge newe=new SemiTagEdge(ie.source,oe.target,ie.methodsig);
						//ie.source.outegdes.remove(ie);
						ie.source.outegdes.add(newe);
						oe.target.inegdes.remove(oe);
						oe.target.inegdes.add(newe);
					}
				}
				toremove.add(n);
				if(heads.contains(n))
				{
					heads.remove(n);
					for(SemiTagEdge oe:n.outegdes)
					{
						heads.add(oe.target);
					}
				}
				if(tails.contains(n))
				{
					tails.remove(n);
					for(SemiTagEdge ie:n.inegdes)
					{
						tails.add(ie.source);
					}
				}
			}
			
				
		}
		allnodes.removeAll(toremove);
		//remove un-useful edges, usually in the start and end of the graph 
		for(SemiTagNode n:allnodes)
		{
			Set<SemiTagEdge> edgeremove=new HashSet<SemiTagEdge>();
			for(SemiTagEdge ie:n.inegdes)
			{
				if(!allnodes.contains(ie.source))
					edgeremove.add(ie);
			}
			n.inegdes.removeAll(edgeremove);
			edgeremove=new HashSet<SemiTagEdge>();
			for(SemiTagEdge oe:n.outegdes)
			{
				if(!allnodes.contains(oe.target))
					edgeremove.add(oe);
			}
			n.outegdes.removeAll(edgeremove);
		}
	}
	public void CommentElimination()
	{
		Hashtable<SemiTagNode, Set<SemiTagNode>> outable=new Hashtable<SemiTagNode, Set<SemiTagNode>>();
		Hashtable<SemiTagNode, SemiTagNode> genable=new Hashtable<SemiTagNode, SemiTagNode>();
		for(SemiTagNode n:allnodes)
		{
			if(CommentTester.IsOpenCommentTag(n))
			{
				genable.put(n, n);
			}
			outable.put(n, new HashSet<SemiTagNode>());
		}
		for(SemiTagNode n:allnodes)
		{
			if(CommentTester.IsOpenCommentTag(n) ||CommentTester.IsEndCommentTag(n))
			{
				n.incomment=true;
			}
		}
		boolean flag=true;
		while(flag=true)
		{
			
		}
	}

}
