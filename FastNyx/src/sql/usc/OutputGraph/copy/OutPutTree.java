package sql.usc.OutputGraph.copy;

import java.util.HashSet;
import java.util.Set;

public class OutPutTree {
	Set<OutputNode> heads=new HashSet<OutputNode>();
	Set<OutputNode> allnodes=new HashSet<OutputNode>();
	Set<OutputNode> tails=new HashSet<OutputNode>();
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

			}
		}
		return b.append("}\n").toString();
	}
}
