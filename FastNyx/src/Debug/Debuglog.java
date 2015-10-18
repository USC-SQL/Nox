package Debug;


public class Debuglog
{
    public final static boolean DEBUG = true;

    public static void log(String message)
    {
        if (DEBUG)
        {
            String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();            
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

            System.out.println(className + "." + methodName + "():" + lineNumber+":"+message);
        }
    }
    public static void log(Object message)
    {
        if (DEBUG)
        {
            String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();            
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

            System.out.println(className + "." + methodName + "():" + lineNumber+":"+message);
        }
    }
}