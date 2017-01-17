package dss.lingvo.hflts;

import java.util.Arrays;

public class TTHFLTS {
    private String[] terms;

    public TTHFLTS(String[] terms) {
        this.terms = terms;
    }

    public String[] getTerms() {
        return terms;
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;
        if (obj != null) {
            isEqual = obj.getClass() == TTHFLTS.class &&
                    Arrays.equals(this.terms, ((TTHFLTS) obj).getTerms());
        }
        return isEqual;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.terms);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Arrays.hashCode(terms);
        return result;
    }
}
