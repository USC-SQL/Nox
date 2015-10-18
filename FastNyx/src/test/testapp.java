package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.Random;

import usc.edu.Hooker.Hooker;


class ttest{
	int myv=1;
	static int ff=0;
}
public class testapp {
	static int v=0;
	int v2=1;
	static String vas()
	{
		return "";
	}
	public  void printweb(String mohamoha,int a)
	{

	}
	public static void main(String argv[])
	{

		Pattern pattern1 = Pattern.compile(".*id\\s*=\"(\\w+)\".*");
		Matcher m1=pattern1.matcher("<div id=\"banner\"  ondblclick=\"hideBanner()\" >");
		if(m1.matches())
		{
			//System.out.println(s);
			String classname=m1.group(1);
			System.out.println(classname);
		}
		//Automaton
	}
}
