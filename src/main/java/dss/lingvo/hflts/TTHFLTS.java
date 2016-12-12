package dss.lingvo.hflts;

import java.util.Arrays;

public class TTHFLTS {
    private String[] terms;

    public TTHFLTS(String [] terms){
        this.terms = terms;
    }

    public void setTerms(String [] terms){
        this.terms = terms;
    }

    public String[] getTerms() {
        return terms;
    }

    @Override
    public boolean equals(Object obj){
        return obj.getClass()!=TTHFLTS.class &&
                Arrays.equals(this.terms, ((TTHFLTS) obj).getTerms());
    }
}
