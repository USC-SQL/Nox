package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;



import dk.brics.automaton.Automaton;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import sql.usc.HTMLAnalyser.MethodHTMLSummery;
import sql.usc.OutputGraph.OutputGraph;
import sql.usc.OutputGraph.OutputSummary;
import sql.usc.OutputGraph.PrintDetecter;
import sql.usc.SemiTagGraph.SemiTagGraph;
import sql.usc.StringResolver.Definition;
import sql.usc.StringResolver.MethodSummary;
import sql.usc.StringResolver.StringDatabase;
import sql.usc.StringResolver.StringResolver;
import sql.usc.StringResolver.Variable;
import sql.usc.StringResolver.Variables.StringVariable;

public class AnalysisTest {
	public static  void main(String[] argv) throws FileNotFoundException
	{
		StringResolver sr=new StringResolver();
		//sr.addClassPath("/home/dingli/Dropbox/work/labproject/StringResolver/bin");
		sr.addClassPath("/home/dingli/Dropbox/work/wam_test/Applications/bookstore/deploy/baseline/WEB-INF/classes");
		sr.addClassPath("/usr/lib/jvm/java-7-openjdk-i386/jre/lib/rt.jar");
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
		Set<String> allclassname=new HashSet<String>();

		SootClass sc=sr.loadClass("org.apache.jsp.Default_jsp");
		Options.v().set_whole_program(true);
		SootMethod methods=sc.getMethodByName("Recommended_Show");
		//SootMethod methods=sc.getMethod("void main(java.lang.String[])");

		StringDatabase strdb=new StringDatabase();
		MethodSummary tmp= sr.doAnalyze(methods);
		strdb.put(methods, tmp);
		OutputSummary osum=new OutputSummary(methods,strdb);
		osum.parsing();
		OutputGraph opg=new OutputGraph(osum.ot);
		SemiTagGraph stg=new SemiTagGraph(opg);
		//System.out.println(methods.getSignature());
		PrintWriter pw=new PrintWriter("/home/dingli/graph1.gv");
		pw.println(osum.ot.ToDot());
		pw.println(stg.toDot());

		pw.close();

		//System.out.println(methods.retrieveActiveBody());
		
		


	}

}
