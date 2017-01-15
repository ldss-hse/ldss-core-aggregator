package dss.lingvo.hflts;

import java.util.Arrays;

public class TTHFLTSScale {
    private final String[] myTerms;
    private final int myGranularity;

    public TTHFLTSScale(String [] terms){
        this.myTerms = terms;
        this.myGranularity = terms.length;
    }

    public int termPosition(String term){
        return Arrays.asList(this.myTerms).indexOf(term);
    }

    public int getGranularity(){
        return this.myGranularity;
    }

    public String getLabelByPosition(int position) {
        return myTerms[position];
    }
}
