package test;

import java.util.Set;

import sql.usc.Color.Color;
import sql.usc.Color.ColorDatabase;
import sql.usc.Color.ColorDistCalculator;

public class ColorTest {
public static void main(String argv[])
{
	//Color c1=new Color("#000000");
	//Color c2=new Color("#000044");
	//Color c3=new Color("blue");
	//Color c4=new Color("#ffffbb");
	//System.out.println(ColorDistCalculator.CalDist(c1,c2)+" "+ColorDistCalculator.CalDist(c3,c4));
	ColorDatabase cdb=new ColorDatabase();
	Set<Color> allcolors=cdb.GetAllColor();
	double max=0;
	Color maxcolorb=null;
	Color maxcolort=null;

	for(Color c1: allcolors)
		for(Color c2: allcolors)
		{
			Color invertc1=new Color(255-c1.GetR(),255-c1.GetG(),255-c1.GetB());
			Color invertc2=new Color(255-c2.GetR(),255-c2.GetG(),255-c2.GetB());

			double blackdist=ColorDistCalculator.CalDist(c1,c2);
			double whitedist=ColorDistCalculator.CalDist(invertc1,invertc2);
			double dist=(blackdist-whitedist)*(blackdist-whitedist);
		if(dist>max && whitedist<5 && blackdist>whitedist)
		{
			max=dist;
			maxcolorb=c1;
			maxcolort=c2;

		}
		}
	System.out.println(max+" "+maxcolorb+" "+maxcolort);

}
}
