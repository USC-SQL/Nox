package sql.usc.ColorTansformScheme;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import sql.usc.Color.Color;
import sql.usc.Color.ColorDatabase;
import sql.usc.Color.ColorDistCalculator;
import sql.usc.ColorConflictGraph.CCGNode;
import sql.usc.ColorConflictGraph.ColorConfictGraph;
import java.util.Random;

public class BackgroundColorScheme {
	Hashtable<CCGNode, Color> transformtable;
	public Color QueryTransformedColor(CCGNode key)
	{
		return transformtable.get(key);
	}
	private double averdiff(ColorConfictGraph ccg, Hashtable<CCGNode, Color>transformtable)
	{
		double h=0;
		int cnt=0;
		for(CCGNode ccgnode1:ccg.GetAllNodes())
			for(CCGNode ccgnode2:ccg.GetAllNodes())
			{
				cnt++;
				if(ccgnode2 == ccgnode1)
					continue;
					Color originalC1=ccgnode1.getColor();
					Color originalC2=ccgnode2.getColor();
					Color TansformedC1=transformtable.get(ccgnode1);
					Color TansformedC2=transformtable.get(ccgnode2);
					double w=ccg.QueryWight(ccgnode1, ccgnode2);

					double originaldist=ColorDistCalculator.CalDist(originalC1, originalC2);
					double transformeddist=ColorDistCalculator.CalDist(TansformedC1, TansformedC2);
					h+=Math.sqrt((originaldist-transformeddist)*(originaldist-transformeddist));

			}
		return h/cnt;
	}
	private double averageDist(ColorConfictGraph ccg)
	{
		double ac=0;
		double cnt=0;
		for(CCGNode ccgnode1:ccg.GetAllNodes())
			for(CCGNode ccgnode2:ccg.GetAllNodes())
			{
				if(ccgnode2 == ccgnode1)
					continue;
				cnt++;
					Color originalC1=ccgnode1.getColor();
					Color originalC2=ccgnode2.getColor();
					double originaldist=ColorDistCalculator.CalDist(originalC1, originalC2);
					ac+=Math.sqrt((originaldist)*(originaldist));
			}
		return ac/cnt;
	}
	private double accumulateDist(ColorConfictGraph ccg)
	{
		double ac=0;
		for(CCGNode ccgnode1:ccg.GetAllNodes())
			for(CCGNode ccgnode2:ccg.GetAllNodes())
			{
				if(ccgnode2 == ccgnode1)
					continue;
					Color originalC1=ccgnode1.getColor();
					Color originalC2=ccgnode2.getColor();
					double w=ccg.QueryWight(ccgnode1, ccgnode2);

					double originaldist=ColorDistCalculator.CalDist(originalC1, originalC2);
					ac+=w*(originaldist)*(originaldist);
			}
		return ac;
	}
	private Hashtable<CCGNode, Color>  findNext(Hashtable<CCGNode, Color> current, CCGNode root)
	{
		int delta=4;
		Hashtable<CCGNode, Color> clonetable=new Hashtable<CCGNode, Color>();
		for(CCGNode key:current.keySet())
			clonetable.put(key, new Color(current.get(key)));
	      Calendar clock1 = Calendar.getInstance();  
			long time1 = clock1.getTimeInMillis();  
		  Random generator = new Random(time1);
			for(CCGNode key:clonetable.keySet())
			{
				if(key==root)
					continue;
				Color c=clonetable.get(key);
				int cR=c.GetR();
				int cG=c.GetG();
				int cB=c.GetB();

				int sign=0;
			     sign=generator.nextInt(3)-1;
			     cR+=sign*delta;
			     c.SetR(cR);
			     sign=generator.nextInt(3)-1;
			     cG+=sign*delta;
			     c.SetG(cG);
			     sign=generator.nextInt(3)-1;
			     cB+=sign*delta;
			     c.SetB(cB);
			     
			}
			return clonetable;
		  
		
	}
	private double H(ColorConfictGraph ccg, Hashtable<CCGNode, Color>transformtable)
	{
		double h=0;
		for(CCGNode ccgnode1:ccg.GetAllNodes())
			for(CCGNode ccgnode2:ccg.GetAllNodes())
			{
				if(ccgnode2 == ccgnode1)
					continue;
					Color originalC1=ccgnode1.getColor();
					Color originalC2=ccgnode2.getColor();
					Color TansformedC1=transformtable.get(ccgnode1);
					Color TansformedC2=transformtable.get(ccgnode2);
					double w=ccg.QueryWight(ccgnode1, ccgnode2);

					double originaldist=ColorDistCalculator.CalDist(originalC1, originalC2);
					double transformeddist=ColorDistCalculator.CalDist(TansformedC1, TansformedC2);
					h+=w*(originaldist-transformeddist)*(originaldist-transformeddist);

			}
		return h;
	}
	public void DisplayTransform(String filename)
	{
		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="convert -fuzz 8000 "+ filename +" -fill \'"+trans.toHexString()+"\' -opaque \'"+n.getColor().toHexString() +"\' "+ filename ;
			System.out.println(out);
		}

	}
	public void DisplayPerl()
	{
		String tag="backgournd";


	}
	public void Display()
	{
		String tag="backgournd";

		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="bgtable.put(new Color(\""+ n.getColor().toHexString()+"\"), new Color(\""+trans.toHexString()+"\"));";
			System.out.println(out);
		}

	}
	public String getJavaCode(){
		String r="";

		for(CCGNode n:transformtable.keySet())
		{
			Color trans=transformtable.get(n);
			String out="bgtable.put(new Color(\""+ n.getColor().toHexString()+"\"), new Color(\""+trans.toHexString()+"\"));";
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
				out+="$file_content =~ s/background: *#"+ shortcase+"/background: "+n.getColor().toHexString()+"/gi;\n";
				out+="$file_content =~ s/background-color: *#"+ shortcase+"/background-color: "+n.getColor().toHexString()+"/gi;\n";

			}
			out+="$file_content =~ s/background: *"+ n.getColor().toHexString()+"/background: "+trans.toHexString()+"/gi;\n";
			out+="$file_content =~ s/background-color: *"+ n.getColor().toHexString()+"/background-color: "+trans.toHexString()+"/gi;\n";

			r+=(out);
		}
		return r;
	}
	private double H(Set<CCGNode> workset, ColorConfictGraph ccg, Hashtable<CCGNode, Color>transformtable)
	{
		double h=0;
		for(CCGNode ccgnode1:workset)
			for(CCGNode ccgnode2:workset)
			{
				if(ccgnode2 == ccgnode1)
					continue;
					Color originalC1=ccgnode1.getColor();
					Color originalC2=ccgnode2.getColor();
					Color TansformedC1=transformtable.get(ccgnode1);
					Color TansformedC2=transformtable.get(ccgnode2);
					double w=ccg.QueryWight(ccgnode1, ccgnode2);

					double originaldist=ColorDistCalculator.CalDist(originalC1, originalC2);
					double transformeddist=ColorDistCalculator.CalDist(TansformedC1, TansformedC2);
					h+=w*(originaldist-transformeddist)*(originaldist-transformeddist);

			}
		return h;
	}
	public BackgroundColorScheme(ColorConfictGraph ccg, Color bg)
	{
		CCGNode root=null;
		ColorDatabase codb=new ColorDatabase();
		Hashtable<CCGNode, Color> tmptransformtable=new Hashtable<CCGNode, Color>();

		for(CCGNode ccgnode:ccg.GetAllNodes())
		{
			if(ccgnode.getColor().equals(bg))//change it to the color you want to adapt
				root=ccgnode;
			tmptransformtable.put(ccgnode, new Color(0,0,0));
		}
		// initialize the value with greedy
		Set<CCGNode> WorkSet=new HashSet<CCGNode>();
		Set<CCGNode> WhiteSet=new HashSet<CCGNode>();
		WorkSet.add(root);
		WhiteSet.addAll(ccg.GetAllNodes());
		WhiteSet.remove(root);
		while(!WhiteSet.isEmpty())
		{
			CCGNode nextnode=null;
			double largestvalue=0;
			for(CCGNode n:WorkSet)
				for(CCGNode w:WhiteSet)
				{
					double wight=ccg.QueryWight(n,w);
					if(wight>largestvalue)
					{
						largestvalue=wight;
						nextnode=w;
					}
				}
			WhiteSet.remove(nextnode);
			WorkSet.add(nextnode);
			double minh=Double.MAX_VALUE;
			Color minColor=null;
			for(Color c:codb.GetAllColor())
			{
				tmptransformtable.put(nextnode, c);
				double h=H(WorkSet,ccg,tmptransformtable);
				if(h<minh)
				{
					minh=h;
					minColor=c;
				}
			}
			tmptransformtable.put(nextnode, minColor);

		}
		transformtable=tmptransformtable;
		//Start Simulated Annealing
		int T=19890415;
	      Calendar clock1 = Calendar.getInstance();  
			long time1 = clock1.getTimeInMillis();  
		  Random generator = new Random(time1);
		while(T>0)
		{
			T-=generator.nextInt(2000)+1000;
			Hashtable<CCGNode, Color> nexttable=findNext(transformtable,root);
			if(H(ccg,transformtable)>H(ccg,nexttable))
				transformtable=nexttable;
			else if(H(ccg,nexttable)>H(ccg,transformtable))
			{
				double index=(H(ccg,transformtable)-H(ccg,nexttable))/((double)T)*10;
				//System.out.println(index);
				double p=Math.pow(Math.E,index);
				double divpoint=generator.nextDouble();
				if(divpoint<p)
				{
					transformtable=nexttable;
				}
			}
		}
		System.out.println(H(ccg,transformtable));
		System.out.println(accumulateDist(ccg));
		System.out.println(averdiff(ccg,transformtable));
		System.out.println(averageDist(ccg));


	}
}
