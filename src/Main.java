import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    static {
        System.setProperty("log4j.configurationFile",
                "logger-config.xml");
    }

    public static final Params params = Params.getInstance();

    public static void main(String[] args) {

        List<String> paramsValues = Parser.parseArguments(args);
        params.setAllParams(paramsValues);

        System.out.println("Input parameters:");
        System.out.println(params);

        Mine mine = new Mine();

        mine.startSimulation();

        mine.waitUntilAllThreadsFinished();

        System.out.println();
        System.out.println("Mined sources by worker:");
        System.out.println(mine.getWorkersWorkUnitCountString());
        System.out.println();

        System.out.println("Imported mine sources to finish: " + mine.getWorkUnitImportedCount());

        copyFile(params.getOutputFilePath());
    }

    /**
     * Copy file created by logger to output file
     * @param targetPath output file path
     */
    private static void copyFile(String targetPath){
        String sourcePath = "temp.txt";

        try {
            Scanner scanner = new Scanner(new File(sourcePath));
            PrintWriter writer = new PrintWriter(targetPath);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                writer.println(line);
            }

            scanner.close();
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }




}