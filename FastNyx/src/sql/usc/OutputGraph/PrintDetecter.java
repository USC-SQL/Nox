package sql.usc.OutputGraph;

import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

public class PrintDetecter {
	public static boolean isPrintln(Unit u)
	{
		if(u instanceof  InvokeStmt)//model new StringBuilder(String), new String()
		{
			InvokeStmt ismt=(InvokeStmt)u;
			InvokeExpr iexp=ismt.getInvokeExpr();
			String sig=iexp.getMethodRef().toString();
			int argnum=iexp.getArgCount();
			if(argnum==0)
				return false;
			//System.out.println(sig);
			if(sig.startsWith("<java.io.PrintStream: void println("))
				return true;
			if(sig.startsWith("<java.io.PrintStream: void print("))
				return true;
			if(sig.startsWith("<javax.servlet.jsp.JspWriter: void println("))
				return true;
			if(sig.startsWith("<javax.servlet.jsp.JspWriter: void print("))
				return true;
			if(sig.startsWith("<javax.servlet.jsp.JspWriter: void write("))
				return true;

		}
		return false;
	}
}
