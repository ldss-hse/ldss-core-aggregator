package dss.lingvo.hflts;

import java.util.Arrays;

public class TTHFLTSUtils {
    private TTHFLTSUtils(){}

    public static TTHFLTS getSubset(TTHFLTS set, int left, int right){
        return new TTHFLTS(Arrays.copyOfRange(set.getTerms(), left, right));
    }
}
