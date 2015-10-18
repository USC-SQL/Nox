package test;

import sql.usc.Color.Color;
import sql.usc.Color.ColorDistCalculator;

/**
 * Created by dingli on 9/21/15.
 */
public class ColorAltitude {
    public static double GetAltitude(Color c)
    {
        Color b=new Color(255,255,255);
        return ColorDistCalculator.CalDist(c,b);
    }
    public static Color GetRandomColor()
    {
        int r = (int )(Math. random() * 255);
        int g = (int )(Math. random() * 255);
        int b = (int )(Math. random() * 255);
        return new Color(r,g,b);
    }

    public static void main(String argv[])
    {
        System.out.println("Alas");
        int max=100;
        double averate=0;
        double avedelta=0;
        for(int i=0;i<max;i++)
        {
            Color c1=GetRandomColor();
            Color c2=GetRandomColor();
            double Altitude1=GetAltitude(c1);
            double Altitude2=GetAltitude(c2);
            double dealtaltitude=Math.abs(Altitude1-Altitude2);
            double dist=ColorDistCalculator.CalDist(c1,c2);
            double rate=Math.abs((dealtaltitude-dist)/dist);
            double delta=Math.abs((dealtaltitude-dist));
            averate+=rate;
            avedelta+=delta;
            System.out.println(c1.toHexString()+" "+c2.toHexString()+" "+Altitude1+" "+Altitude2+" "+dist );
        }
        averate/=max;
        avedelta/=max;
        System.out.println("Final "+averate+" "+avedelta );

    }

}
