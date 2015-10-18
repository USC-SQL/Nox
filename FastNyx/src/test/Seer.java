package test;

import java.util.Iterator;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.options.Options;
import soot.tagkit.BytecodeOffsetTag;
import sql.usc.StringResolver.StringResolver;


public class Seer {
	public static void main(String argv[])
	{
		StringResolver sr=new StringResolver();
		Options.v().set_whole_program(true);
		Options.v().set_verbose(false);
		Options.v().set_keep_line_number(true);
		Options.v().set_keep_offset(true);
		sr.addClassPath("/home/dingli/bugzero/web/WEB-INF/classes");

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
		
		sr.addClassPath("/usr/lib/jvm/java-6-openjdk-i386/jre/lib/rt.jar");
		sr.addClassPath("/usr/lib/jvm/java-6-openjdk-i386/jre/lib/jce.jar");
		sr.addClassPath("/usr/lib/jvm/java-6-openjdk-amd64/jre/lib/rt.jar");
		sr.addClassPath("/usr/lib/jvm/java-6-openjdk-amd64/jre/lib/jce.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/jasper.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/jasper-el.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/annotations-api.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/commons-dbcp.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/jsp-api.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/tomcat-i18n-es.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/catalina-ant.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/commons-pool.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/servlet-api.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/tomcat-i18n-fr.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/catalina-ha.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/el-api.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/tomcat-api.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/tomcat-i18n-ja.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/catalina.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/jasper-el.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/tomcat-coyote.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/tomcat-util.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/catalina-tribes.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/jasper.jar");
		sr.addClassPath("/usr/share/tomcat6/lib/tomcat-dbcp.jar");
		sr.addClassPath("/usr/lib/jvm/java-7-openjdk-i386/jre/lib/rt.jar");
		
		sr.addClassPath("/home/dingli/bugzero/web/WEB-INF/lib/bugzero.jar");
		sr.addClassPath("/home/dingli/bugzero/web/WEB-INF/lib/mail.jar");

		sr.addClassPath("/home/dingli/bugzero/web/WEB-INF/lib/activation.jar");

		sr.addClassPath("/home/dingli/bugzero/web/WEB-INF/lib/lizongbo.jar");
		sr.addClassPath("/home/dingli/bugzero/web/WEB-INF/lib/lizongbo.jar");
		SootClass sc=Scene.v().loadClassAndSupport("org.apache.jsp.jsp.login_jsp");
		Scene.v().loadNecessaryClasses();
		SootMethod methods=sc.getMethodByName("_jspService");
		System.out.println(methods.retrieveActiveBody());

		Iterator<Unit> iu=methods.retrieveActiveBody().getUnits().iterator();
		while(iu.hasNext())
		{
			Unit u=iu.next();
			BytecodeOffsetTag jtag=(BytecodeOffsetTag)u.getTag("BytecodeOffsetTag");
			int offset;
			if(jtag==null)
				offset=-1;
			else
				offset=jtag.getBytecodeOffset();
			System.out.println(offset+" "+u);
		}

	}
}
