/* 
 * File:    FAD_run.java
 * Created: September 2, 2019 3:15:00 PM
 * Author:  Kevin Clark
 * Project: Facial Activity Detection
 * 
 * Updated: January 11, 2020
 * Author: Kevin Clark
 */

package facialActivityDetection_GUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JTextArea;
import java.awt.Font;

/**
 * Class FAD_Launcher_GUI is the preliminary class that sets up and runs the Facial Activity Detection project for the first time.
 * This class creates all the initial files and folders needed to run the main project.
 * 
 * @author Kevin
 *
 */

public class FAD_Launcher_GUI {
	
	/**
	 * Attribute lblSelExe of type JLabel is a label that informs the user what they should look for when they select the first button
	 */
    private static JLabel lblSelExe;
    
    /**
     *Attribute btnExeLoc of type JButton is a user selectable button that opens a file-viewer that displays files ending in .exe. 
     *This allows the user to select FAD.exe. 
     */
    private static JButton btnExeLoc;
    
    /**
     * Attribute btnRun of type JButton is a user selectable button that activates once the user has selected the proper .exe file
     */
    private static JButton btnRun;
    
    /**
     * Attribute content of type String takes in and holds path directories to be written to directory_do_not_delete 
     */
    private static String content;
    
    /**
     * Attribute path of type String takes in the path directory for directory_do_not_delete
     */
    private static String path;

    /**
     * Attribute contentImportDir of type String hold the powershell command that checks for, and if it does not exist, creates directory FAD-import
     */
	private static String contentImportDir= "if(!(Test-Path -Path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\"" + ")){New-Item " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Import" + "\"" + " -ItemType directory -Force}";
	
	/**
	 * Attribute contentEImportDateDir of type String holds the powershell command that checks for, and if it does not exist, creates directory FAD-Export and the sub-folder import-date
	 */
	private static String contentEImportDateDir = "if(!(Test-Path -Path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Import-$((Get-Date).ToString('yyyy-MM-dd'))" + "\"" + ")){New-Item " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Import-$((Get-Date).ToString('yyyy-MM-dd'))" + "\" " + " -Itemtype directory -Force}";
	
	/**
	 * Attribute contentEExportDateDataDir of type String holds the powershell command that checks for, and if it does not exist, creates directory FAD-Export/Export(date), and the sub-folder FADData
	 */
	private static String contentEExportDateDataDir = "if(!(Test-Path -Path " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))\\FADData" + "\"" + ")){New-Item " + "\"" + System.getProperty("user.home") + "\\Pictures\\Fad-Export\\Export-$((Get-Date).ToString('yyyy-MM-dd'))\\FADData" + "\" " + " -Itemtype directory -Force}";
	
	/**
	 * Attribute contentAppData of type String holds the powershell command that checks for, and if it does not exist, creates directory FAD in the host users APPData directory. This is where directory_do_not_delete will live. 
	 */
	private static String contentAppData = "if(!(Test-Path -Path " + "\"" + System.getProperty("user.home") + "\\AppData\\Roaming\\FAD" + "\"" + ")){new-Item " + "\"" + System.getProperty("user.home") + "\\AppData\\Roaming\\FAD" + "\"" + " -ItemType directory -Force}";
	
	/**
	 * Attribute txtWarningSelectingRun of type JTextArea is a text area that contains a warning detailing what FAD creates on the host computer when selecting run.
	 */
	private static JTextArea txtWarningSelectingRun;

	/**
	 * Method main attempts to call runShell in a try/catch block to create the directories where directory_do_not_delete will be placed, then opens the setup GUI
	 * @param args
	 */
    public static void main(String[] args) {
    	try {
			runShell(contentAppData, "Done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		createWindow();
	   }
    
    public static void txtPath (Path txtDir) {
	      
    	try {

              // Java 11 , default StandardCharsets.UTF_8
              Files.write(Paths.get(path), content.getBytes());

              // encoding
              Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));

          } catch (IOException e) {
              e.printStackTrace();
          }
    }
      
    //create GUI window
    private static void createWindow() {    
        JFrame frame = new JFrame("Facial Activity Detection Setup GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI(frame);
        frame.setSize(442, 217);      
        frame.setLocationRelativeTo(null);  
        frame.setVisible(true);
     }
   
    //create GUI items
   private static void createUI(final JFrame frame){  
      JPanel panel = new JPanel();
      panel.setLayout(null);
      
      //create button for Exe location
      btnExeLoc = new JButton("FAD Executable location");
      btnExeLoc.setBounds(21, 25, 147, 23);
      panel.add(btnExeLoc);
      btnExeLoc.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             JFileChooser ExportChooser = new JFileChooser();
             FileFilter filter = new FileNameExtensionFilter("EXE File","exe");
             ExportChooser.setFileFilter(filter);
             int option = ExportChooser.showOpenDialog(frame);
             if(option == JFileChooser.APPROVE_OPTION){
                File file = ExportChooser.getSelectedFile();
                lblSelExe.setText(file.getAbsolutePath());
                btnRun.setEnabled(true);
             }else{
            	 lblSelExe.setText("Open command canceled");
             }
          }
       });
      
      //Create label alongside EXE location button
      lblSelExe = new JLabel("Please select FADAnalyzer.exe ");
      lblSelExe.setBounds(178, 25, 238, 23);
      panel.add(lblSelExe);
      

      frame.getContentPane().add(panel, BorderLayout.CENTER);
           panel.setLayout(null);            
           
         //Create button run
           btnRun = new JButton("Run");
           btnRun.setEnabled(false);
           btnRun.addActionListener(new ActionListener() {
           	public void actionPerformed(ActionEvent arg0) {
           		//creates file containing path directory information 
           		content = lblSelExe.getText() + "\r\n" + System.getProperty("user.home") + "\\Pictures\\Fad-Export" + "\r\n" + System.getProperty("user.home") + "\\Pictures\\Fad-Import"; 
           		//sets path new file is created in
           		path = System.getProperty("user.home") + "\\AppData\\Roaming\\FAD\\directory_do_not_delete.txt";
           		txtPath(Paths.get(path));
           		frame.dispose();
           		try {         
           			runShell(contentImportDir, "Done");
           			runShell(contentEImportDateDir, "Done");
           			runShell(contentEExportDateDataDir, "Done");
					GuiWatcher_GUI.Watcher();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           	}
           });
           btnRun.setBounds(118, 70, 171, 23);
           panel.add(btnRun);
           
           txtWarningSelectingRun = new JTextArea();
           txtWarningSelectingRun.setEditable(false);
           txtWarningSelectingRun.setFont(new Font("Tahoma", Font.PLAIN, 11));
           txtWarningSelectingRun.setText("Warning! Selecting Run will allow FAD to create\r\nfolders in your C:\\Users\\Kevin\\Pictures directory \r\nand create and move files within the created directories.");
           txtWarningSelectingRun.setBounds(68, 103, 289, 57);
           panel.add(txtWarningSelectingRun);

       }
   
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
