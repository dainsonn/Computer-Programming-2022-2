import server.Server;
import course.Bidding;
import course.Course;
import utils.ErrorCode;
import utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        Problem2_1TestCase();
        Problem2_2TestCase();
        Problem2_3TestCase();
    }

    static void printOX(String prompt, boolean condition) {
        if (condition) {
            System.out.println("------" + prompt + "O");
        } else {
            System.out.println("------" + prompt + "X");
        }
    }

    static void Problem2_1TestCase() {
        println("Problem 2.1.");
        Server server = new Server();

        List<Course> searchResult = server.search(new HashMap<>(), null);
        printOX("2.1.1 search entire courses : ", checkCourseListWithIDArray(searchResult, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}));

        searchResult = server.search(null, "");
        printOX("2.1.2 search entire courses with empty string : ", checkCourseListWithIDArray(searchResult, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}));

        searchResult = server.search(new HashMap<>(), "id");
        printOX("2.1.3 search sort by id : ", checkCourseListWithIDArray(searchResult, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}));

        searchResult = server.search(new HashMap<>(), "name");
        printOX("2.1.4 search sort by name : ", checkCourseListWithIDArray(searchResult, new int[]{7, 9, 1, 10, 11, 3, 8, 5, 6, 4, 2, 12}));

        searchResult = server.search(new HashMap<>(), "degree");
        printOX("2.1.5 search sort by degree : ", checkCourseListWithIDArray(searchResult, new int[]{1, 2, 3, 4, 5, 6, 7, 9, 10, 12, 8, 11}));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "program");

        searchResult = server.search(map, null);
        printOX("2.1.6 search by name : ", checkCourseListWithIDArray(searchResult, new int[]{3}));

        map = new HashMap<String, Object>();
        map.put("ay", 3);
        searchResult = server.search(map, null);
        printOX("2.1.7 search by academic year : ", checkCourseListWithIDArray(searchResult, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));

        map = new HashMap<String, Object>();
        map.put("degree", "Master");
        searchResult = server.search(map, null);
        printOX("2.1.8 search by degree : ", checkCourseListWithIDArray(searchResult, new int[]{8}));

        map = new HashMap<String, Object>();
        map.put("name", "");
        searchResult = server.search(map, null);
        printOX("2.1.9 search by name with empty string : ", checkCourseListWithIDArray(searchResult, new int[]{}));

        map = new HashMap<String, Object>();
        map.put("degree", "Bachelor");
        map.put("name", "Research and Thinking Design");
        map.put("ay", 3);
        searchResult = server.search(map, null);
        printOX("2.1.10 search by academic year, degree and name : ", checkCourseListWithIDArray(searchResult, new int[]{5}));
    }

    static void Problem2_2TestCase() {
        println("Problem 2.2.");
        Server server = new Server();
        resetUserDirs();
        int status;
        int first_status; int second_status;
        Pair<Integer, List<Bidding>> bidResult;
        Pair<Integer, List<Bidding>> first_bidResult; Pair<Integer, List<Bidding>> second_bidResult;

        bidResult = server.retrieveBids("");
        printOX("2.2.1 Empty string case : retrieve bidding : ",
                bidResult.key==ErrorCode.USERID_NOT_FOUND && checkBiddingListWithIDArray(bidResult.value, new int[]{}, new int[]{}));

        bidResult = server.retrieveBids("2021-22221");
        printOX("2.2.2 retrieve bidding without any bidding in current execution : ",
                checkBiddingListWithIDArray(bidResult.value, new int[]{10, 9, 8, 2}, new int[]{18, 18, 18, 17}));

        bidResult = server.retrieveBids("2021-22233");
        printOX("2.2.3 retrieve bidding without any bidding in current execution + check unordered list : ",
                checkBiddingListWithIDArray(bidResult.value, new int[]{10, 9, 8, 1, 3}, new int[]{17, 15, 15, 1, 2}));

        bidResult = server.retrieveBids("2020-29991");
        printOX("2.2.4 retrieve bidding with wrong user id : ",
                bidResult.key == ErrorCode.USERID_NOT_FOUND && checkBiddingListWithIDArray(bidResult.value, new int[]{}, new int[]{}));

        status = server.bid(7, 7, "2022-22221");
        bidResult = server.retrieveBids("2022-22221");
        printOX("2.2.5 bidding once and checking bidding status : ",
                status == ErrorCode.SUCCESS && bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(bidResult.value, new int[]{3, 4, 5, 2, 7}, new int[]{17, 16, 12, 8, 7}));

        status = server.bid(7, -2, "2022-22221");
        bidResult = server.retrieveBids("2022-22221");
        printOX("2.2.6 bidding minus value and checking bidding status : ",
                status == ErrorCode.SUCCESS && bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(bidResult.value, new int[]{3, 4, 5, 2, 7}, new int[]{17, 16, 12, 8, 5}));

        status = server.bid(15, 7, "2022-22221");
        bidResult = server.retrieveBids("2022-22221");
        printOX("2.2.7 attempt bidding to nonexistent course id and checking bidding status : ",
                status == ErrorCode.NO_COURSE_ID && bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(bidResult.value, new int[]{3, 4, 5, 2, 7}, new int[]{17, 16, 12, 8, 5}));

        status = server.bid(9, 34, "2022-22221");
        bidResult = server.retrieveBids("2022-22221");
        printOX("2.2.8 attempt bidding larger than max mileage per course and checking bidding status : ",
                status == ErrorCode.OVER_MAX_COURSE_MILEAGE && bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(bidResult.value, new int[]{3, 4, 5, 2, 7}, new int[]{17, 16, 12, 8, 5}));

        status = server.bid(4, -50, "2022-22221");
        bidResult = server.retrieveBids("2022-22221");
        printOX("2.2.9 attempt bidding smaller than bidded mileage per special course and checking bidding status : ",
                status == ErrorCode.SUCCESS && bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(bidResult.value, new int[]{3, 5, 2, 7}, new int[]{17, 12, 8, 5}));

        status = server.bid(9, -9, "2021-22221");
        bidResult = server.retrieveBids("2021-22221");
        printOX("2.2.10 modifying mileage of already existing bid course : ",
                status == ErrorCode.SUCCESS && bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(bidResult.value, new int[]{10, 8, 2, 9}, new int[]{18, 18, 17, 9}));

        first_status = server.bid(7, 10, "2021-22221");
        second_status = server.bid(11, 1, "2021-22221");
        bidResult = server.retrieveBids("2021-22221");
        printOX("2.2.11 check if bid method can prevent slight increase from the max mileage : ",
                first_status == ErrorCode.SUCCESS && second_status == ErrorCode.OVER_MAX_MILEAGE && bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(bidResult.value, new int[]{10, 8, 2, 7, 9}, new int[]{18, 18, 17, 10, 9}));

        first_bidResult = server.retrieveBids("2021-48931");
        status = server.bid(5, 0, "2021-48931");
        second_bidResult = server.retrieveBids("2021-48931");
        printOX("2.2.12 check if bid.txt doesn't exist and make bid.txt so it can properly work : ",
                status == ErrorCode.NO_BID_FILE && first_bidResult.key == ErrorCode.IO_ERROR && second_bidResult.key == ErrorCode.SUCCESS && checkBiddingListWithIDArray(second_bidResult.value, new int[]{}, new int[]{}));
    }

    static void Problem2_3TestCase() {
        println("Problem 2.3.");
        Server server = new Server();
        resetUserDirs();
        Pair<Integer, List<Course>> confirmed;

        server.bid(5, 0, "2021-48931");
        if (server.confirmBids()) {
            confirmed = server.retrieveRegisteredCourse("2021-22221");
            printOX("2.3.1", checkCourseListWithIDArray(confirmed.value, new int[]{10, 9, 8, 2}));

            confirmed = server.retrieveRegisteredCourse("2021-22233");
            printOX("2.3.2", checkCourseListWithIDArray(confirmed.value, new int[]{10, 9, 8, 1}));

            confirmed = server.retrieveRegisteredCourse("2018-12344");
            printOX("2.3.3", checkCourseListWithIDArray(confirmed.value, new int[]{3}));

            confirmed = server.retrieveRegisteredCourse("2020-26633");
            printOX("2.3.4", checkCourseListWithIDArray(confirmed.value, new int[]{8, 3}));

            confirmed = server.retrieveRegisteredCourse("2023-45677");
            printOX("2.3.5", checkCourseListWithIDArray(confirmed.value, new int[]{}));

            confirmed = server.retrieveRegisteredCourse("2021-48931");
            printOX("2.3.6", checkCourseListWithIDArray(confirmed.value, new int[]{}));
        } else {
            System.out.println("failed confirmation");
            printOX("2.3.1", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.2", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.3", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.4", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.5", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.6", checkCourseListWithIDArray(null, new int[]{}));
        }
        resetUserDirs();
        server.bid(5, 0, "2021-48931");
        server.bid(3, 11, "2015-45211");
        server.bid(7, 8, "2018-12344");
        server.bid(8, 8, "2018-12344");
        server.bid(9, 8, "2018-12344");
        server.bid(10, 8, "2018-12344");
        server.bid(11, 8, "2018-12344");
        server.bid(12, 8, "2018-12344");
        server.bid(2, 13, "2018-12344");
        server.bid(5, 1, "2018-12344");
        server.bid(1, 1, "2018-12344");
        server.bid(5, 18, "2020-26633");
        server.bid(6, 19, "2020-26633");
        server.bid(8, 17, "2020-26633");
        server.bid(11, 10, "2020-26633");
        server.bid(7, 18, "2022-22221");
        server.bid(3, 17, "2023-45677");
        println("confirmBids with bidding in current execution");
        if (server.confirmBids()) {
            confirmed = server.retrieveRegisteredCourse("2021-22221");
            printOX("2.3.7", checkCourseListWithIDArray(confirmed.value, new int[]{10, 9, 8, 2}));

            confirmed = server.retrieveRegisteredCourse("2022-22221");
            printOX("2.3.8", checkCourseListWithIDArray(confirmed.value, new int[]{5, 4, 3, 2}));

            confirmed = server.retrieveRegisteredCourse("2021-22233");
            printOX("2.3.9", checkCourseListWithIDArray(confirmed.value, new int[]{10, 9, 8, 1}));

            confirmed = server.retrieveRegisteredCourse("2018-12344");
            printOX("2.3.10", checkCourseListWithIDArray(confirmed.value, new int[]{12, 11, 10, 9, 8, 7, 5}));

            confirmed = server.retrieveRegisteredCourse("2020-26633");
            printOX("2.3.11", checkCourseListWithIDArray(confirmed.value, new int[]{11, 8}));

            confirmed = server.retrieveRegisteredCourse("2015-45211");
            printOX("2.3.12", checkCourseListWithIDArray(confirmed.value, new int[]{11, 4, 3}));

            confirmed = server.retrieveRegisteredCourse("2023-45677");
            printOX("2.3.13", checkCourseListWithIDArray(confirmed.value, new int[]{3}));

            confirmed = server.retrieveRegisteredCourse("2021-48931");
            printOX("2.3.14", checkCourseListWithIDArray(confirmed.value, new int[]{}));
        } else {
            println("failed confirmation");
            printOX("2.3.7", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.8", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.9", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.10", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.11", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.12", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.13", checkCourseListWithIDArray(null, new int[]{}));
            printOX("2.3.14", checkCourseListWithIDArray(null, new int[]{}));
        }
    }

    static void println(Object o) {
        System.out.println(o);
    }

    static boolean checkCourseListWithIDArray(List<Course> courses, int[] idarray) {
        if(courses == null) {
            return false;
        }
        if (idarray.length != courses.size()) {
            return false;
        }
        if (courses != null) {
            int index = 0;
            for (Course course : courses) {
                if (course.courseId != idarray[index]) {
                    return false;
                }
                index++;
            }
        }
        return true;
    }

    static boolean checkBiddingListWithIDArray(List<Bidding> biddings, int[] idarray, int[] mileagearray) {
        if(biddings == null){
            return false;
        }
        if (idarray.length != biddings.size()) {
            return false;
        }
        if (mileagearray.length != biddings.size()) {
            return false;
        }
        int index = 0;
        for (Bidding bidding : biddings) {
            if (bidding.courseId != idarray[index] || bidding.mileage != mileagearray[index]) {
                return false;
            }
            index++;
        }
        return true;
    }

    static void resetUserDirs() {
        List<String> userIDList = getUserIDList();
        try {
            for (String userID : userIDList) {
                String userDir = "data/Users/";
                String backupDir = "data/Users_backup/";
                String bidPath = userDir + userID + "/bid.txt";
                String bidBackupPath = backupDir + userID + "/bid.txt";
                String userinfoPath = userDir + userID + "/userinfo.txt";
                String userinfoBackupPath = backupDir + userID + "/userinfo.txt";
                File userDirFile = new File(userDir + userID);
                if (userDirFile.isDirectory()) {
                    for (File file : userDirFile.listFiles())
                        if (!file.isDirectory())
                            file.delete();
                }
                fileCopyOverWrite(bidBackupPath, bidPath);
                fileCopyOverWrite(userinfoBackupPath, userinfoPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<String> getUserIDList() {
        String userDir = "data/Users/";
        File userDirFile = new File(userDir);
        String[] userIDs = userDirFile.list();
        List<String> result = new ArrayList<>();
        if (userIDs != null) {
            for (String userid : userIDs) {
                if (userid.matches("\\d{4}-\\d{5}")) {
                    result.add(userid);
                }
            }
        }
        return result;
    }

    static void fileCopyOverWrite(String fromPath, String toPath) throws IOException {
        Path from = Paths.get(fromPath);
        Path to = Paths.get(toPath);
        if (Files.exists(from)) {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
