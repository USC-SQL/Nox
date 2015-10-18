package sql.usc.SemiTagGraph;


public class SemiTagEdge {
String methodsig=null;
SemiTagNode target=null;
SemiTagNode source=null;
boolean intercall;
int position=0; //0: inside a method. 1, entrance of a method 2. exit of a method
public SemiTagEdge(SemiTagNode from, SemiTagNode to,String sig)
{
	methodsig=sig;
	target=to;
	source=from;
}
public SemiTagNode getSource()
{
	return source;
}
public SemiTagNode getTarget()
{
	return target;
}
public boolean equals(Object o)
{
	if(!(o instanceof SemiTagEdge))
		return false;
	SemiTagEdge ine=(SemiTagEdge)o;
	if(!this.source.equals(ine.source))
		return false;
	if(!this.target.equals(ine.target))
		return false;
	if(!this.methodsig.equals(ine.methodsig))
		return false;
	return true;
}
public int hashCode() {
    int hash = methodsig.hashCode()+10*target.hashCode()+20*source.hashCode();
    
    return hash;
}
}
