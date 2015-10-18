package sql.usc.Color;

public class CIELAB {
		public double L;
		public double a;
		public double b;
		public String toString()
		{
			return "("+L+","+a+","+b+")";
		}
		public boolean equals(Object o)
		{
			if(!(o instanceof CIELAB))
				return false;
			CIELAB input=(CIELAB)o;
			double deltaL=Math.sqrt((this.L -input.L)*this.L -input.L);
			double deltaa=Math.sqrt((this.a -input.a)*this.a -input.a);
			double deltab=Math.sqrt((this.b -input.b)*this.b -input.b);
			if(deltaL/this.L>0.01)
				return false;
			if(deltaa/this.a>0.01)
				return false;
			if(deltab/this.b>0.01)
				return false;

			return true;
		}
		public int hashCode() 
		{
	    	int hash =(int) (this.L+this.a+this.b);
	    
	    	return hash;
		}	
		public CIELAB(double L, double a, double b)
		{
			this.L=L;
			this.a=a;
			this.b=b;
		}
		public CIELAB(Color a)
		{
			double R0=(double)a.r/255.0;
			double G0=(double)a.g/255.0;
			double B0=(double)a.b/255.0;

			double R1,G1,B1;
			if(R0<0.04045)
				R1=R0/12.92;
			else
			{
				R1=Math.pow(((R0+0.055)/1.055), 2.4);
			}
			if(G0<0.04045)
				G1=G0/12.92;
			else
			{
				G1=Math.pow(((G0+0.055)/1.055), 2.4);
			}
			if(B0<0.04045)
				B1=B0/12.92;
			else
			{
				B1=Math.pow(((B0+0.055)/1.055), 2.4);
			}
			double  X,Y,Z;
			X=0.4124*R1+0.3576*G1+0.1805*B1;
			Y=0.2126*R1+0.7152*G1+0.0722*B1;
			Z=0.0193*R1+0.1192*G1+0.9505*B1;
			double Xn=0.9505, Yn=1.00,Zn=1.089;
			double q=Y/Yn;
			double p=X/Xn;
			double r=Z/Zn;
			double q1,p1,r1;
			if(q<=0.008856)
				q1=7.787*q+16.0/116.0;
			else
				q1=Math.pow(q, 0.33333333);
			if(p<=0.008856)
				p1=7.787*p+16.0/116.0;
			else
				p1=Math.pow(p, 0.33333333);
			if(r<=0.008856)
				r1=7.787*r+16.0/116.0;
			else
				r1=Math.pow(r, 0.33333333);
			
			if(q>0.008856)
				this.L=116*Math.pow(q, 0.33333333)-16;
			else
				this.L=903.3*q;
			this.a=500.0*(p1-q1);
			this.b=200.0*(q1-r1);
		}
}
