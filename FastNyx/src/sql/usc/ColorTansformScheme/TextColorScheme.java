package sql.usc.ColorTansformScheme;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import Debug.Debuglog;

import sql.usc.Color.Color;
import sql.usc.Color.ColorDatabase;
import sql.usc.Color.ColorDistCalculator;
import sql.usc.ColorConflictGraph.CCGNode;
import sql.usc.ColorConflictGraph.ConflictPair;
import sql.usc.ColorConflictGraph.TexColorMap;

public class TextColorScheme {
	Hashtable<CCGNode, Color> transformtable=new Hashtable<CCGNode, Color>();
	public void DisplayTransform(String filename)
	{
		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="convert  -fuzz 10% "+ filename +" -fill \'"+n.getColor().toHexString()+"\' -opaque \'"+n.getColor().toHexString() +"\' "+ filename ;
			System.out.println(out);
		}
		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="convert "+ filename +" -fill \'"+trans.toHexString()+"\' -opaque \'"+n.getColor().toHexString() +"\' "+ filename ;
			System.out.println(out);
		}

	}
	public String GetImageScript(String filename)
	{
		String r="";
		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="convert  -fuzz 10% "+ filename +" -fill \'"+n.getColor().toHexString()+"\' -opaque \'"+n.getColor().toHexString() +"\' "+ filename ;
			r+=(out+"\n");
		}
		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="convert "+ filename +" -fill \'"+trans.toHexString()+"\' -opaque \'"+n.getColor().toHexString() +"\' "+ filename ;
			r+=(out+"\n");
		}
		return r;

	}
	public void Display()
	{
		String tag="text";
		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="texttable.put(new Color(\""+ n.getColor().toHexString()+"\"), new Color(\""+trans.toHexString()+"\"));";
			System.out.println(out);
		}
	}
	public String getJavaCode()
	{
		String r="";
		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="texttable.put(new Color(\""+ n.getColor().toHexString()+"\"), new Color(\""+trans.toHexString()+"\"));";
			r+=(out+"\n");
		}
		return r;
	}
	public String getPerlCode(){
		String r="";

		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String color=n.getColor().toHexString();
			String out="";
			if(color.charAt(1)==color.charAt(2)&&color.charAt(3)==color.charAt(4)&&color.charAt(5)==color.charAt(6))
			{
				String shortcase=Character.toString(color.charAt(1))+Character.toString(color.charAt(3))+Character.toString(color.charAt(5));
				out+="$file_content =~ s/; *color: *#"+ shortcase+"/;color: "+n.getColor().toHexString()+"/gi;\n";
				out+="$file_content =~ s/{ *color: *#"+ shortcase+"/{color: "+n.getColor().toHexString()+"/gi;\n";

			}
			out+="$file_content =~ s/; *color: *"+ n.getColor().toHexString()+"/;color: "+trans.toHexString()+"/gi;";
			out+="$file_content =~ s/{ *color: *"+ n.getColor().toHexString()+"/{color: "+trans.toHexString()+"/gi;";

			r+=(out+"\n");
		}
		return r;
	}
	private double subH(Set<CCGNode> bgs, BackgroundColorScheme bgt, Color newtext, Color oldtext)
	{
		double h=0;
		for(CCGNode bg:bgs)
		{
				Color bgcolor=bg.getColor();
				Color transbgcolor=bgt.QueryTransformedColor(bg);
				double originaldist=ColorDistCalculator.CalDist(bgcolor, oldtext);
				double transdist=ColorDistCalculator.CalDist(transbgcolor, newtext);

				h+=(originaldist-transdist)*(originaldist-transdist);
		}
		return h;
	}

	public TextColorScheme(TexColorMap tcm, BackgroundColorScheme bgt)
	{
		Set<CCGNode> texts=tcm.GetTextColor();
		Set<CCGNode> bg=tcm.GetBGColor();
		ColorDatabase codb=new ColorDatabase();

		for(CCGNode node:texts)
		{
			Set<CCGNode> conflictbg=tcm.QueryAllConfBGColor(node);
			double minsubh=Double.MAX_VALUE;
			Color besycolor=null;
			for(Color c:codb.GetAllColor())
			{
				double temp=subH(conflictbg, bgt, c, node.getColor());
				if(temp<minsubh)
				{
					minsubh=temp;
					besycolor=c;
				}
			}
			Debuglog.log(node.getColor());
			Debuglog.log(besycolor);
			transformtable.put(node, besycolor);
		}


		
	}
}
