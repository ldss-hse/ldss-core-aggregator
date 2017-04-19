package dss.lingvo.t2hflts.multilevel;

import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.t2hflts.TT2HFLTSMHTWAOperator;
import dss.lingvo.t2hflts.TT2HFLTSMTWAOperator;
import dss.lingvo.utils.TTUtils;
import dss.lingvo.utils.models.input.TTAbstractionLevelModel;
import dss.lingvo.utils.models.input.TTCriteriaModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TT2HFLTSMHTWOWAMultiLevelOperator {

    public List<TT2HFLTS> calculate() {
        return null;
    }

    public List<ArrayList<ArrayList<TT2HFLTS>>> aggregateByAbstractionLevel(Map<String, List<TTCriteriaModel>> criteria, List<TTAbstractionLevelModel> abstractionLevels, List<ArrayList<ArrayList<TT2HFLTS>>> all, int targetScaleSize) {
        List<TTCriteriaModel> orderedCriteria = TTUtils.getOrderedCriteriaList(criteria, abstractionLevels);
        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperator = new TT2HFLTSMHTWAOperator();
        List<ArrayList<ArrayList<TT2HFLTS>>> expEstimates = new ArrayList<>();
        for (ArrayList<ArrayList<TT2HFLTS>> singleExpertEst : all) {
            ArrayList<ArrayList<TT2HFLTS>> altEstimates = new ArrayList<>();
            for (ArrayList<TT2HFLTS> singleAltEst : singleExpertEst) {
                ArrayList<TT2HFLTS> levelEstimates = new ArrayList<>();
                for (TTAbstractionLevelModel ttAbstractionLevelModel : abstractionLevels) {
                    List<TTCriteriaModel> criteriaList = criteria.get(ttAbstractionLevelModel.getAbstractionLevelID());
                    int[] indices = IntStream.range(0, orderedCriteria.size())
                            .filter(i -> {
                                TTCriteriaModel res = criteriaList
                                        .stream()
                                        .filter((e) -> e.getCriteriaID().equals(orderedCriteria.get(i).getCriteriaID()))
                                        .findFirst()
                                        .orElse(null);
                                return res != null;
                            })
                            .toArray();
                    List<TT2HFLTS> tmp = new ArrayList<>();
                    for (int i : indices) {
                        tmp.add(singleAltEst.get(i));
                    }
                    // weights are currently equal
                    float[] weights = new float[tmp.size()];
                    for (int i = 0; i < tmp.size(); i++) {
                        weights[i] = 1f / tmp.size();
                    }
                    TT2HFLTS aggRes = tt2HFLTSMHTWAOperator.calculate(tmp, weights, targetScaleSize);
                    levelEstimates.add(aggRes);
                }
                altEstimates.add(levelEstimates);
            }
            expEstimates.add(altEstimates);
        }
        return expEstimates;
    }
}
