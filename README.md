***************************************************************************************************************************************************************************************************************************************
Welcome to The Facial Activity Detection app designed by Kevin Clark.

Facial Activity Detection provides Action Unit (AU) analysis of user selected images. 
For more information on AUs please see the following links:



Facial Action Coding System wiki
https://en.wikipedia.org/wiki/Facial_Action_Coding_System

Paul Ekman Group - designer and trainer of the Facial Action Coding System
https://www.paulekman.com/

***************************************************************************************************************************************************************************************************************************************
!!!Warning!!! 
First run through of each application may throw anti-virus false positives. If so, give anti-virus permissions, move photos back to the original import folder from export\export-(date) and run a second time.

***************************************************************************************************************************************************************************************************************************************
Please make sure you have the most recent Java JDK installed as well as x64(vc_redist.x64.exe) & x86 (vc_redist.x86.exe) Visual C++ Redistributable. Installation locations in links below:

https://www.oracle.com/technetwork/java/javase/downloads/index.html

https://support.microsoft.com/en-us/help/2977003/the-latest-supported-visual-c-downloads (should be included in JDK)

***************************************************************************************************************************************************************************************************************************************
This application utilizes and requires the OpenFace Toolkit which can be found at https://github.com/TadasBaltrusaitis/OpenFace

Facial Activity Detection was designed for academic use only, and is expected to receive no updates or further usage.


***************************************************************************************************************************************************************************************************************************************
SETUP

This Git repository contains two significant branches. 

The master branch FAD_Gui must be run first, it's prime class is FAD_Launcher_GUI. 
Saving this project as an .exe will result in the file FAD_setup.exe mentioned in the user guide below. 

The second branch FAD_NO_GUI can be run once FAD_Gui has been run one time provided the .exe file mentioned in step 1 of the user guide does not move.

***************************************************************************************************************************************************************************************************************************************
User Guide:

1. Run Facial Activity Detection GUI (FAD_Setup.exe)
	- This will launch a graphical user interface asking the user to select
	  the executable file from the OpenFace Toolkit
	- upon selection of .exe and selecting run the program will comence an initial run 
	  creating the following items
		- "C:\Users\$User\Pictures\Fad-Import"
			*Destination where user will put the images they want analyzed
			*NOTE: ONLY .jpg, jpeg, .bmp, .png FILES CAN BE ANALYZED all other files and any items in subfolders will be ignored
		- "C:\Users\$User\Pictures\Fad-Export"
			*Desination analyzed images and data will initialy be moved to
		- "C:\Users\Kevin\Pictures\Fad-Export\Import-(date 'yyyy-mm-dd)"
			*Desination original images will be moved to
		- "C:\Users\$User\Pictures\Fad-Export\Export-(date 'yyyy-mm-dd)"
			*Destination analyzed photos and Action Unit graphs will be moved to
		- "C:\Users\$User\Pictures\Fad-Export\Export-(date 'yyyy-mm-dd)\FADData"
			*Destination detailed image analysis data will be moved to
		- directory_do_not_delete (if this is removed or changed launch FacialActivityDetection_GUI to restore)
		- fadScript.ps1 a backup and editable PowerShell script that provides most of the applications basic funcionality should any errors occur

2. Once initial items are created the user can export or move photos to "C:\Users\$User\Pictures\Fad-Import" for analysis.

3. To analyze photos the user can either launch FacialActivityDetection_GUI which will require the user to again select the .exe, 
   OR the user can run Facial Activity Detection.exe which will run through all the images in "C:\Users\$User\Pictures\Fad-Import" without any additonal user input. 

***************************************************************************************************************************************************************************************************************************************

Possible Errors:

	ERROR 1
 	- "The code execution cannot proceed beacuse MSVCP140.dll was not found. Reinstalling the program may fix this problem."
 	- "The code execution cannot proceed beacuse VCRUNTIME140.DLL was not found. Reinstalling the program may fix this problem."
 	- "The code execution cannot proceed beacuse CONCRT140.DLL was not found. Reinstalling the program may fix this problem."
	SOLUTION 1
	- Ensure Visual C++ Redistributable is installed

	ERROR 2
	- .jar file asks how it should be opened
	Solution 2
	- Ensure JDK is installed, although will function with JRE

	ERROR 3
	- Graphs are not displayed for images in Fad-Export\Export-(date)
	Solution 3
	- Ensure JDK is installed, WILL NOT WORK with only the JRE

	ERROR 4
	- Folders have not been created in Pictures directory
	SOLUTION 4
	-Check your current directory, many PCs display a Pictures Directory in OneDrive C:Users\$User\OneDrive\Pictures, Picture are created in C:\Users\$user\Pictures.
	
***************************************************************************************************************************************************************************************************************************************
 
