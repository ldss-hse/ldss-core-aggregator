package dss.lingvo.utils.models.input;

import java.util.List;

public class TTScaleModel {
    private String scaleID;
    private String scaleName;
    private List<String> labels;
    private List<Float> values;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getScaleName() {
        return scaleName;
    }

    public void setScaleName(String scaleName) {
        this.scaleName = scaleName;
    }

    public String getScaleID() {
        return scaleID;
    }

    public void setScaleID(String scaleID) {
        this.scaleID = scaleID;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }
}
