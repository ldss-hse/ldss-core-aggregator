package dss.lingvo.hflts;

import dss.lingvo.utils.TTUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class TTHFLTSCoordinator {
    private TTUtils log = TTUtils.getInstance();

    private ArrayList<TTHFLTSAlternativeEstimation> rawEstimates;

    public TTHFLTSCoordinator(){
        rawEstimates = new ArrayList<TTHFLTSAlternativeEstimation>();
    }

    public void go() {
        log.info("Step 1. Gather feedback and parse to HFLTS...");
        // prepare mock matrix
        String [] x1c1 = {"vl","l", "m"};
        TTHFLTS x1c1H = new TTHFLTS(x1c1);

        String [] x1c2 = {"h", "vh"};
        TTHFLTS x1c2H = new TTHFLTS(x1c2);

        String [] x1c3 = {"h"};
        TTHFLTS x1c3H = new TTHFLTS(x1c3);

        TTHFLTS[] x1H = {x1c1H, x1c2H, x1c3H};
        rawEstimates.add(new TTHFLTSAlternativeEstimation(x1H));

        String [] x2c1 = {"l", "m"};
        TTHFLTS x2c1H = new TTHFLTS(x2c1);

        String [] x2c2 = {"m"};
        TTHFLTS x2c2H = new TTHFLTS(x2c2);

        String [] x2c3 = {"n", "vl", "l"};
        TTHFLTS x2c3H = new TTHFLTS(x2c3);

        TTHFLTS[] x2H = {x2c1H, x2c2H, x2c3H};
        rawEstimates.add(new TTHFLTSAlternativeEstimation(x2H));

        String [] x3c1 = {"h", "vh", "p"};
        TTHFLTS x3c1H = new TTHFLTS(x3c1);

        String [] x3c2 = {"vl", "l"};
        TTHFLTS x3c2H = new TTHFLTS(x3c2);

        String [] x3c3 = {"h", "vh", "p"};
        TTHFLTS x3c3H = new TTHFLTS(x3c3);

        TTHFLTS[] x3H = {x3c1H, x3c2H, x3c3H};
        rawEstimates.add(new TTHFLTSAlternativeEstimation(x3H));

        log.info(rawEstimates);

        log.info("Step 1. Done.");
        log.info("Step 2. Aggregate HFLTS for each alternative...");
        log.info("Step 2. Done.");
        log.info("Step 3. Find Pareto frontier...");
        log.info("Step 3. Done.");
    }
}
