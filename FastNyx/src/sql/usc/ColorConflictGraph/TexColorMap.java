package sql.usc.ColorConflictGraph;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import sql.usc.Color.Color;
import sql.usc.HTMLContentGraph.*;


public class TexColorMap {
Set<CCGNode> textcolors=new HashSet<CCGNode>();
Set<CCGNode> bgcolors=new HashSet<CCGNode>();
Set<ConflictPair> conflicts=new HashSet<ConflictPair>();
public TexColorMap(Color background, Set<Color> foreground)
{
	CCGNode bgcolor=new CCGNode(background,0);
	bgcolors.add(bgcolor);
	int id=0;
	for(Color c:foreground)
	{
		CCGNode textc=new CCGNode(c,id);
		id++;
		textcolors.add(textc);
		conflicts.add(new ConflictPair(bgcolor,textc));
	}
}
public Set<CCGNode> GetTextColor()
{
	return textcolors;
}
public Set<CCGNode> GetBGColor()
{
	return bgcolors;
}
public Set<ConflictPair> ConflictPairs()
{
	return conflicts;
}
public Set<CCGNode> QueryAllConfBGColor(CCGNode text)
{
	Set<CCGNode> r=new HashSet<CCGNode>();
	for(ConflictPair confp:conflicts)
	{
		if(confp.text.equals(text))
			r.add(confp.background);
	}
	return r;
}
public String toDot()
{
	StringBuilder b = new StringBuilder("digraph TextColorMap {\n");
	b.append("  rankdir = LR;\n");
	for(CCGNode node:textcolors)
	{
		String tagname="\""+node.getID()+";"+node.color.toString()+"\"";
		b.append("  ").append(tagname);
		b.append("[shape=circle];\n");
		int thisid=node.getID();
		//b.append("  ").append(current.Value.getFiniteStrings());
	}
	for(CCGNode node:bgcolors)
	{
		String tagname="\""+node.getID()+";"+node.color.toString()+"\"";
		b.append("  ").append(tagname);
		b.append("[shape=box];\n");
		int thisid=node.getID();
		//b.append("  ").append(current.Value.getFiniteStrings());
	}
	for (ConflictPair conf:conflicts) {
		b.append("  ");
		String tagname="\""+conf.text.getID()+";"+conf.text.color.toString()+"\"";

		//System.out.println(tagname);
		String nextsig="\""+conf.background.getID()+";"+conf.background.color.toString()+"\"";
		b.append(tagname+" -> "+nextsig+"\n");
	}
	return b.append("}\n").toString();
}
public TexColorMap(HTMLContentGraph HTMLcg)
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
				id++;
				ccgnode.AddSigs(contentnode.getSig());
				nodetable.put(c, ccgnode);
				bgcolors.add(ccgnode);
			}
		}
	}
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
	Hashtable<Color, CCGNode> textnodetable=new Hashtable<Color, CCGNode>();
	id=bgcolors.size()+1;
	for(HTMLContentNode contentnode:allcontentnode)
	{	
		Set<CCGNode> BGset=OUTtable.get(contentnode);
   
		for(Color c: contentnode.getTextColors())
		{
			CCGNode ctext;
			if(textnodetable.containsKey(c))
			{
				ctext=textnodetable.get(c);
				ctext.AddSigs(contentnode.getSig());
				textnodetable.put(c, ctext);
			}
			else
			{
				ctext=new CCGNode(c,id);
				id++;
				ctext.AddSigs(contentnode.getSig());
				textnodetable.put(c, ctext);
				textcolors.add(ctext);
			}
			for(CCGNode bg:BGset)
			{
				ConflictPair conf=new ConflictPair(bg,ctext);
				conflicts.add(conf);
			}
		}
		for(Color c: contentnode.getLinkColors())
		{
			CCGNode ctext;
			if(textnodetable.containsKey(c))
			{
				ctext=textnodetable.get(c);
				ctext.AddSigs(contentnode.getSig());
				textnodetable.put(c, ctext);
			}
			else
			{
				ctext=new CCGNode(c,id);
				id++;
				ctext.AddSigs(contentnode.getSig());
				textnodetable.put(c, ctext);
				textcolors.add(ctext);
			}
			for(CCGNode bg:BGset)
			{
				ConflictPair conf=new ConflictPair(bg,ctext);
				conflicts.add(conf);
			}
		}
	}

}
}
