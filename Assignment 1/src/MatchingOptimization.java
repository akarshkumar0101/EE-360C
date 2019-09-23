import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to optimize access times for the Matching problem.
 */
public class MatchingOptimization {

    private final Matching matching;

    /**
     * Given a student s, studentPreferenceIndices.get(s) will give a mapping from internship i to i's preference index from s's point of view.
     */
    private final List<Map<Integer, Integer>> studentsPreferenceIndices;

    /**
     * Given an internship x, internshipsPreferenceIndices.get(x) will give a mapping from student s to s's preference index from x's point of view.
     */
    private final List<Map<Integer, Integer>> internshipsPreferenceIndices;

    /**
     * Optimize given the matching
     * @param matching
     */
    public MatchingOptimization(Matching matching){
        this.matching = matching;

        studentsPreferenceIndices = new ArrayList<>(matching.getStudentCount());
        for(int student=0;student<matching.getStudentCount();student++){
            Map<Integer, Integer> preferenceIndices = new HashMap<>(matching.getInternshipCount());

            List<Integer> preferences = matching.getStudentPreference().get(student);
            for(int i=0;i<preferences.size();i++){
                preferenceIndices.put(preferences.get(i), i);
            }
            studentsPreferenceIndices.add(preferenceIndices);
        }
        internshipsPreferenceIndices = new ArrayList<>(matching.getInternshipCount());
        for(int internship=0;internship<matching.getInternshipCount();internship++){
            Map<Integer, Integer> preferenceIndices = new HashMap<>(matching.getStudentCount());

            List<Integer> preferences = matching.getInternshipPreference().get(internship);

            //preferences = Program1.computeInternshipPreferences(matching.getInternshipCount(),matching.getStudentCount(),matching.getInternshipWeights(),matching.getStudentGPA(),matching.getStudentMonths(),matching.getStudentProjects()).get(internship);


            for(int i=0;i<preferences.size();i++){
                preferenceIndices.put(preferences.get(i), i);
            }
            internshipsPreferenceIndices.add(preferenceIndices);
        }
//        System.out.println("STARTING");
//        System.out.println(matching.getStudentPreference());
//        System.out.println(studentsPreferenceIndices);
//        System.out.println(matching.getStudentPreference());
//        System.out.println(internshipsPreferenceIndices);
//
//        System.out.println("DONE\n\n\n");

    }

    /**
     * Access the student preference index for a student, internship in O(1) time
     * @param student
     * @param internship
     * @return
     */
    public int getStudentPreferenceIndex(int student, int internship){
        if(studentsPreferenceIndices.get(student).containsKey(internship)) {
            return studentsPreferenceIndices.get(student).get(internship);
        }
        else{
            return -1;
        }
    }

    /**
     * Access the internship preference index for a internship, student in O(1) time
     * @param internship
     * @param student
     * @return
     */
    public int getInternshipPreferenceIndex(int internship, int student){
        if(internshipsPreferenceIndices.get(internship).containsKey(student)) {
            return internshipsPreferenceIndices.get(internship).get(student);
        }
        else{
            return -1;
        }
    }
}
