package dss.lingvo;

import dss.lingvo.hflts.TTHFLTSCoordinator;
import dss.lingvo.t2hflts.TT2HFLTSCoordinator;

public class Main {

    public static void main(String[] args) {
//        TTHFLTSCoordinator simpleHFLTSCoordinator = new TTHFLTSCoordinator();
//        simpleHFLTSCoordinator.go();

        TT2HFLTSCoordinator complextT2HFLTSCoordinator = new TT2HFLTSCoordinator();
        complextT2HFLTSCoordinator.go();
    }
}
