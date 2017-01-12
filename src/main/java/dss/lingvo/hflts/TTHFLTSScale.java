package dss.lingvo.hflts;

import java.util.Arrays;

public class TTHFLTSScale {
    private final String[] myTerms;

    public TTHFLTSScale(String [] terms){
        this.myTerms = terms;
    }

    public int termPosition(String term){
        return Arrays.asList(this.myTerms).indexOf(term);
    }
}
