package sql.usc.OutputGraph;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class OutPutTree {
	Set<OutputNode> heads=new HashSet<OutputNode>();
	Set<OutputNode> allnodes=new HashSet<OutputNode>();
	Set<OutputNode> tails=new HashSet<OutputNode>();
	public OutPutTree clone()
	{
		Hashtable<OutputNode,OutputNode> copytable=new Hashtable<OutputNode,OutputNode>();
		for(OutputNode n:this.allnodes)
		{
			copytable.put(n, new OutputNode(n));
		}
		OutPutTree r=new OutPutTree();
		for(OutputNode n:this.allnodes)
		{
			if(this.heads.contains(n))
				r.heads.add(copytable.get(n));
			if(this.tails.contains(n))
				r.tails.add(copytable.get(n));
			r.allnodes.add(copytable.get(n));
			for(Edge e:n.outegdes)
			{
				OutputNode to=copytable.get(e.target);
				OutputNode from=copytable.get(e.source);
				if(to==null)
					System.out.println(n.Methodsig+" "+e.target.offset+" "+e.target.Methodsig);
				Edge ne=new Edge(from,to,e.methodsig);
				from.outegdes.add(ne);
				to.inegdes.add(ne);		
			}
		}
		return r;
	}
	public String ToDot()
	{
		StringBuilder b = new StringBuilder("digraph HtmlTree {\n");
		b.append("  rankdir = LR;\n");
		for(OutputNode node:allnodes)
		{
			String tagname="\""+node.getId()+""+node.GetSampleString()+"\"";
			b.append("  ").append(tagname);
			b.append("[shape=box];\n");
			//b.append("  ").append(current.Value.getFiniteStrings());
			for (Edge e : node.outegdes) {
				OutputNode next=e.target;
				b.append("  ");
				//System.out.println(tagname);
				b.append(tagname+" -> \""+next.getId()+""+next.GetSampleString()+"\"\n");
				b.append("[label = \""+e.methodsig+"\"]");


			}
		}
		return b.append("}\n").toString();
	}
}
