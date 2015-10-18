package sql.usc.HTMLContentGraph;

import java.util.HashSet;
import java.util.Set;

import sql.usc.Color.Color;


import dk.brics.automaton.Automaton;

public class HTMLContentNode {
Set<HTMLContentNode> successors=new HashSet<HTMLContentNode>();
Set<HTMLContentNode>predecessors=new HashSet<HTMLContentNode>();
String tagname;
String sig;
Set<Color> bgcolor=new HashSet<Color>();
Set<Color> textcolor=new HashSet<Color>();
Set<Color> linkcolor=new HashSet<Color>();
boolean define=false;
int id;
static int next_id;
boolean comment=false;
public Set<HTMLContentNode> getPreds()
{
	return predecessors;
}
public Set<HTMLContentNode> getSuccs()
{
	return successors;
}
public String getSig()
{
	return sig;
}
public Set<Color> getBgColors()
{
	return bgcolor;
}
public Set<Color> getTextColors()
{
	return textcolor;
}
public Set<Color> getLinkColors()
{
	return linkcolor;
}
public HTMLContentNode(String tagname, String sig)
{
	this.tagname=tagname;
	this.sig=sig;
	next_id++;

	id=next_id;
}
public String toString()
{
	return tagname;
}
}
