package dss.lingvo;

import dss.lingvo.t2hflts.TT2HFLTSCoordinator;

import java.io.IOException;

public class Main {

    private Main(){}

    public static void main(String[] args) throws IOException {
        TT2HFLTSCoordinator complextT2HFLTSCoordinator = new TT2HFLTSCoordinator();
        complextT2HFLTSCoordinator.go();
    }
}
