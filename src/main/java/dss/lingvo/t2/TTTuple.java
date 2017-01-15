package dss.lingvo.t2;

import dss.lingvo.hflts.TTHFLTSScale;

public class TTTuple {
    private final String myLabel;
    private float myTranslation;
    private int scaleSize;
    private int index;

    public TTTuple(String label, int scaleSize) {
        this.myLabel = label;
        this.scaleSize = scaleSize;
    }

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
        return "<2Tuple> { label: " + this.myLabel + "; translation: " + this.myTranslation +"; }";
    }

    public int getIndex() {
        return index;
    }

    public int getScaleSize() {
        return scaleSize;
    }
}
