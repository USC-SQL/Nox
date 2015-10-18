package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import org.apache.bcel.classfile.ClassFormatException;

import CSSdatabase.CSSdataBase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import soot.Scene;
import sql.usc.Color.Color;
import sql.usc.ColorConflictGraph.ColorConfictGraph;
import sql.usc.ColorConflictGraph.TexColorMap;
import sql.usc.ColorTansformScheme.BackgroundColorScheme;
import sql.usc.ColorTansformScheme.ColorTansformScheme;
import sql.usc.ColorTansformScheme.TextColorScheme;
import sql.usc.HTMLContentGraph.*;
import sql.usc.MIF.Image;
import sql.usc.MIF.MIF;
import sql.usc.OutputGraph.OutputGraph;
import sql.usc.SemiTagGraph.SemiTagGraph;
import wam.configuration.exceptions.WAMConfigurationException;

public class Outputtest {

	public static void main(String argv[]) throws ClassFormatException, WAMConfigurationException, IOException
	{
		MIF mif=new MIF(argv[0]);
		File tempf = new File(mif.modifyscriptpath);
		String modifyscriptfodler=tempf.getParent();
		PrintWriter pw=new PrintWriter(mif.modifyscriptpath);
		pw.println("#! /bin/bash");
		if(!mif.usetemplate)
		{
			long start=System.currentTimeMillis();
			OutputGraph opg=new OutputGraph(mif);
			PrintWriter pw2=new PrintWriter("HOG.html");

			pw2.println(opg.dumpToHtml());
			pw2.close();

			SemiTagGraph hg=new SemiTagGraph(opg);
			CSSdataBase cssdb=null;
			if(mif.csspath.equals("None"))
			{
				 cssdb=null;
			}
			else{
				cssdb=new CSSdataBase(mif.csspath);
			}
			//CSSdataBase cssdb=null;
			HTMLContentGraph htmlg=new HTMLContentGraph(hg,cssdb);
			//pw.close();

			ColorConfictGraph CCG=new ColorConfictGraph(htmlg);
			pw2=new PrintWriter("BCCG.dot");

			pw2.println(CCG.toDot());
			pw2.close();
			TexColorMap TCM=new TexColorMap(htmlg);

			ColorTansformScheme CTS =new ColorTansformScheme(CCG, TCM,mif.bgcolor);
			CTS.GenerateJavaFromTemplate("./Hooker_template.java",mif.hookerpath+"usc/edu/Hooker/Hooker.java");


			pw.println("rm -r hookerlib");
			pw.println("cp -r "+mif.hookerpath+" ./hookerlib");
			pw.println("cd hookerlib");
			pw.println("javac usc/edu/Hooker/Hooker.java");
			pw.println("cd ..");
			pw.println("java -jar NyxModifier.jar "+mif.classpath);
			pw.println("cp -r hookerlib/* GeneratedClasses/");
		}
		else{
			File f=new File(mif.templatepath);
			OutputGraph opg=new OutputGraph(f);
			//pw.println(opg.toDot());
			long time1=System.currentTimeMillis();
			SemiTagGraph hg=new SemiTagGraph(opg);
			//pw.println(hg.toDot());
				CSSdataBase cssdb;
				if(mif.csspath.equals("None"))
				{
					cssdb=null;
				}
				else{
					cssdb=new CSSdataBase(mif.csspath);
				}
			//CSSdataBase cssdb=null;
			HTMLContentGraph htmlg=new HTMLContentGraph(hg,cssdb);
			//pw.println(htmlg.toDot());
			//pw.close();

			ColorConfictGraph CCG=new ColorConfictGraph(htmlg);
			//pw.println(CCG.toDot());
			TexColorMap TCM=new TexColorMap(htmlg);
			//pw.println(TCM.toDot());

			long time2=System.currentTimeMillis();
			ColorTansformScheme CTS =new ColorTansformScheme(CCG, TCM,mif.bgcolor);
			long time3=System.currentTimeMillis();
			PrintWriter pw2=new PrintWriter(modifyscriptfodler+"/replace.pl");
			pw2.print(CTS.GeneratePerl(mif.csspath));
			pw2.close();
			pw.println("chmod +x replace.pl");
			pw.println("./replace.pl>"+new File(mif.csspath).getName());

			//System.out.println(CTS.GeneratePerl(mif.csspath));
			//CTS.Display();
		}




		//CTS.Display();
		int i;

		for(Image img:mif.images)
		{
			String tmp[]=img.path.split("/");
			String filename=tmp[tmp.length-1];
			System.out.println(filename);
			pw.println("cp "+img.path+" ./"+filename);

			ColorConfictGraph CCGimg=new ColorConfictGraph(new Color("#ffffff"),new HashSet<Color>());
			BackgroundColorScheme bgsheme=new BackgroundColorScheme(CCGimg,new Color("#ffffff"));
			TexColorMap TCMimg=new TexColorMap(mif.bgcolor,img.colorpalette);

			TextColorScheme textsheme=new TextColorScheme(TCMimg,bgsheme);

			//BackgroundColorScheme bgsheme=new BackgroundColorScheme(CCG);
			pw.print(textsheme.GetImageScript(filename));
			//textsheme.DisplayTransform(mif);
		}
		pw.close();


	}
}
