import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Seminar {

    final int numClass = 5;
    final int numTime = 5;
    final int maxSize = 16;

    int[][] classSchedule = new int[numTime][numClass];

    ArrayList<String> names = new ArrayList<String>();
    
    ArrayList<String> courses = new ArrayList<String>();
    int[][] studentNumber = new int[numTime][numClass];
  
    ArrayList<int[]> choices = new ArrayList<int[]>();
    int[][] actualSchedule;

    public Seminar(String filename) {

      //From w3schools
      try {	
        //File Input
        File name = new File(filename);
        Scanner s1 = new Scanner(name);  
        
        while (s1.hasNextLine()) {
          String data = s1.nextLine();
          String[] array = data.split(",");
          //names
          names.add(array[2]);
          //courses
          if(array[8].charAt(0)!=' ') {
            courses.add(array[8]);
          }
          //choices
          int temp[] = new int[5];
          for(int i=0; i<5; ++i) {
            String place = array[i+3];
            if(place.charAt(0)==' ') {
                temp[i] = Integer.parseInt(place.substring(1,2));
            } else {
                temp[i] = Integer.parseInt(place.substring(0,2));
            }
          }
        choices.add(temp);        
        }
        
        actualSchedule = new int[names.size()][5];
        for(int i=0; i<actualSchedule.length; ++i) {
			for(int j=0; j<5; ++j) {
				actualSchedule[i][j]=-1;
			}
		}
		
        
        s1.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } 
  }


  ArrayList<Integer> courseID = new ArrayList<Integer>();
  ArrayList<Integer> coursevote = new ArrayList<Integer>();
  public void popularRank() {
     //Initialization of CourseID with 0,1,...n and creating n+1 elements in coursevote array list
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
     //First slot is a buffer it is useless
     coursevote.set(0, 0);
     //Uses modulus to create duplicates
    int length = coursevote.size();
     for(int i=0; i<length; ++i) {
        if(coursevote.get(i)>80) {
          coursevote.add(coursevote.get(i)-80);
          coursevote.set(i, 80);
          courseID.add(i);
        }
     }
 }
 public void selectionSort() {
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
      //Now Swap courseIDs
      temp = courseID.get(i);
      courseID.set(i, courseID.get(maxIndex));
      courseID.set(maxIndex, temp);
    }    
  }
  

  public void synthesis(ArrayList<Integer>ID) {
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
      classSchedule[i][j] = ID.get(ID.size()-1);
      ID.remove(ID.size()-1);
    }
	}
	if(courseID.size()<25) {
	//Fill in procedure
	  
	  int pointer=0;
	  
		for(int i=0; i<numTime; ++i) {
		  for(int j=0; j<numClass; ++j) {
		    if(classSchedule[i][j]==0) {
			  boolean condition=true;
			  
			  while(condition==true) {
				 condition=false;
			    for(int k=0; k<copy.size(); ++k) {
				  if(k==pointer) {
				    continue;	  
				  }
				  if(copy.get(pointer)==copy.get(k)) {
				    condition=true;	
				    ++pointer;  
				    break;
				  }	
			    }	  
			  }
			  
			  classSchedule[i][j]=copy.get(pointer);
			  ++pointer;
			  
			}	  
	      }	
	    } 
	}
	
	
	//Small Optimization
	//If there are the same type of classes that share the same bell, they are rearranged
	
	
	for(int i=0; i<numTime; ++i) {
	  int dupIndex = findDup(classSchedule[i]); 
	  if(dupIndex!=-1) {
		int switchIndex;
		if(i==numTime-1) {
		  switchIndex=i-1;
		} else {
		  switchIndex=i+1;	
		}
        changeUp(i, switchIndex, dupIndex, classSchedule);
	  }
    }
      //PRINT SCHEDULE
	 System.out.println("\n\n");
	 System.out.println("CLASS SCHEDULE: ");
    for(int i=0; i<numTime; ++i) {
	  for(int j=0; j<numClass; ++j) {
		 System.out.print(classSchedule[i][j]+" "); 
	  }
	  System.out.println(" ");
    }
  }
  public void changeUp(int original, int newIndex, int dup, int[][] schedule) {
	  int[] test1 = schedule[original];
	  int[] test2 = schedule[newIndex];
	  
	  
	  int counter;
	  int increment;
	  //CONFIGURE SO USE LEFT IF POPULAR AND RIGHT IF UNPOPULAR
	  if(dup>2) {
	    counter=5;
	    increment=-1;
	  } else {
	    counter=0;
	    increment=1;	  
	  }
	  while(!(findDup(test1)==-1 && findDup(test2)==-1)) {
	     counter+=increment;
	     int temp = test1[dup];
	     test1[dup] = test2[counter];
	     test2[counter] = temp;
	  }
	  classSchedule[original] = test1;
	  classSchedule[newIndex] = test2; 
  }
  
  public int findDup(int[] input) {
    int max=-1;
    for(int i=0; i<input.length; ++i) {
	  if(input[i]>max) {
		max=input[i];  
      }	
    }
    int[] map = new int[max+1];
    for(int i=0; i<input.length; ++i) {
	  if(map[input[i]]!=0) {
	    return i;
	  }
	  map[input[i]]=1;
	}
	return -1;	  
  }
  
  // ---------------------------------------------------------------------------


  public void placement() {
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
         //Find the starting point
         int start=-1;
         for(int k=0; k<5; ++k) {
           if(choices.get(priority.get(0))[k]!=-1) {
             start=k;
             break;
           }
         }
         
         
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
             //Increase enrollment number
             int column=-1;
             for(int k=0; k<5; ++k) {
			   if(classSchedule[value][k]==choices.get(priority.get(0))[m]) {
				 column=k;   
			   }	 
		     }
		     ++studentNumber[value][column];
             
             //Set the courseID in the appropriate time slot
             actualSchedule[priority.get(0)][value]=choices.get(priority.get(0))[m];
             //Set the choice thing to filled
             choices.get(priority.get(0))[m]=-1;
             break;
           } else {
             choices.get(priority.get(0))[m]=-1;
           }
         }
         priority.remove(0);
       }
       
     }

    
  }
  public int availability(int classID, int person) {
	 if(classID==0) {
	   return -1; 	 
	 }
    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<Integer> col = new ArrayList<Integer>();
    //Stores the place or locations of the class on the schedule
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
        //Returns the compatible time slot
        score.add(studentNumber[list.get(i)][col.get(i)]);
      } else {
	    list.remove(i);
	    col.remove(i);	  
	  }
    }
   if(score.size()==1) {
	 return list.get(0);
   }
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
    //Check if the nameID is already in the priority list for the layer
    for(int i=0; i<IDs.size(); ++i) {
      if(IDs.get(i)==j) {
        return true;
      }
    }
    return false;
  }
  
  public void wildcard() {
  //Special case if asking for class 0 then default actual schedule to -1
  //This loop covers each individual
  for(int i=0; i<actualSchedule.length; ++i) {
    //This loops through each of the 5 time slots
    for(int j=0; j<5; ++j) {
      if(actualSchedule[i][j]==-1) {
        ArrayList<Integer> canidates = new ArrayList<Integer>();
        //This loops over each of the classes within a time slot
        for(int k=0; k<5; ++k) {
		  if(checker(actualSchedule[i], classSchedule[j][k]) && (studentNumber[j][k]!=16)) {
             canidates.add(k);
		  }	
		}
		int min=0;
		for(int k=1; k<canidates.size(); ++k) {
		  if(studentNumber[j][canidates.get(k)]<studentNumber[j][canidates.get(min)]) {
		    min=k;	  
		  }	
	    }
	    ++studentNumber[j][canidates.get(min)];
        actualSchedule[i][j]=classSchedule[j][canidates.get(min)]; 

      }
    }
  }
  //PRINT WILDCARD FILL INS
    System.out.println("ACTUAL SCHEDULE: ");
    for(int i=0; i<names.size(); ++i) {
	  for(int j=0; j<5; ++j) {
	    System.out.print(actualSchedule[i][j]+" ");	  
	  }	
	  System.out.println(" ");
    }
    System.out.println("CLASS OCCUPANCY: ");
    for(int i=0; i<numTime; ++i) {
	  for(int j=0; j<numClass; ++j) {
	    System.out.print(studentNumber[i][j]+" ");	  
	  }	
	  System.out.println(" ");
	}
}

public boolean checker(int[] own, int val) {
  for(int i=0; i<own.length; ++i) {
    if(own[i]==-1) {
	  continue;
	}
    if(own[i]==val) {
	  return false;
    }	  
  }	
  return true;
}

ArrayList<ArrayList<String>> allname = new ArrayList<ArrayList<String>>();
public void gather() {
	//Initialize to size 25
    for(int i=0; i<25; ++i) {
	  allname.add(new ArrayList<String>());	
	} 
    for(int i=0; i<actualSchedule.length; ++i) {
	//i represents the personID, while j represents the row of classSchedule
	  for(int j=0; j<5; ++j) {
		int col=-1;
		
		//checks the row on classSchedule
		for(int k=0; k<5; ++k) {
	      if(classSchedule[j][k]==actualSchedule[i][j]) {
	        col = k;
	      }	
        }
    
		allname.get(5*j + col).add(names.get(i));	  
	  }	
    }  
}

public void finish() {
		
    //PRINT TIME TABLE
	System.out.println("TIME TABLE: ");
	System.out.print("                ");
    for(int i=0; i<numClass; ++i) {
      System.out.print("Classroom "+(i+1)+"                         ");
    }
    System.out.println(" ");
	for(int i=0; i<numTime; ++i) {
      System.out.print("BLOCK "+(i+1)+"       ");
	  for(int j=0; j<numClass; ++j) {												              
	    System.out.print(courses.get(classSchedule[i][j]-1).substring(0, 20)+"..."+"            ");	  
	  }	
	  System.out.println("\n\n");
	}
	
    System.out.println("FULL COURSE NAMES:");
	for(int i=0; i<courses.size(); ++i) {
	  System.out.println("\n"+courses.get(i));	
    }
	
	System.out.println("OUTPUT PANEL: ");
	while (true) {
      String mode=" ";
	  System.out.println("\n\nType 0 to exit, Type 1 to get the full roster for all sessions, Type 2 to return roster of specific class, Type 3 to search for an individual's full schedule for the day");
	  Scanner m1 = new Scanner(System.in);
	  mode = m1.nextLine();	
	  if(mode.equals("0")) {
	    break;	  
	  }
	  if(mode.equals("1")) {
	    for(int i=0; i<numTime; ++i) {
		  for(int j=0; j<numClass; ++j) { 
		    System.out.println("\n\nBLOCK"+(i+1)+"  |  Classroom "+(j+1));
		    System.out.println("COURSE NAME: "+courses.get(classSchedule[i][j]-1));
		    System.out.println("ROSTER");
		    
		    for(int k=0; k<allname.get(5*i + j).size(); ++k) {
			  System.out.println(allname.get(5*i + j).get(k));
		    }
		    	  
		  }	
	    }	  
		  
	  }
	  
	  if(mode.equals("2")) {
		System.out.print("\nTime Block of Class: ");
	    Scanner m2 = new Scanner(System.in);
	    int blockIndex = m2.nextInt()-1;
	    System.out.print("Classroom Number of Class: ");
	    Scanner m3 = new Scanner(System.in);
	    int classIndex = m3.nextInt()-1;
	    
	    System.out.println("\nCOURSE NAME: "+courses.get(classSchedule[blockIndex][classIndex]-1));
	    System.out.println("ROSTER");
	    for(int i=0; i<allname.get(5*blockIndex + classIndex).size(); ++i) {
	      System.out.println(allname.get(5*blockIndex + classIndex).get(i));
	    }	  
		  
	  }
	  
	  
	  if(mode.equals("3")) {
		//reformatting names arrayList to get rid of bunch of trailing spaces 
		for(int i=0; i<names.size(); ++i) {
		  String initial = names.get(i);
		  for(int j=1; j<initial.length(); ++j) {
		    if(initial.substring(j-1, j).equals(" ") && initial.substring(j, j+1).equals(" ")) {
			  names.set(i, initial.substring(0, j-1));	
			  break;
			}	  
		  }	
	    }  
		  
		 
		System.out.print("\n\nName of Individual [Given first name (space) middle name or initial for those who submitted that way (space) last name]: ");
	    Scanner m2 = new Scanner(System.in);
        String name = m2.nextLine();
	    System.out.println("\n CLASSES: ");
	    for(int i=0; i<names.size(); ++i) {
		  if(names.get(i).equals(name)) {
		     for(int j=0; j<5; ++j) {
			   System.out.print("\nTIME SLOT "+ (j+1)+"     |");
			   int classNum=-1;
			   for(int k=0; k<5; ++k) {
				  if(classSchedule[j][k]==actualSchedule[i][j]) {
				   classNum = k+1;	  
				  }   
			   }

			   System.out.print("     CLASSROOM "+classNum+"     |     ");
			   System.out.print("COURSE NAME: "+courses.get(actualSchedule[i][j]-1));	 
			 }	  
	      }	
	    }	  
	  }
	  
	}
	
}

}
