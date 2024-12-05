package server;

import course.*;
import utils.Config;
import utils.ErrorCode;
import utils.Pair;

import java.io.*;
import java.util.*;



public class Server {

    List<Course> loadAllCourses(){
        List<Course> allCourses = new ArrayList<>();
        String filePath = "data/Courses/2023_Spring/";
        File seasonFile = new File(filePath);
        List<String> colleges = List.of(seasonFile.list());

        for(String college: colleges){
            String fP = filePath + college + "/";
            if(!fP.contains(".txt")){
                File collegeFile = new File(fP);
                List<String> cF = List.of(collegeFile.list());
                for(String courseIdTxt: cF) {
                    fP = fP + courseIdTxt;
                    Scanner sc = null;
                    try {
                        sc = new Scanner(new File(fP));
                    } catch (FileNotFoundException e) {
                    }
                    String info = null;
                    while (sc != null && sc.hasNextLine()) {
                        info = sc.nextLine();
                    }
                    String[] infos = info.split("\\|");
                    int maxMileage;
                    if(infos.length == 8){
                        maxMileage = Config.MAX_MILEAGE_PER_COURSE;
                    } else { maxMileage = Integer.parseInt(infos[8]); }
                    allCourses.add(new Course(Integer.parseInt(courseIdTxt.substring(0, courseIdTxt.length()-4)),
                            college, infos[0], infos[1], Integer.parseInt(infos[2]),
                            infos[3], Integer.parseInt(infos[4]), infos[5], infos[6],
                            Integer.parseInt(infos[7]), maxMileage));
                    fP = filePath + college + "/";
                }
            }
        }
        return allCourses;
    }

    List<User> loadAllUsers(){
        List<User> allUsers = new ArrayList<>();
        File userFile = new File("data/Users/");
        String[] userId = userFile.list();

        for(String id: userId){
            File userInfo = new File("data/Users/" + id +
                    "/userinfo.txt");
            try {
                Scanner sc = new Scanner(userInfo);
                String[] infos = sc.nextLine().split("\\|");
                allUsers.add(new User(id, infos[0], infos[1], Integer.parseInt(infos[2])));
                sc.close();
            } catch (FileNotFoundException e) { }
        }
        return allUsers;
    }

    public List<Course> search(Map<String,Object> searchConditions, String sortCriteria){
        List<Course> courses = loadAllCourses();

        if (searchConditions != null && !searchConditions.isEmpty()) {
            for (String key : searchConditions.keySet()) {
                if (key.equals("name") && searchConditions.get(key) != null) {
                    List<Course> temp = new ArrayList<>();
                    if (searchConditions.get(key).toString().isBlank() ||
                            searchConditions.get(key).toString() == null) {
                        return new ArrayList<>();
                    }
                    for (Course course : courses) {
                        String[] courseNameKeyword = course.courseName.toLowerCase().split(" ");
                        String[] searchKeyword = searchConditions.get(key).toString().toLowerCase().split(" ");
                        boolean keywordsAreContained = true;
                        for(int i=0; i<searchKeyword.length; i++){
                            if(!Arrays.asList(courseNameKeyword).contains(searchKeyword[i])){
                                keywordsAreContained = false; break;
                            }
                        }
                        if (!course.courseName.toLowerCase().contains(searchConditions.get(key).toString().toLowerCase())
                        && !keywordsAreContained) {
                            temp.add(course);
                        }
                    } courses.removeAll(temp);

                } else if (key.equals("ay") && searchConditions.get(key) != null) {
                    List<Course> temp = new ArrayList<>();
                    for (Course course : courses) {
                        if (course.academicYear > Integer.parseInt(searchConditions.get(key).toString())) {
                            temp.add(course);
                        }
                    } courses.removeAll(temp);

                } else {
                    if(searchConditions.get(key) != null){
                        List<Course> temp = new ArrayList<>();
                        for (Course course : courses) {
                            if (!course.academicDegree.equals(searchConditions.get(key))) {
                                temp.add(course);
                            }
                        } courses.removeAll(temp);
                    }
                }
            }
        }
        if(sortCriteria == null || sortCriteria.isBlank() || sortCriteria == "id"){
            Collections.sort(courses, new courseIdComparator());
        } else if(sortCriteria == "name"){
            Collections.sort(courses, new courseNameComparator());
        } else if(sortCriteria == "degree"){
            Collections.sort(courses, new academicDegreeComparator());
        } else{
            Collections.sort(courses, new academicYearComparator());
        }
        return courses;
    }

    public int bid(int courseId, int mileage, String userId) {
        List<Course> allCourses = loadAllCourses();
        boolean EXIST_COURSE = false;
        Course course = null;
        for (Course c : allCourses) {
            if (c.courseId == courseId) {
                EXIST_COURSE = true;
                course = c;
                break;
            }
        }
        if (!EXIST_COURSE) {
            return ErrorCode.NO_COURSE_ID;
        } else {
            File bidFile = new File("data/Users/" + userId + "/bid.txt");
            if(!bidFile.exists()){
                try {
                    bidFile.createNewFile();
                    return ErrorCode.NO_BID_FILE;
                } catch (IOException e) { }
            }
            List<Bidding> current = retrieveBids(userId).value;
            boolean contain = false;
            int mileageSum = 0;
            for(Bidding bid: current){
                mileageSum += bid.mileage;
            }
            for (Bidding bid : current) {
                if (bid.courseId == courseId) {
                    bid.mileage += mileage;
                    mileageSum += mileage;
                    if(bid.mileage > course.maxMileage) { return ErrorCode.OVER_MAX_COURSE_MILEAGE; }
                    if(mileageSum > Config.MAX_MILEAGE) {
                        bid.mileage -= mileage;
                        mileageSum -= mileage;
                        return ErrorCode.OVER_MAX_MILEAGE;
                    }
                    if(bid.mileage <= 0) { current.remove(bid); }
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                if(mileage > course.maxMileage) { return ErrorCode.OVER_MAX_COURSE_MILEAGE; }
                Bidding newBid = new Bidding(courseId, mileage);
                current.add(newBid);
                mileageSum += mileage;
                if(mileageSum > Config.MAX_MILEAGE) {
                    current.remove(newBid);
                    mileageSum -= mileage;
                    return ErrorCode.OVER_MAX_MILEAGE;
                }
                if(current.size() > Config.MAX_COURSE_NUMBER){
                    current.remove(newBid);
                    return ErrorCode.OVER_MAX_COURSE_NUMBER;
                }
            }

            Collections.sort(current, new mileageComparator());
            try {
                new FileWriter(bidFile, false).close();
                BufferedWriter writer = new BufferedWriter(new FileWriter(bidFile, true));
                for (int i = 0; i < current.size(); i++) {
                    String bidInfo = current.get(i).courseId + "|" + current.get(i).mileage;
                    if (i != current.size() - 1) {
                        writer.write(bidInfo);
                        writer.write("\n");
                    } else {
                        writer.write(bidInfo);
                    }
                }
                writer.close();
            } catch (IOException e) { }
            return ErrorCode.SUCCESS;
        }
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userId){
        int key = 0; List<Bidding> value = new ArrayList<>();
        if(userId.isBlank() ||
            !new File("data/Users/" + userId).exists()){
            key = ErrorCode.USERID_NOT_FOUND;
        } else {
            key = ErrorCode.SUCCESS;
            File bidFile = new File("data/Users/" + userId + "/bid.txt");
            Scanner sc = null;
            try {
                sc = new Scanner(bidFile);
            } catch (FileNotFoundException e) { }
            if(sc == null){
                key = ErrorCode.IO_ERROR;
            } else {
                while(sc.hasNext()){
                    String bidInfo = sc.nextLine();
                    int courseId = Integer.parseInt(bidInfo.substring(0,bidInfo.lastIndexOf("|")));
                    int mileage = Integer.parseInt(bidInfo.substring(bidInfo.lastIndexOf("|")+1));
                    value.add(new Bidding(courseId, mileage));
                } sc.close();
            }
        }
        Pair<Integer, List<Bidding>> bidsResult = new Pair<>(key, value);
        return bidsResult;
        }

    public boolean confirmBids(){
        List<Course> allCourses = loadAllCourses();
        List<User> allUsers = loadAllUsers();
        Collections.sort(allCourses, new courseIdComparator());
        File userFolder = new File("data/Users/");
        String[] users = userFolder.list();
        List<Pair<String, Bidding>> bidsByUser = new ArrayList<>();

        for(int courseId=1; courseId<=allCourses.size(); courseId++){
            Course course = allCourses.get(courseId-1);
            bidsByUser.removeAll(bidsByUser);
            for(String user: users){
                for(Bidding bid: retrieveBids(user).value){
                    if(bid.courseId == courseId){
                        bidsByUser.add(new Pair<>(user, bid));
                    }
                }
            } Collections.sort(bidsByUser, new pairMileageComparator());
            int quota = course.quota;
            
            int temp = 0;
            for(int i=0; i<bidsByUser.size()-1; i++){
                if(bidsByUser.get(i).value.mileage == bidsByUser.get(i+1).value.mileage){
                    User u1 = null;
                    User u2 = null;
                    for (User user : allUsers) {
                        if (user.userId.equals(bidsByUser.get(i).key)) {
                            u1 = user;
                        }
                        if (user.userId.equals(bidsByUser.get(i+1).key)) {
                            u2 = user;
                        }
                    }
                    if (!u1.department.equals(course.department) &&
                            u2.department.equals(course.department)) {
                        Collections.swap(bidsByUser, i, i+1);
                        temp = i + 1;
                        i = -1;
                    }
                }
            }

            for(int i=temp; i<bidsByUser.size()-1; i++){
                if(bidsByUser.get(i).value.mileage == bidsByUser.get(i+1).value.mileage){
                    User u1 = null;
                    User u2 = null;
                    for (User user : allUsers) {
                        if (user.userId.equals(bidsByUser.get(i).key)) {
                            u1 = user;
                        }
                        if (user.userId.equals(bidsByUser.get(i+1).key)) {
                            u2 = user;
                        }
                    }
                    int sum1 = 0, sum2 = 0;
                    for(Bidding b1: retrieveBids(u1.userId).value){
                        sum1 += b1.mileage;
                    } for(Bidding b2: retrieveBids(u2.userId).value){
                        sum2 += b2.mileage;
                    }
                    if ((float)bidsByUser.get(i).value.mileage / (float)sum1 < (float)bidsByUser.get(i+1).value.mileage / (float)sum2) {
                        Collections.swap(bidsByUser, i, i+1);
                        i = temp - 1;
                    }
                }
            }

            for(int j=0; j<bidsByUser.size(); j++){
                if(quota <= 0) { break; }
                File registered = new File("data/Users/" + bidsByUser.get(j).key
                 + "/registered.txt");
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(registered, true));
                    String courseInfo = course.department + "|" + course.academicDegree + "|" +
                            course.academicYear + "|" + course.courseName + "|" + course.credit
                            + "|" + course.location + "|" + course.instructor + "|" + course.quota
                            + "|" + course.maxMileage + "\n";
                    writer.write(courseInfo);
                    writer.close();
                } catch (IOException e) { }
                quota -= 1;
            }
        }
        return true;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userId){
        List<Course> allCourses = loadAllCourses();
        int key = 0; List<Course> value = new ArrayList<>();
        if(userId.isBlank() ||
                !new File("data/Users/" + userId).exists()){
            key = ErrorCode.USERID_NOT_FOUND;
        } else {
            key = ErrorCode.SUCCESS;
            File courseFile = new File("data/Users/" + userId + "/registered.txt");
            Scanner sc = null;
            try {
                sc = new Scanner(courseFile);
            } catch (FileNotFoundException e) { }
            if(sc == null){
                key = ErrorCode.IO_ERROR;
            } else {
                String info = null;
                while (sc.hasNext()) {
                    info = sc.nextLine();
                    String[] infos = info.split("\\|");
                    for(Course course: allCourses){
                        if(course.courseName.equals(infos[3])){
                            value.add(course);
                        }
                    }
                }
                sc.close();
            }
        }
        Collections.sort(value, new courseIdComparator());
        Collections.reverse(value);
        Pair<Integer, List<Course>> registerResult = new Pair<>(key, value);

        try {
            File registered = new File("data/Users/" + userId
                    + "/registered.txt");
            new FileWriter(registered, false).close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(registered, true));
            for (Course course: value) {
                String courseInfo = course.department + "|" + course.academicDegree + "|" +
                        course.academicYear + "|" + course.courseName + "|" + course.credit
                        + "|" + course.location + "|" + course.instructor + "|" + course.quota
                        + "|" + course.maxMileage + "\n";
                writer.write(courseInfo);
            } writer.close();
        } catch (IOException e) { }

        return registerResult;
    }
}

class courseIdComparator implements Comparator<Course>{
    @Override
    public int compare(Course c1, Course c2){
        if(c1.courseId > c2.courseId){
            return 1;
        } else if(c1.courseId < c2.courseId){
            return -1;
        } else {
            return 0;
        }
    }
}
class courseNameComparator implements Comparator<Course>{
    @Override
    public int compare(Course c1, Course c2){
        if(c1.courseName.compareTo(c2.courseName) > 0){
            return 1;
        } else if(c1.courseName.compareTo(c2.courseName) < 0){
            return -1;
        } else {
            if(c1.courseId > c2.courseId){
                return 1;
            } else if(c1.courseId < c2.courseId){
                return -1;
            } else {
                return 0;
            }
        }
    }
}
class academicDegreeComparator implements Comparator<Course>{
    @Override
    public int compare(Course c1, Course c2){
        if(c1.academicDegree.equals("Bachelor")){
            if(c2.academicDegree.equals("Bachelor")){
                if(c1.courseId > c2.courseId){
                    return 1;
                } else if(c1.courseId < c2.courseId){
                    return -1;
                } else {
                    return 0;
                }
            } else if(c2.academicDegree.equals("Master")){
                return -1;
            } else{
                return -1;
            }
        } else if(c1.academicDegree.equals("Master")){
            if(c2.academicDegree.equals("Bachelor")){
                return 1;
            } else if(c2.academicDegree.equals("Master")){
                if(c1.courseId > c2.courseId){
                    return 1;
                } else if(c1.courseId < c2.courseId){
                    return -1;
                } else {
                    return 0;
                }
            } else{
                return -1;
            }
        } else{
            if(c2.academicDegree.equals("Bachelor")){
                return 1;
            } else if(c2.academicDegree.equals("Master")){
                return 1;
            } else{
                if(c1.courseId > c2.courseId){
                    return 1;
                } else if(c1.courseId < c2.courseId){
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}
class academicYearComparator implements Comparator<Course>{
    @Override
    public int compare(Course c1, Course c2){
        if(c1.academicYear > c2.academicYear){
            return 1;
        } else if(c1.academicYear < c2.academicYear){
            return -1;
        } else {
            if(c1.courseId > c2.courseId){
                return 1;
            } else if(c1.courseId < c2.courseId){
                return -1;
            } else {
                return 0;
            }
        }
    }
}
class mileageComparator implements Comparator<Bidding> {
    @Override
    public int compare(Bidding b1, Bidding b2) {
        if (b1.mileage < b2.mileage) {
            return 1;
        } else if (b1.mileage > b2.mileage) {
            return -1;
        } else {
            if (b1.courseId < b2.courseId) {
                return 1;
            } else if (b1.courseId > b2.courseId) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
class pairMileageComparator implements Comparator<Pair<String, Bidding>>{
    @Override
    public int compare(Pair<String, Bidding> p1, Pair<String, Bidding> p2){
        if(p1.value.mileage < p2.value.mileage){
            return 1;
        } else if(p1.value.mileage > p2.value.mileage){
            return -1;
        } else {
            return 0;
        }
    }
}