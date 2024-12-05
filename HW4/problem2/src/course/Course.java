package course;

public class Course {
    public int courseId;
    public String college;
    public String department;
    public String academicDegree;
    public int academicYear;
    public String courseName;
    public int credit;
    public String location;
    public String instructor;
    public int quota;
    public int maxMileage;

    public Course (int courseId, String college, String department, String academicDegree, int academicYear,
                   String courseName, int credit, String location, String instructor, int quota, int maxMileage){
        this.courseId = courseId;
        this.college = college;
        this.department = department;
        this.academicDegree = academicDegree;
        this.academicYear = academicYear;
        this.courseName = courseName;
        this.credit = credit;
        this.location = location;
        this.instructor = instructor;
        this.quota = quota;
        this.maxMileage = maxMileage;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Course){
            return courseId == ((Course)obj).courseId;
        }else{
            return false;
        }
    }
}
