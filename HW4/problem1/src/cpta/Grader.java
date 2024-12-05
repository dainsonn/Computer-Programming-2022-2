package cpta;

import cpta.environment.Compiler;
import cpta.environment.Executer;
import cpta.exam.ExamSpec;
import cpta.exam.Problem;
import cpta.exam.Student;
import cpta.exam.TestCase;
import cpta.exceptions.CompileErrorException;
import cpta.exceptions.FileSystemRelatedException;
import cpta.exceptions.InvalidFileTypeException;
import cpta.exceptions.RunTimeErrorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Grader {
    Compiler compiler;
    Executer executer;

    public Grader(Compiler compiler, Executer executer) {
        this.compiler = compiler;
        this.executer = executer;
    }

    public Map<String, Map<String, List<Double>>> gradeSimple(ExamSpec examSpec, String submissionDirPath) {
        Map<String, Map<String, List<Double>>> result = new HashMap<>();
        List<Student> students = examSpec.students;
        List<Problem> problems = examSpec.problems;

        for (Student student : students) {
            Map<String, List<Double>> scoresByPbId = new HashMap<>();
            for (Problem problem : problems) {
                String filePath = submissionDirPath + student.id + "/" + problem.id + "/";
                File dir = new File(filePath);
                String targetFileName;

                String[] fileNames = dir.list();
                if (fileNames.length == 1) {
                    targetFileName = fileNames[0];
                } else {
                    targetFileName = fileNames[Arrays.asList(fileNames).indexOf(problem.targetFileName)];
                    String str = problem.targetFileName;
                    for (int i = Arrays.asList(fileNames).indexOf(problem.targetFileName) - 1; i >= 0; i--) {
                        if (fileNames[i].substring(0, str.length()).equals(str)) {
                            if (fileNames[i].indexOf("(") > 0 && fileNames[i].indexOf(")") > 0) {
                                if (fileNames[i].substring(fileNames[i].indexOf("(") + 1, fileNames[i].indexOf(")")).matches("-?\\d+")) {
                                    targetFileName = fileNames[i];
                                    break;
                                }
                            }
                        }
                    }
                }

                try {
                    compiler.compile(filePath + targetFileName, problem.targetFileName.substring(0, problem.targetFileName.indexOf(".")));
                } catch (CompileErrorException e) {
                } catch (InvalidFileTypeException e) {
                } catch (FileSystemRelatedException e) {
                }
                int tcNum = 1;

                List<Double> tcScores = new ArrayList<>();
                for (TestCase testCase : problem.testCases) {
                    try {
                        executer.execute(filePath + targetFileName.substring(0, targetFileName.length() - 4) + "yo",
                                problem.testCasesDirPath + testCase.inputFileName,
                                filePath + "/" + tcNum + ".out");
                    } catch (RunTimeErrorException e) {
                    } catch (InvalidFileTypeException e) {
                    } catch (FileSystemRelatedException e) {
                    }

                    File tcOutput = new File(problem.testCasesDirPath + testCase.outputFileName);
                    File output = new File(filePath + "/" + tcNum + ".out");
                    if (isEqual(tcOutput.toPath(), output.toPath())) {
                        tcScores.add(testCase.score);
                    } else {
                        tcScores.add(0.0);
                    }
                }
                scoresByPbId.put(problem.id, tcScores);
            }
            result.put(student.id, scoresByPbId);
        }
        return result;
    }
    public boolean isEqual(Path f1, Path f2) {
        try {
            if (Files.size(f1) != Files.size(f2)) {
                return false;
            }
            byte[] first = Files.readAllBytes(f1);
            byte[] second = Files.readAllBytes(f2);
            String s1 = new String(first);
            String s2 = new String(second);
            return s1.equals(s2);
        } catch (IOException e) {
        }
        return false;
    }
    public Map<String, Map<String, List<Double>>> gradeRobust(ExamSpec examSpec, String submissionDirPath) {
        Map<String, Map<String, List<Double>>> result = new HashMap<>();
        List<Student> students = examSpec.students;
        List<Problem> problems = examSpec.problems;

        for (Student student : students) {
            Map<String, List<Double>> scoresByPbId = new HashMap<>();
            String subDirPath = submissionDirPath;
            String[] subDirPaths = new File(subDirPath).list();
            String folderName = null;
            for(String fName: subDirPaths){
                if(fName.contains(student.id)){
                    folderName = fName;
                }
            }
            for (Problem problem : problems) {
                String filePath = submissionDirPath + folderName + "/" + problem.id + "/";
                boolean exception = false;

                String targetFileName;
                String[] fileNames = new File(filePath).list();

                if(problem.wrappersDirPath != null && fileNames != null){
                    String[] wrapFileNames = new File(problem.wrappersDirPath).list();
                    for(String fileName: wrapFileNames){
                        File file = new File(filePath + fileName);
                        File newFile = new File(problem.wrappersDirPath + fileName);
                        try {
                            Files.copy(newFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) { }
                    }
                }

                File additionalDir = null;
                if(fileNames != null){
                    File[] files = new File(filePath).listFiles();
                    for(File file: files){
                        if(file.isDirectory()){
                            additionalDir = file;
                        }
                    }
                }

                if(additionalDir != null) {
                    File[] oldFiles = new File(String.valueOf(additionalDir.toPath())).listFiles();
                    for(File oldFile: oldFiles){
                        Path old = Paths.get(oldFile.toURI());
                        String oldName = String.valueOf(oldFile.toPath()).substring(String.valueOf(oldFile.toPath()).lastIndexOf("\\") + 1);
                        try {
                            Files.move(old, Paths.get(filePath + oldName));
                        } catch (IOException e) { }
                    }
                }

                fileNames = new File(filePath).list();

                if(fileNames == null){
                    targetFileName = null;
                }else if(fileNames.length == 1 && fileNames[0].contains(".sugo")){
                    targetFileName = fileNames[0];
                }else{
                    if(Arrays.asList(fileNames).indexOf(problem.targetFileName) == -1){
                        targetFileName = null;
                    } else {
                        targetFileName = fileNames[Arrays.asList(fileNames).indexOf(problem.targetFileName)];
                        String str = problem.targetFileName;
                        for (int i = Arrays.asList(fileNames).indexOf(problem.targetFileName) - 1; i >= 0; i--) {
                            if (fileNames[i].indexOf(".") >= str.length()-5  && fileNames[i].substring(0, str.length()-5).equals(str.substring(0, str.length()-5))) {
                                if (fileNames[i].indexOf("(") > 0 && fileNames[i].indexOf(")") > 0) {
                                    if (fileNames[i].substring(fileNames[i].indexOf("(") + 1, fileNames[i].indexOf(")")).matches("-?\\d+")) {
                                        targetFileName = fileNames[i];
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if(targetFileName == null) { exception = true; }
                boolean onlyYo = false;
                if(targetFileName == null &&
                        new File(filePath + problem.targetFileName.substring(0, problem.targetFileName.indexOf(".")) + ".yo").exists()){
                    onlyYo = true;
                    exception = false;
                }

                try {
                    if(fileNames != null){
                        for(String fileName: fileNames){
                            if(!fileName.contains(".sugo")) { continue; }
                            else{
                                if(fileName.equals(targetFileName)){
                                    compiler.compile(filePath + fileName, problem.targetFileName.substring(0, problem.targetFileName.indexOf(".")));
                                } else {
                                    compiler.compile(filePath + fileName);
                                }
                            }
                        }
                    }
                } catch (CompileErrorException e) {
                    exception = true;
                } catch (InvalidFileTypeException e) {
                    exception = true;
                } catch (FileSystemRelatedException e) {
                    exception = true;
                }
                int tcNum = 1;

                List<Double> tcScores = new ArrayList<>();
                for (TestCase testCase : problem.testCases) {
                    if(exception) { tcScores.add(0.0); }
                    else {
                        try {
                            String substring = problem.targetFileName.substring(0, problem.targetFileName.indexOf(".")+1);
                            executer.execute(filePath + substring + "yo",
                                    problem.testCasesDirPath + testCase.inputFileName,
                                    filePath + tcNum + ".out");
                        } catch (RunTimeErrorException e) { tcScores.add(0.0); continue;
                        } catch (InvalidFileTypeException e) { tcScores.add(0.0); continue;
                        } catch (FileSystemRelatedException e) { tcScores.add(0.0); continue;
                        }

                        File tcOutput = new File(problem.testCasesDirPath + testCase.outputFileName);
                        File output = new File(filePath + tcNum + ".out");
                        byte[] first = new byte[0];
                        byte[] second = new byte[0];
                        try {
                            first = Files.readAllBytes(tcOutput.toPath());
                            second = Files.readAllBytes(output.toPath());
                        } catch (IOException e) { }
                        String s1 = new String(first);
                        String s2 = new String(second);

                        switch (problem.judgingTypes) {
                            case 0:
                                if (isEqual(tcOutput.toPath(), output.toPath())) {
                                    if (onlyYo) {
                                        tcScores.add(testCase.score / 2);
                                    } else {
                                        tcScores.add(testCase.score);
                                    }
                                } else {
                                    tcScores.add(0.0);
                                }
                                break;

                            case 1:
                                s1 = s1.replaceAll("\\\\s+$", "");
                                s2 = s2.replaceAll("\\\\s+$", "");
                                if (s1.equals(s2)) {
                                    if (onlyYo) {
                                        tcScores.add(testCase.score / 2);
                                    } else {
                                        tcScores.add(testCase.score);
                                    }
                                } else {
                                    tcScores.add(0.0);
                                }
                                break;

                            case 2:
                                s1 = s1.replaceAll("^\\s+", "");
                                s2 = s2.replaceAll("^\\s+", "");
                                if (s1.equals(s2)) {
                                    if (onlyYo) {
                                        tcScores.add(testCase.score / 2);
                                    } else {
                                        tcScores.add(testCase.score);
                                    }
                                } else {
                                    tcScores.add(0.0);
                                }
                                break;

                            case 4:
                                if (s1.equalsIgnoreCase(s2)) {
                                    if (onlyYo) {
                                        tcScores.add(testCase.score / 2);
                                    } else {
                                        tcScores.add(testCase.score);
                                    }
                                } else {
                                    tcScores.add(0.0);
                                }
                                break;

                            case 8:
                                s1 = s1.replaceAll("[^a-zA-Z0-9 ]", "");
                                s2 = s2.replaceAll("[^a-zA-Z0-9 ]", "");
                                if (s1.equals(s2)) {
                                    if (onlyYo) {
                                        tcScores.add(testCase.score / 2);
                                    } else {
                                        tcScores.add(testCase.score);
                                    }
                                } else {
                                    tcScores.add(0.0);
                                }
                                break;

                            case 16:
                                String sub2 = null;
                                if (s2.indexOf("DEBUG") > 0 && s2.indexOf("\n", s2.indexOf("DEBUG")) > 0) {
                                    sub2 = s2.substring(s2.indexOf("DEBUG"), s2.indexOf("\n", s2.indexOf("DEBUG")) + 1);
                                } if(sub2 != null){
                                    s2 = s2.replace(sub2, "");
                                }
                                if (s1.equals(s2)) {
                                    if (onlyYo) {
                                        tcScores.add(testCase.score / 2);
                                    } else {
                                        tcScores.add(testCase.score);
                                    }
                                } else {
                                    tcScores.add(0.0);
                                }
                                break;


                            case 35:
                                s1 = s1.replaceAll("\\s", "");
                                s2 = s2.replaceAll("\\s", "");
                                if (s1.equals(s2)) {
                                    if (onlyYo) {
                                        tcScores.add(testCase.score / 2);
                                    } else {
                                        tcScores.add(testCase.score);
                                    }
                                } else {
                                    tcScores.add(0.0);
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }
                scoresByPbId.put(problem.id, tcScores);
            }
            result.put(student.id, scoresByPbId);
        }
        return result;
    }
}

