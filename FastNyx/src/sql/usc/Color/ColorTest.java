package sql.usc.Color;

import java.util.HashSet;
import java.util.Set;

public class ColorTest {
	public static void main(String argv[])
	{
		Set<Color> emp=new HashSet<Color>();
		Color c1=new Color(0,0,0);
		Color c2=new Color(127,255,212);
		Color c3=new Color(255,255,255);
		//ColorDistCalculator.CalDist(c3, c2);
		emp.add(c2);
		emp.add(c1);
		//ColorDistCalculator.CalDist(new CIELAB(50,2.6772,-79.7751),new CIELAB(50,0.0,-82.7485));
		ColorDistCalculator.CalDist(new CIELAB(90.9257,-0.5406,-0.9208),new CIELAB(88.6381,-0.8985,-0.7239));
		ColorDistCalculator.CalDist(new CIELAB(22.7233,20.0904,-46.6940),new CIELAB(23.0331,14.9730,-42.5619));
		int n='\t';
		System.out.println(n);
		
	}
}
