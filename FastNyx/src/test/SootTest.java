package test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;


import soot.*;
import soot.jimple.*;
import soot.options.Options;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
public class SootTest {
	public static void main(String argv[]) throws ClassFormatException, IOException
	{
		/*Options.v().set_keep_line_number(true);
		Options.v().set_keep_offset(true);
		Scene.v().setSootClassPath("/home/dingli/Dropbox/work/labproject/StringResolver/target/classes/:/usr/lib/jvm/java-7-openjdk-i386/jre/lib/rt.jar");
		Iterator<SootClass> classes=Scene.v().getClasses().iterator();
		while(classes.hasNext())
			System.out.println(classes.next());
		SootClass sc=Scene.v().loadClassAndSupport("test.testapp");
		SootMethod methods=sc.getMethodByName("printweb");
		UnitGraph cfg=new BriefUnitGraph(methods.retrieveActiveBody());
		Iterator<Unit> ir=cfg.iterator();

		System.out.println(methods.retrieveActiveBody());*/
		//System.out.println(File.pathSeparator);

		  // Directory path here
		  //String path = "/home/dingli/Dropbox/work/labproject/webtestapp/bin"; 
		String path = "/home/dingli/Dropbox/work/wam_test/Applications/bookstore/deploy/baseline/WEB-INF/classes"; 
		  File folder = new File(path);
		 
		  Collection<File> files = FileUtils.listFiles(folder,new RegexFileFilter(".*\\.class"), DirectoryFileFilter.DIRECTORY);
		  Iterator<File> iterf=files.iterator();
		  while(iterf.hasNext())
		  {
			 JavaClass jc= new ClassParser(iterf.next().getAbsolutePath()).parse();
			  System.out.println(jc.getClassName());
		  }
	}
}
