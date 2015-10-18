package sql.usc.HTMLContentGraph;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import CSSdatabase.CSSdataBase;



import sql.usc.Color.Color;
import sql.usc.SemiTagGraph.SemiTagEdge;
import sql.usc.SemiTagGraph.SemiTagGraph;
import sql.usc.SemiTagGraph.SemiTagNode;

public class HTMLContentGraph {
	SemiTagGraph stg;
	Set<HTMLContentNode> allnodes=new HashSet<HTMLContentNode>();
	Set<HTMLContentNode> heads=new HashSet<HTMLContentNode>();
	public Set<HTMLContentNode> GetAllNodes()
	{
		return allnodes;
	}
	public String toDot()
	{
		StringBuilder b = new StringBuilder("digraph HtmlTree {\n");
		b.append("  rankdir = LR;\n");
		for(HTMLContentNode node:allnodes)
		{
			String tagname="\""+node.id+";"+node.tagname+node.sig+"\"";
			b.append("  ").append(tagname);
			b.append("[shape=box];\n");
			//b.append("  ").append(current.Value.getFiniteStrings());
			for (HTMLContentNode next : node.successors) {
				b.append("  ");
				//System.out.println(tagname);
				String nextsig="\""+next.id+";"+next.tagname+next.sig+"\"";
				b.append(tagname+" -> "+nextsig+"\n");
				b.append("[label = \""+node.sig+"\"]");

				

			}
		}
		return b.append("}\n").toString();
	}
	public HTMLContentGraph(SemiTagGraph stg, CSSdataBase cssdb )
	{
		this.stg=stg;
		Set<SemiTagNode> alltagnodes=stg.getAllNodes();
		Set<SemiTagNode> tagheads=stg.getHeads();
		Hashtable<SemiTagNode, Set<SemiTagNode>> OUTtable=new Hashtable<SemiTagNode, Set<SemiTagNode>>();
		Hashtable<SemiTagNode, Set<SemiTagNode>> INtable=new Hashtable<SemiTagNode, Set<SemiTagNode>>();

		Hashtable<SemiTagNode, HTMLContentNode> gentable=new Hashtable<SemiTagNode, HTMLContentNode>();
		TagInforAbstractor ta=new TagInforAbstractor(cssdb);
		for(SemiTagNode n:alltagnodes)
		{
			if(TagTester.IsOpenTag(n) || TagTester.IsSelfEndTag(n))
			{
				n.Acyclize();
				//String tagname=TagTester.getTagName(n);
				String tagname=TagInforAbstractor.getTagName(n);
				Set<Color> bgcolors=ta.getBackgroundColors(n);
				Set<Color> textcolors=ta.getTextColors(n);
				Set<Color> linkcolors=ta.getLinkColors(n);	
				HTMLContentNode ctntnode=new HTMLContentNode(tagname,n.Methodsig);
				ctntnode.bgcolor=bgcolors;
				ctntnode.textcolor=textcolors;
				ctntnode.linkcolor=linkcolors;

				gentable.put(n, ctntnode);
				allnodes.add(gentable.get(n));
				if(tagheads.contains(n))
					heads.add(gentable.get(n));
				if(gentable.get(n).tagname==null)
					throw new Error(tagname+" "+n.toString());
			}
			OUTtable.put(n, new HashSet<SemiTagNode>());
			INtable.put(n, new HashSet<SemiTagNode>());

		}
		boolean flag=true;
		//calculate the dependent relationship
		while(flag)
		{
			flag=false;
			for(SemiTagNode n:alltagnodes)
			{
				//prepare in set

				Set<SemiTagNode> INset=new HashSet<SemiTagNode>();

				for(SemiTagEdge ine:n.getInEdges())
				{
					SemiTagNode p=ine.getSource();
					Set<SemiTagNode> pout=OUTtable.get(p);
					INset.addAll(pout);
				}
				INtable.put(n, INset);
				//prepare out set
				if(TagTester.IsSelfEndTag(n))
				{
					Set<SemiTagNode> newOut=INset;
					Set<SemiTagNode> oldOut=OUTtable.get(n);
					if(!oldOut.equals(newOut))
					{
						flag=true;
						OUTtable.put(n, newOut);

					}		
				}
				else if(TagTester.IsOpenTag(n))
				{
					Set<SemiTagNode> newOut=new HashSet<SemiTagNode>();
					newOut.add(n);
					Set<SemiTagNode> oldOut=OUTtable.get(n);
					if(!oldOut.equals(newOut))
					{
						flag=true;
						OUTtable.put(n, newOut);
					}
				}
				else if(TagTester.IsEndTag(n))
				{
					//HTMLContentNode current=genable.get(n);
					String tagname=TagTester.getTagName(n);
					Set<SemiTagNode> genOut=new HashSet<SemiTagNode>();
					if(tagname.equals("!--"))
					{
							Set<SemiTagNode> visited=new HashSet<SemiTagNode>();
							Queue<SemiTagNode> worklist=new LinkedList<SemiTagNode>();
							worklist.addAll(INset);
							visited.addAll(INset);
							while(!worklist.isEmpty())
							{
								SemiTagNode head=worklist.poll();
								String pretagname=TagTester.getTagName(head);
								if(pretagname.equals("!--"))
									genOut.addAll(INtable.get(head));
								else if(heads.contains(head))
									throw new Error("there is an unmatched comment tag");
								else
								{
									for(SemiTagNode pre:INtable.get(head))
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
					//	System.out.println(tagname+"+++"+INset);

						for(SemiTagNode pre:INset)
						{
							/*if(pre==null)
								System.out.println(tagname);
							System.out.println(pre.tagname+"asa");*/
							String pretagname=TagTester.getTagName(pre);
							if(pretagname.equals(tagname))
							{
								genOut.addAll(INtable.get(pre));
							}
							else	
							{
								try{
									throw new HTMLException("unmatched tags "+ pretagname+" "+tagname+" "+n.Methodsig);
								}
								catch(HTMLException e)
								{
									e.printStackTrace();
								}
								//genOut.addAll(INtable.get(pre));


							}
						}
					}
					Set<SemiTagNode> newOut=genOut;
					Set<SemiTagNode> oldOut=OUTtable.get(n);
					if(!oldOut.equals(newOut))
					{
						flag=true;
						OUTtable.put(n, newOut);
					}
				}
				
			}
			
		}
		//connnect all nodes
		for(SemiTagNode n:gentable.keySet())
		{
			HTMLContentNode current=gentable.get(n);
			Set<SemiTagNode> INset=INtable.get(n);
			//System.out.println(current.toString()+":"+INset);
			for(SemiTagNode pre:INset)
			{
				HTMLContentNode p=gentable.get(pre);
				if(p==null)
					throw new Error("unexpected node ");
				current.predecessors.add(p);
				p.successors.add(current);
			}
		}
			
	}
}
