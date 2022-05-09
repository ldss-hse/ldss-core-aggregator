package dss.lingvo.utils.models.input.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import dss.lingvo.utils.models.input.*;

import java.util.List;
import java.util.Map;

public class TTCommonInputModel {
    private List<TTAlternativeModel> alternatives;
    private List<TTScaleModel> scales;
    private List<TTAbstractionLevelModel> abstractionLevels;
    private List<TTExpertModel> experts;
    @JsonProperty("estimations")
    private Map<String, List<TTExpertEstimationsModel>> estimations;
    @JsonProperty("abstractionLevelWeights")
    private Map<String, Float> abstractionLevelWeights;
    @JsonProperty("expertWeights")
    private Map<String, Float> expertWeights;
    @JsonProperty("expertWeightsRule")
    private Map<String, Float> expertWeightsRule;
    @JsonProperty("criteriaWeightsPerGroup")
    private Map<String, List<Float> > criteriaWeightsPerGroup;

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

    public Map<String, List<TTExpertEstimationsModel>> getEstimations() {
        return estimations;
    }

    public void setEstimations(Map<String,  List<TTExpertEstimationsModel>> estimations) {
        this.estimations = estimations;
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

    public Map<String, Float> getExpertWeights() {
        return expertWeights;
    }

    public void setExpertWeights(Map<String, Float> expertWeights) {
        this.expertWeights = expertWeights;
    }

    public Map<String, List<Float>> getCriteriaWeightsPerGroup() {
        return criteriaWeightsPerGroup;
    }

    public void setCriteriaWeightsPerGroup(Map<String, List<Float>>criteriaWeightsPerGroup) {
        this.criteriaWeightsPerGroup = criteriaWeightsPerGroup;
    }
}
