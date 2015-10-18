package sql.usc.OutputGraph;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.tagkit.BytecodeOffsetTag;
import soot.toolkits.graph.UnitGraph;
import sql.usc.StringResolver.Definition;
import sql.usc.StringResolver.MethodSummary;
import sql.usc.StringResolver.StringDatabase;
import sql.usc.StringResolver.Variable;
import sql.usc.StringResolver.Variables.StringVariable;
import dk.brics.automaton.Automaton;

public class OutputSummary {
	public OutPutTree ot=new OutPutTree();
	 Hashtable<Unit,OutputNode> genmap=new Hashtable<Unit,OutputNode>();
	 Hashtable<Unit,OutputNode> insmap=new Hashtable<Unit,OutputNode>();
	 Hashtable<Unit,Set<Unit>> INmap=new Hashtable<Unit,Set<Unit>>();
	 Hashtable<Unit,Set<Unit>> OUTmap=new Hashtable<Unit,Set<Unit>>();
	 Hashtable<Unit,Set<OutputNode>> INnodemap=new Hashtable<Unit,Set<OutputNode>>();
	 Hashtable<Unit,Set<OutputNode>> OUTnodemap=new Hashtable<Unit,Set<OutputNode>>();
	 OutputNode START;
	 OutputNode END;
	MethodSummary ms;
	boolean solved;

	public OutputSummary(SootMethod method, StringDatabase strdb)
	{
		 this.ms=strdb.query(method);
		 START=new OutputNode(Automaton.makeEmptyString(),method,-1);
		 this.solved=false;
		 END=new OutputNode(Automaton.makeEmptyString(),method,Integer.MAX_VALUE);

		 ot.allnodes.add(START);
		 ot.allnodes.add(END);
		 ot.heads.add(START);
		 ot.tails.add(END);

		 UnitGraph cfg=ms.getCFG();
		 Iterator<Unit> ir=cfg.iterator();
		 Set<Unit> entries=new HashSet<Unit>();
		 entries.addAll(cfg.getHeads());
		 Set<Unit> exits=new HashSet<Unit>();
		 exits.addAll(cfg.getTails());

		while(ir.hasNext())
		{
			Unit u=ir.next();
			//insmap.put(u, new HTMLTree());
			INmap.put(u, new HashSet<Unit>());
			OUTmap.put(u, new HashSet<Unit>());
			INnodemap.put(u, new HashSet<OutputNode>());
			OUTnodemap.put(u, new HashSet<OutputNode>());
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
				//System.out.println(u);
				Value v=iexp.getArg(0);
				if(v instanceof Constant)
				{
					String s=v.toString();
					if(s.equals("62"))
						s=">";
					a=Automaton.makeString(s.replaceAll("\"", ""));

				}
				else
				{
					Set<Definition> in=ms.QueryInset(u);
					Set<Definition> same=ms.SearchSameValue(in, v);
					a=Automaton.makeEmptyString();
					String logstring="";
					for(Definition def: same)
					{
						Variable value=ms.QueryValue(def);
						a=a.union(value.StringValue());
						logstring+=value.StringValue().toDot();
						//System.out.println(def);
						//System.out.println(a.toDot());
					}

				}
				//a.removeDeadTransitions();
				//a.minimize();
				
				OutputNode opn=new OutputNode(a,method,offset);
				/*if(opn.Methodsig.equals("What_Show") && offset==304)
				{
					StringVariable sv=new StringVariable();
					sv.setName("$r43");
					Definition ndef=new Definition();
					ndef.setoffset(226);
					ndef.v=sv;
					Variable value=ms.QueryValue(ndef);
					throw new Error("in whatshow "+value.StringValue().toDot());
				}*/
				genmap.put(u, opn);
				ot.allnodes.add(opn);

			}
			else if(entries.contains(u) )
			{
				genmap.put(u, START);
			}
			else if(exits.contains(u))
			{
				genmap.put(u, END);
			}

		}
	}
	public void SCCparseing(Metainfor meta)
	{
		//calculate the predecessors and successors of each node
		 UnitGraph cfg=ms.getCFG();
		 Iterator<Unit> ir=cfg.iterator();
		 Set<Unit> entries=new HashSet<Unit>();
		 entries.addAll(cfg.getHeads());
		 Set<Unit> exits=new HashSet<Unit>();
		 exits.addAll(cfg.getTails());
		System.out.println("==================================");
		 sql.usc.StringResolver.Utils.Utils.Log("start parseing "+ms.getSig());
			solved=true;

		boolean flag=true;
		ir=cfg.iterator();
		Hashtable<Unit, OutPutTree> outputtable=new Hashtable<Unit, OutPutTree>();
		//prepare all the summary of other methods
		while(ir.hasNext())
		{
			Unit u=ir.next();
			if(meta.InvokeAppMethod(u))
			{
				for(OutputSummary os:meta.GetTargetSummary(u))
				{
					if(os.solved==false)
					{
						System.out.println(os.ms.getSig());
						this.solved=false;
						continue;
					}
					else
					{
						OutPutTree clonet= os.ot.clone();
						this.ot.allnodes.addAll(clonet.allnodes);
						outputtable.put(u, clonet);
					}
				}
			}
		}
		//calculate the predecessors and successors of each node

		while(flag)
		{
			flag=false;
			ir=cfg.iterator();
			while(ir.hasNext())
			{
				Unit u=ir.next();
				Set<OutputNode> INset=new HashSet<OutputNode>();
				Set<OutputNode> OUTset=new HashSet<OutputNode>();

				List<Unit> pres=cfg.getPredsOf(u);
				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}
				if(PrintDetecter.isPrintln(u) ||entries.contains(u) || exits.contains(u))//create the gen set
				{
					OUTset=new HashSet<OutputNode>();
					if(genmap.get(u)==null)
						throw new Error(u.toString()+" "+ms.getSig());
					OUTset.add(genmap.get(u));
				}
				else if(meta.InvokeAppMethod(u))
				{
					OUTset=new HashSet<OutputNode>();
					for(OutputSummary os:meta.GetTargetSummary(u))
					{
						if(os.solved==false)
							continue;
						OUTset.addAll(outputtable.get(u).tails);
					}
				}
				else
				{
					OUTset=INset;
				}
				Set<OutputNode> oldOUT=OUTnodemap.get(u);
				if(!OUTset.equals(oldOUT))
				{
					flag=true;
					OUTnodemap.put(u, OUTset);
				}
			}
		}
		//connect all nodes;
		ir=cfg.iterator();
		while(ir.hasNext())
		{
			Unit u=ir.next();
			BytecodeOffsetTag jtag=(BytecodeOffsetTag)u.getTag("BytecodeOffsetTag");
			int offset;
			if(jtag==null)
				offset=-1;
			else
				offset=jtag.getBytecodeOffset();
			if(PrintDetecter.isPrintln(u)||entries.contains(u) || exits.contains(u))
			{
				Set<OutputNode> INset=new HashSet<OutputNode>();
				OutputNode current=genmap.get(u);
				List<Unit> pres=cfg.getPredsOf(u);
				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}

				for(OutputNode p:INset)
				{
					//System.out.println(p+" "+current);
					Edge newedge=new Edge(p,current, this.ms.getSig()+";"+offset);
					current.inegdes.add(newedge);
					p.outegdes.add(newedge);
					//p.succesors.add(current);
					
				}
			}
			else if(meta.InvokeAppMethod(u))
			{

				Set<OutputNode> INset=new HashSet<OutputNode>();
				OutputNode current=genmap.get(u);
				List<Unit> pres=cfg.getPredsOf(u);

				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}

				for(OutputSummary os: meta.GetTargetSummary(u))
					for(OutputNode p: INset)
					{
						if(os.solved==false)
							continue;
						OutPutTree copytree=outputtable.get(u);
						for(OutputNode sp:copytree.heads)
						{
							Edge newedge=new Edge(p,sp, this.ms.getSig()+";"+offset);
						
							p.outegdes.add(newedge);
							sp.inegdes.add(newedge);
						}
						//p.succesors.add(os.START);
						//os.START.predecessor.add(p);
					}
				
				//System.out.println(u);
			}

		}
	}
	public void parsing(Metainfor meta)
	{
		//calculate the predecessors and successors of each node
		 UnitGraph cfg=ms.getCFG();
		 Iterator<Unit> ir=cfg.iterator();
		 Set<Unit> entries=new HashSet<Unit>();
		 entries.addAll(cfg.getHeads());
		 Set<Unit> exits=new HashSet<Unit>();
		 exits.addAll(cfg.getTails());
		System.out.println("==================================");
		 sql.usc.StringResolver.Utils.Utils.Log("start parseing "+ms.getSig());
			solved=true;

		boolean flag=true;
		ir=cfg.iterator();
		Hashtable<Unit, OutPutTree> outputtable=new Hashtable<Unit, OutPutTree>();
		//prepare all the summary of other methods
		while(ir.hasNext())
		{
			Unit u=ir.next();
			if(meta.InvokeAppMethod(u))
			{
				for(OutputSummary os:meta.GetTargetSummary(u))
				{
					if(os.solved==false)
					{
						System.out.println(os.ms.getSig());
						this.solved=false;
						continue;
					}
					else
					{
						OutPutTree clonet= os.ot.clone();
						this.ot.allnodes.addAll(clonet.allnodes);
						outputtable.put(u, clonet);
					}
				}
			}
		}
		//calculate the predecessors and successors of each node

		while(flag)
		{
			flag=false;
			ir=cfg.iterator();
			while(ir.hasNext())
			{
				Unit u=ir.next();
				Set<OutputNode> INset=new HashSet<OutputNode>();
				Set<OutputNode> OUTset=new HashSet<OutputNode>();

				List<Unit> pres=cfg.getPredsOf(u);
				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}
				if(PrintDetecter.isPrintln(u) ||entries.contains(u) || exits.contains(u))//create the gen set
				{
					OUTset=new HashSet<OutputNode>();
					if(genmap.get(u)==null)
						throw new Error(u.toString()+" "+ms.getSig());
					OUTset.add(genmap.get(u));
				}
				else if(meta.InvokeAppMethod(u))
				{
					OUTset=new HashSet<OutputNode>();
					for(OutputSummary os:meta.GetTargetSummary(u))
					{
						if(os.solved==false)
							continue;
						OUTset.addAll(outputtable.get(u).tails);
					}
				}
				else
				{
					OUTset=INset;
				}
				Set<OutputNode> oldOUT=OUTnodemap.get(u);
				if(!OUTset.equals(oldOUT))
				{
					flag=true;
					OUTnodemap.put(u, OUTset);
				}
			}
		}
		//connect all nodes;
		ir=cfg.iterator();
		while(ir.hasNext())
		{
			Unit u=ir.next();
			BytecodeOffsetTag jtag=(BytecodeOffsetTag)u.getTag("BytecodeOffsetTag");
			int offset;
			if(jtag==null)
				offset=-1;
			else
				offset=jtag.getBytecodeOffset();
			if(PrintDetecter.isPrintln(u)||entries.contains(u) || exits.contains(u))
			{
				Set<OutputNode> INset=new HashSet<OutputNode>();
				OutputNode current=genmap.get(u);
				List<Unit> pres=cfg.getPredsOf(u);
				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}

				for(OutputNode p:INset)
				{
					//System.out.println(p+" "+current);
					Edge newedge=new Edge(p,current, this.ms.getMethodname()+";"+offset);
					current.inegdes.add(newedge);
					p.outegdes.add(newedge);
					//p.succesors.add(current);
					
				}
			}
			else if(meta.InvokeAppMethod(u))
			{

				Set<OutputNode> INset=new HashSet<OutputNode>();
				OutputNode current=genmap.get(u);
				List<Unit> pres=cfg.getPredsOf(u);

				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}

				for(OutputSummary os: meta.GetTargetSummary(u))
					for(OutputNode p: INset)
					{
						if(os.solved==false)
							continue;
						OutPutTree copytree=outputtable.get(u);
						for(OutputNode sp:copytree.heads)
						{
							Edge newedge=new Edge(p,sp, this.ms.getMethodname()+";"+offset);
						
							p.outegdes.add(newedge);
							sp.inegdes.add(newedge);
						}
						//p.succesors.add(os.START);
						//os.START.predecessor.add(p);
					}
				
				//System.out.println(u);
			}

		}

	}
	public void parsing()
	{
		//calculate the predecessors and successors of each node
		 UnitGraph cfg=ms.getCFG();
		 Iterator<Unit> ir=cfg.iterator();
		 Set<Unit> entries=new HashSet<Unit>();
		 entries.addAll(cfg.getHeads());
		 Set<Unit> exits=new HashSet<Unit>();
		 exits.addAll(cfg.getTails());
		System.out.println("==================================");
		 sql.usc.StringResolver.Utils.Utils.Log("start parseing "+ms.getSig());
		boolean flag=true;
		while(flag)
		{
			flag=false;
			ir=cfg.iterator();
			while(ir.hasNext())
			{
				Unit u=ir.next();
				Set<OutputNode> INset=new HashSet<OutputNode>();
				Set<OutputNode> OUTset=new HashSet<OutputNode>();

				List<Unit> pres=cfg.getPredsOf(u);
				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}
				if(PrintDetecter.isPrintln(u) ||entries.contains(u) || exits.contains(u))//create the gen set
				{
					OUTset=new HashSet<OutputNode>();
					OUTset.add(genmap.get(u));
				}
				else
				{
					OUTset=INset;
				}
				Set<OutputNode> oldOUT=OUTnodemap.get(u);
				if(!OUTset.equals(oldOUT))
				{
					flag=true;
					OUTnodemap.put(u, OUTset);
				}
			}
		}
		//connect all nodes;
		ir=cfg.iterator();
		while(ir.hasNext())
		{
			Unit u=ir.next();
			if(PrintDetecter.isPrintln(u)||entries.contains(u) || exits.contains(u))
			{
				Set<OutputNode> INset=new HashSet<OutputNode>();
				OutputNode current=genmap.get(u);
				List<Unit> pres=cfg.getPredsOf(u);
				for(int i=0;i<pres.size();i++)
				{
					Unit s=pres.get(i);
					INset.addAll(OUTnodemap.get(s));
				}
				//System.out.println(u);

				//System.out.println(INset);
				for(OutputNode p:INset)
				{
					//OutputNode outputp=genmap.get(p);
					Edge newedge=new Edge(p,current, this.ms.getMethodname());
					current.inegdes.add(newedge);
					p.outegdes.add(newedge);
					
				}
			}

		}

	}
}
