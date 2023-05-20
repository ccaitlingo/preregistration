import java.util.ArrayList;
import java.util.List;

/**
 * StudentLoader takes info on a student from a single String line, and
 * parses it to create a Student object.
 *
 * @author CG
 * @version 5.3.2023
 */
public class StudentLoader extends DataLoader
{
    private List<Student> students = new ArrayList<Student>();
    /**
     * Empty constructor for testing.
     */
    public StudentLoader(){}
    
    /**
     * Constructor calls the load(file) method in abstract parent class.
     * 
     * @param file: the path to the file.
     */
    public StudentLoader(String file){
        load(file);
    }
    
    /**
     * Parse a single line of CSV in the form:
     * Name, idNum, gradYear, drawNummber
     * Hector Tran,999248624,2023,1
     * 
     * Method should create a new Student Object and add it to 
     * the students instance variable.
     * 
     * @param data: a single line from the csv file.
     */
    public void parseAndLoadLine(String data){
        String[] tokens = data.split(",");
        String name;
        int idNum;
        int gradYear;
        int drawNum;
        Student student;
        try{
            name = tokens[0];
            idNum = Integer.parseInt(tokens[1]);
            gradYear = Integer.parseInt(tokens[2]);
            drawNum = Integer.parseInt(tokens[3]);
            student = new Student(name, idNum, gradYear, drawNum);
        }catch(NumberFormatException e){
            System.out.println("Invalid input. Student not added.");
            return;
        }catch(IndexOutOfBoundsException e){
            System.out.println("Not enough fields in student. Student not added.");
            return;
        }
        students.add(student);
    }
    
    /**
     * Easy getter method to return the completed student roster.
     * @return students: the roster in the form of a List<Student>
     */
    public List<Student> getStudents(){
        return students;
    }
}
