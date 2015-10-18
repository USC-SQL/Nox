package sql.usc.HTMLAnalyser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


import dk.brics.automaton.Automaton;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.tagkit.BytecodeOffsetTag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import sql.usc.OutputGraph.OutputGraph;
import sql.usc.OutputGraph.PrintDetecter;
import sql.usc.SemiTagGraph.SemiTagTree;
import sql.usc.StringResolver.Definition;
import sql.usc.StringResolver.MethodSummary;
import sql.usc.StringResolver.StringResolver;
import sql.usc.StringResolver.Variable;

public class MethodHTMLSummery {
	public SemiTagTree htmltags;
	MethodSummary ms;
	Hashtable<Unit,Set<Unit>> OUTmap=new Hashtable<Unit,Set<Unit>>();

	PrintWriter pw;
	public void printout()
	{
		UnitGraph cfg=ms.getCFG();
		Iterator<Unit> ir=cfg.iterator();
		while(ir.hasNext())
		{
			Unit u=ir.next();
			System.out.println(u+" => "+OUTmap.get(u));
			
		}
	}
	/*public MethodHTMLSummery(OutputGraph OG)
	{

		 Hashtable<Unit,HTMLTree> insmap=new Hashtable<Unit,HTMLTree>();
		 Hashtable<Unit,HTMLTree> genmap=new Hashtable<Unit,HTMLTree>();
		 Hashtable<Unit,Set<Unit>> INmap=new Hashtable<Unit,Set<Unit>>();
		UnitGraph cfg=ms.getCFG();
		Iterator<Unit> ir=cfg.iterator();
		while(ir.hasNext())
		{
			Unit u=ir.next();
			insmap.put(u, new HTMLTree());
			genmap.put(u,new HTMLTree());
			INmap.put(u, new HashSet<Unit>());
			OUTmap.put(u, new HashSet<Unit>());
			if(PrintDetecter.isPrintln(u))//create the gen set
			{
				Automaton a;
				InvokeStmt ismt=(InvokeStmt)u;
				InvokeExpr iexp=ismt.getInvokeExpr();
				String sig=iexp.getMethodRef().toString();
				BytecodeOffsetTag jtag=(BytecodeOffsetTag)u.getTag("BytecodeOffsetTag");
				int offset;
				if(jtag==null)
					offset=-1;
				else
					offset=jtag.getBytecodeOffset();
				//int offset=ismt.get
				Value v=iexp.getArg(0);
				if(v instanceof Constant)
				{
					String s=v.toString();
					a=Automaton.makeString(s.replaceAll("\"", ""));
					//System.out.println(v.toString());
				}
				else
				{
					Set<Definition> in=ms.QueryInset(u);
					Set<Definition> same=ms.SearchSameValue(in, v);
					a=Automaton.makeEmptyString();
					for(Definition def: same)
					{
						Variable value=ms.QueryValue(def);
						a=a.union(value.StringValue());
						//System.out.println(value.StringValue().getFiniteStrings());
					}
				}
				TagAbstractor ta=new TagAbstractor(a);
				//String mark=this.ms.getSig()+":"+offset;
				String mark=""+offset;
				ta.tagtree.mark(mark);
				genmap.put(u, ta.tagtree);
			}

		}
		//calculate the predecessors and successors of each node
		boolean flag=true;
		while(flag)
		{
			flag=false;
			ir=cfg.iterator();
			while(ir.hasNext())
			{
				Unit u=ir.next();
				Set<Unit> INset=new HashSet<Unit>();
				Set<Unit> OUTset=new HashSet<Unit>();

				List<Unit> sucs=cfg.getSuccsOf(u);
				for(int i=0;i<sucs.size();i++)
				{
					Unit s=sucs.get(i);
					INset.addAll(OUTmap.get(s));
				}
				if(PrintDetecter.isPrintln(u))//create the gen set
				{
					OUTset=new HashSet<Unit>();
					OUTset.add(u);
				}
				else
				{
					OUTset=INset;
				}
				Set<Unit> oldOUT=OUTmap.get(u);
				if(!OUTset.equals(oldOUT))
				{
					flag=true;
					OUTmap.put(u, OUTset);
				}
			}
		}
		//connection all nodes;
		ir=cfg.iterator();
		htmltags=new HTMLTree();
		Set<HTMLNode> removable=new HashSet<HTMLNode>();
		while(ir.hasNext())
		{
			Unit u=ir.next();
			if(PrintDetecter.isPrintln(u))//create the gen set
			{
				Set<Unit> INset=new HashSet<Unit>();
				HTMLTree inTree=new HTMLTree();
				List<Unit> sucs=cfg.getSuccsOf(u);
				for(int i=0;i<sucs.size();i++)
				{
					Unit s=sucs.get(i);
					INset.addAll(OUTmap.get(s));
					//System.out.println(genmap.get(s).allnodes);
				}
				for(Unit iu: INset)
				{
					inTree.union(genmap.get(iu));
				}
				HTMLTree genTree=genmap.get(u);
				htmltags.allnodes.addAll(genTree.allnodes);
				htmltags.allnodes.addAll(inTree.allnodes);
				if(genTree.isEmpty())
					throw new Error("should not have an empty tree");
				Set<HTMLNode> tailset=genTree.tails;
				Set<HTMLNode> headset=inTree.heads;
				for(HTMLNode hn1:tailset)
					for(HTMLNode hn2:headset)
					{
						if(hn1.IsComplete()&&hn2.IsComplete())
						{
							//System.out.println("here");
							hn1.succesors.add(hn2);
							hn2.predecessor.add(hn1);
							//newtree.allnodes.add(hcup);
						}
						else if(hn1.IsUpperIncomplete()&&hn2.IsComplete())
						{
							hn1.succesors.add(hn2);
							hn2.predecessor.add(hn1);
						}
						else if(hn1.IsComplete()&&hn2.IsLowerIncomplete())
						{
							hn1.succesors.add(hn2);
							hn2.predecessor.add(hn1);
						}
						else if(hn1.IsLowerIncomplete()&&hn2.IsUpperIncomplete())// connect incompleted node
						{
							removable.add(hn1);
							removable.add(hn2);
							HTMLNode nn=new HTMLNode();
							nn.sigs.addAll(hn1.sigs);
							nn.sigs.addAll(hn2.sigs);
							nn.Value=hn1.Value.concatenate(hn2.Value);
							nn.setStatus();
							nn.succesors=hn2.succesors;
							nn.predecessor=hn1.predecessor;
							for(HTMLNode p:nn.predecessor)
							{
								p.succesors.add(nn);
								//p.succesors.remove(hn1);
							}
							for(HTMLNode s:nn.succesors)
							{
								s.predecessor.add(nn);
								//s.predecessor.remove(hcup);
							}
							htmltags.allnodes.add(nn);
						}
						else
							throw new Error("unmatced "+hn1.Value.getFiniteStrings()+" "+hn2.Value.getFiniteStrings());
					}				
			}
		}
		for(HTMLNode r: htmltags.allnodes)
		{
			r.predecessor.removeAll(removable);
			r.succesors.removeAll(removable);
		}
		htmltags.allnodes.removeAll(removable);
		htmltags.heads.removeAll(removable);
		htmltags.tails.removeAll(removable);
		

		
	}*/
}
