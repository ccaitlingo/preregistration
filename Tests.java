import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The test class Tests.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class Tests
{
    @Test
    public void studentLoaderTestCorrect(){
        StudentLoader s = new StudentLoader();
        s.parseAndLoadLine("Hector Tran,999248624,2023,1");
        assertTrue(s.getStudents().get(0).name.equals("Hector Tran"));
        
        //For any size list 
        //(i.e. if you didn't have empty constructor)
        /*
        boolean includesStudent = false;
        for(Student student : s.getStudents()){
            if(student.name.equals("Hector Tran")){
                includesStudent = true;}}
        assertTrue(includesStudent);
        */
    }
    
    @Test
    public void studentLoaderTestIncorrect(){
        StudentLoader s = new StudentLoader();
        s.parseAndLoadLine("Hector,Tran,2023,1");
        assertEquals(0, s.getStudents().size());
        /*
        int prevLength = s.getStudents().size();
        s.parseAndLoadLine("Hector,Tran,2023,1");
        int curLength = s.getStudents().size();
        assertEquals(prevLength, curLength);
        */
    }
    
    @Test
    public void studentLoaderTestIncorrectLength(){
        StudentLoader s = new StudentLoader();
        s.parseAndLoadLine("Hector Tran,2023,1");
        assertEquals(0, s.getStudents().size());
    }
    
    @Test
    public void courseLoaderTestCorrect(){
        CourseLoader c = new CourseLoader();
        c.parseAndLoadLine("CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        assertTrue(c.getCourses().get(0).courseNum == 145);
    }
    
    @Test
    public void courseLoaderTestIncorrect(){
        CourseLoader c = new CourseLoader();
        c.parseAndLoadLine("CMPU,one-forty-five,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        assertEquals(0, c.getCourses().size());
    }
    
    @Test
    public void courseLoaderTestIncorrectLength(){
        CourseLoader c = new CourseLoader();
        c.parseAndLoadLine("CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309");
        assertEquals(0, c.getCourses().size());
    }
    
    @Test
    public void requestLoader(){
        System.out.println(":D");
        
        Student s = new Student("Hector Tran", 999248624, 2023, 1);
        CourseLoader cl = new CourseLoader();
        RequestLoader r = new RequestLoader();
        
        cl.parseAndLoadLine("CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        Course c = cl.getCourses().get(0);
        
        r.mapStudents.put(s.idNum, s);
        r.mapCourses.put(c.getKey(), c);
        r.parseAndLoadLine("999248624,CMPU-145-51,CMPU-145-51,CMPU-145-51,CMPU-145-51,CMPU-145-51,CMPU-145-51");
        
        for(Course course : s.requests){
            System.out.println(course);
        }
        
        assertEquals(s.requests.size(), 6);
    }
    
    @Test
    public void compareToStudent(){
        StudentLoader s = new StudentLoader();
        s.parseAndLoadLine("Hector Tran,999248624,2023,1");
        s.parseAndLoadLine("Chace Sanford,999248625,2023,2");
        s.parseAndLoadLine("Cade Young,999248626,2023,3");
        s.parseAndLoadLine("Desirae Welch,999249374,2024,1");
        s.parseAndLoadLine("Alina Kidd,999249375,2024,2");
        
        List<Student> sortedStudents = s.getStudents();
        
        RequestLoader r = new RequestLoader();
        for(Student student : sortedStudents){
            r.mapStudents.put(student.idNum, student);
        }
        
        List<Student> students = new ArrayList<Student>();
        students.addAll(r.mapStudents.values());
        Collections.sort(students);
        
        for(int i = 0; i < students.size(); i++){
            if(!sortedStudents.get(i).name.equals(students.get(i).name)){
                fail();
            }
        }
        return;
    }
    
    @Test
    public void compareToCourse1(){
        CourseLoader cl = new CourseLoader();
        cl.parseAndLoadLine("CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        cl.parseAndLoadLine("CMPU,145,52,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        Course c1 = cl.getCourses().get(0);
        Course c2 = cl.getCourses().get(1);
        assertEquals(c1.compareTo(c2), -1);
    }
    
    @Test
    public void compareToCourse2(){
        CourseLoader cl = new CourseLoader();
        cl.parseAndLoadLine("CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        cl.parseAndLoadLine("CMPU,145,52,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        Course c1 = cl.getCourses().get(0);
        Course c2 = cl.getCourses().get(1);
        assertEquals(c2.compareTo(c1), 1);
    }
    
    @Test
    public void conflictsWith1(){
        CourseLoader cl = new CourseLoader();
        cl.parseAndLoadLine("CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        cl.parseAndLoadLine("CMPU,145,52,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        Course c1 = cl.getCourses().get(0);
        Course c2 = cl.getCourses().get(1);
        assertEquals(c1.conflictsWith(c2), true);
    }
    
    @Test
    public void conflictsWith2(){
        CourseLoader cl = new CourseLoader();
        cl.parseAndLoadLine("CMPU,145,51,Foundations/Computer Science,1,24,TRF,720,795,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        cl.parseAndLoadLine("CMPU,145,52,Foundations/Computer Science,1,24,TRF,800,875,75,1200PM-0115PM,SP 309,Gomerschdat Anna");
        Course c1 = cl.getCourses().get(0);
        Course c2 = cl.getCourses().get(1);
        assertEquals(c1.conflictsWith(c2), false);
    }
}
