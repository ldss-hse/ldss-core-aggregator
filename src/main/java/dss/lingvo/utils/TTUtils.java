package dss.lingvo.utils;

public class TTUtils {
    public static TTUtils myInstance = null;

    private TTUtils(){}

    public static TTUtils getInstance(){
        if (myInstance == null){
           myInstance = new TTUtils();
        }
        return myInstance;
    }

    public void info(Object obj){
        System.out.println(obj.toString());
    }
}
