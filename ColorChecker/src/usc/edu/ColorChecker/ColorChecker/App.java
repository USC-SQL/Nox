package usc.edu.ColorChecker.ColorChecker;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import CallGraph.Node;
import org.xmlpull.v1.XmlPullParserException;
import SootEvironment.AndroidApp;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

/**
 * Hello world!
 */

public class App {
    private static HashMap<String,Boolean> pullutedTable=new HashMap<String,Boolean>();
    private static HashSet<String> sysAPI=new HashSet<String>();
    private static boolean isPulluted(SootMethod sm)
    {
        if(pullutedTable.containsKey(sm.getSignature())&&pullutedTable.get(sm.getSignature()))
        {
            return true;
        }
        Body b = sm.retrieveActiveBody();
        Chain units = b.getUnits();
        Iterator stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Unit stmt = (Unit) stmtIt.next();
            if(ToolKit.isInvocation(stmt))
            {
                String subsig=ToolKit.getInvokesubSignature(stmt);
                String sig= ToolKit.getInvokeSignature(stmt);
                if(sysAPI.contains(subsig))
                {
                    return true;
                }
                if(pullutedTable.containsKey(sig))
                {
                    if(pullutedTable.get(sig)==true)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static boolean isActivity(SootMethod sm)
    {
        if(sm.getSubSignature().startsWith("void onCreate"))
        {
            return true;
        }
        if(sm.getSubSignature().startsWith("void onRestart"))
        {
            return true;
        }
        if(sm.getSubSignature().startsWith("void onResume"))
        {
            return true;
        }
        if(sm.getSubSignature().startsWith("void onPause"))
        {
            return true;
        }
        if(sm.getSubSignature().startsWith("void onStop"))
        {
            return true;
        }
        if(sm.getSubSignature().startsWith("void onDestroy"))
        {
            return true;
        }
        return false;
    }
    public static void main(String[] args) throws IOException, XmlPullParserException {
        if (args.length != 3) {
            System.out.println("Usage: android_jar_path apk classlist.txt");
            return ;
        }
        long time1=System.currentTimeMillis();
        sysAPI.add("void setAlpha(float)");
        //sysAPI.add("void setAnimation(android.view.animation.Animation)");
        //sysAPI.add("void setBackground(android.graphics.drawable.Drawable)");
        sysAPI.add("void setBackgroundColor(int)");
        //sysAPI.add("void setBackgroundDrawable(android.graphics.drawable.Drawable)");
//        apiMap.put("<android.view.View: void setBackgroundResource(int)>", 0);
        //sysAPI.add("void setBackgroundTintList(android.content.res.ColorStateList)");
        //sysAPI.add("void setBackgroundTintMode(android.graphics.PorterDuff.Mode)");
        // For android.widget.TextView
        //sysAPI.add("void setHighlightColor(int)");
        //sysAPI.add("void setHintTextColor(android.content.res.ColorStateList)");
        //sysAPI.add("void setHintTextColor(int)");
        //sysAPI.add("void setLinkTextColor(android.content.res.ColorStateList)");
        //sysAPI.add("void setLinkTextColor(int)");
        //sysAPI.add("void setShadowLayer(float,float,float,int)");
        //sysAPI.add("void setTextColor(android.content.res.ColorStateList)");
        sysAPI.add("void setTextColor(int)");
        // For android.widget.ImageView
        //sysAPI.add("void setImageAlpha(int)");
        //sysAPI.add("void setImageBitmap(android.graphics.Bitmap)");
        //sysAPI.add("void setImageDrawable(android.graphics.drawable.Drawable)");
//        apiMap.put("<android.widget.ImageView: void setImageResource(int)>", 0);
        //sysAPI.add("void setImageTintList(android.content.res.ColorStateList)");
        //sysAPI.add("void setImageTintMode(android.graphics.PorterDuff.Mode)");
        // For android.widget.ProgressBar
        //sysAPI.add("void setIndeterminateTintList(android.content.res.ColorStateList)");
        //sysAPI.add("void setProgressBackgroundTintList(android.content.res.ColorStateList)");
        //sysAPI.add("void setProgressBackgroundTintMode(android.graphics.PorterDuff.Mode)");
        //sysAPI.add("void setProgressTintList(android.content.res.ColorStateList)");
        //sysAPI.add("void setProgressTintMode(android.graphics.PorterDuff.Mode)");
        //sysAPI.add("void setSecondaryProgressTintList(android.content.res.ColorStateList)");
        //sysAPI.add("void setSecondaryProgressTintMode(android.graphics.PorterDuff.Mode)");
        AndroidApp App = new AndroidApp(args[0], args[1], args[2]);
        for(Node n:App.getCallgraph().getAllNodes())
        {
            SootMethod sm=n.getMethod();
            pullutedTable.put(sm.getSignature(),false);
            //System.out.println(sm.getSignature());
        }
        boolean changed=true;
        while(changed) {
            changed = false;
            for (Node n : App.getCallgraph().getAllNodes()) {
                SootMethod sm = n.getMethod();
                boolean old = pullutedTable.get(sm.getSignature());
                boolean newv = isPulluted(sm);
                if (old != newv) {
                    pullutedTable.put(sm.getSignature(), newv);
                    changed=true;
                }
                //System.out.println(sm.getSignature());
            }
        }
        int polluted=0;
        int notp=0;
        for(Node n:App.getCallgraph().getAllNodes())
        {
            SootMethod sm=n.getMethod();
            if(isActivity(sm))
            {
                if(pullutedTable.get(sm.getSignature()))
                {
                    polluted++;
                }
                else{
                    notp++;
                }
            }

        }
        System.out.println(args[1]+" "+polluted+" "+notp);

        /*PrintWriter pw = new PrintWriter(new FileOutputStream(
                new File("result.txt"),
                true ));
        pw.println(args[1]+" "+polluted+" "+notp);
        pw.close();*/


    }
}
