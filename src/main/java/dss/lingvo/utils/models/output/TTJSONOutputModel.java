package dss.lingvo.utils.models.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import dss.lingvo.utils.models.input.TTAbstractionLevelModel;
import dss.lingvo.utils.models.input.TTAlternativeModel;
import dss.lingvo.utils.models.input.TTExpertModel;
import dss.lingvo.utils.models.input.TTScaleModel;

import java.util.List;
import java.util.Map;

public class TTJSONOutputModel {
    private List<TTAlternativeModel> alternatives;
    private List<TTScaleModel> scales;
    private List<TTAbstractionLevelModel> abstractionLevels;
    private List<TTExpertModel> experts;
    @JsonProperty("abstractionLevelWeights")
    private Map<String, Float> abstractionLevelWeights;
    @JsonProperty("expertWeightsRule")
    private Map<String, Float> expertWeightsRule;
    @JsonProperty("alternativesOrdered")
    private List<TTAlternativeEstimationModel> alternativesOrdered;

    public List<TTAlternativeModel> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<TTAlternativeModel> alternatives) {
        this.alternatives = alternatives;
    }

    public List<TTScaleModel> getScales() {
        return scales;
    }

    public void setScales(List<TTScaleModel> scales) {
        this.scales = scales;
    }

    public List<TTAbstractionLevelModel> getAbstractionLevels() {
        return abstractionLevels;
    }

    public void setAbstractionLevels(List<TTAbstractionLevelModel> abstractionLevels) {
        this.abstractionLevels = abstractionLevels;
    }

    public List<TTExpertModel> getExperts() {
        return experts;
    }

    public void setExperts(List<TTExpertModel> experts) {
        this.experts = experts;
    }

    public Map<String, Float> getAbstractionLevelWeights() {
        return abstractionLevelWeights;
    }

    public void setAbstractionLevelWeights(Map<String, Float> abstractionLevelWeights) {
        this.abstractionLevelWeights = abstractionLevelWeights;
    }

    public Map<String, Float> getExpertWeightsRule() {
        return expertWeightsRule;
    }

    public void setExpertWeightsRule(Map<String, Float> expertWeightsRule) {
        this.expertWeightsRule = expertWeightsRule;
    }

    public List<TTAlternativeEstimationModel> getAlternativesOrdered() {
        return alternativesOrdered;
    }

    public void setAlternativesOrdered(List<TTAlternativeEstimationModel> alternativesOrdered) {
        this.alternativesOrdered = alternativesOrdered;
    }
}
