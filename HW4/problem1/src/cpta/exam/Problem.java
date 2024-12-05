package cpta.exam;

import java.util.List;
import java.util.Set;

public class Problem {
    public String id;
    public String testCasesDirPath;
    public List<TestCase> testCases;
    public String targetFileName;
    public String wrappersDirPath;

    public static int DEFAULT = Integer.parseInt("0", 2);
    public static int LEADING_WHITESPACES = Integer.parseInt("10", 2);
    public static int TRAILING_WHITESPACES = Integer.parseInt("01", 2);
    public static int IGNORE_WHITESPACES = Integer.parseInt("100011", 2);
    public static int CASE_INSENSITIVE = Integer.parseInt("100", 2);
    public static int IGNORE_SPECIAL_CHARACTERS = Integer.parseInt("1000", 2);
    public static int DEBUG_MESSAGES = Integer.parseInt("10000", 2);


    public int judgingTypes;

    public Problem(
            String id, String testCasesDirPath, List<TestCase> testCases,
            String targetFileName, String wrappersDirPath, int judgingTypes
    ) {
        this.id = id;
        this.testCasesDirPath = testCasesDirPath;
        this.testCases = testCases;
        this.targetFileName = targetFileName;
        this.wrappersDirPath = wrappersDirPath;
        this.judgingTypes = judgingTypes;
    }
}

