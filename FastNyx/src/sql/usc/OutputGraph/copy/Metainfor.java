package sql.usc.OutputGraph.copy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.bcel.classfile.ClassFormatException;


import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import sql.usc.StringResolver.MethodSummary;
import sql.usc.StringResolver.StringDatabase;
import sql.usc.StringResolver.StringResolver;
import sql.usc.StringResolver.Utils.ClassReader;
import sql.usc.StringResolver.Utils.Utils;
import wam.configuration.ConfMainMethod;
import wam.configuration.WAMConfiguration;
import wam.configuration.exceptions.WAMConfigurationException;

public class Metainfor {
	CallGraph cg;
	Hashtable<String, OutputSummary> SummaryTable=new Hashtable<String, OutputSummary>();
	StringDatabase strdb=new StringDatabase();
	public Set<SootMethod> entries=new HashSet<SootMethod>(); 
	public Set<SootMethod> allmethods=new HashSet<SootMethod>(); 

	public Metainfor(String pathtoconfig)  throws WAMConfigurationException, ClassFormatException, IOException
	{
		 StringResolver sr=new StringResolver();
			Options.v().set_whole_program(true);
			Options.v().set_verbose(false);
			Options.v().set_keep_line_number(true);
			Options.v().set_keep_offset(true);
		sr.addClassPath("/usr/lib/jvm/java-7-openjdk-i386/jre/lib/rt.jar");
		sr.addClassPath("/usr/lib/jvm/java-7-openjdk-i386/jre/lib/jce.jar");
		sr.addClassPath("/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar");
		sr.addClassPath("/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/jce.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/jasper.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/jasper-el.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/annotations-api.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/commons-dbcp.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/jsp-api.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/tomcat-i18n-es.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/catalina-ant.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/commons-pool.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/servlet-api.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/tomcat-i18n-fr.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/catalina-ha.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/el-api.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/tomcat-api.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/tomcat-i18n-ja.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/catalina.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/jasper-el.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/tomcat-coyote.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/tomcat-util.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/catalina-tribes.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/jasper.jar");
		sr.addClassPath("/usr/share/tomcat7/lib/tomcat-dbcp.jar");

		WAMConfiguration conf=WAMConfiguration.load(pathtoconfig);
		Set<String> allclasses=new HashSet<String>();
		for(String s:conf.getAllClassPaths())
		{
			sr.addClassPath(s);
			Set<String> classes=ClassReader.readClasses(s);
			allclasses.addAll(classes);
		}
		Set<String> subset=new HashSet<String>();
		for(String s:allclasses)
		{
				subset.add(s);
		}
		sr.loadAllClasses(subset);
		Utils.Log("loading finished");
		List<SootMethod> entrieslist=new LinkedList<SootMethod>();

		for(ConfMainMethod mm:conf.getMainMethods())
		{
			SootClass sc=Scene.v().loadClassAndSupport(mm.classname);
			SootMethod methods=sc.getMethod(mm.subsig);
			entrieslist.add(methods);
			entries.add(methods);
		}
		Scene.v().setEntryPoints(entrieslist);
		CHATransformer.v().transform();
		this.cg=Scene.v().getCallGraph();
		Utils.Log("call graph is built");
		for(SootClass cls:sr.getAllAppClasses())
		{
			List<SootMethod> classmethods=cls.getMethods();
			for(int i=0;i<classmethods.size();i++)
			{
				SootMethod m=classmethods.get(i);
				if(!m.isConcrete())
					continue;
				MethodSummary tmp= sr.doAnalyze(classmethods.get(i));
				this.strdb.put(classmethods.get(i), tmp);
				allmethods.add(classmethods.get(i));
			}
		}
		Utils.Log("Strnig analysis finished");
	}
	Set<OutputSummary> GetTargetSummary(Unit u)
	{
		if(!InvokeAppMethod(u))
		{

			return null;
		}
		Set<OutputSummary> r=new HashSet<OutputSummary>();
		InvokeStmt ismt=(InvokeStmt)u;
		InvokeExpr iexp=ismt.getInvokeExpr();
		String sig=iexp.getMethodRef().toString();
		//System.out.println(u);
		Iterator sources=new Targets(cg.edgesOutOf(u));
		while(sources.hasNext())
		{
			SootMethod target=(SootMethod)sources.next();
			if(SummaryTable.get(target.getSignature())==null)
				continue;
			r.add(SummaryTable.get(target.getSignature()));
		}
		return r;
				
	}
	boolean InvokeAppMethod(Unit u)
	{
		if(u instanceof  InvokeStmt)//model new StringBuilder(String), new String()
		{
			InvokeStmt ismt=(InvokeStmt)u;
			InvokeExpr iexp=ismt.getInvokeExpr();
			String sig=iexp.getMethodRef().toString();
			//System.out.println(u);
			Iterator sources=new Targets(cg.edgesOutOf(u));
			while(sources.hasNext())
			{
				SootMethod target=(SootMethod)sources.next();
				if(target.isConcrete()&&SummaryTable.containsKey(target.getSignature()))
					return true;
			}
			//System.out.println(sig);
			
		}
		return false;
	}
	
	public Queue<SootMethod> sortMethodsRTO() {
		Set<SootMethod> visited=new HashSet<SootMethod>();
		Queue<SootMethod> sortedList = new LinkedList<SootMethod>();
    	Stack<SootMethod> methodstack=new Stack<SootMethod>();
    	Stack<SootMethod> sortstack=new Stack<SootMethod>();

    	for (SootMethod entryPoint:entries)
    	{
    		methodstack.push(entryPoint);
    		visited.add(entryPoint);
    	}
    	while(!methodstack.isEmpty())
    	{
    		SootMethod top=methodstack.pop();
    		sortstack.push(top);
        	Iterator<Edge> it = cg.edgesOutOf(top);
        	List<SootMethod> temp = new ArrayList<SootMethod>();
        	while(it.hasNext()) {
        		Edge e = it.next();
        		SootMethod targetMethod = e.tgt();
        		if ((!visited.contains(targetMethod)) &&  allmethods.contains(targetMethod)) {
        			methodstack.push(targetMethod);
        			visited.add(targetMethod);
        		}
        	}
    	}
    	while(!sortstack.isEmpty())
    	{
    		SootMethod top=sortstack.pop();
    		sortedList.add(top);
    	}
    	
    	return sortedList;
    }

}
