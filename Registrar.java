import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The registrar loads students, courses, and the student's requests.
 * Sets up the enrollment number for each course (begins at 0), runs the
 * algorithm, and prints the results.
 */
public class Registrar {
    HashMap<Course,Integer> enrollment = new HashMap<Course,Integer>();

    /**
     * Constructor loads all files, and runs the algorithm for preregistration and 
     * prints the results.
     */
    public Registrar(){
        StudentLoader sl = new StudentLoader("../data/shortRoster.csv");
        List<Student> students = sl.getStudents();
        
        CourseLoader cl = new CourseLoader("../data/course_shortRequests.csv");
        List<Course> courses = cl.getCourses();
                
        RequestLoader rl = new RequestLoader("../data/shortRequests.csv", students, courses);
        students = rl.mapStudentRequests();
        
        for(Course c : courses){
            enrollment.put(c,0); //The number of students currently enrolled in each Course.
        }

        //The fully loaded students, associated with all of their requests, and the list of courses
        //initialized to 0 students enrolled.
        Algorithm a = new Algorithm(students,enrollment);
        a.run();
        a.printEnrollment();
    }

}
