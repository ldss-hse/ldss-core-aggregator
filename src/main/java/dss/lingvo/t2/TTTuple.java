package dss.lingvo.t2;

import dss.lingvo.utils.TTConstants;

public class TTTuple {
    private final String myLabel;
    private float myTranslation;
    private int scaleSize;
    private int index;

    public TTTuple(String label, int scaleSize, float translation, int index) {
        this.myLabel = label;
        this.scaleSize = scaleSize;
        this.myTranslation = translation;
        this.index = index;
    }

    public String getLabel() {
        return myLabel;
    }

    public float getTranslation() {
        return myTranslation;
    }

    @Override
    public String toString(){
        return "<2Tuple> { label: " + this.myLabel + "; index: " + this.index
                + "; translation: " + this.myTranslation +"; }";
    }

    public int getIndex() {
        return index;
    }

    public int getScaleSize() {
        return scaleSize;
    }

    @Override
    public boolean equals(Object obj){
        boolean isEqual = false;
        final TTNormalizedTranslator translator = TTNormalizedTranslator.getInstance();
        if (obj != null) {
            isEqual = obj.getClass() == TTTuple.class &&
                    Math.abs(translator.getTranslationFrom2Tuple(this)-translator.getTranslationFrom2Tuple((TTTuple) obj))< TTConstants.FLOAT_PRECISION_DELTA;
        }
        return isEqual;
    }

    @Override
    public int hashCode(){
        int result = 17;
        result = 31 * result + this.myLabel.hashCode();
        result = 31 * result + this.scaleSize;
        return result;
    }
}
