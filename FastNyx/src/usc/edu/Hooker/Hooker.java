package usc.edu.Hooker;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sql.usc.Color.Color;

public class Hooker {
	public static String replace(String s)
	{	
		//change background color

		Hashtable<Color, Color> bgtable=new Hashtable<Color, Color>();
		Hashtable<Color, Color> texttable=new Hashtable<Color, Color>();

		bgtable.put(new Color("rgb(255,204,102)"), new Color("rgb(36,64,120)"));
		bgtable.put(new Color("rgb(255,255,238)"), new Color("rgb(64,68,84)"));
		bgtable.put(new Color("rgb(153,0,0)"), new Color("rgb(163,163,0)"));
		bgtable.put(new Color("rgb(255,255,255)"), new Color("rgb(0,0,0)"));
		texttable.put(new Color("rgb(255,255,255)"), new Color("rgb(128,0,32)"));
		texttable.put(new Color("rgb(0,0,255)"), new Color("rgb(96,192,208)"));
		texttable.put(new Color("rgb(0,0,0)"), new Color("rgb(255,255,0)"));
		texttable.put(new Color("blue"), new Color("rgb(255,0,255)"));

		//Pattern pattern1 = Pattern.compile(".*background-color\\s*:\\s*([\\#A-Za-z0-9\\(\\),]+).*");
		Pattern pattern1 = Pattern.compile(".*background-color\\s*:\\s*([\\#A-Za-z0-9\\(\\),]+).*");
		Pattern pattern2 = Pattern.compile(".*bgcolor\\s*=[^\\#]*([\\#A-Za-z0-9\\(\\),]+)\\W*.*");
		String p=s.replaceAll("\\s", " ");
		String r=s;


		//System.out.println(r);
		Matcher m1=pattern1.matcher(p);
		Matcher m2=pattern2.matcher(p);
		if(m1.matches())
		{
			//System.out.println(s);
			// throw new Error(s);

			String colordec=m1.group(1);
			Color c=new Color(colordec);
			Color nc=bgtable.get(c);
			if(nc==null)
				throw new Error(c.toString() + p);
			 r=r.replaceAll(colordec, nc.toString());
			 
			//return r;
			
		}
		else if(m2.matches())
		{
			String colordec=m2.group(1);
			Color c=new Color(colordec);
			Color nc=bgtable.get(c);
			if(nc==null)
				throw new Error(c.toString());
			 r=r.replaceAll(colordec, nc.toString());

			//return r;
		}
		//change text color
		Pattern pattern3 = Pattern.compile(".*[^bg-]color\\s*:\\s*([\\#A-Za-z0-9\\(\\),]+).*");
		//Pattern pattern4 = Pattern.compile(".*[^bg-]color\\s*=\\s*\\\"\\s*([\\#A-Za-z0-9\\(\\),]+)\\s*\\\".*");
		Pattern pattern4 = Pattern.compile(".*[^bg-]color\\s*=[^\\#]*([\\#A-Za-z0-9\\(\\),]+)\\W*.*");

		Matcher m3=pattern3.matcher(p);
		Matcher m4=pattern4.matcher(p);
		if(m3.matches())
		{
			//System.out.println(s);
			String colordec=m3.group(1);
			Color c=new Color(colordec);
			Color nc=texttable.get(c);
			if(nc==null)
				throw new Error(c.toString());
			 r=r.replaceAll(colordec,nc.toString());
		}
		else if(m4.matches())
		{
			//System.out.println(s);
			String colordec=m4.group(1);
			Color c=new Color(colordec);
			Color nc=texttable.get(c);
			if(nc==null)
				throw new Error(c.toString());
			 r=r.replaceAll(colordec,nc.toString());
			//	throw new Error(s);

		}

		//add default color
		//for body background
		Pattern pattern5 = Pattern.compile(".*<body.*");
		Matcher m5=pattern5.matcher(p);
		Pattern pattern7 = Pattern.compile(".*link\\s*=[^\\#]*([\\#A-Za-z0-9\\(\\),]+)\\W*.*");
		Matcher m7=pattern7.matcher(p);

		if(m5.matches()&&!m1.matches()&&!m2.matches())
		{
			Color defb=bgtable.get(new Color("WHITE"));
			r=r.replaceAll("<body", "<body bgcolor=\""+defb.toString()+"\" ");
		}
		if(m5.matches()&&!m3.matches()&&!m4.matches())
		{
			Color defc=texttable.get(new Color("BLACK"));
			r=r.replaceAll("<body", "<body text=\""+defc.toString()+"\" ");
		}
		if(m5.matches()&&!m7.matches())
		{
			Color defc=texttable.get(new Color("BLUE"));
			r=r.replaceAll("<body", "<body link=\""+defc.toString()+"\" ");
		}
		return r;
	}
}
