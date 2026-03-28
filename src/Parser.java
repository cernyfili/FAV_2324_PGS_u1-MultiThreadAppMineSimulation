import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 The Parser class is responsible for parsing the input arguments and the work blocks from the file.
 It contains static methods for both tasks.
 */
public class Parser {
    private static final int ARGSC = 14;

    private static final String[] ARG_FORMAT = new String[] {"i", "o", "cWorker", "tWorker",
            "capLorry", "tLorry", "capFerry"};

    private static final char ARG_NAMEPREFIX = '-';

    private static final char WORK_SOURCE_CHAR = 'X';

    /**
     Parses the input arguments and returns a list with their corresponding values.
     The input arguments must be in the format
     "-i filepath -o filepath -cWorker int -tWorker int -capLorry int -tLorry int -capFerry int"
     @param args an array of String objects representing the input arguments.
     @return a List of String objects with the values of the input arguments.
     */
    public static List<String> parseArguments(String[] args) {
        if(args.length != ARGSC){
            MyLogger.error("Wrong number of arguments");
            System.exit(0);
        }

        List<String> paramsValues = new ArrayList<>();

        for (int i = 0; i < args.length - 1; i+=2) {
            if(!args[i].equals(ARG_NAMEPREFIX + ARG_FORMAT[i/2])){
                MyLogger.error("Wrong arguments format");
                System.exit(0);
            }

            paramsValues.add(args[i+1]);
        }

        return paramsValues;
    }

    /**
     Parses the work blocks from the input file and returns a list with their corresponding WorkBlock objects.
     The input file must contain rows of characters that represent the work sources, marked by the character 'X'.
     The WorkBlock objects represent the consecutive occurrences of work sources in a row.
     @param file a File object representing the input file.
     @return a List of WorkBlock objects representing the work blocks in the input file.
     @throws FileNotFoundException if the input file is not found.
     */
    public static List<WorkBlock> parseWorkBlocks(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        List<WorkBlock> foundWorkBlocks = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();

            int blockCounter = 0;

            for (char curChar :
                    currentLine.toCharArray()) {
                if (curChar == WORK_SOURCE_CHAR) blockCounter++;
                else if(blockCounter != 0){
                    foundWorkBlocks.add(new WorkBlock(blockCounter));

                    blockCounter = 0;
                }
            }
            if(blockCounter != 0){

                foundWorkBlocks.add(new WorkBlock(blockCounter));
            }
        }

        scanner.close();


        return foundWorkBlocks;
    }

}
