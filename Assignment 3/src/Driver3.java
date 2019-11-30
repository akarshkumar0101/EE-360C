import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver3 {

    public static int[] cities;

    public static void main (String[] args) throws FileNotFoundException {
        parseArgs(args);
        testRun(cities);
    }

    public static void parseArgs (String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(args[0]));

        // the input should only be one continuous line of values, separated by spaces
        String[] line = sc.nextLine().split(" ");
        cities = new int[line.length];
        sc.close();

        for (int i = 0; i < line.length; i++) {
            cities[i] = Integer.parseInt(line[i]);
        }

    }

    public static void testRun (int[] cities) {
        Program3 pa3 = new Program3();

        long start = System.currentTimeMillis();
        int opt = pa3.maxCoinValue(cities);
        long end = System.currentTimeMillis();

        System.out.println("Optimal amount of coins: " + opt);
        System.out.println("Completed in " + (end - start) + " milliseconds");
    }
}
