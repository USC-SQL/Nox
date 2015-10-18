package test;

import com.steadystate.css.parser.CSSOMParser;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CSSParserTest 
{

    protected static CSSParserTest oParser;

    public static void main(String[] args) {

            oParser = new CSSParserTest();

            if (oParser.Parse("/home/dingli/scarab-0.21/target/scarab/style/allcss.css")) {

                System.out.println("Parsing completed OK");

            } else {

                System.out.println("Unable to parse CSS");

            }   
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
     public boolean Parse(String cssfile) 
     {

         FileOutputStream out = null; 
         PrintStream ps = null; 
         boolean rtn = false;



         try
         {

                // cssfile accessed as a resource, so must be in the pkg (in src dir).
        	 	File f=new File(cssfile);
                InputStream stream = new FileInputStream(f);  

                 // overwrites and existing file contents
                 out = new FileOutputStream("/home/dingli/log.txt");

                 if (out != null)
                 {
                     //log file
                     ps = new PrintStream( out );
                     //System.setErr(ps); //redirects stderr to the log file as well

                 } else {

                     return rtn; 

                }


                InputSource source = new InputSource(new InputStreamReader(stream));
                CSSOMParser parser = new CSSOMParser();
                // parse and create a stylesheet composition
                CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);

                //ANY ERRORS IN THE DOM WILL BE SENT TO STDERR HERE!!
                // now iterate through the dom and inspect.

                CSSRuleList ruleList = stylesheet.getCssRules();

                ps.println("Number of rules: " + ruleList.getLength());


               for (int i = 0; i < ruleList.getLength(); i++) 
               {
                 CSSRule rule = ruleList.item(i);
                 if (rule instanceof CSSStyleRule) 
                 {
                     CSSStyleRule styleRule=(CSSStyleRule)rule;
                     ps.println("selector:" + i + ": " + styleRule.getSelectorText());
                     CSSStyleDeclaration styleDeclaration = styleRule.getStyle();
                     ps.println(TagMatches(styleRule.getSelectorText(),"a"));



                     for (int j = 0; j < styleDeclaration.getLength(); j++) 
                     {
                          String property = styleDeclaration.item(j);
                          ps.println("property: " + property);
                          ps.println("value: " + styleDeclaration.getPropertyCSSValue(property).getCssText());
                          ps.println("priority: " + styleDeclaration.getPropertyPriority(property));   
                     }



                  }// end of StyleRule instance test
                } // end of ruleList loop

               if (out != null) out.close();
               if (stream != null) stream.close();
               rtn = true;
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
                if (ps != null) ps.close(); 
            }

            return rtn;

    }

}