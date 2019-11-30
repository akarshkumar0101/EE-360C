import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to generate a test case for the matching problem.
 * Pass in the name of the file you need to create to args[0]
 */
public class GeneratingTestCase {
    public static final int NUM_STUDENTS = 6;
    public static final int NUM_INTERNSHIPS = 3;

    public static void main(String... args) throws IOException {
        File file = new File(args[0]);
        file.delete();
        file.createNewFile();

        PrintWriter pw = new PrintWriter(file);

        int n = about(NUM_STUDENTS);
        int m = about(NUM_INTERNSHIPS);
        pw.println(m+" "+n);

        for(int internship = 0; internship<m;internship++){
            int slots = about(NUM_STUDENTS/NUM_INTERNSHIPS);
            pw.print(slots);
            if(internship<m-1){
                pw.print(" ");
            }
        }
        pw.println();
        for(int stu =0; stu<n;stu++){
            pw.printf("%.2f",4*Math.random());
            if(stu<n-1){
                pw.print(" ");
            }
        }
        pw.println();
        for(int stu =0; stu<n;stu++){
            pw.print((int)(10*Math.random()));
            if(stu<n-1){
                pw.print(" ");
            }
        }
        pw.println();
        for(int stu =0; stu<n;stu++){
            pw.print((int)(10*Math.random()));
            if(stu<n-1){
                pw.print(" ");
            }
        }
        pw.println();
        for(int internship = 0; internship<m;internship++){
            int first = (int) (Math.random()*50);
            int second = (int) (Math.random()*50);
            int third = 100-first-second;
            pw.println(first+" "+second+" "+third);
        }


        for(int stu =0; stu<n;stu++){
            List<Integer> internships = new ArrayList<>(m);
            for(int i=0;i<m;i++){
                internships.add(i);
            }
            shuffle(internships);

            for(int i=0;i<m;i++){
                pw.print(internships.get(i));
                if(i<m-1){
                    pw.print(" ");
                }
            }
            pw.println();
        }




        pw.close();
    }
    public static void shuffle(List<Integer> list){
        for(int i=0;i<list.size();i++){
            int i2 = (int) (Math.random()*list.size());
            int temp = list.get(i);
            list.set(i,list.get(i2));
            list.set(i2,temp);
        }
    }

    public static int about(int num){
        int offset = (int) ((Math.random()-0.5)*num*0.20);

        return num+offset;
    }


}
