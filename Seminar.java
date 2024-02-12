import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Seminar.java
 * @author Andrew Zhou 
 * @since 2/11/24
 * @version 1.0.1
 * The Seminar class holds various methods, data structures, and variables which help to convert the file that the user provides to a schedule arrangement for the organizers of the event.
 * The file is processesd and reorganized and after various procedures and methods within the class, printed output will appear based on user input, accomplishing the goal of
 * arranging a schedule while attempting to minimize collisions.
 */

public class Seminar {
	/* The Seminar class basically represents the entire event hosted by the school. It holds various important methods, data structures, and variables which help to convert
	 * the given file into a valid output. A file is given as input and after various processing and different procedures, various printed output will appear based on user
	 * input. The Seminar class essentially serves to act as the various body parts of the program processing the input to accomplish the goal of placing all students in classes while attempting to minimize collisions.
	 * */
	

    final int numClass = 5;
    final int numTime = 5;
    final int maxSize = 16;

    //A 2d matrix size 5X5 which represents the course located in each row (representing time slot) and column (representing Classroom)
    int[][] classSchedule = new int[numTime][numClass];

    //ArrayList of all student names
    ArrayList<String> names = new ArrayList<String>();
    
    //ArrayList of course names    
    ArrayList<String> courses = new ArrayList<String>();
    //2d Matrix which represents enrollemnt number for each session
    int[][] studentNumber = new int[numTime][numClass];
    //2d matrix represents all of the choices of each individual
    ArrayList<int[]> choices = new ArrayList<int[]>();
    //2d matrix which will ultimately be the final arrangment of the program after placement and finishign procedures
    int[][] actualSchedule;

    public Seminar(String filename) {
		/* This function is actually a constructor and what it does is it takes the filename as a String input through one of its parameters. 
		 * After it recieves this input, it parses the data with fileio code (from w3schools), makes various adjustments to the data if there are things like uneccesary spaces
		 * and works to convert file informaiton into the various data structures for the function to run. There is no return or output to this function.
		 * */
		

      //FileIo code from w3schools
      try {	
        //File Input
        File name = new File(filename);
        Scanner s1 = new Scanner(name);  
        
        while (s1.hasNextLine()) {
          String data = s1.nextLine();
          //Parses the commas
          String[] array = data.split(",");
          //importing names
          names.add(array[2]);
          //importing courses, there is a conditional to not import into course arraylist if there is just spaces in the file
          if(array[8].charAt(0)!=' ') {
            courses.add(array[8]);
          }
          //choices
          int temp[] = new int[5];
          for(int i=0; i<5; ++i) {
            String place = array[i+3];
            //If there are spaces like for example ' 23' it will be parsed to just '23' to allow parseInt to be used
            if(place.charAt(0)==' ') {
                temp[i] = Integer.parseInt(place.substring(1,2));
            } else {
                temp[i] = Integer.parseInt(place.substring(0,2));
            }
          }
          //Adds choices of the individual represented through a size 5 temp array into the larger 2d matrix that is choices (holding the choices of all individuals)
        choices.add(temp);        
        }
        //Initializes acutal schedule to have enough rows to represent all individuals
        actualSchedule = new int[names.size()][5];
        for(int i=0; i<actualSchedule.length; ++i) {
			for(int j=0; j<5; ++j) {
				//Fills them with -1 to represent that each space is empty
				actualSchedule[i][j]=-1;
			}
		}
		
        
        s1.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } 
  }
  
  //These two arraylists are connected with coursevote representing the "popularityIndex" while courseID is the corresponding ID (1-18 in this case) identifying the course name, same index as in coursevote in courseID will return the coursename of the corresponding popularityIndex in coursevote
  ArrayList<Integer> courseID = new ArrayList<Integer>();
  ArrayList<Integer> coursevote = new ArrayList<Integer>();
  public void popularRank() {
	 /*This function does not take in any parameters as input and does not return any output. It initializes the courseID and coursevote data structure
	  * and works to iterate through the user choices to assign a popularityIndex based on how often it appears and what vote individuals put it at "first vote or 5th vote".
	  * It then uses this popularityIndex to create duplicatesif the popularity is over a set value.
	  * */ 
	  
     //Initialization of CourseID and coursevote and setting its size
     for(int i=0; i<courses.size()+1; ++i) {
        courseID.add(i);
        coursevote.add(0);
     }
    //Using weighting system to add choice matrix votes into coursevote arraylist
     for(int i=0; i<choices.size(); ++i) {
        for(int j=0; j<5; ++j) {
          //Increments the choice value index by 5-j (dependent on index)
          coursevote.set(choices.get(i)[j], coursevote.get(choices.get(i)[j])+(5-j));
        }
     }
     //First slot is a buffer it is useless it is used to sync the two arraylists so courseID with 1 will be placed in index 1 of the arrayList
     coursevote.set(0, 0);
     //Uses modulus to create duplicates
    int length = coursevote.size();
     for(int i=0; i<length; ++i) {
		 //If courses's popularityIndex is above 80, duplciate it 
        if(coursevote.get(i)>80) {
			//decrements popularityIndex of second one because attention is now split between main and duplciate
          coursevote.add(coursevote.get(i)-80);
          coursevote.set(i, 80);
          courseID.add(i);
        }
     }
 }
 public void selectionSort() {
	 /* This function does not take in any parameters as input and does not return any output. It serves to sort the coursevote data structure while syncing the courseID arrayList
	  * storage with it. It puts the most popular courses at the front and less popular courses at the back (includes the duplciates), which utlimately plays its role in helping
	  * the arrangmenet function for the classSchedule
	  * */
  //Selection sort sorts the coursevotes from largest to smallest (adjusts the courseID array in conjunction to maintain identifaction)
    for(int i=0; i<coursevote.size()-1; ++i) {
      int maxIndex=i;
      for(int j=i+1; j<coursevote.size(); ++j) {
        if(coursevote.get(j)>coursevote.get(maxIndex)) {
          maxIndex=j;
        }
      }
      //Swapping coursevotes
      int temp = coursevote.get(i);
      coursevote.set(i, coursevote.get(maxIndex));
      coursevote.set(maxIndex, temp);
      //Now Swap courseIDs to keep the two arrayLists in sync
      temp = courseID.get(i);
      courseID.set(i, courseID.get(maxIndex));
      courseID.set(maxIndex, temp);
    }    
  }
  

  public void synthesis(ArrayList<Integer>ID) {
	  /* This function takes in a parameter ID (an ArrayList), which is essentially a passed in courseID, with the courses ranked by popularityIndex, however, the function does not return
	   * anything as output. The function's purpose is to act as an arrangement method for classSchedule, with a rough idea of placing popular classes with less popular classes to minimize
	   * collision and competition in the same time slot.
	   * */
	  //Used later in the function and copy is needed because ID will be modified
	ArrayList<Integer> copy = new ArrayList<Integer>();
	for(int i=0; i<ID.size(); ++i) {
	  copy.add(ID.get(i));	
    }
	
  //Places the courses with the most popular classes being paired with relatively unpopular classes
    for(int i=0; i<numTime; ++i) {
      //Reserve classroom 1 for the most popular classes
      classSchedule[i][0] = ID.get(0);
      ID.remove(0);
    }  
    for(int i=0; i<numTime; ++i) {
    for(int j=1; j<numClass; ++j) {
      if(ID.size()==0) {
		//Is a blank
		ID.add(0);  
      }
      //Takes the least popular class, places it in the row
      classSchedule[i][j] = ID.get(ID.size()-1);
      //Then deletes it from the arrayList not to be used again (repeating the cycle, having a new least popular class)
      ID.remove(ID.size()-1);
    }
	}
	if(courseID.size()<25) {
	//Fill in procedure if there are "free bells"/holes within the classSchedule
	  
	  int pointer=0;
	  
		for(int i=0; i<numTime; ++i) {
		  for(int j=0; j<numClass; ++j) {
			  //If there is a zero, which signifies free bell/open space
		    if(classSchedule[i][j]==0) {
			  boolean condition=true;
			  
			  //Traverses sorted popularityIndex coursevote
			  while(condition==true) {
				 condition=false;
			    for(int k=0; k<copy.size(); ++k) {
				  if(k==pointer) {
				    //boolean set to false so if it does not get switched then the loop will stop (like here)
				    continue;	  
				  }
				  //Conditional to check if a course is already duplicated sets back to true to continue loop (want to find unduplicated course)
				  if(copy.get(pointer)==copy.get(k)) {
				    condition=true;	
				    ++pointer;  
				    break;
				  }	
			    }	  
			  }
			  //Sets unduplicated course to be duplicated and placed in classSchedule matrix to fill in free bels
			  classSchedule[i][j]=copy.get(pointer);
			  ++pointer;
			  
			}	  
	      }	
	    } 
	}
	
	
	//Small Optimization
	//If there are the same type of classes that share the same bell, they are rearranged
	
	
	for(int i=0; i<numTime; ++i) {
		//Checks if there is a duplicated part
	  int dupIndex = findDup(classSchedule[i]);
	  //-1 means no duplicate 
	  if(dupIndex!=-1) {
		int switchIndex;
		//Switches with the time slot below it (if there is duplciate in the same time slot), unless it is the last row which switches with the time slot above it (duplication removal in the same time slot)
		if(i==numTime-1) {
		  switchIndex=i-1;
		} else {
		  switchIndex=i+1;	
		}
		//Change up which rearranges the classSchedule matrix to avoid such duplication
        changeUp(i, switchIndex, dupIndex, classSchedule);
	  }
    }
  }
  public void changeUp(int original, int newIndex, int dup, int[][] schedule) {
	  /* The purpose of this function is to change up the classSchedule in the case that two class duplicates end up in the same time slot (since teachers cannot teach in two classrooms at once).
	   * It switches the duplicate class with classes in nearby time slots until neither the exchanged time slot nor the originally duplicated
	   * time slot end up having any duplicates within that "row". The function takes in the row index (integer) with duplicated classes as input (called original), the row 
	   * index (integer) that is supposed to have some of its classes exchanged with the "duplicated row" (called new Index), the index (integer) of the duplicated value (dup), and the
	   * schedule matrix (2d array) which is basically a passed in classSchedule matrix, all as paremeters. However, there is no direct output for this function.
       * */
	  //Original with duplicate contained
	  int[] test1 = schedule[original];
	  //Array which is going to be switched with the one with a duplicate
	  int[] test2 = schedule[newIndex];
	  
	  
	  int counter;
	  int increment;
	  //Unpopular classes tend to be on the right, and popular on the left
	  //So, if a class has a duplicate in the same time slot and is fairly popular, more on the left, it should start check switching from the left, to not create to much unbalance and competition among competitive courses
	  //Adjusts the starting index based on presumed popularity (assessed based on position of the duplicate)
	  if(dup>2) {
		  //Starts from the right go left
	    counter=5;
	    increment=-1;
	  } else {
		  //Start form the left go right
	    counter=0;
	    increment=1;	  
	  }
	  //Continues until there are no duplicates in both the original and the switched arrays
	  while(!(findDup(test1)==-1 && findDup(test2)==-1)) {
	     counter+=increment;
	     //switch procedure
	     int temp = test1[dup];
	     test1[dup] = test2[counter];
	     test2[counter] = temp;
	  }
	  //sets the new switched arrays to the classSchedule matrix
	  classSchedule[original] = test1;
	  classSchedule[newIndex] = test2; 
  }
  
  public int findDup(int[] input) {
	 /*This function is an auxillary function and works in conjunction with changeUp where its purpose is to search for duplicates in the 5 index array in each 
	  *row of classSchedule. It takes in an array of integers (courseIDs in a time slot) and outputs an integer (index of the duplicate value or -1 if there is no duplicate) as a return.
	  * */
	  
    int max=-1;
    //Finds max number in array  
    for(int i=0; i<input.length; ++i) {
	  if(input[i]>max) {
		max=input[i];  
      }	
    }
    //Creates map with size of max number
    int[] map = new int[max+1];
    for(int i=0; i<input.length; ++i) {
		//Checks if the element has already appeared in the frequency map, if it has, the function returns the index of the duplicated element (the second occurance at least)
	  if(map[input[i]]!=0) {
	    return i;
	  }
	  //While iterating through the inputted array, increment the element in the frequency map (setting it to 1 to signify that it has appeared)
	  map[input[i]]=1;
	}
	return -1;	  
  }
  
  // ---------------------------------------------------------------------------


  public void placement() {
	  /* The purpose of this function is to place the individuals into their courses based on their choices that they made. 
	   * This function is implemented by placing all of the first choices first followed by second choices etc.
	   * There is also a priority system where if an individual's selection is not picked, they have priority in the next round.
	   * The function does not take in any parameteres as input nor does it output/return anything directly.
	   * */
	  
    //5 loops for the 5 selections
     ArrayList<Integer> priority = new ArrayList<Integer>();
     for(int i=0; i<5; ++i) {
       
       //Looping through the choice matrix
       for(int j=0; j<names.size(); ++j) {
         //Check if the nameID is already in the priority list for the layer
         if(!verify(priority, j)) {
           priority.add(j);
         }
       }
       
       //PRIORITY CHECKPOINT MET
       
       //Now the Priority List should be name length
       //Traversing the priority list
       for(int j=0; j<names.size(); ++j) {
         //Find the starting point for the selection using priority list
         int start=-1;
         for(int k=0; k<5; ++k) {
           if(choices.get(priority.get(0))[k]!=-1) {
			   //sets starting value using priority
             start=k;
             break;
           }
         }
         
         //removes strange/incompatible case
         if(start==-1) {
           priority.remove(0);
           continue;
         }

         //Find availiability
         for(int m=start; m<5; ++m) {
           //PRIORITY STORES A LIST OF NAMEIDs
           int value = availability(choices.get(priority.get(0))[m], priority.get(0));
           //IF it is compatible -1 means incompatible
           if(value!=-1) {
             if(m!=start) {
               //Add to priority list if it doesn't get its most optimal solution in the selection
               priority.add(priority.get(0));
             }

             int column=-1;
             //Finds column based on courseId and time slot
             for(int k=0; k<5; ++k) {
			   if(classSchedule[value][k]==choices.get(priority.get(0))[m]) {
				 column=k;   
			   }	 
		     }
		      //Increase enrollment number
		     ++studentNumber[value][column];
             
             //Set the courseID in the appropriate time slot
             actualSchedule[priority.get(0)][value]=choices.get(priority.get(0))[m];
             //Set the choice thing to filled
             choices.get(priority.get(0))[m]=-1;
             break;
           } else {
			   //Fills it in the choice matrix to show that the choice has been achieved or at least tried and failed
             choices.get(priority.get(0))[m]=-1;
           }
         }
         //Removes it from the priority list after done
         priority.remove(0);
       }
       
     }

    
  }
  public int availability(int classID, int person) {
	  /* This function works in conjunction with the placement method. The purpose of this function is to check if there given a courseID
	   * and a specific individual, if that courseID is at all compatible and can fit in that person's schedule. The method takes in the integer courseID
	   * as an input parameter (called classID within this function), and the integer unique ID of the individual (which can be used in the names list to return the corresponding name)
	   * as a parameter as well (called person). The return type of this function is the available/optimal time slot (integer) for the specific course and person, or -1 if the course is incompatile with the individual.   
	   * */
	 
	 //Discards incompatible/"blank" case
	 if(classID==0) {
	   return -1; 	 
	 }
    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<Integer> col = new ArrayList<Integer>();
    //Stores the place or locations of the class on the schedule (both main and duplicates)
    for(int i=0; i<numTime; ++i) {
      for(int j=0; j<numClass; ++j) {
        if(classSchedule[i][j]==classID) {
          //Store time slot only
          list.add(i);
          col.add(j);
        }
      }
    }

   ArrayList<Integer> score = new ArrayList<Integer>();
    //Check if there is a bell is compatible
    //MINI OPTIMIZATION: If there are two options available pick 
    for(int i=0; i<list.size(); ++i) {
		//Check if there is a spot open on the schedule
      if(actualSchedule[person][list.get(i)]==-1) {
		    //Check if the participant size is greater than 16
		if(studentNumber[list.get(i)][col.get(i)]==16) {
		  list.remove(i);
		  col.remove(i);
		  continue;
		}  
        //Adds a "score", which evaluates how good this option is "based on number of participants", (lower score is better for balancing), optimization only applicable when there are multiple options and there can be choice
        score.add(studentNumber[list.get(i)][col.get(i)]);
      } else {
	    list.remove(i);
	    col.remove(i);	  
	  }
    }
    //If there is only one compatible time slot, returns it
   if(score.size()==1) {
	 return list.get(0);
   }
   //If there are two compatible time slots pick the one which has the least enrollment, list and score are linked, ends up returning the valid time slot for the class
   if(score.size()==2) {
	 if(score.get(0)<score.get(1)) {
	   return list.get(0); 
     } else {
	   return list.get(1);	 
	 }
   }
   
    return -1;
  }
    


  
  public boolean verify(ArrayList<Integer> IDs, int j) {
    /*This function acts in conjunction with the placement function. The purpose of the function is to check if the nameID 
     * is already in the priority list for the layer (so that names aren't double added to prioirty). The method takes in 
     * priority as an ArrayList (called IDs) and the unique ID of a person as an integer (called j)
     * */
    for(int i=0; i<IDs.size(); ++i) {
      if(IDs.get(i)==j) {
        return true;
      }
    }
    return false;
  }
  
  public void wildcard() {
	  /* The purpose of this function is to fill in the blank holes that some people have in their schedules after  
	   * the placement procedure (blank holes occur when a selection fails or was not filled out). The function
	   * does not take in any parameters as input nor does it have a direct return type output.
	   * */
	  
  //Special case if asking for class 0 then default actual schedule to -1
  //This loop covers each individual
  for(int i=0; i<actualSchedule.length; ++i) {
    //This loops through each of the 5 time slots
    for(int j=0; j<5; ++j) {
      if(actualSchedule[i][j]==-1) {
        ArrayList<Integer> canidates = new ArrayList<Integer>();
        //This loops over each of the classes within a time slot
        for(int k=0; k<5; ++k) {
			//Checks to makes sure the wildcard selection class is not already taken by the individual in a different time slot and the course enrollment is less than 16
		  if(checker(actualSchedule[i], classSchedule[j][k]) && (studentNumber[j][k]!=16)) {
             canidates.add(k);
		  }	
		}
		//Balancing optimization, it picks out of the avaiable canidates the one with lowest enrollment to maintain balance
		int min=0;
		for(int k=1; k<canidates.size(); ++k) {
		  if(studentNumber[j][canidates.get(k)]<studentNumber[j][canidates.get(min)]) {
		    min=k;	  
		  }	
	    }
	    //Sets the selected canidate to the actualschedule and increments enrollment of that class
	    ++studentNumber[j][canidates.get(min)];
        actualSchedule[i][j]=classSchedule[j][canidates.get(min)]; 

      }
    }
  }

}

public boolean checker(int[] own, int val) {
     /*This function is an auxillary function to wildcard and its purpose is to check if a filled in wildcard selection is already in
      * a person's schedule, which would render it invalid. The input is a row (array) of a person's schedule (called own) and an integer (val) 
      * which is the courseID that wildcard has selected, which needs to be checked. The output is a boolean value true or false, with false
      * returned if the course already exists in a persons's schedule.
      * */	

	//Checks to see if the value appears in array own
	//Auxillary to wildcard, checks if a wildcard fill in class is aleady in a person's schedule (displayed through actualSchedule)
  for(int i=0; i<own.length; ++i) {
    if(own[i]==-1) {
	  continue;
	}
	//If already exists return false
    if(own[i]==val) {
	  return false;
    }	  
  }	
  return true;
}

//Array of ArrayLists data structure which holds the rosters for all of the classes
ArrayList<ArrayList<String>> allname = new ArrayList<ArrayList<String>>();
public void gather() {
	/*The purpose of this function is to go through the individual schedules of all the individuals and person by person, synthesize the overall roster
	 * for every single session on the schedule. There are no input parameters for this function and no direct return type either.
	 * */
	//Initialize to size 25
    for(int i=0; i<25; ++i) {
	  allname.add(new ArrayList<String>());	
	} 
	//Uses row by row of actualSchedule to generate roster
    for(int i=0; i<actualSchedule.length; ++i) {
	//i represents the personID, while j represents the row of classSchedule
	  for(int j=0; j<5; ++j) {
		 //initial placeholder
		int col=-1;
		
		//Uses the row/time slot and courseId to calculate the column, checks the row on classSchedule
		for(int k=0; k<5; ++k) {
	      if(classSchedule[j][k]==actualSchedule[i][j]) {
	        col = k;
	      }	
        }
        //converts row and column to indexes 0 to 24
        //5*row + column correspondance between the two, syncing them together and adds the name of the person corresponding to that row of actualSchedule to the "allname" roster
		allname.get(5*j + col).add(names.get(i));	  
	  }	
    }  
}

public void finish() {
	/*The purpose of this function is to accomodate printed output based on user input such as roster information or individual schedule.
	 * Common information such as time tables and course names are printed with users being prompted for further information that they may
	 * want to know. The function has no input parameters and no direct return output either. 
	 * */
	//A full out printing function
		
    //PRINT TIME TABLE
	System.out.println("\n\nTIME TABLE: ");
	System.out.print("                ");
    for(int i=0; i<numClass; ++i) {
      System.out.print("Classroom "+(i+1)+"                         ");
    }
    System.out.println(" ");
	for(int i=0; i<numTime; ++i) {
      System.out.print("BLOCK "+(i+1)+"       ");
	  for(int j=0; j<numClass; ++j) {	
		 //Abreviates coursre names so it is more readable											              
	    System.out.print(courses.get(classSchedule[i][j]-1).substring(0, 20)+"..."+"            ");	  
	  }	
	  System.out.println("\n\n");
	}
	
	//PRINTS ALL OF THE COURSES AVAILABLE AS A KEY SINCE SOME OF THE NAMES ARE TRUNCATED
    System.out.println("FULL COURSE NAMES:");
	for(int i=0; i<courses.size(); ++i) {
	  System.out.println("\n"+courses.get(i));	
    }
	
	//Handles user input with three modes
	while (true) {
      String mode=" ";
      //Three different modes
	  System.out.println("\n\nType 0 to exit, Type 1 to get the full roster for all sessions, Type 2 to return roster of specific class, Type 3 to search for an individual's full schedule for the day");
	  Scanner m1 = new Scanner(System.in);
	  mode = m1.nextLine();	
	  //Exit
	  if(mode.equals("0")) {
	    break;	  
	  }
	  //Prints every single roster 
	  if(mode.equals("1")) {
		  //Goes through every cell on classSchedule, basically every single available session
	    for(int i=0; i<numTime; ++i) {
		  for(int j=0; j<numClass; ++j) { 
		    System.out.println("\n\nBLOCK"+(i+1)+"  |  Classroom "+(j+1));
		    System.out.println("COURSE NAME: "+courses.get(classSchedule[i][j]-1));
		    System.out.println("ROSTER");
		    
		    //For a specific row and column, convert that to an all name index with 5*row+col and loops through the roster of names and prints them
		    for(int k=0; k<allname.get(5*i + j).size(); ++k) {
			  System.out.println(allname.get(5*i + j).get(k));
		    }
		    	  
		  }	
	    }	  
		  
	  }
	  
	  //Prints specific roster
	  if(mode.equals("2")) {
		  //Similar to later part of mode 1
		  //User input based on time slot and classroom number after looking at the TIME TABLE already printed
		System.out.print("\nTime Block of Class: ");
	    Scanner m2 = new Scanner(System.in);
	    //Remember time slot 1 corresponds to row 0
	    int blockIndex = m2.nextInt()-1;
	    System.out.print("Classroom Number of Class: ");
	    Scanner m3 = new Scanner(System.in);
	    //classIndex similar to blockIndex, same decrement
	    int classIndex = m3.nextInt()-1;
	    
	    //REMEMBER courses 0th index has a courseID of 1 so to get the corresponding name, courseID needs to be subtracted by 1 to sync the courseID with the correct name of the course
	    System.out.println("\nCOURSE NAME: "+courses.get(classSchedule[blockIndex][classIndex]-1));
	    System.out.println("ROSTER");
	    //Prints out all names in that roster corresponding to that particular user identified class
	    for(int i=0; i<allname.get(5*blockIndex + classIndex).size(); ++i) {
	      System.out.println(allname.get(5*blockIndex + classIndex).get(i));
	    }	  
		  
	  }
	  
	  //Individual search mode
	  if(mode.equals("3")) {
		//reformatting names arrayList to get rid of bunch of trailing spaces: "Andrew            " to "Andrew", so that user input of name will match the name stored
		for(int i=0; i<names.size(); ++i) {
		  String initial = names.get(i);
		  for(int j=1; j<initial.length(); ++j) {
			  //If there are two consecuetive spaces (indicates presence of trialing spaces)
		    if(initial.substring(j-1, j).equals(" ") && initial.substring(j, j+1).equals(" ")) {
				//reset the name in the arrayList to a substring of the element without the trailing space, j-1 is the first space of the trailing spaces and the endpoint, which is not included in the substring grab  
			  names.set(i, initial.substring(0, j-1));	
			  //breaks away from loop after completion
			  break;
			}	  
		  }	
	    }  
		  
		//Scanning for user input 
		System.out.print("\n\nName of Individual [Given first name (space) middle name or initial for those who submitted that way (space) last name]: ");
	    Scanner m2 = new Scanner(System.in);
        String name = m2.nextLine();
	    System.out.println("\n CLASSES: ");
	    for(int i=0; i<names.size(); ++i) {
			//Checks user-inputted name exists and if so grabs the index to use for actualSchedule which stores the courseIds for each time slot for each individual
		  if(names.get(i).equals(name)) {
			  //Iterates j, which represents the time slots
		     for(int j=0; j<5; ++j) {
			   System.out.print("\nTIME SLOT "+ (j+1)+"     |");
			   int classNum=-1;
			   //Finds column based on courseID and row
			   for(int k=0; k<5; ++k) {
				   //k represents the columns, iterates
				  if(classSchedule[j][k]==actualSchedule[i][j]) {
					//Column index is one less than the classroom number (classroom 1 corresponds to 0th column), so increment to sync
				   classNum = k+1;	  
				  }   
			   }
                //Printing
			   System.out.print("     CLASSROOM "+classNum+"     |     ");
			   //Courses needs to be offset by one to sync (courseID of 1 is in index 0)
			   System.out.print("COURSE NAME: "+courses.get(actualSchedule[i][j]-1));	 
			 }	  
	      }	
	    }	  
	  }
	  
	}
	
}

}
