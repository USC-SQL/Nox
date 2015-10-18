package CSSdatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import Debug.Debuglog;

import com.steadystate.css.parser.CSSOMParser;

import sql.usc.Color.Color;

public class CSSdataBase {
	CSSStyleSheet stylesheet;
	public CSSdataBase(String path)
	{
         boolean rtn = false;
         try
         {

                // cssfile accessed as a resource, so must be in the pkg (in src dir).
        	 	File f=new File(path);
                InputStream stream = new FileInputStream(f);  



                InputSource source = new InputSource(new InputStreamReader(stream));
                CSSOMParser parser = new CSSOMParser();
                // parse and create a stylesheet composition
                stylesheet = parser.parseStyleSheet(source, null, null);

                //ANY ERRORS IN THE DOM WILL BE SENT TO STDERR HERE!!
                // now iterate through the dom and inspect.

                CSSRuleList ruleList = stylesheet.getCssRules();



               if (stream != null) stream.close();
            }
            catch (IOException ioe)
            {
                System.err.println ("IO Error: " + ioe);
            }
            catch (Exception e)
            {
                e.printStackTrace();

            }
            finally
            {
            }


	}
	public boolean Matches(CSSStyleRule r,String tagname, String id, String classname)
	{
		String text=r.getSelectorText();
	/*	String t, c, n;
		if(tagname==null)
			t="*";
		else
			t=tagname;

		if(classname!=null)
			t+="\\."+classname;
		if(id!=null)
			t+="#"+id;
		String pattern=t;

		Debuglog.log(pattern);
		Pattern pattern1 = Pattern.compile(".* "+pattern+"\\W*.*");
		Matcher m1=pattern1.matcher(text);
		return m1.matches();*/

		return TagMatches(text,tagname) || ClassMatches(text,classname) || IdMatches(text,id);
	}
    public boolean TagMatches(String ff, String tagname)
    {
   	 
		Pattern pattern1 = Pattern.compile(".* "+tagname+"\\W*.*");
		Pattern pattern2 = Pattern.compile(tagname+"\\W*.*");

		Matcher m1=pattern1.matcher(ff);
		Matcher m2=pattern2.matcher(ff);

		return m1.matches() || m2.matches();

		

    }
    public boolean ClassMatches(String ff, String tagname)
    {
   	 
		Pattern pattern1 = Pattern.compile(".*\\."+tagname+"\\W*.*");
		Matcher m1=pattern1.matcher(ff);

		return m1.matches();

		

    }
    public boolean IdMatches(String ff, String tagname)
    {
		Pattern pattern1 = Pattern.compile(".*\\#"+tagname+"\\W*.*");
		Matcher m1=pattern1.matcher(ff);

		return m1.matches();

		

    }
	public Color QueryBgColor(String tagname, String id, String classname)
	{
        CSSRuleList ruleList = stylesheet.getCssRules();

        for (int i = 0; i < ruleList.getLength(); i++) 
        {
          CSSRule rule = ruleList.item(i);
          if (rule instanceof CSSStyleRule) 
          {
              CSSStyleRule styleRule=(CSSStyleRule)rule;

              if(Matches(styleRule,tagname,id,classname))
              {
            	  //Debuglog.log("matches");
                  CSSStyleDeclaration styleDeclaration = styleRule.getStyle();
            	  for (int j = 0; j < styleDeclaration.getLength(); j++) 
            	  {
            		  String property = styleDeclaration.item(j);
            		  property=property.trim();
            		  if(property.equals("background"))
            		  {
            			  String v=styleDeclaration.getPropertyCSSValue(property).getCssText();
            			  if(Color.isColor(v))
            				  return new Color(v);
            		  }
            		  else if(property.equals("background-color"))
            		  {
            			  String v=styleDeclaration.getPropertyCSSValue(property).getCssText();
            			  if(Color.isColor(v))
            				  return new Color(v);
            		  }
            	  }
              }
           }// end of StyleRule instance test
         } // end of ruleList loop
		return null;
	}
	public Color QueryTextColor(String tagname, String id, String classname)
	{
	/*	if(tagname.equals("body"))
			return new Color("BLACK");
		if(tagname.equals("h1") || tagname.equals("h2"))
			return new Color("DARKGRAY");
		if(tagname.equals("h3"))
			return new Color("DARKGREEN");*/
        CSSRuleList ruleList = stylesheet.getCssRules();

        for (int i = 0; i < ruleList.getLength(); i++) 
        {
          CSSRule rule = ruleList.item(i);
          if (rule instanceof CSSStyleRule) 
          {
              CSSStyleRule styleRule=(CSSStyleRule)rule;
              if(Matches(styleRule,tagname,id,classname))
              {
                  CSSStyleDeclaration styleDeclaration = styleRule.getStyle();
            	  for (int j = 0; j < styleDeclaration.getLength(); j++) 
            	  {
            		  String property = styleDeclaration.item(j);
            		  property=property.trim();
            		  if(property.equals("color"))
            		  {
            			  String v=styleDeclaration.getPropertyCSSValue(property).getCssText();
            			  if(Color.isColor(v))
            				  return new Color(v);
            		  }
            	  }
              }
           }// end of StyleRule instance test
         } // end of ruleList loop

		return null;
	}
	public Color QueryLinkColor(String tagname, String id, String classname)
	{
		
		return new Color("#660033");
	}
}
