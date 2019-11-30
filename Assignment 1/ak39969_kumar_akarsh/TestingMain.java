import java.io.File;
import java.util.*;

/**
 * This class tests all the test cases in the directory given by args[0].
 * It tests against the brute force algorithm and my Gale-Shapley Algorithm.
 */
public class TestingMain {
    public static boolean testing = true;

    public static void main(String... args) throws Exception{
        String[] flags = new String[]{"-b","-g"};

        File directory = new File(args[0]);

        File[] files = directory.listFiles();
        for(File file:files){
            System.err.println("Running "+file+"...");

            for(String flag: flags){
                System.err.println("Running Flag "+flag+"...");
                Driver.parseArgs(new String[]{flag,file.toString()});
                Matching problem = Driver.parseMatchingProblem(Driver.filename);
                Driver.testRun(problem);
            }

        }

    }
}
