package dss.lingvo.utils.models.output;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class TTEstimationModel {
    private Map<String, Float> estimation;

    public Map<String, Float> getEstimation() {
        return estimation;
    }

    public void setEstimation(Map<String, Float> estimation) {
        this.estimation = estimation;
    }
}