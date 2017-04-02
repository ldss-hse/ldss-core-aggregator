package dss.lingvo.t2hflts;

import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTUtils;

import java.util.*;

public class TT2HFLTSMHTWAOperator {

    public TT2HFLTS calculate(List<TT2HFLTS> sets, float[] weights, int targetScaleSize) {
        TT2HFLTSMTWAOperator mtwaOperator = new TT2HFLTSMTWAOperator();

        ArrayList<TTTuple> input = new ArrayList<>();
        for (int i = 0; i < sets.size(); ++i) {
            for (int index = 0; index < sets.get(i).getTerms().size(); index++) {
                input.add(sets.get(i).getTerms().get(index));
            }
        }

        int[] sizes = new int[sets.size()];
        for (int i = 0; i < sets.size(); ++i){
            sizes[i] = sets.get(i).getSize();
        }

        List<int[]> subsets = getAllPossibleCombinations(sets.size(), input.size());

        ArrayList<ArrayList<TTTuple>> actualSubsets = eliminateImpossibleCombinations(subsets, sizes, input);

        ArrayList<TT2HFLTS> t2HFLTSstore = new ArrayList<>();
        for (ArrayList<TTTuple> est : actualSubsets) {
            t2HFLTSstore.add(new TT2HFLTS(est));
        }

        ArrayList<TTTuple> res = new ArrayList<>();
        for (TT2HFLTS el : t2HFLTSstore) {
            res.add(mtwaOperator.calculate(el.getTerms(), weights, targetScaleSize));
        }
        List<TTTuple> sortedRes = TTUtils.sortTuples(res, false);
        TT2HFLTS sortedHFTLS = new TT2HFLTS(sortedRes);
        return removeDuplicates(sortedHFTLS);
    }

    /**
     * To get all possible combination of indexes
     * @param k  - sequence length
     * @param inputSize
     * @return
     */
    private List<int[]> getAllPossibleCombinations(int k, int inputSize){
        List<int[]> subsets = new ArrayList<>();

        int[] s = new int[k];                  // here we'll keep indices
        // pointing to elements in input array

        if (k <= inputSize) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; i < k - 1; i++){
                s[i] = i;
            }
            int[] tmpArr = new int[k];
            System.arraycopy(s, 0, tmpArr, 0, s.length);
            subsets.add(tmpArr);
            for (; ; ) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == inputSize - k + i; ){
                    i--;
                }
                if (i < 0) {
                    break;
                } else {
                    s[i]++;                    // increment this item
                    for (++i; i < k; i++) {    // fill up remaining items
                        s[i] = s[i - 1] + 1;
                    }
                    int[] tmpArr2 = new int[k];
                    System.arraycopy(s, 0, tmpArr2, 0, s.length);
                    subsets.add(tmpArr2);
                }
            }
        }
        return subsets;
    }


    /**
     * after we have got all the combinations, we need to sort out wrong
     * because some of combinations are from one set
     * @param subsets - combinations of indexes
     * @param sizes - sizes of TT2HFLTS to get the shift in indexes
     * @param input - all ttuples listeed in one vector
     * @return
     */
    private ArrayList<ArrayList<TTTuple>> eliminateImpossibleCombinations(List<int[]> subsets,
                                                                          int[] sizes,
                                                                          ArrayList<TTTuple> input){
        ArrayList<ArrayList<TTTuple>> actualSubsets = new ArrayList<>();
        for(int [] subset : subsets){
            boolean isOK = true;
            for (int positionInSubset = 0; positionInSubset < subset.length; ++positionInSubset){
                int elIndex = subset[positionInSubset];
                int shiftInInput = 0;
                for (int i = 0; i < sizes.length; i++){
                    if (i == positionInSubset){
                        break;
                    }
                    shiftInInput += sizes[i];
                }
                if ((elIndex-shiftInInput) > sizes[positionInSubset]-1 || (elIndex-shiftInInput) < 0){
                    isOK = false;
                    break;
                }

            }
            if (isOK){
                ArrayList<TTTuple> listToAdd = new ArrayList<>();
                for (int i = 0; i < subset.length; ++i){
                    listToAdd.add(input.get(subset[i]));
                }
                actualSubsets.add(listToAdd);
            }
        }
        return actualSubsets;
    }

    private TT2HFLTS removeDuplicates(TT2HFLTS set){
        List<TTTuple> newList = new ArrayList<>();
        for (TTTuple el : set.getTerms()){
            TTTuple existing = newList.stream()
                    .filter(x -> x.equals(el))
                    .findFirst()
                    .orElse(null);
            if (existing == null){
                newList.add(el);
            }
        }
        return new TT2HFLTS(newList);
    }
}
