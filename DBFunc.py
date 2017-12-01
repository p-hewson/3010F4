#Code by Steven D'Souza

#!TO DO LIST!#
#Check what input values are needed for the decoder likely using string

import MySQLdb, glob, os

db = MySQLdb.connect("localhost", "Team_F4", "sysc3010", "sysc3010") #MySQL login credentials
curs = db.cursor() #Use for manipulation of the database
filePath = '/home/pi/Documents/sysc3010/Data Files/' #where files are stored on the pi
os.chdir(filePath)#Changing the file path to where the data is

#dataTest = ["1243-01-23", "543.234", "Did it work"] #variable for testing createText

#Show the table collected_data in database sysc3010 onto the terminal 
#collected data shows all the data that has been collected by the arduinos
def showDataDB():
	
	print ("\nsensor_id      Date      Time      Temmperature      Tilt")
	print ("============================================================")
	
	curs.execute("SELECT * FROM collected_data")
	data = curs.fetchall()
        
	for row in data:
            print (row)
#Show the table info in the database sysc3010 onto the terminal
#info shows the info abouth each arduino collecting data
def showInfoDB():
	
	print ("\nsensor_id      Name      Location      Time Interval")
	print ("======================================================")
	
	
	curs.execute("SELECT * FROM info")
	data = curs.fetchall()
        
	for row in data:
            print (row)

#Creates a text file with a user specified file name
def createText(fileName, data):
    startString = ""
    f = open(fileName +'.txt', 'a+')
    for i in range(len(data)):
        startString += data[i] + "\t"
    startString += "\n"
    f.write(startString)
    f.close
    showFiles()

#Show all files available for maniplation
def showFiles():
	#os.chdir(filePath)
	for file in glob.glob("*"):
		print(file)
		
#Print the contents of the specified file
def printFile(fileName):
    if os.path.isfile(fileName):
        with open(fileName, 'r') as f:
            print (f.read())
    else:
        print("File does not exist")
        
#delete file
def fileDelete(fileName):
	if os.path.isfile(fileName):
                os.remove(fileName)
                showFiles()
	else:
                print("File does not exist")

#Sends any data to be appended to a text document for further use
#This function should automatically decode byte values
def dataDecode(fileName, byteData):

	stringData = '' #A starting variable to store the decoded data collected 
	
	for i in range(sizeByteData):#sizeByteData is a temporary value that will have to replaced by a functional variable 
		#Attempts to format the decoded values nicely into string, tabs may need to be formatted properly
		stringData += byteData.decode('utf-8') +'\n' 
	
	#This statement opens the specified file name, or creates a new file if it has not been made already,
	#to append data to
	f = open(fileName + '.txt', "a+")
	#an if or try statement should probably be added to see if the fileName ends with .txt if the staetment does not work
	
	#Iterate through the string and append the string to a text document until the end of the data has been reached
	#Append formatting needs to be improved upon
	for i in range(sizeStringData):#stringSizeData is a temporary value that will have to replaced by a functional variable 
		f.write(stringData % (i+1))
	
	f.close()
	
	#Print the contents of the new text file
	printFile(fileName)
	
	#Print statement shows where the file is located for further manipulation
	print("\nNew data has been sent to ", filePath,fileName,"\n")

#A user specified txt file is chosen to import into the database
def appendTextToDB(fileName):
        if os.path.isfile(fileName):
                #open the specified file name, there is only one file which the user can access
                f = open(fileName, 'r')
                #copy the contents of the file to variable file_content
                fileContent = f.read()
                f.close()	
                userChoice = 'x'
                #show the file contents to the user for confirmation that they selected the right file
                print(fileContent)
                #Ask if they still want to append this file
                while userChoice not in ['y', 'n']:
                        userChoice = input("\n would you like to append " + fileName + "? (y/n): ")
                        userChoice.lower()
                
                #This option is when the user still wants to append the file
                if userChoice == 'y':
                        #specifed action for MySQL
                        query = "LOAD DATA LOCAL INFILE " + "\'" + filePath + fileName + "\'" + " INTO TABLE collected_data"
                        
                        print(query)
                        curs.execute(query)
                        db.commit()	
                        showDataDB() 
                        
                        #If the user no longer finds the data useful they can delete the file
                        #This can be moved to another function if needed
                        userChoice = 'x'
                        while userChoice not in ['y', 'n']:
                                userChoice = input("\n would you like to delete " + fileName + "? (y/n): ")
                                userChoice.lower()
                
                        if userChoice == 'y':	
                                fileDelete(fileName)
                        elif userChoice == 'n':
                                print(fileName + " is located at " + filePath + fileName + "\n")
                        
                
                elif userChoice == 'n':
                        print("File append has been canceled\n")		
                db.close()
        else:
            print("File does not exist")
			
#This is used for testing functions dealing with files by creating a file with a user specified 
#number of lines
def createTextTest(filename, lines):
    f = open(filename, 'a+')
    for lines in range(0, lines):
        f.write("\\N\t1234-10-23\t533.972\tTesting\n")
    f.close
    showFiles()