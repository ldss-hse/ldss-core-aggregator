package dss.lingvo.utils.models.output;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class TTAlternativeEstimationModel {
    private String scaleID;
    private String alternativeID;
    @JsonProperty("estimation")
    private List<Map<String, Float>> estimation;

    public String getScaleID() {
        return scaleID;
    }

    public void setScaleID(String scaleID) {
        this.scaleID = scaleID;
    }

    public String getAlternativeID() {
        return alternativeID;
    }

    public void setAlternativeID(String alternativeID) {
        this.alternativeID = alternativeID;
    }

    public List<Map<String, Float>> getEstimation() {
        return estimation;
    }

    public void setEstimation(List<Map<String, Float>> estimation) {
        this.estimation = estimation;
    }
}