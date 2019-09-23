/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.lang.reflect.Array;
import java.util.*;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Compute the preference lists for each internship, given weights and student metrics.
     * Return a ArrayList<ArrayList<Integer>> prefs, where prefs.get(i) is the ordered list of preferred students for
     * internship i, with length studentCount.
     */
    public static ArrayList<ArrayList<Integer>> computeInternshipPreferences(int internshipCount, int studentCount,
                                                                      ArrayList<ArrayList<Integer>>internship_weights,
                                                                      ArrayList<Double> student_GPA,
                                                                      ArrayList<Integer> student_months,
                                                                      ArrayList<Integer> student_projects){
        ArrayList<ArrayList<Integer>> internshipPref = new ArrayList<>(internshipCount);
        for(int internship=0;internship<internshipCount;internship++){
            ArrayList<Integer> preferredStudents = new ArrayList<>(studentCount);
            ArrayList<Double> studentToScore = new ArrayList<>(studentCount);
            for(int student = 0;student<studentCount;student++){
                ArrayList<Integer> iWeights = internship_weights.get(internship);
                studentToScore.add(computeInternshipStudentScore(student_GPA.get(student),student_months.get(student),student_projects.get(student),iWeights.get(0),iWeights.get(1), iWeights.get(2)));
                preferredStudents.add(student);
            }
            //now sort preferred students using studentToScore
            Collections.sort(preferredStudents, new Comparator<Integer>() {
                @Override
                public int compare(Integer s1, Integer s2) {
                    //return -1 if u want s1 first
                    if(studentToScore.get(s1)<studentToScore.get(s2)){//s2 is better
                        return 1;
                    }
                    else {
                        return -1;
                    }
                }
            });

            internshipPref.add(preferredStudents);
        }

        return internshipPref;
    }
    private static Double computeInternshipStudentScore(double studentGPA, int studentExp, int studentProjects, int
                                                        weightGPA, int weightExp, int weightProjects){
        return studentGPA*weightGPA+studentExp*weightExp+studentProjects*weightProjects;
    }

    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
        //System.out.println(marriage);

        int unemployedStudents = 0, emptyInternships = 0;

        ArrayList<Integer> internshipCountRemaining = new ArrayList<>(marriage.getInternshipSlots().size());
        for(int slots:marriage.getInternshipSlots()){
            internshipCountRemaining.add(slots);
        }
        // number of unemployed students
        for(int s =0;s<marriage.getStudentCount();s++){
            int internship = marriage.getStudentMatching().get(s);
            if(internship==-1){
                unemployedStudents++;
            }
            else{
                internshipCountRemaining.set(internship,internshipCountRemaining.get(internship)-1);
            }
        }
        // empty internships
        for(int remainingSlots: internshipCountRemaining){
            emptyInternships+=remainingSlots;
        }

        if(unemployedStudents>0 && emptyInternships>0){
            //that means unemployed students could have been hired
            return false;
        }

        MatchingOptimization matchingOptimization = new MatchingOptimization(marriage);
        for(int s1=0;s1<marriage.getStudentCount();s1++){
            for(int s2=0;s2<marriage.getStudentCount();s2++){
                //skip duplicates
                if(s1==s2){
                    continue;
                }
                //going through all students pairs (with order)
                int i1 = marriage.getStudentMatching().get(s1);
                int i2 = marriage.getStudentMatching().get(s2);

                //check if s1 cheat with i2 only

                //check if (s1 is jobless or prefers i2 over i1) AND (i2 prefers s1 over s2)

                if(i2==-1){
                    //then s2 is unemployed and s1 won't want s2's "internship"
                    continue;
                }

                if(matchingOptimization.getInternshipPreferenceIndex(i2, s1)< matchingOptimization.getInternshipPreferenceIndex(i2,s2)){
                    //i2 prefers s1 over s2;

                    if(i1==-1 || (matchingOptimization.getStudentPreferenceIndex(s1,i2)<matchingOptimization.getStudentPreferenceIndex(s1,i1))){
                        //s1 is jobless or s1 prefers i2 over i1

                        return false;
                    }
                }

            }
        }

        return true;
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_studentoptimal(Matching marriage) {

        MatchingOptimization optimization = new MatchingOptimization(marriage);

        Queue<Integer> studentQueue = new LinkedList<>();

        //studentToCurrentInternshipProposalIndex holds the index in preference list the student is at
        List<Integer> studentToCIPI = new ArrayList<>(marriage.getStudentCount());
        //the actual matching
        ArrayList<Integer> studentMatching = new ArrayList<Integer>(marriage.getStudentCount());
        for(int student = 0; student<marriage.getStudentCount();student++){
            //all students start with first preference (student optimal results from this)
            studentToCIPI.add(0);
            //no matches yet
            studentMatching.add(-1);
            //student needs to be in queue to be matched
            studentQueue.add(student);
        }

        //create a internship matching for all internships
        List<InternshipMatching> internshipMatchings = new ArrayList<>(marriage.getInternshipCount());
        for(int internship = 0; internship<marriage.getInternshipCount();internship++){
            InternshipMatching internshipMatching = new InternshipMatching(marriage, optimization, internship);
            internshipMatchings.add(internshipMatching);
        }

        //run while not all students are matched
        while(!studentQueue.isEmpty()){
            //get next student
            int student = studentQueue.remove();
            //current proposal index the student is at
            int startInternshipIndex = studentToCIPI.get(student);
            boolean matched = false;
            //go through list of preferences until an internship accepts student or runs out of internships
            for(int internshipIndex = startInternshipIndex;!matched;internshipIndex++){
                //try proposing to internship i

                //ran out of internships :(
                if(internshipIndex >= marriage.getStudentPreference().get(student).size()){
                    studentMatching.set(student, -1);
                    matched = true;
                }
                else {
                    //try applying to internship
                    int internship = marriage.getStudentPreference().get(student).get(internshipIndex);
                    boolean accepted = internshipMatchings.get(internship).canTakeStudent(student);
                    if (accepted) {
                        //was accepted so write match and put rejected student back in queue.
                        int rejectedStudent = internshipMatchings.get(internship).addStudent(student);
                        matched = true;
                        if (rejectedStudent != -1) {
                            studentQueue.add(rejectedStudent);
                        }
                        studentMatching.set(student, internship);
                    }
                }
            }
        }


        //set the matching to what we found
        marriage.setStudentMatching(studentMatching);

        return marriage;
    }

    private ArrayList<Matching> getAllStableMarriages(Matching marriage) {
        ArrayList<Matching> marriages = new ArrayList<>();
        int n = marriage.getStudentCount();
        int slots = marriage.totalInternshipSlots();

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
            if (isStableMatching(matching)) {
                marriages.add(matching);
            }
        }

        return marriages;
    }

    @Override
    public Matching stableMarriageBruteForce_studentoptimal(Matching marriage) {
        ArrayList<Matching> allStableMarriages = getAllStableMarriages(marriage);
        Matching studentOptimal = null;
        int n = marriage.getStudentCount();
        int m = marriage.getInternshipCount();
        System.out.println("student" + n + "internship" + m);
        ArrayList<ArrayList<Integer>> student_preference = marriage.getStudentPreference();

        //Construct an inverse list for constant access time
        ArrayList<ArrayList<Integer>> inverse_student_preference = new ArrayList<ArrayList<Integer>>(0) ;
        for (Integer i=0; i<n; i++) {
            ArrayList<Integer> inverse_preference_list = new ArrayList<Integer>(m) ;
            for (Integer j=0; j<m; j++)
                inverse_preference_list.add(-1) ;
            ArrayList<Integer> preference_list = student_preference.get(i) ;

            for (int j=0; j<m; j++) {
                inverse_preference_list.set(preference_list.get(j), j) ;
            }
            inverse_student_preference.add(inverse_preference_list) ;
        }

        // bestStudentMatching stores the rank of the best Internship each student matched to
        // over all stable matchings
        int[] bestStudentMatching = new int[marriage.getStudentCount()];
        Arrays.fill(bestStudentMatching, -1);
        for (Matching mar : allStableMarriages) {
            ArrayList<Integer> student_matching = mar.getStudentMatching();
            for (int i = 0; i < student_matching.size(); i++) {
                if (student_matching.get(i) != -1 && (bestStudentMatching[i] == -1 ||
                        inverse_student_preference.get(i).get(student_matching.get(i)) < bestStudentMatching[i])) {
                    bestStudentMatching[i] = inverse_student_preference.get(i).get(student_matching.get(i));
                    studentOptimal = mar;
                }
            }
        }

        return studentOptimal;
    }
}
