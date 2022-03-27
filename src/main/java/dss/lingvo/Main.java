package dss.lingvo;

import dss.lingvo.samples.TT2HFLTSCoordinator;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        Option input2 = new Option("o", "output-dir", true,
                "output directory containing decision making artifacts");
        input2.setRequired(true);

        options.addOption(input);
        options.addOption(input2);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String inputFilePathRaw = cmd.getOptionValue("input");
        String outputDirectoryPathRaw = cmd.getOptionValue("output-dir");

        File inputFile  = new File(inputFilePathRaw);
        if (!inputFile.exists()) {
            System.out.println("Specified file with task description does not exist. Specify a real description");
            System.exit(1);
        }

        File outputDirectory = new File(outputDirectoryPathRaw);

        if (!outputDirectory.exists()) {
            System.out.println("Specified output directory does not exist. Need to create it first");
            Files.createDirectories(outputDirectory.toPath());
        }


        if (outputDirectory.exists() && !outputDirectory.isDirectory()) {
            System.out.println("The path provided as an output directory is not a directory. Specify a real folder.");
            System.exit(1);
            return;
        }

        Path outputFilePath = Paths.get(outputDirectory.toString(), "out.log");

        PrintStream out = new PrintStream(outputFilePath.toString(), "UTF-8");
        System.setOut(out);

        TT2HFLTSCoordinator complexT2HFLTSCoordinator = new TT2HFLTSCoordinator();
        complexT2HFLTSCoordinator.go(inputFile, outputDirectory);
    }
}
