import java.util.List;
import java.util.ArrayList;

/**
 * CourseLoader takes info on a course from a single String line, and
 * parses it to create a Course object.
 *
 * @author CG
 * @version 5.3.2023
 */
public class CourseLoader extends DataLoader
{
    private List<Course> courses = new ArrayList<Course>();
    /**
     * Empty constructor for testing.
     */
    CourseLoader(){}
    
    /**
     * Constructor calls the load(file) method in abstract parent class.
     * 
     * @param file: the path to the file.
     */
    CourseLoader(String file){
        load(file);
    }

    /**
     * Parse a single line of CSV in the form:
     * dept, courseNum, section, name, credits, maxEnrol, days, startTime, EndTime, Duration, String time, room, professor
     * CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna
     * 
     * Method should create a new Course Object and add it to 
     * the students instance variable.
     * 
     * @param data: a single line from the csv file.
     */
    public void parseAndLoadLine(String data){
        String[] tokens = data.split(",");
        String dept;
        int courseNum;
        int section;
        String title;
        double credits;
        int maxEnrollment;
        String daysOfTheWeek;
        int startTime;
        int endTime;
        int duration;
        String timeString;
        String loc;
        String instructor;
        Course course;
        try{
            dept = tokens[0];
            courseNum = Integer.parseInt(tokens[1]);
            section = Integer.parseInt(tokens[2]);
            title = tokens[3];
            credits = Double.parseDouble(tokens[4]);
            maxEnrollment = Integer.parseInt(tokens[5]);
            daysOfTheWeek = tokens[6];
            startTime = Integer.parseInt(tokens[7]);
            endTime = Integer.parseInt(tokens[8]);
            duration = Integer.parseInt(tokens[9]);
            timeString = tokens[10];
            loc = tokens[11];
            instructor = tokens[12];
            course = new Course(dept, courseNum, section, title, credits, maxEnrollment, daysOfTheWeek, startTime, endTime, duration, timeString, loc, instructor);
        }catch(NumberFormatException e){
            System.out.println("Invalid input. Course not added.");
            return;
        }catch(IndexOutOfBoundsException e){
            System.out.println("Not enough fields in course. Course not added.");
            return;
        }
        courses.add(course);
    }

    /**
     * Easy getter method to return the completed student roster.
     * @return students: the roster in the form of a List<Course>
     */
    public List<Course> getCourses(){
        return courses;
    }
}
