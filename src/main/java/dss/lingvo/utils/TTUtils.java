package dss.lingvo.utils;

public class TTUtils {
    public static TTUtils myInstance = new TTUtils();

    private TTUtils(){}

    public static TTUtils getInstance(){
        return myInstance;
    }

    public void info(Object obj){
        System.out.println(obj.toString());
    }
}
