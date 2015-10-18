package sql.usc.OutputGraph;

public class Edge {
String methodsig=null;
OutputNode target=null;
OutputNode source=null;
boolean intercall; 
public Edge(OutputNode from, OutputNode to, String method)
{
	methodsig=method;
	target=to;
	source=from;
}
public OutputNode getTarget()
{
	return target;
}
public String getMethodSig()
{
	return methodsig;
}
public boolean equals(Object o)
{
	if(!(o instanceof Edge))
		return false;
	Edge ine=(Edge)o;
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
