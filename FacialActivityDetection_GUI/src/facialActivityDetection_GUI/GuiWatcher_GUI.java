/*
 * File:    CSVBarChart_GUI.java
 * Created: September 2, 2019 3:15:00 PM
 * Author:  Kevin Clark
 * Project: FAD_setup
 * Updated: January 11, 2020
 * Author: Kevin Clark
 */
package facialActivityDetection_GUI;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GuiWatcher_GUI {


    /**
     * Attribute contentFileExtCheck of type String is a shell command that checks images in the import directory for files ending in .JPG and changes them to .jpg.
     */
    private static final String contentFileExtCheck = "Dir -path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\"" + " *.JPG | rename-item -newname {  $_.name  -replace " + "\"'" + ".JPG" + "\"'" + ", " + "\"'" + ".jpg" + "\"'" + "}";
    /**
     * Attribute contentMvImpToExpDate of type String is a shell command that moves all image files from the import directory to the dated import directory in the export folder
     */
    private static final String contentMvImpToExpDate = "Move-Item -path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\\*.jpg" + "\"" + ", " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\\*.jpeg" + "\"" + ", " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\\*.bmp" + "\"" + ", " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\\*.png" + "\" " + " -Destination " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\Import-$((Get-Date).ToString('yyyy-MM-dd'))" + "\"" + " -Force";
    /**
     * Attribute contentMvExptoExpDate of type String is a shell command that moves all files in the Export Directory to the most recent dated Export directory within the export folder
     */
    private static final String contentMvExpToExpDate = "Move-Item -path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\*.jpg" + "\"" + ", " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\*.jpeg" + "\"" + ", " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\*.csv" + "\"" + ", " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\*.txt" + "\" " + " -Destination " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\Export-$((Get-Date).ToString('yyyy-MM-dd'))" + "\"" + " -Force";
    /**
     * Attribute contentMvExptoExpDate of type String is a shell command that moves all CSV and txt filed in the dated export directory to a folder titled FADData
     */
    private static final String contentMvExpDateToData = "Move-Item -path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))" + "\\*.csv" + "\"" + ", " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))" + "\\*.txt" + "\"" + " -Destination " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))\\FADData" + "\"" + " -Force";
    /**
     * Attribute date of type Date creates a new Date element
     */
    private static final Date date = new Date();
    /**
     * Attribute SimpleDateFormat declares a yyyy-MM-dd date format
     */
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * Attribute srtDate of type string takes the date and format and uses it for naming of the date portion of the import-date and export-date directories
     */
    private static final String strDate = formatter.format(date);
    /**
     * Attribute exeDir of type Path is the path file of the export directory that is extracted from directory_do_not_delete.txt
     */
    private static Path exeDir;
    /**
     * Attribute contentExeRun of type String calls FADAnalyzer and points it at two folders. 1. the import directory where the photos to be analyzed exist. 2.The Export directory where the photos and data files will be created during analysis.
     */
    private static String contentExeRun;

    /**
     * Method Watcher attempts to read the executable directory from directory_do_not_delete.txt and use it to define contentExeRun.
     * It also writes all shell commands to file, and checks to make sure all required folders have been created. Then calls method run().
     *
     * @throws IOException
     * @author Kevin Clark
     */
    public static void Watcher() throws IOException {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(System.getProperty("user.home") + "\\AppData\\Roaming\\FAD\\directory_do_not_delete.txt"));
            exeDir = Paths.get(allLines.get(0));
            System.out.println(exeDir);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        contentExeRun = "& " + "\"'" + exeDir + "'\"" + " -wild" + " -aus" + " -tracked" + " -fdir " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\"" + " -out_dir " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\"";
        run();
    }

    /**
     * Method run, in a try/catch block, calls multiple shell commands and method chartExecutor within class CSVBarChart to create all necessary barcharts,
     * before finishing the movement of all files and exiting the appliction
     *
     * @author Kevin Clark
     */
    public static void run() {
        try {
            runShell(contentFileExtCheck, "Checked File Extensions");
            runShell(contentExeRun, "FadAnalyzer has completed");
            runShell(contentMvImpToExpDate, "Moved all eligible files from FAD-Import to Fad-Export\\Import-" + strDate);
            runShell(contentMvExpToExpDate, "Moved photos from Export directory to \\Export-" + strDate);
            CSVBarChart_GUI.chartExecutor();
            runShell(contentMvExpDateToData, "Moved data file to FADData" + "\n" + "Facial Activity Detection analysis complete.");
            System.exit(0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Method runShell takes in the string shell commands and executes them as well as throwing any errors involved with each shell command
     *
     * @param content
     * @param result
     * @throws IOException
     */
    public static void runShell(String content, String result) throws IOException {

        //Calling Powershell command
        String command = "powershell.exe " + content;

        //Executing the command
        Process powerShellProcess = Runtime.getRuntime().exec(command);

        //Getting the results
        powerShellProcess.getOutputStream().close();
        String line;

        //Getting the output
        System.out.println("Standard Output:");
        BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            System.out.println(line);
        }
        stdout.close();

        //Getting any errors
        System.out.println("Standard Error:");
        BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
        while ((line = stderr.readLine()) != null) {

            System.out.println(line);
        }
        stderr.close();

        System.out.println(result);
    }


}
