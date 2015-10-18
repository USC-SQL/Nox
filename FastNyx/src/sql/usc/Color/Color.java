package sql.usc.Color;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Debug.Debuglog;

import sql.usc.OutputGraph.OutputNode;

public class Color implements Serializable{
	int r;
	int g;
	int b;
	public int GetR()
	{
		return r;
	}
	public int GetG()
	{
		return g;
	}
	public int GetB()
	{
		return b;
	}
	public void SetR(int newr)
	{
		if(newr<0)
			this.r=0;
		else if(newr>255)
			this.r=255;
		else
			this.r=newr;
	}
	public void SetG(int newg)
	{
		if(newg<0)
			this.g=0;
		else if(newg>255)
			this.g=255;
		else
			this.g=newg;
	}
	public void SetB(int newb)
	{
		if(newb<0)
			this.b=0;
		else if(newb>255)
			this.b=255;
		else
			this.b=newb;
	}
	public Color(Color other)
	{
		this.r=other.r;
		this.g=other.g;
		this.b=other.b;
	}
	public Color(int r, int g, int b)
	{
		if(r>=256)
			throw new Error("illegal color, r should be less tha 256, not "+r);
		if(g>=256)
			throw new Error("illegal color, g should be less tha 256, not "+g);
		if(b>=256)
			throw new Error("illegal color, b should be less tha 256, not "+b);
		this.r=r;
		this.g=g;
		this.b=b;
	}
	public static boolean isColor(String dec)
	{
		String upper=dec.trim().toUpperCase();
		if(ColorDatabase.isPredefined(upper))
			return true;
		Pattern pattern1 = Pattern.compile("\\#([A-Za-z0-9]+)");
		 Matcher m1 = pattern1.matcher(upper);
		 if(m1.matches())
			 return true;
		 Pattern pattern2 = Pattern.compile("RGB\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\)");
		Matcher m2 = pattern2.matcher(upper);
		if(m2.matches())
		{
			return true;
		}
		return false;

	}
	public Color(String dec)
	{
		String upper=dec.trim().toUpperCase();
		if(ColorDatabase.isPredefined(upper))
		{
			Color c= ColorDatabase.getPredefinedColor(upper);
			this.r=c.r;
			this.g=c.g;
			this.b=c.b;
			return;
		}
		Pattern pattern1 = Pattern.compile("\\#([A-Za-z0-9]+)");
		 Matcher m1 = pattern1.matcher(upper);
			Pattern pattern11 = Pattern.compile("([A-Za-z0-9]+)");
			 Matcher m11 = pattern1.matcher(upper);
		 if(m1.matches() || m11.matches())
		 {
			 String cs=m1.group(1);
			 int num=Integer.parseInt(cs, 16);
			 this.b=num &0x000000FF;
			 this.g=(num>>8) &0x000000FF;
			 this.r=(num>>16) &0x000000FF;
			 return;


		 }
		 Pattern pattern2 = Pattern.compile("RGB\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\)");
		Matcher m2 = pattern2.matcher(upper);
		if(m2.matches())
		{
			this.b=Integer.parseInt(m2.group(3), 10);
			this.g=Integer.parseInt(m2.group(2), 10);
			this.r=Integer.parseInt(m2.group(1), 10);
		}


	}
	public String toHexString()
	{
		String r=Integer.toHexString(this.r);
		String g=Integer.toHexString(this.g);
		String b=Integer.toHexString(this.b);
		if(r.length()==1)
			r="0"+r;
		if(g.length()==1)
			g="0"+g;
		if(b.length()==1)
			b="0"+b;
		return "#"+r+g+b;
	}
	public String toString()
	{
		return "rgb("+this.r+","+this.g+","+this.b+")";
	}
	public boolean equals(Object o)
	{
		if(!(o instanceof Color))
			return false;
		Color input=(Color)o;
		if(this.r==input.r && this.g==input.g && this.b==input.b) 
			return true;
		return false;
	}
	public int hashCode() 
	{
    	int hash =256*256*this.r+256*this.g+this.b;
    
    	return hash;
	}	
}
