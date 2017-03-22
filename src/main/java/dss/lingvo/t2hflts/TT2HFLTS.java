package dss.lingvo.t2hflts;

import dss.lingvo.t2.TTNormalizedTranslator;
import dss.lingvo.t2.TTTuple;

import java.util.Arrays;
import java.util.List;

public class TT2HFLTS {
    private List<TTTuple> terms;

    public TT2HFLTS(List<TTTuple> terms) {
        this.terms = terms;
    }

    public List<TTTuple> getTerms() {
        return terms;
    }

    public int getSize() {
        return terms.size();
    }

    public float getScore() {
        TTNormalizedTranslator myTranslator = TTNormalizedTranslator.getInstance();
        return (1f / this.getSize()) *
                (float) (Arrays
                        .stream(terms.toArray())
                        .reduce(0f, (a, b) ->
                                (float) a + myTranslator.getTranslationFrom2Tuple((TTTuple) b)));
    }

    public float getVariance() {
        float score = this.getScore();
        TTNormalizedTranslator myTranslator = TTNormalizedTranslator.getInstance();
        return (float) Math.sqrt((1f / this.getSize()) *
                (float) (Arrays
                        .stream(terms.toArray())
                        .reduce(0f, (a, b) ->
                                (float) a +
                                        (float)Math.pow(myTranslator.getTranslationFrom2Tuple((TTTuple) b)- score, 2))));
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = true;
        if (obj != null && obj.getClass() == TT2HFLTS.class) {
            List<TTTuple> objTerms = ((TT2HFLTS) obj).getTerms();
            if (objTerms.size()!= this.terms.size()){
                isEqual = false;
            }
            if (isEqual) {
                for (int i = 0; i < this.terms.size(); ++i) {
                    isEqual = this.terms.get(i).equals(objTerms.get(i));
                    if (!isEqual) {
                        break;
                    }
                }
            }
        } else {
            isEqual = false;
        }
        return isEqual;
    }

    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (TTTuple i : this.terms) {
            bld.append(i.toString());
        }
        return bld.toString();
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (TTTuple i: this.terms){
            result = 31*result + (i==null ? 0 : i.hashCode());
        }
        return result;
    }
}
