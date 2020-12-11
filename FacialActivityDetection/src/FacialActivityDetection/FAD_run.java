/* 
 * File:    FAD_run.java
 * Created: September 2, 2019 3:15:00 PM
 * Author:  Kevin Clark
 * Project: Facial Activity Detection
 * 
 * Updated: January 11, 2020
 * Author: Kevin Clark
 */

package FacialActivityDetection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date; 
import java.util.List;

/**
 * Class FAD_run is the primary controlling class for the Facial Activity Detection project. It works in conjunction with the FAD_setup project. This class tests to make sure all folders are created, 
 * and creates them if they are not. And will run FADAnalyzer, and move and graph all necessary folders. The main prerequisite for the running of this project is the existence of directory_do_not_delete.txt which is created in FAD_setup
 * 
 * @author Kevin Clark
 *
 */

public class FAD_run {
 
	/**
	 * Attribute exeDir of type Path is the path file of the export directory that is extracted from directory_do_not_delete.txt
	 */
	private static Path exeDir;
	
	/**
	 * Attribute pShell of type String provides the path to the Powershell file all shell commands are redundantly written to. This is for checking for errors in shell commands.
	 */
	private static String pShell = System.getProperty("user.home") + "\\AppData\\Roaming\\FAD\\FADScript.ps1";
	
	/**
	 * Attribute createImportDir of type String is a shell command that tests if the import directory has been created and if not creates it.
	 */
	private static String createImportDir = "if(!(Test-Path -Path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\"" + ")){New-Item " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\"" + " -ItemType directory -Force}";
	
	/**
	 * Attribute createExportImportDateDir of type String is a shell command that tests if the secondary import directory with date has been created in the Export directory, if not creates it.
	 */
	private static String createExportImportDateDir = "if(!(Test-Path -Path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Import-$((Get-Date).ToString('yyyy-MM-dd'))" + "\"" + ")){New-Item " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Import-$((Get-Date).ToString('yyyy-MM-dd'))" + "\" " + " -Itemtype directory -Force}";
	
	/**
	 * Attribute createExportImportDateDir of type String is a shell command that tests if the secondary export directory with date and data directory has been created in the Export directory, if not creates it.
	 */
	private static String createExportDateDataDir = "if(!(Test-Path -Path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))\\FADData" + "\"" + ")){New-Item " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))\\FADData" + "\" " + " -Itemtype directory -Force}";
	
	/**
	 * Attribute contentExeRun of type String calls FADAnalyzer and points it at two folders. 1. the import directory where the photos to be analyzed exist. 2.The Export directory where the photos and data files will be created during analysis.
	 */
	private static String contentExeRun;
	
	/**
	 * Attribute contentFileExtCheck of type String is a shell command that checks images in the import directory for files ending in .JPG and changes them to .jpg. 
	 */
	private static String contentFileExtCheck = "Dir -path " + "\""  + System.getProperty("user.home") + "\\Pictures\\Fad-Import" +"\"" + " *.JPG | rename-item -newname {  $_.name  -replace " + "\"'" + ".JPG" + "\"'" +  ", " + "\"'" + ".jpg" +"\"'" + "}";
	
	/**
	 *Attribute contentMvImpToExpDate of type String is a shell command that moves all image files from the import directory to the dated import directory in the export folder 
	 */
	private static String contentMvImpToExpDate = "Move-Item -path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" +  "\\*.jpg" +  "\"" + ", " +  "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\\*.jpeg" + "\"" + ", " +  "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\\*.bmp" + "\"" + ", " +  "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\\*.png" + "\" " + " -Destination " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\Import-$((Get-Date).ToString('yyyy-MM-dd'))" + "\"" + " -Force";  		
	
	/**
	 * Attribute contentMvExptoExpDate of type String is a shell command that moves all files in the Export Directory to the most recent dated Export directory within the export folder
	 */
	private static String contentMvExptoExpDate = "Move-Item -path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" +  "\\*.jpg" + "\"" + ", " +  "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\*.jpeg" + "\"" + ", " +  "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\*.csv" + "\"" + ", " +  "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\*.txt" + "\" " + " -Destination " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\\Export-$((Get-Date).ToString('yyyy-MM-dd'))" + "\"" + " -Force";
	
	/**
	 * Attribute contentMvExptoExpDate of type String is a shell command that moves all CSV and txt filed in the dated export directory to a folder titled FADData
	 */
	private static String contentMvExpDatetoData = "Move-Item -path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))" +  "\\*.csv" + "\"" + ", " + "\"" +  System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))" + "\\*.txt" + "\"" + " -Destination " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))\\FADData" + "\"" + " -Force";		
	
	/**
	 * Attribute contentPshllWrt of type String is a container for all shell commands used in running FAD_run. These commands are passed to a Powershell file for easy user review and analysis of possible errors in shell execution.
	 */
	private static String contentPshllWrt;
	
	/**
	 * Attribute date of type Date creates a new Date element
	 */
	private static Date date = new Date();
	
	/**
	 * Attribute SimpleDateFormat declares a yyyy-MM-dd date format
	 */
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	
	/**
	 * Attribute srtDate of type string takes the date and format and uses it for naming of the date portion of the import-date and export-date directories
	 */
	private static String strDate = formatter.format(date);  

	/**
	 * Method main attempts to read the executable directory from directory_do_not_delete.txt and use it to define contentExeRun. 
	 * It also writes all shell commands to file, and checks to make sure all required folders have been created. Then calls method run().
	 * 
	 * @param args
	 * @throws IOException
	 * 
	 * @author Kevin Clark
	 */
	
	public static void main(String[] args) throws IOException 
		{
			try 
				{
					List<String> allLines = Files.readAllLines(Paths.get(System.getProperty("user.home") + "\\AppData\\Roaming\\FAD\\directory_do_not_delete.txt"));
					exeDir = Paths.get(allLines.get(0));
				} 
			catch (IOException e) 
				{
					e.printStackTrace();
					System.exit(1);
				}
			contentExeRun = "& " + "\"'" + exeDir + "'\"" + " -wild" + " -aus" + " -tracked" + " -fdir " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\"" +  " -out_dir " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\"";
			contentPshllWrt = contentFileExtCheck  + "\r\n" + createImportDir + "\r\n" + createExportImportDateDir + "\r\n" + createExportDateDataDir + "\r\n" + contentExeRun + "\r\n" + contentMvImpToExpDate + "\r\n" + contentMvExptoExpDate + "\r\n" + contentMvExpDatetoData;
			txtPath(pShell, contentPshllWrt);	
			runShell(createImportDir, "Done");
			runShell(createExportImportDateDir, "Done");
			runShell(createExportDateDataDir, "done");
			run();
	}
	
	/**
	 * Method run, in a try/catch block, calls multiple shell commands and method chartExecutor within class CSVBarChart to create all necessary barcharts, 
	 * before finishing the movement of all files and exiting the appliction
	 * 
	 * @author Kevin Clark
	 */
    public static void run () 
    	{
			try 
				{
					runShell(contentFileExtCheck, "Checked File Extensions");
					runShell(contentExeRun, "FadAnalyzer has completed");
			    	runShell(contentMvImpToExpDate, "Moved all eligible files from FAD-Import to Fad-Export\\Import-"+ strDate);
			    	runShell(contentMvExptoExpDate, "Moved photos from Export directory to \\Export-" + strDate);	    	
			    	CSVBarChart.chartExecutor();			
					runShell(contentMvExpDatetoData, "Moved data file to FADData" + "\n" + "Facial Activity Detection analysis complete.");			
			    	System.exit(0);
				} 
			catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    }
	 
    /**
     * Method txtPath is the method that takes the file path and shell commands and writes them to a powershell file
     * 
     * @param flNm
     * @param contents
     */
    public static void txtPath (String flNm, String contents) 
    	{   	
    		try 
    			{
		            // Java 11 , default StandardCharsets.UTF_8
		            Files.write(Paths.get(pShell), contentPshllWrt.getBytes());
		
		            // encoding
		            Files.write(Paths.get(pShell), contentPshllWrt.getBytes(StandardCharsets.UTF_8));

    			}
    		catch (IOException e)
    			{
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
    public static void runShell(String content, String result) throws IOException 
    	{
	    	
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
	    	while ((line = stdout.readLine()) != null)
	    		{
	    			System.out.println(line);
    			}
	    	stdout.close();
	    	
	    	//Getting any errors
	    	System.out.println("Standard Error:");
	    	BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
	    	while ((line = stderr.readLine()) != null) 
	    		{
	    		
	    		System.out.println(line);
	    		}
	    	stderr.close();
	    	
	    	System.out.println(result);
     }
 }