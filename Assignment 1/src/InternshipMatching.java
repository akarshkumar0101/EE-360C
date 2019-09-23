import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * This class holds the current students that are matched to a specific internship.
 * Normally, you would hold these students in a list and find the worst student to replace given a new student.
 * But this implementation uses a specialized min heap prioritizing least preferred students.
 * This allows for finding the worst student in O(1) time instead of O(n) time where n is the
 * number of slots in this internship.
 */
public class InternshipMatching {

    /**
     * The queue of students
     */
    private final PriorityQueue<Integer> queue;

    private final Matching matching;
    private final MatchingOptimization optimization;
    private final int internship;
    private final int numSlots;

    /**
     * The specialized comparator that optimizes for least preferred students
     */
    private final Comparator<Integer> comparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer s1, Integer s2) {
            //we want least valuable student to go first
            //-1 for s1 first, 1 for s2 first
            if(s1==-1){
                return -1;
            }
            if(s2==-1){
                return 1;
            }

            //now give the student with the least value
            if(prefers(s1,s2)){
                return 1;
            }
            else{
                return -1;
            }
        }
    };
    /**
     *
     * @param s1
     * @param s2
     * @return true if this internship prefers s1 over s2
     */
    private boolean prefers(int s1, int s2){
        int i1 = optimization.getInternshipPreferenceIndex(internship,s1);
        int i2 = optimization.getInternshipPreferenceIndex(internship,s2);

        return i1<i2;
    }

    public InternshipMatching(Matching matching, MatchingOptimization optimization, int internship){
        this.queue = new PriorityQueue<>(comparator);

        this.matching = matching;
        this.optimization = optimization;
        this.internship = internship;

        this.numSlots = matching.getInternshipSlots().get(internship);
    }

    /**
     * Determines if this internship has room for the given student or will take in the student because
     * they are better than a current student in the internship.
     * @param student
     * @return
     */
    public boolean canTakeStudent(int student){
        if(queue.size()<numSlots){
            return true;
        }
        else{
            int worstStudent = queue.peek();
            return prefers(student,worstStudent);
        }
    }

    /**
     * Adds the student to this internship and returns a rejected student that was kicked out of this
     * internship.
     * @param student
     * @return
     */
    public int addStudent(int student){
        if(queue.size()<numSlots){
            queue.add(student);
            return -1;
        }
        else{
            int worstStudent = queue.poll();
            queue.add(student);
            return worstStudent;
        }
    }

}
