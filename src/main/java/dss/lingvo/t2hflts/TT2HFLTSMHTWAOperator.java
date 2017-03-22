package dss.lingvo.t2hflts;

import dss.lingvo.t2.TTTuple;
import dss.lingvo.utils.TTUtils;

import java.util.*;

public class TT2HFLTSMHTWAOperator {

    public TT2HFLTS calculate(List<TT2HFLTS> sets, float[] weights, int targetScaleSize) {
        TT2HFLTSMTWAOperator mtwaOperator = new TT2HFLTSMTWAOperator();

        int expectedQuantity = (int) Arrays
                .stream(sets.toArray())
                .reduce(1, (el, partial) -> (int) el * ((TT2HFLTS) partial).getSize());

        ArrayList<ArrayList<TTTuple>> store = new ArrayList<>();
        for (int i = 0; i < expectedQuantity; i++) {
            store.add(new ArrayList<>());
        }

        for (TT2HFLTS set : sets) {
            int shift = 0;
            for (int i = 0; i < expectedQuantity; i++) {
                int actualIndex = i % set.getSize() + shift;
                store.get(i).add(set.getTerms().get(actualIndex));
            }
            shift++;
        }
//
//        int [] depth = {2, 2, 2};
//        int shift = 0;
//        for (TT2HFLTS set : sets) {
//            // iterate through the list 1st level, now the element is set
//
//            for (int i = 0; i < expectedQuantity; i++) {
//                int actualIndex = i % set.getSize()+shift;
////                if (actualIndex == set.getTerms().size()-1){
////                    actualIndex = 0;
////                }
//                store.get(i).add(set.getTerms().get(actualIndex));
//            }
//            shift++;
//        }

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

//        int[] input = {10, 20, 30, 40, 50};    // input array
        int k = sets.size();                             // sequence length

        List<int[]> subsets = new ArrayList<>();

        int[] s = new int[k];                  // here we'll keep indices
        // pointing to elements in input array

        if (k <= input.size()) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++) ;
            int[] tmpArr = new int[k];
            System.arraycopy(s, 0, tmpArr, 0, s.length);
            subsets.add(tmpArr);
            for (; ; ) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == input.size() - k + i; i--) ;
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

        System.out.println(subsets);

        // after we have got all the combinations, we need to sort out wrong
        // because some of combinations are from one set

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


        ArrayList<TT2HFLTS> t2HFLTSstore = new ArrayList<>();
        for (ArrayList<TTTuple> est : actualSubsets) {
            t2HFLTSstore.add(new TT2HFLTS(est));
        }

        ArrayList<TTTuple> res = new ArrayList<>();
        for (TT2HFLTS el : t2HFLTSstore) {
            res.add(mtwaOperator.calculate(el.getTerms(), weights, targetScaleSize));
        }
        List<TTTuple> sortedRes = TTUtils.sortTuples(res, false);
        return new TT2HFLTS(sortedRes);
    }
}
