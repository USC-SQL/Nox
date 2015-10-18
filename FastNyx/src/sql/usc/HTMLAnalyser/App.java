package sql.usc.HTMLAnalyser;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.bcel.classfile.ClassFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import sql.usc.StringResolver.MethodSummary;
import sql.usc.StringResolver.StringDatabase;
import sql.usc.StringResolver.StringResolver;
import sql.usc.StringResolver.Utils.ClassReader;
import sql.usc.StringResolver.Utils.Configer;
import wam.configuration.ConfMainMethod;
import wam.configuration.WAMConfiguration;
import wam.configuration.exceptions.WAMConfigurationException;

 
/**
 * Hello world!
 *
 */
public class App 
{
	private static Hashtable<String ,MethodSummary > stringdatabase=new  Hashtable<String ,MethodSummary >();
    public static void main( String[] args ) throws  ClassFormatException, IOException, WAMConfigurationException
    {
		/*StringResolver sr=new StringResolver();

		//sr.addClassPath("/home/dingli/wam_test/Applications/bookstore/build/new/");
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
		//SootClass sc=sr.loadClass("org.apache.jsp.AdminBooks_jsp");
		//SootMethod methods=sc.getMethodByName("Search_Show");
		//System.out.println(methods.retrieveActiveBody());
		//WAMConfiguration conf=WAMConfiguration.load("/home/dingli/Dropbox/work/wam_test/Applications/bookstore/config.color.xml");
		WAMConfiguration conf=WAMConfiguration.load("/home/dingli/Dropbox/work/labproject/webtestapp/config.color.xml");
		//String classpath="/home/dingli/Dropbox/work/labproject/webtestapp/bin";
		//Set<String> classes=ClassReader.readClasses(c);
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
			//if(s.equals("org.apache.jsp.Books_jsp"))
			//System.out.println(s);
				subset.add(s);
		}
		sr.loadAllClasses(subset);
		System.out.println("loading finished");
		List<SootMethod> entries=new LinkedList<SootMethod>();

		for(ConfMainMethod mm:conf.getMainMethods())
		{
			//System.out.println(mm.classname);
			SootClass sc=Scene.v().loadClassAndSupport(mm.classname);
			SootMethod methods=sc.getMethod(mm.subsig);
			entries.add(methods);
		}
		Scene.v().setEntryPoints(entries);
		CHATransformer.v().transform();
		//CallGraph cg=Scene.v().getCallGraph();
		System.out.println("call graph is built");*/

		/*for(SootClass cls:sr.getAllAppClasses())
		{
			System.out.println(cls.getName());
			List<SootMethod> classmethods=cls.getMethods();
			for(int i=0;i<classmethods.size();i++)
			{
				SootMethod m=classmethods.get(i);
				System.out.println(m.getSubSignature());

			}

		}*/
    }
}
