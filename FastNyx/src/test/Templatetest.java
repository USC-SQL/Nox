package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.bcel.classfile.ClassFormatException;

import CSSdatabase.CSSdataBase;

import sql.usc.Color.Color;
import sql.usc.ColorConflictGraph.ColorConfictGraph;
import sql.usc.ColorConflictGraph.TexColorMap;
import sql.usc.ColorTansformScheme.ColorTansformScheme;
import sql.usc.HTMLContentGraph.HTMLContentGraph;
import sql.usc.OutputGraph.OutputGraph;
import sql.usc.SemiTagGraph.SemiTagGraph;
import wam.configuration.exceptions.WAMConfigurationException;

public class Templatetest {
	public static void main(String argv[]) throws ClassFormatException, WAMConfigurationException, IOException
	{
		//OutputGraph opg=new OutputGraph("/home/dingli/Dropbox/work/labproject/webtestapp/config.color.xml");
		//OutputGraph opg=new OutputGraph("/home/dingli/Dropbox/work/wam_test/Applications/portal/config.color.xml");
		//OutputGraph opg=new OutputGraph("/home/dingli/JavaLibrary/new/config.color.xml");
		//OutputGraph opg=new OutputGraph("/home/dingli/class-room-project-read-only/web/config.color.xml");
		long time0=System.currentTimeMillis();

		File f=new File("/home/dingli/Nyxjournal/whitehtml/onlineexam.html");
		OutputGraph opg=new OutputGraph(f);
		PrintWriter pw=new PrintWriter("/home/dingli/graph1.gv");
		//pw.println(opg.toDot());
		long time1=System.currentTimeMillis();
		System.out.println("Output time:"+(time1-time0));
		SemiTagGraph hg=new SemiTagGraph(opg);
		pw.println(hg.toDot());
		CSSdataBase cssdb=new CSSdataBase("/home/dingli/Nyxjournal/whitehtml/onlineexam_files/login-box.css");
		//CSSdataBase cssdb=null;
		HTMLContentGraph htmlg=new HTMLContentGraph(hg,cssdb);
		pw.println(htmlg.toDot());
		//pw.close();

		ColorConfictGraph CCG=new ColorConfictGraph(htmlg);
		pw.println(CCG.toDot());
		TexColorMap TCM=new TexColorMap(htmlg);
		pw.println(TCM.toDot());
		pw.close();

		long time2=System.currentTimeMillis();
		System.out.println("Structure time:"+(time2-time1));
		ColorTansformScheme CTS =new ColorTansformScheme(CCG, TCM,new Color(255,255,255));
		long time3=System.currentTimeMillis();
		System.out.println("Transformation time:"+(time3-time2));

		CTS.Display();
		//System.out.println(Integer.toHexString(43));
	}
}
