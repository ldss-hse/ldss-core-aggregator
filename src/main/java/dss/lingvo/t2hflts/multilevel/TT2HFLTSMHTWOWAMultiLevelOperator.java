package dss.lingvo.t2hflts.multilevel;

import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.t2hflts.TT2HFLTSMHTWAOperator;
import dss.lingvo.utils.TTUtils;
import dss.lingvo.utils.models.input.TTAbstractionLevelModel;
import dss.lingvo.utils.models.input.TTCriteriaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TT2HFLTSMHTWOWAMultiLevelOperator {
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
                                        .filter(e -> e.getCriteriaID().equals(orderedCriteria.get(i).getCriteriaID()))
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
                    float[] weights = getEqualWeights(tmp.size());
                    TT2HFLTS aggRes = tt2HFLTSMHTWAOperator.calculate(tmp, weights, targetScaleSize);
                    levelEstimates.add(aggRes);
                }
                altEstimates.add(levelEstimates);
            }
            expEstimates.add(altEstimates);
        }
        return expEstimates;
    }

    public List<ArrayList<TT2HFLTS>> aggregateByExpert(int levelsSize,
                                                       int altSize,
                                                       int targetScaleSize,
                                                       List<ArrayList<ArrayList<TT2HFLTS>>> all,
                                                       float[] distribution) {
        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperator = new TT2HFLTSMHTWAOperator();
        List<ArrayList<TT2HFLTS>> levelEstimates = new ArrayList<>();

        //initialize the matrix
        IntStream.range(0, altSize).forEach(i -> {
            ArrayList<TT2HFLTS> levelEst = new ArrayList<>();
            IntStream.range(0, levelsSize).forEach(j -> levelEst.add(null));
            levelEstimates.add(levelEst);
        });

        // fill matrix
        for (int levelIndex = 0; levelIndex < levelsSize; levelIndex++) {
            for (int altIndex = 0; altIndex < altSize; altIndex++) {
                float[] weights = TTUtils.calculateWeightsVector(distribution, all.get(levelIndex).get(altIndex).size());
                List<TT2HFLTS> newSet = TTUtils.sortTT2HFLTS(all.get(levelIndex).get(altIndex), true);
                TT2HFLTS aggRes = tt2HFLTSMHTWAOperator.calculate(newSet, weights, targetScaleSize);
                levelEstimates.get(altIndex).set(levelIndex, aggRes);
            }
        }
        return levelEstimates;
    }

    public List<TT2HFLTS> aggregateFinalAltEst(int targetScaleSize, List<ArrayList<TT2HFLTS>> all) {
        TT2HFLTSMHTWAOperator tt2HFLTSMHTWAOperator = new TT2HFLTSMHTWAOperator();
        List<TT2HFLTS> altEstimates = new ArrayList<>();

        // fill matrix
        for (ArrayList<TT2HFLTS> levelEstimates: all) {
                // weights are currently equal
                float[] weights = getEqualWeights(levelEstimates.size());
                TT2HFLTS aggRes = tt2HFLTSMHTWAOperator.calculate(levelEstimates, weights, targetScaleSize);
            altEstimates.add(aggRes);
        }
        return altEstimates;
    }

    public List<ArrayList<ArrayList<TT2HFLTS>>> transposeByAbstractionLevel(int levelsSize, int altSize, int expertsSize,
                                                                            List<ArrayList<ArrayList<TT2HFLTS>>> estGroupedByAlternatives) {
        // init the matrixes
        List<ArrayList<ArrayList<TT2HFLTS>>> levelEstimates = new ArrayList<>();
        IntStream.range(0, levelsSize).forEach(i -> {
            ArrayList<ArrayList<TT2HFLTS>> levelEst = new ArrayList<>();
            IntStream.range(0, altSize).forEach(j -> {
                ArrayList<TT2HFLTS> altEst = new ArrayList<>();
                IntStream.range(0, expertsSize).forEach(k -> altEst.add(null));
                levelEst.add(altEst);
            });
            levelEstimates.add(levelEst);
        });

        // fill matrixes
        for (int expertIndex = 0; expertIndex < expertsSize; expertIndex++) {
            for (int altIndex = 0; altIndex <altSize; altIndex++) {
                for (int levelIndex = 0; levelIndex < levelsSize; levelIndex++) {
                    TT2HFLTS est = estGroupedByAlternatives.get(expertIndex).get(altIndex).get(levelIndex);
                    levelEstimates.get(levelIndex).get(altIndex).set(expertIndex, est);
                }
            }
        }
        return levelEstimates;
    }

    private float[] getEqualWeights(int size){
        // weights are currently equal
        float[] weights = new float[size];
        for (int i = 0; i < size; i++) {
            weights[i] = 1f / size;
        }
        return weights;
    }
}
