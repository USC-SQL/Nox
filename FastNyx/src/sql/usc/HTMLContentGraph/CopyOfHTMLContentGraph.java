package sql.usc.HTMLContentGraph;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


import sql.usc.SemiTagGraph.SemiTagEdge;
import sql.usc.SemiTagGraph.SemiTagGraph;
import sql.usc.SemiTagGraph.SemiTagNode;

public class CopyOfHTMLContentGraph {
	SemiTagGraph stg;
	Set<HTMLContentNode> allnodes=new HashSet<HTMLContentNode>();
	Set<HTMLContentNode> heads=new HashSet<HTMLContentNode>();
	public String toDot()
	{
		StringBuilder b = new StringBuilder("digraph HtmlTree {\n");
		b.append("  rankdir = LR;\n");
		for(HTMLContentNode node:allnodes)
		{
			String tagname="\""+node.id+";"+node.tagname+"\"";
			b.append("  ").append(tagname);
			b.append("[shape=box];\n");
			//b.append("  ").append(current.Value.getFiniteStrings());
			for (HTMLContentNode next : node.successors) {
				b.append("  ");
				//System.out.println(tagname);
				String nextsig="\""+next.id+";"+next.tagname+"\"";
				b.append(tagname+" -> "+nextsig+"\n");
				b.append("[label = \""+node.sig+"\"]");

				

			}
		}
		return b.append("}\n").toString();
	}
	public CopyOfHTMLContentGraph(SemiTagGraph stg)
	{
		this.stg=stg;
		Set<SemiTagNode> alltagnodes=stg.getAllNodes();
		Set<SemiTagNode> tagheads=stg.getHeads();
		Hashtable<SemiTagNode, Set<HTMLContentNode>> outable=new Hashtable<SemiTagNode, Set<HTMLContentNode>>();
		Hashtable<SemiTagNode, HTMLContentNode> genable=new Hashtable<SemiTagNode, HTMLContentNode>();

		for(SemiTagNode n:alltagnodes)
		{
			if(TagTester.IsOpenTag(n) || TagTester.IsSelfEndTag(n))
			{
				String tagname=TagTester.getTagName(n);
				genable.put(n, new HTMLContentNode(tagname,n.Methodsig));
				allnodes.add(genable.get(n));
				if(tagheads.contains(n))
					heads.add(genable.get(n));
				if(genable.get(n).tagname==null)
					throw new Error(tagname+" "+n.toString());
			}
			outable.put(n, new HashSet<HTMLContentNode>());
		}
		boolean flag=true;
		while(flag)
		{
			flag=false;
			for(SemiTagNode n:alltagnodes)
			{
				Set<HTMLContentNode> INset=new HashSet<HTMLContentNode>();
				for(SemiTagEdge ine:n.getInEdges())
				{
					SemiTagNode p=ine.getSource();
					Set<HTMLContentNode> pout=outable.get(p);
					/*if(pout==null)
					{
						System.out.println(n.toString());
						System.out.println(p.getTagname());
						System.out.println(p.toString());
						System.out.println(alltagnodes.contains(p));
					}*/
					INset.addAll(pout);
				}
				if(TagTester.IsSelfEndTag(n))
				{
					HTMLContentNode current=genable.get(n);
					for(HTMLContentNode pre:INset)
					{
						if(!pre.successors.contains(current))
						{
							pre.successors.add(current);
							flag=true;
						}
						if(!current.predecessors.contains(pre))
						{
							current.predecessors.add(pre);
							flag=true;
						}
					}
					Set<HTMLContentNode> newOut=INset;
					Set<HTMLContentNode> oldOut=outable.get(n);
					if(!oldOut.equals(newOut))
					{
						flag=true;
						outable.put(n, newOut);
					}		
				}
				else if(TagTester.IsOpenTag(n))
				{
					HTMLContentNode current=genable.get(n);
					for(HTMLContentNode pre:INset)
					{
						if(!pre.successors.contains(current))
						{
							pre.successors.add(current);
							flag=true;
						}
						if(!current.predecessors.contains(pre))
						{
							current.predecessors.add(pre);
							flag=true;
						}
					}
					Set<HTMLContentNode> newOut=new HashSet<HTMLContentNode>();
					newOut.add(current);
					Set<HTMLContentNode> oldOut=outable.get(n);
					if(!oldOut.equals(newOut))
					{
						flag=true;
						outable.put(n, newOut);
					}
				}
				else if(TagTester.IsEndTag(n))
				{
					//HTMLContentNode current=genable.get(n);
					String tagname=TagTester.getTagName(n);
					Set<HTMLContentNode> genOut=new HashSet<HTMLContentNode>();
					if(tagname.equals("!--"))
					{
							Set<HTMLContentNode> visited=new HashSet<HTMLContentNode>();
							Queue<HTMLContentNode> worklist=new LinkedList<HTMLContentNode>();
							worklist.addAll(INset);
							visited.addAll(INset);
							while(!worklist.isEmpty())
							{
								HTMLContentNode head=worklist.poll();
								if(head.tagname.equals("!--"))
									genOut.addAll(head.predecessors);
								else if(heads.contains(head))
									throw new Error("there is an unmatched comment tag");
								else
								{
									for(HTMLContentNode pre:head.predecessors)
										if(!visited.contains(pre))
										{
											visited.add(pre);
											worklist.add(pre);
										}
								}
							}
					}
					else
					{
						if(tagname.startsWith("head"))
							System.out.println("aha"+INset);
						for(HTMLContentNode pre:INset)
						{
							/*if(pre==null)
								System.out.println(tagname);
							System.out.println(pre.tagname+"asa");*/
	
							if(pre.tagname.equals(tagname))
							{
								if(pre.tagname.equals("meta"))
									System.out.println(pre.predecessors);
								genOut.addAll(pre.predecessors);
							}
							else	
							{
								//throw new Error("unmatched tags "+ pre.tagname+" "+tagname+" "+n.Methodsig);
								//genOut.addAll(pre.predecessors);


							}
						}
					}
					Set<HTMLContentNode> newOut=genOut;
					Set<HTMLContentNode> oldOut=outable.get(n);
					if(!oldOut.equals(newOut))
					{
						flag=true;
						outable.put(n, newOut);
					}
				}
				
			}
			
		}
			
	}
}
