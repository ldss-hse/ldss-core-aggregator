package dss.lingvo.utils;

import dss.lingvo.t2.TTTuple;
import dss.lingvo.t2hflts.TT2HFLTS;
import dss.lingvo.utils.models.input.TTAbstractionLevelModel;
import dss.lingvo.utils.models.input.TTAlternativeModel;
import dss.lingvo.utils.models.input.TTCriteriaModel;
import dss.lingvo.utils.models.input.TTExpertModel;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TTReportUtils {
    public static String dumpAggregationByAbstraction(List<ArrayList<ArrayList<TT2HFLTS>>> allByLevel,
                                                      List<TTAbstractionLevelModel> levelsList,
                                                      List<TTAlternativeModel> altList,
                                                      List<TTExpertModel> expertsList,
                                                      int targetScaleSize) {
        StringBuilder bld = new StringBuilder();
        for (int expertIndex = 0; expertIndex < allByLevel.size(); expertIndex++) {
            String res = "\n\n" + expertsList.get(expertIndex).getExpertName() + "\n\n";
            for (int altIndex = 0; altIndex < altList.size(); altIndex++) {
                res += altList.get(altIndex).getAlternativeID() + " & ";
                for (int levelIndex = 0; levelIndex < levelsList.size(); levelIndex++) {
                    res += hfltsToTableValue(allByLevel.get(expertIndex).get(altIndex).get(levelIndex), targetScaleSize);
                    if (levelIndex == levelsList.size() - 1) {
                        res += "\\\\ \\hline \n";
                    } else {
                        res += " & ";
                    }
                }
            }
            bld.append(res);
        }
        return bld.toString();
    }

    private static String hfltsToTableValue(TT2HFLTS val, int targetScale) {
        StringBuilder bld = new StringBuilder();
        bld.append(" {");
        for (TTTuple i : val.getTerms()) {
            bld.append("\\(s_{" + i.getIndex() + "}^{" + targetScale + "}\\)");
        }
        bld.append("} ");
        return bld.toString();
    }

    public static String dumpTransposeByExpert(List<ArrayList<ArrayList<TT2HFLTS>>> allByExpert,
                                               List<TTAbstractionLevelModel> levelsList,
                                               List<TTAlternativeModel> altList,
                                               List<TTExpertModel> expertsList,
                                               int targetScaleSize) {
        StringBuilder bld = new StringBuilder();
        for (int levelIndex = 0; levelIndex < allByExpert.size(); levelIndex++) {
            String res = "\n\n" + levelsList.get(levelIndex).getAbstractionLevelID() + "\n\n";
            for (int altIndex = 0; altIndex < altList.size(); altIndex++) {
                res += altList.get(altIndex).getAlternativeID() + " & ";
                for (int expertIndex = 0; expertIndex < expertsList.size(); expertIndex++) {
                    res += hfltsToTableValue(allByExpert.get(levelIndex).get(altIndex).get(expertIndex), targetScaleSize);
                    if (expertIndex == expertsList.size() - 1) {
                        res += "\\\\ \\hline \n";
                    } else {
                        res += " & ";
                    }
                }
            }
            bld.append(res);
        }
        return bld.toString();
    }

    public static String dumpAggregationByAltToLevel(List<ArrayList<TT2HFLTS>> altToLevel,
                                                     List<TTAbstractionLevelModel> level,
                                                     List<TTAlternativeModel> altList,
                                                     int targetScaleSize) {

        StringBuilder bld = new StringBuilder();
        for (int altIndex = 0; altIndex < altList.size(); altIndex++) {
            bld.append(altList.get(altIndex).getAlternativeID() + " & ");
            for (int levelIndex = 0; levelIndex < level.size(); levelIndex++) {
                bld.append(hfltsToTableValue(altToLevel.get(altIndex).get(levelIndex), targetScaleSize));
                if (levelIndex == level.size() - 1) {
                    bld.append("\\\\ \\hline \n");
                } else {
                    bld.append(" & ");
                }
            }
        }
        return bld.toString();
    }

    public static String dumpFinalVector(List<TT2HFLTS> originalVec,
                                         List<TTAlternativeModel> altList,
                                         int targetScaleSize) {
        StringBuilder bld = new StringBuilder();
        for (int sortedIndex = 0; sortedIndex < originalVec.size(); sortedIndex++) {
            bld.append(altList.get(sortedIndex).getAlternativeID() + " & ");
            bld.append(hfltsToTableValue(originalVec.get(sortedIndex), targetScaleSize));
            bld.append("\\\\ \\hline \n");
        }
        return bld.toString();
    }

    public static String dumpFinalSortedZippedVector(List<Pair<String, TT2HFLTS>> resZippedVec,
                                                     List<TTAlternativeModel> altList) {
        StringBuilder bld = new StringBuilder();
        for (Pair<String, TT2HFLTS> stringTT2HFLTSPair : resZippedVec) {
            TTAlternativeModel altInstance = altList
                    .stream()
                    .filter((TTAlternativeModel ttAlternativeModel) -> ttAlternativeModel.getAlternativeID()
                            .equals(stringTT2HFLTSPair.getKey()))
                    .findFirst()
                    .orElse(null);
            bld.append(stringTT2HFLTSPair.getKey()+" & ");
            bld.append(altInstance.getAlternativeName());
            bld.append("\\\\ \\hline \n");
        }
        return bld.toString();
    }
}
