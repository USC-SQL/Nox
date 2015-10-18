package sql.usc.HTMLContentGraph;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import CSSdatabase.CSSdataBase;
import Debug.Debuglog;


import soot.util.Debug;
import sql.usc.Color.Color;
import sql.usc.SemiTagGraph.SemiTagNode;
import dk.brics.automaton.Automaton;

public class TagInforAbstractor {
	CSSdataBase css;
	public TagInforAbstractor(CSSdataBase cssdata)
	{
		this.css=cssdata;
	}
	public static String getTagName(SemiTagNode n)
	{
		//Set<String> asa=["dasaad"];
		HTMLTagTable tags=new HTMLTagTable();
		if(TagTester.IsOpenCommentTag(n) || TagTester.IsEndCommentTag(n))
			return "!--";
		Automaton a=n.getValue();
		Set<String> strings=a.getFiniteStrings();
		if(strings==null)
			throw new Error("n has loop, please remove it first");
		String prefix=n.getValue().getCommonPrefix();
		Pattern pattern = Pattern.compile("^<([a-zA-Z0-9!\\-]+).*");
		 Matcher m = pattern.matcher(prefix);
		 if(!m.matches())
			 throw new Error("ilegal tag" + prefix);
		 String tagname=m.group(1);
		 tagname=tagname.toLowerCase();
		if(!tags.IsLegal(tagname))
			 throw new Error("ilegal tag name:"+tagname);

		
		return tagname;
	}
	public  String getId(SemiTagNode n)
	{
		String s=n.Value.getCommonPrefix();

		Pattern pattern1 = Pattern.compile(".*id\\s*=\\s*\"(\\w+)\".*");
		Matcher m1=pattern1.matcher(s);
		if(m1.matches())
		{
			//System.out.println(s);
			String classname=m1.group(1);
			return classname.trim();
			
		}
		return null;
	}
	public  String getClassname(SemiTagNode n)
	{
		String s=n.Value.getCommonPrefix();

		Pattern pattern1 = Pattern.compile(".*class\\s*=\\s*\"(\\w+)\".*");
		Matcher m1=pattern1.matcher(s);
		if(m1.matches())
		{
			//System.out.println(s);
			String classname=m1.group(1);
			return classname.trim();
			
		}
		return null;

	}
	public  Set<Color> getBackgroundColors(SemiTagNode n)
	{
		//Set<String> asa=["dasaad"];
		Set<Color> r=new HashSet<Color>();
		Set<String> all_strings=n.Value.getFiniteStrings();
		if(all_strings==null)
			throw new Error("has loop");
		Pattern pattern1 = Pattern.compile(".*background-color\\s*:\\s*([\\#A-Za-z0-9\\(\\),]+).*");
		Pattern pattern2 = Pattern.compile(".*bgcolor\\s*=[^\\#]*([\\#A-Za-z0-9\\(\\),]+)\\W*.*");
		for(String s:all_strings)
		{
			s=s.toLowerCase();
			Matcher m1=pattern1.matcher(s);
			Matcher m2=pattern2.matcher(s);
			if(m1.matches())
			{
				//System.out.println(s);
				String colordec=m1.group(1);
				r.add(new Color(colordec));
				
			}
			else if(m2.matches())
			{
				String colordec=m2.group(1);
				Debuglog.log("flower:"+colordec);
				r.add(new Color(colordec));
				Debuglog.log(new Color(colordec));

			}
		}
		if(css!=null)
		{
			CSSdataBase db=css;


			Color CSScolor=db.QueryBgColor(getTagName(n), getId(n), getClassname(n));
			if(getClassname(n)!=null && getClassname(n).equals("functnbar2"))
			{
				Debuglog.log("functnbar2"+CSScolor);
			}
			if(CSScolor!=null)
				r.add(CSScolor);
		}
		if(getTagName(n).equals("body") && r.isEmpty())
		{
			r.add(new Color("WHITE"));
		}
		return r;
	}
	public  Set<Color> getTextColors(SemiTagNode n)
	{
		//Set<String> asa=["dasaad"];
		Set<Color> r=new HashSet<Color>();
		Set<String> all_strings=n.Value.getFiniteStrings();
		if(all_strings==null)
			throw new Error("has loop");
		Pattern pattern0 = Pattern.compile(".*[^bg-]color\\s*:\\s*([\\#A-Za-z0-9\\(\\),]+).*");
		Pattern pattern1 = Pattern.compile(".*[^bg-]color\\s*=[^\\#]*([\\#A-Za-z0-9\\(\\),]+)\\W*.*");
		Pattern pattern2 = Pattern.compile(".*[^bg-]color\\s*=([\\#A-Za-z0-9\\(\\),]+).*");

		//Debuglog.log(getTagName(n));

		for(String s:all_strings)
		{
			s=s.toLowerCase();

			Matcher m0=pattern0.matcher(s);
			Matcher m1=pattern1.matcher(s);
			Matcher m2=pattern2.matcher(s);

			if(m0.matches())
			{
				//System.out.println(s);
				String colordec=m0.group(1);
				r.add(new Color(colordec));
				
			}
			else if(m1.matches())
			{
				
				String colordec=m1.group(1);
				r.add(new Color(colordec));
			}
			else if(m2.matches())
			{
				
				String colordec=m2.group(1);
				r.add(new Color(colordec));
			}

		}
		
		if(css!=null)
		{
		CSSdataBase db=css;
		Color CSScolor=db.QueryTextColor(getTagName(n), getId(n), getClassname(n));
		if(CSScolor!=null)
			r.add(CSScolor);
		}
		if(getTagName(n).equals("body") && r.isEmpty())
		{
			r.add(new Color("BLACK"));
		}
		return r;
	}
	public  Set<Color> getLinkColors(SemiTagNode n)
	{
		//Set<String> asa=["dasaad"];
		Set<Color> r=new HashSet<Color>();
		Set<String> all_strings=n.Value.getFiniteStrings();
		if(all_strings==null)
			throw new Error("has loop");

		Pattern pattern0 = Pattern.compile(".*color\\s*:\\s*([\\#A-Za-z0-9\\(\\),]+).*");
		Pattern pattern1 = Pattern.compile(".*link\\s*=[^\\#]*([\\#A-Za-z0-9\\(\\),]+)\\W*.*");


		for(String s:all_strings)
		{
			s=s.toLowerCase();

			Matcher m0=pattern0.matcher(s);
			Matcher m1=pattern1.matcher(s);

			if(m0.matches()&&getTagName(n).equals("a"))
			{
				//System.out.println(s);
				String colordec=m0.group(1);
				r.add(new Color(colordec));
				
			}
			else if(m1.matches())
			{
				
				String colordec=m1.group(1);
				r.add(new Color(colordec));
				throw new Error(s);
			}

		}
		if(css!=null)
		{
			CSSdataBase db=css;
			Color CSScolor=db.QueryLinkColor(getTagName(n), getId(n), getClassname(n));
			if(CSScolor!=null)
				r.add(CSScolor);
		}
		if(getTagName(n).equals("body") && r.isEmpty())
		{
			r.add(new Color("BLUE"));
		}
		return r;
	}

	
}
