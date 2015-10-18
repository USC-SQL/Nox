package sql.usc.SemiTagGraph;

import java.util.Hashtable;
import java.util.Set;

import sql.usc.OutputGraph.Edge;
import sql.usc.OutputGraph.OutputGraph;
import sql.usc.OutputGraph.OutputNode;


public class SemiTagGraph {
	SemiTagTree tagtree=new SemiTagTree();
	OutputGraph og;
	public String toDot()
	{
		return tagtree.ToDot();
	}
	public Set<SemiTagNode> getAllNodes()
	{
		return tagtree.allnodes;
	}
	public Set<SemiTagNode> getHeads()
	{
		return tagtree.heads;
	}
	public SemiTagGraph(OutputGraph o)
	{
		og=o;
		Set<OutputNode> alloutnodes=o.getAllNodes();
		Hashtable<OutputNode,SemiTagTree> OutputToHTMLMap=new Hashtable<OutputNode,SemiTagTree>();
		for(OutputNode n:alloutnodes)
		{
			TagAbstractor abs=new TagAbstractor(n);
			OutputToHTMLMap.put(n, abs.tagtree);
			tagtree.allnodes.addAll(abs.tagtree.allnodes);
			if(o.getHeads().contains(n))
				tagtree.heads.addAll(abs.tagtree.heads);
			if(o.getTails().contains(n))
				tagtree.heads.addAll(abs.tagtree.tails);
		}
		for(OutputNode n:alloutnodes)
		{
			SemiTagTree segment=OutputToHTMLMap.get(n);
			for(Edge e:n.getOutedge())
			{
				OutputNode target=e.getTarget();
				SemiTagTree targetsegment=OutputToHTMLMap.get(target);
				for(SemiTagNode h1:segment.tails)
					for(SemiTagNode h2:targetsegment.heads)
					{
						SemiTagEdge nhe=new SemiTagEdge(h1,h2,e.getMethodSig());
						h1.outegdes.add(nhe);
						h2.inegdes.add(nhe);

					}
				
			}
		}
		tagtree.Merge();
		tagtree.Trim();
		//tagtree.AcyclizeAll();
		
		
	}
}
