/**
 * Courses have various info associated with them such as a department,
 * number, section number, time, etc. Using these fields they can be
 * compared and sorted.
 */
public class Course implements Comparable {
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
    
    /**
     * Constructor takes in all values from the CSV.
     */
    public Course(String dept, int courseNum, int section, String title, double credits, int maxEnrollment, String daysOfTheWeek, int startTime,int endTime,int duration,String timeString, String loc, String instructor){
        //AFRS,100,51,Intro to Africana Studies,1,20,TR,810,885,75,0130PM-0245PM,BH 305,"Harriford, Diane"
        this.dept = dept;
        this.courseNum = courseNum;
        this.section = section;
        this.title = title;
        this.credits = credits;
        this.maxEnrollment = maxEnrollment;
        this.daysOfTheWeek = daysOfTheWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.timeString = timeString;
        this.loc = loc;
        this.instructor = instructor;
    }

    /**
     * Key is a string representation of department-courseNumber-section.
     * Example: CMPU-102-51
     */
    public String getKey(){
        return dept + "-" + courseNum + "-" + section;
    }

    /**
     * Returns true if key is the same.  (Department, Course Number, and Section).
     */
    public boolean equals(Object o){
        if(!(o instanceof  Course)){
            return false;
        }
        Course c = (Course) o;
        return this.getKey().equals(c.getKey());
    }

    /**
     * String representation as it might appear on askBanner.
     */
    public String toString(){
        return getKey() + " " + title + "\t" + credits + "\t" + daysOfTheWeek + " " + timeString;
    }

    /**
     * Should sort classes by department, then course number, then section. (just like
     * askBanner)
     */
    public int compareTo(Object o){
        if(o instanceof Course){
            Course c = (Course) o;
            return this.getKey().compareTo(c.getKey());
        }
        return -1;
    }

    /**
     * a method that checks times and days to determine whether or not they overlap.
     */
    public boolean conflictsWith(Course maybe){
        if(dayOverlap(maybe)){
            if((maybe.startTime <= endTime) && (maybe.endTime >= startTime)){
                return true;
            }
            if((startTime <= maybe.endTime) && (endTime >= maybe.startTime)){
                return true;
            }   
        }   
        return false;
    }
    
    /**
     * helper method for conflictsWith that checks for day overlap
     */
    public boolean dayOverlap(Course maybe){
        String[] days = daysOfTheWeek.split("");
        for(String day : days){
            if(maybe.daysOfTheWeek.contains(day)){
                return true;
            }
        }
        String[] maybeDays = maybe.daysOfTheWeek.split("");
        for(String day : maybeDays){
            if(daysOfTheWeek.contains(day)){
                return true;
            }
        }
        return false;
    }
}
