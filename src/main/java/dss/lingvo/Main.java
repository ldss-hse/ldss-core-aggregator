package dss.lingvo;

import dss.lingvo.samples.TT2HFLTSCoordinator;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {

    private Main(){}

    public static void main(String[] args) throws IOException {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

        String inputFilePath = cmd.getOptionValue("input");
        TT2HFLTSCoordinator complextT2HFLTSCoordinator = new TT2HFLTSCoordinator();
        complextT2HFLTSCoordinator.go(inputFilePath);
    }
}
