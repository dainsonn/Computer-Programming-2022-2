package server;

public class User {
    public String userId;
    public String department;
    public String academicDegree;
    public int academicYear;

    public User(String userId, String department, String academicDegree,
                int academicYear){
        this.userId = userId;
        this.department = department;
        this.academicDegree = academicDegree;
        this.academicYear = academicYear;
    }
}
