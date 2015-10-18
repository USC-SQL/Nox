package sql.usc.ColorConflictGraph;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import sql.usc.Color.Color;
import sql.usc.HTMLContentGraph.*;


public class ColorConfictGraph {
	final double alpha=3;
	final double beta=1;
	final double gamma=0.5;
	double[][] Matrix;
	Set<CCGNode> allnodes=new HashSet<CCGNode>();
	Hashtable<Integer,CCGNode> idtondoe=new Hashtable<Integer,CCGNode>();

	public ColorConfictGraph(Color background, Set<Color> foreground)
	{
		CCGNode bgnode=new CCGNode(background,0);
		idtondoe.put(bgnode.getID(), bgnode);
		allnodes.add(bgnode);
		Hashtable<Color, CCGNode> nodetable=new Hashtable<Color, CCGNode>();
		int id=1;
		for(Color c:foreground)
		{
				CCGNode ccgnode=new CCGNode(c,id);
				idtondoe.put(ccgnode.getID(), ccgnode);
				id++;
				allnodes.add(ccgnode);
				
		}
		Matrix=new double[allnodes.size()][allnodes.size()];
		for(int i=0;i<allnodes.size();i++)
			for(int j=0;j<allnodes.size();j++)
			{
				if(i==0||j==0)
					Matrix[i][j]=alpha;
				else
					Matrix[i][j]=beta;

			}

	}
	public Set<CCGNode> GetAllNodes()
	{
		return allnodes;
	}
	public double QueryWight(CCGNode a, CCGNode b)
	{
		return Matrix[a.getID()][b.getID()];
	}
	public String toDot()
	{
		StringBuilder b = new StringBuilder("digraph GolorConflictGraph {\n");
		b.append("  rankdir = LR;\n");
		for(CCGNode node:allnodes)
		{
			String tagname="\""+node.getID()+";"+node.color.toString()+"\"";
			b.append("  ").append(tagname);
			b.append("[shape=circle];\n");
			int thisid=node.getID();
			//b.append("  ").append(current.Value.getFiniteStrings());
			for (int i=0;i<allnodes.size();i++) {
				if(i==thisid)
					continue;
				b.append("  ");
				CCGNode nextnode=idtondoe.get(i);
				//System.out.println(tagname);
				String nextsig="\""+nextnode.getID()+";"+nextnode.color.toString()+"\"";
				b.append(tagname+" -> "+nextsig+"\n");
				b.append("[label = \""+Matrix[thisid][i]+"\"]");

				

			}
		}
		return b.append("}\n").toString();
	}
	public ColorConfictGraph(HTMLContentGraph HTMLcg)
	{
		Hashtable<Color, CCGNode> nodetable=new Hashtable<Color, CCGNode>();
		Hashtable<HTMLContentNode, Set<CCGNode>> INtable=new Hashtable<HTMLContentNode, Set<CCGNode>>();
		Hashtable<HTMLContentNode, Set<CCGNode>> OUTtable=new Hashtable<HTMLContentNode, Set<CCGNode>>();
		Set<HTMLContentNode> allcontentnode=HTMLcg.GetAllNodes();
		int id=0;
		for(HTMLContentNode contentnode:allcontentnode)
		{
			INtable.put(contentnode, new HashSet<CCGNode>());
			OUTtable.put(contentnode, new HashSet<CCGNode>());
			
			for(Color c: contentnode.getBgColors())
			{
				if(nodetable.containsKey(c))
				{
					CCGNode ccgnode=nodetable.get(c);
					ccgnode.AddSigs(contentnode.getSig());
					nodetable.put(c, ccgnode);
				}
				else
				{
					CCGNode ccgnode=new CCGNode(c,id);

					idtondoe.put(ccgnode.getID(), ccgnode);
					id++;
					ccgnode.AddSigs(contentnode.getSig());
					nodetable.put(c, ccgnode);
					allnodes.add(ccgnode);
				}
			}
		}
		Matrix =new double[allnodes.size()][allnodes.size()];
		boolean flag=true;
		while(flag)
		{
			flag=false;
			for(HTMLContentNode contentnode:allcontentnode)
			{
				Set<CCGNode> INset=new HashSet<CCGNode>();
				Set<CCGNode> OUTset;
				for(HTMLContentNode p:contentnode.getPreds())
				{
					INset.addAll(OUTtable.get(p));
				}
				Set<CCGNode> GENset=new HashSet<CCGNode>();

				for(Color c: contentnode.getBgColors())
				{
					CCGNode node=nodetable.get(c);
					if(node!=null)
						GENset.add(node);
				}
				if(GENset.isEmpty())
				{
					OUTset=INset;
				}
				else
				{
					OUTset=GENset;
				}
				Set<CCGNode> oldOUT=OUTtable.get(contentnode);
				if(!oldOUT.equals(OUTset))
				{
					flag=true;
					OUTtable.put(contentnode, OUTset);
				}
			}
		}
		System.out.println(allnodes.size());
		for(int i=0;i<allnodes.size();i++)
			for(int j=0;j<allnodes.size();j++)
			{
				Matrix[i][j]=gamma;
			}

		for(HTMLContentNode contentnode:allcontentnode)
		{
			Set<CCGNode> INset=new HashSet<CCGNode>();
			Set<CCGNode> OUTset=OUTtable.get(contentnode);
			for(HTMLContentNode p:contentnode.getPreds())
			{
				INset.addAll(OUTtable.get(p));
			}
			for(HTMLContentNode s1:contentnode.getSuccs())
				for(HTMLContentNode s2:contentnode.getSuccs())
				{
					if(s1==s2)
						continue;
					for(CCGNode suc1:OUTtable.get(s1))
						for(CCGNode suc2:OUTtable.get(s2))
						{
							Matrix[suc1.getID()][suc2.getID()]=beta;
							Matrix[suc2.getID()][suc1.getID()]=beta;
						}
				}
			
		}

		for(HTMLContentNode contentnode:allcontentnode)
		{
			Set<CCGNode> INset=new HashSet<CCGNode>();
			Set<CCGNode> OUTset=OUTtable.get(contentnode);
			for(HTMLContentNode p:contentnode.getPreds())
			{
				INset.addAll(OUTtable.get(p));
			}
			for(CCGNode p:INset)
				for(CCGNode def:OUTset)
				{
					Matrix[p.getID()][def.getID()]=alpha;
					Matrix[def.getID()][p.getID()]=alpha;
				}
		}	
	}
}
