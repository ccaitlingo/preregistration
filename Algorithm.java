import java.util.*;

/**
 * Algorithm for enrolling students in classes.
 */
public class Algorithm
{
    HashMap<Integer, PriorityQueue<Student>> pqHash;
    HashMap<Integer, Stack<Student>> sHash;
    HashMap<Course, Integer> enrollment;

    /**
     * Constuctor for algorithm
     * @param mapStudents List of all students who should be enrolled in
     * courses
     * @param enrollment Stores the number of students enrolled in each 
     * course
     */
    public Algorithm(List<Student> mapStudents,  HashMap<Course,Integer> enrollment){
        pqHash = new HashMap<>();
        sHash = new HashMap<>();
        this.enrollment = enrollment;
        fillpqHash(mapStudents);
        makesHash();
    }
    
    /**
     * Fills the hashmap containing the data structure used for the
     * forward direction for enrollment, a priority queue.
     * @param The list of students who need to be enrolled
     */
    public void fillpqHash(List<Student> mapStudents){
        for(Student student : mapStudents){
            int year = student.gradYear;
            if(!pqHash.containsKey(year)){
                //If not in directory already, make a new entry
                PriorityQueue<Student> stuqueue = new PriorityQueue<Student>();
                stuqueue.add(student);
                pqHash.put(year, stuqueue);
            }
            else{
                //If in directory, add to existing
                PriorityQueue<Student> queueAtYear = pqHash.get(year);
                queueAtYear.add(student);
                pqHash.put(year, queueAtYear);
            }
        }
    }
    
    /**
     * Sets up the hashmap containing the data structure used for
     * the reverse direction of the enrollment, a stack.
     */
    public void makesHash(){
        List<Integer> keys = new ArrayList<Integer>();
        keys.addAll(pqHash.keySet());
        Collections.sort(keys);
        for(int key : keys){
            sHash.put(key, new Stack<Student>());
        }
    }
    
    /** 
     * Description of the algorithm from the Registrar's website:
     * 
     * Entry into a section is determined by the combination of your class year, the priority you give each section, and your draw number.
     * Seniors’ requests are processed first followed sequentially by juniors’, sophomores’, and first-years requests.
     * Your requests are considered in the order that you list them on the registration screen, with the first item having the highest priority. If one of your requests cannot be filled, then the next item in your list will be considered instead.
     * For your class year, your draw number determines when one of your requests is considered. Your top request is considered immediately after the top requests of all of the students in your class with lower draw numbers. As mentioned above, if your top request cannot be granted you will be enrolled in the first request on your list that can be.
     * 
     * In a second pass through the requests from your class, your top request among your remaining requests will be considered immediately before all of the students in your class with lower draw numbers. That is, the draw numbers work in reverse compared to the first pass. The remaining passes through the requests from your class continue the pattern of the first two passes, switching direction through the draw numbers on each pass.
     * You may list multiple sections of the same course among your requests but you will be enrolled only in the first one on your list that is available. You will not be enrolled in multiple sections of the same course.
     * You may also list sections of different courses that meet at the same time but you will be enrolled only in the first one on your list that is available. You will not be enrolled in sections with time conflicts.
     * 
     */    
    public void run(){
        for(int i = 0; i < 7; i++){
            if(i%2 == 0){
                goForward();
            }else{
                goBackward();
            }
        }
    }
    
    /**
     * Goes through the students in order, adds one course to their
     * schedule if possible, then adds them to the hashMap that will
     * continue the enrollment process in reverse order.
     */
    public void goForward(){
        for(int classOf : pqHash.keySet()){
            PriorityQueue<Student> queueAtYear = pqHash.get(classOf);
            while(!queueAtYear.isEmpty()){
                //Get student
                Student student = queueAtYear.poll();
                addCourse(student);
                //Add the student to the other data structure
                Stack<Student> stackAtYear = sHash.get(classOf);
                stackAtYear.push(student);
                sHash.put(classOf, stackAtYear);
            }
        }
    }
    
    /**
     * Goes through the students in reverse order, adds one course to 
     * their schedule if possible, then adds them to the hashMap that 
     * will continue the enrollment process in forward order.
     */
    public void goBackward(){
        for(int classOf : sHash.keySet()){
            Stack<Student> stackAtYear = sHash.get(classOf);
            while(!stackAtYear.isEmpty()){
                Student student = stackAtYear.pop();
                addCourse(student);
                PriorityQueue<Student> queueAtYear = pqHash.get(classOf);
                queueAtYear.offer(student);
                pqHash.put(classOf, queueAtYear);
            }
        }
    }
    
    /**
     * Calls a helper method to get a course to add, then adds it.
     * If there are no courses to add, returns false.
     * @param student The student whose schedule we want to add a
     * course to
     */
    public void addCourse(Student student){
        Course courseToAdd = findCourseToAdd(student);
        if(courseToAdd != null){
            //Add course
            student.schedule.add(courseToAdd);
            //Update enrollment
            int currentEnroll = enrollment.get(courseToAdd);
            enrollment.put(courseToAdd, ++currentEnroll);
        }
    }
    
    /**
     * Finds the first available course to add.
     * If there are no courses that can be added, returns false.
     * @param student Same as above.
     * @return Course The first course that works to add to the
     * student's schedule. Returns null if none are available
     */
    public Course findCourseToAdd(Student student){
        List<Course> requests = student.requests;
        for(Course course : requests){
            if(checkValidAdd(student, course)){
                return requests.remove(requests.indexOf(course));
            }
        }
        return null;
    }
    
    /**
     * Checks to see if the course can be added (doesn't conflict
     * with student's schedule, etc.)
     * @param student The student.
     * @param maybe The course that is being considered
     * @return Whether the course is valid to be added
     */
    public boolean checkValidAdd(Student student, Course maybe){
        if(isThereSpace(maybe) && !overCredits(student, maybe) && !inDifSection(student, maybe) && !student.hasAConflict(maybe)){
            return true;
        }
        return false;
    }
    
    /**
     * @param course The course.
     * @return Whether there is space in the chosen course.
     */
    public boolean isThereSpace(Course course){
        int currentEnroll = enrollment.get(course);
        int maxEnroll = course.maxEnrollment;
        return currentEnroll < maxEnroll;
    }
    
    /**
     * @param student The student.
     * @param maybe The course being considered
     * @return Whether the student is already enrolled in a different
     * section
     */
    public boolean inDifSection(Student student, Course maybe){
        for(Course course : student.schedule){
            //If same course
            if(course.dept.equals(maybe.dept) && course.courseNum == maybe.courseNum){
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param student
     * @param maybe The course being considered
     * @return Whether adding the course will put the student over their
     * max amount of credits
     */
    public boolean overCredits(Student student, Course maybe){
        int credits = 0;
        for(Course course : student.schedule){
            credits += course.credits;
        }
        return credits + maybe.credits > 4.5;
    }
    
    /**
      * Print the toString of the student, 
      * followed by their schedule (using course toString).
      * 
      * Hector Tran 2023 1
      * CMPU-145-51 Foundations/Computer Science    1.0    TRF 1200PM-0115PM
      * EDUC-361-51 Sem: Math/Science/Elem Classrm    1.0    R 0310PM-0610PM
      * ECON-235-51 Sports Economics    1.0    TR 1030AM-1145AM
      * PHED-105-51 Foundations of Wellness    0.5    TR 0900AM-1015AM
      * --------------------
      * Chace Sanford 2023 2
      * GNCS-355-51 Childhood/Childrn 19C Britain    1.0    R 0310PM-0510PM
      * ART-318-51 Building the Museum    1.0    T 0100PM-0300PM
      * CHEM-352-51 Phys Chem-Molec Structr    1.0    MW 1030AM-1145AM
      * INTL-109-51 A Lexicon of Forced Migration    1.0    TR 1030AM-1145AM
      * --------------------
      * etc...
      */
    public void printEnrollment(){
        System.out.println();
        for(int key : sHash.keySet()){
            for(Student student : sHash.get(key)){
                System.out.println(student);
                for(Course course : student.schedule){
                    System.out.println(course);
                }
                System.out.println("--------------------");
            } 
        }
    }
}
