package cpta.environment;

import cpta.exceptions.CompileErrorException;
import cpta.exceptions.FileSystemRelatedException;
import cpta.exceptions.InvalidFileTypeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Compiler {
    public void compileWriter(File file, String outputFilePath) throws CompileErrorException, FileSystemRelatedException{
        try{
            Scanner in = new Scanner(file);
            FileWriter out = new FileWriter(outputFilePath);
            while (in.hasNext()) {
                String line = in.nextLine();
                if (!line.startsWith(">> ")) {
                    in.close();
                    out.close();
                    File outputFile = new File(outputFilePath);
                    outputFile.delete();
                    throw new CompileErrorException();
                }
                out.write(line.substring(3) + "\n");
            }
            in.close();
            out.close();
        } catch (FileNotFoundException exception) {
            throw new FileSystemRelatedException("File not found: " + exception.getMessage());
        } catch (IOException exception) {
            throw new FileSystemRelatedException("IOException: " + exception.getMessage());
        }
    }

    public void compile(String filePath)
            throws CompileErrorException, InvalidFileTypeException, FileSystemRelatedException {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                throw new FileSystemRelatedException("File does not exist.");
            }

            String dirPath = file.getParent();
            String fileNameWithExtension = file.getName();
            String[] pieces = fileNameWithExtension.split("\\.");
            if (pieces.length != 2) {
                throw new InvalidFileTypeException("File name is invalid.");
            }

            String inputfileName = pieces[0];
            String inputfileExtension = pieces[1];

            if (!inputfileExtension.toLowerCase().equals("sugo")) {
                throw new InvalidFileTypeException("File does not have .sugo extension.");
            }

            String outputFilePath = Paths.get(dirPath, inputfileName + ".yo").toString();

            compileWriter(file, outputFilePath);

        }
        catch(CompileErrorException exception){
            throw exception;
        }
        catch (FileSystemRelatedException exception){
            throw exception;
        }
    }
    
    public void compile(String filePath, String fileName)
            throws CompileErrorException, InvalidFileTypeException, FileSystemRelatedException {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                throw new FileSystemRelatedException("File does not exist.");
            }

            String dirPath = file.getParent();
            String fileNameWithExtension = file.getName();
            String[] pieces = fileNameWithExtension.split("\\.");
            if (pieces.length != 2) {
                throw new InvalidFileTypeException("File name is invalid.");
            }

            String inputfileName = pieces[0];
            String inputfileExtension = pieces[1];

            if (!inputfileExtension.toLowerCase().equals("sugo")) {
                throw new InvalidFileTypeException("File does not have .sugo extension.");
            }

            if(fileName==null){
                throw new InvalidFileTypeException("File name is invalid.");
            }
            if(fileName.isBlank()){
                throw new InvalidFileTypeException("File name is invalid.");
            }

            String outputFilePath = Paths.get(dirPath, fileName + ".yo").toString();
            
            compileWriter(file, outputFilePath);

        } 
        catch(CompileErrorException exception){
            throw exception;
        }
        catch (FileSystemRelatedException exception){
            throw exception;
        }
    }
}

