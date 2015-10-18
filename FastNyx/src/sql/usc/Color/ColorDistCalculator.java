package sql.usc.Color;

public class ColorDistCalculator {
	//private static 
	public static double CalDist(Color c1, Color c2)
	{
		CIELAB ciea=new CIELAB(c1);
		CIELAB cieb=new CIELAB(c2);
		return  CalDist( ciea,  cieb);

	}
	public static double CalDist(CIELAB ciea, CIELAB cieb)
	{
		double L1,a1,b1,L2,a2,b2;
		L1=ciea.L;
		a1=ciea.a;
		b1=ciea.b;
		L2=cieb.L;
		a2=cieb.a;
		b2=cieb.b;
		//step 1, calculate Cdot1, Cdot2, h1,h2
		double Cstar1=Math.sqrt((a1*a1)+(b1*b1));
		double Cstar2=Math.sqrt((a2*a2)+(b2*b2));
		double Cstarbar=(Cstar1+Cstar2)/2;
		double Cstarbar7=Math.pow(Cstarbar, 7);
		double twentyfive7=Math.pow(25, 7);
		double G=0.5*(1.0-Math.sqrt(
				(Cstarbar7/(Cstarbar7+twentyfive7)
						)));
		double adot1=(1+G)*a1,adot2=(1+G)*a2;


		double Cdot1=Math.sqrt((adot1*adot1)+(b1*b1)),Cdot2=Math.sqrt((adot2*adot2)+(b2*b2));

		double h1,h2;
		if(adot1==b1 && b1==0)
			h1=0;
		else
		{
			h1=Math.atan2(b1, adot1);
			if(h1<0)
				h1+=2*Math.PI;
		}
		if(adot2==b2 && b2==0)
			h2=0;
		else
		{
			h2=Math.atan2(b2, adot2);
			if(h2<0)
				h2+=2*Math.PI;
		}

		//step 2, calculate deltaL, deltaC and deltaH
		double angleh1=h1*180/Math.PI, angleh2=h2*180/Math.PI;
		double deltaL=L2-L1;

		double deltaC=Cdot2-Cdot1;

		double deltah=0;
		double angledeltah=0;

		if(Cdot1*Cdot2==0)
			angledeltah=0;
		else if(Math.abs(angleh2-angleh1)<=180.0)
			angledeltah=angleh2-angleh1;
		else if(angleh2-angleh1>180)
			angledeltah=angleh2-angleh1-360;
		else if(angleh2-angleh1<-180)
			angledeltah=angleh2-angleh1+360;
		angledeltah=angledeltah/2;
		deltah=(angledeltah/180)*Math.PI;

		double deltaH=2*Math.sqrt(Cdot1*Cdot2)*Math.sin(deltah);
		double Lbar=(L1+L2)/2, Cdotbar=(Cdot1+Cdot2)/2;
		double hbar=0;
		double anglehbar=0;

		if(Cdot1*Cdot2==0)
			anglehbar=angleh1+angleh2;
		else if(Math.abs(angleh2-angleh1)<=180)
			anglehbar=(angleh1+angleh2)/2;
		else if(Math.abs(angleh2-angleh1)>180 && angleh2+angleh1<360)
			anglehbar=(angleh1+angleh2+360)/2;
		else 
			anglehbar=(angleh1+angleh2-360)/2;
		hbar=(anglehbar/180)*Math.PI;


		double T=1-0.17*Math.cos(hbar-(Math.PI/6))+0.24*Math.cos(2*hbar)+0.32*Math.cos(3*hbar+(6.0/180.0)*Math.PI)-0.2*Math.cos(4*hbar-(63.0/180.0)*Math.PI);
		
		double angledeltatheta=30*Math.pow(Math.E, -((anglehbar-275)/25)*((anglehbar-275)/25));
		double deltatheta= (angledeltatheta/180)*Math.PI; //convert angle to radian		
		
		double Cdotbar7=Math.pow(Cdotbar, 7);
		double Rc=2*Math.sqrt(Cdotbar7/(Cdotbar7+twentyfive7));
		double SL=1+0.015*(Lbar-50)*(Lbar-50)/Math.sqrt((Lbar-50)*(Lbar-50)+20);
		double Sc=1+0.045*Cdotbar;

		double SH=1+0.015*Cdotbar*T;

		double Rt=-Math.sin(2*deltatheta)*Rc;

		double dist=(deltaL/SL)*(deltaL/SL)+(deltaC/Sc)*(deltaC/Sc)+(deltaH/SH)*(deltaH/SH)+Rt*(deltaC/Sc)*(deltaH/SH);
		 dist=Math.sqrt(dist);



		return dist;
	}
}
