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
    ArrayList<Integer> studentNumber = new ArrayList<Integer>();
  
    ArrayList<int[]> choices = new ArrayList<int[]>();
    int[][] actualSchedule = new int[names.size()][5];
    
    ArrayList<Integer> courseID = new ArrayList<Integer>();
    ArrayList<Integer> coursevote = new ArrayList<Integer>();

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
        
        /*
        //PRINT 
        for(int i=0; i<choices.size(); ++i) {
          for(int j=0; j<5; ++j) {
            System.out.print(choices.get(i)[j]+" ");
          }
          System.out.println("");
        }
        
        for(int i=0; i<actualSchedule.length; ++i) {
			for(int j=0; j<5; ++j) {
				actualSchedule[i][j]=-1;
			}
		}
		*/
        
        s1.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } 
  popularRank();
  }

  public void popularRank() {
     int numDup = numClass*numTime - courses.size();
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
     
     /*
     //PRINT
     for(int i=0; i<coursevote.size(); ++i) {
		System.out.print(coursevote.get(i)+" ");
	 }
	 System.out.println(" ");
	 for(int i=0; i<courseID.size(); ++i) {
		System.out.print(courseID.get(i)+"  ");
	 } */
     
    //Uses modulus to create duplicates
    int length = coursevote.size();
     for(int i=0; i<length; ++i) {
        if(coursevote.get(i)>80) {
          coursevote.add(coursevote.get(i)-80);
          coursevote.set(i, 80);
          courseID.add(i);
        }
     }
     /*
     //PRINT2
      for(int i=0; i<coursevote.size(); ++i) {
		System.out.print(coursevote.get(i)+" ");
	 }
	 System.out.println(" ");
	 for(int i=0; i<courseID.size(); ++i) {
		System.out.print(courseID.get(i)+"  ");
	 } */
	 
	 selectionSort();
	 
	 //PRINT3
	  System.out.println("\n\n");
      for(int i=0; i<coursevote.size(); ++i) {
		System.out.print(coursevote.get(i)+" ");
	 }
	 System.out.println(" ");
	 for(int i=0; i<courseID.size(); ++i) {
		System.out.print(courseID.get(i)+"  ");
	 }
	 
	 //PRINT4
	 
	 System.out.println("\n\n");
	synthesis(courseID);
    for(int i=0; i<numTime; ++i) {
	  for(int j=0; j<numClass; ++j) {
		 System.out.print(classSchedule[i][j]+" "); 
	  }
	  System.out.println(" ");
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
    //FIX NUM DUP PART
    
    
  }

  public void synthesis(ArrayList<Integer>ID) {
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

/*
  public void placement() {
    //5 loops for the 5 selections
     ArrayList<Integer> priority;
     for(int i=0; i<5; ++i) {
       
       //Looping through the choice matrix
       for(int j=0; j<names.size(); ++j) {
         //Check if the nameID is already in the priority list for the layer
         if(!verify(priority, j)) {
           priority.add(j);
         }
       }
       //Now the Priority List should be name length
       for(int j=0; j<names.size(); ++j) {
         //Find the starting point
         int start;
         for(int k=0; k<5; ++k) {
           if(choices[priority.get(0)][k]!=-1) {
             start=k;
             break;
           }
         }
         if(start==null) {
           priority.remove(0);
           break;
         }

         //Find availiability
         for(int m=start; m<5; ++m) {
           //PRIORITY STORES A LIST OF NAMEIDs
           int value = availability(choices[priority.get(0)][m], priority.get(0))
           if(value!=-1) {
             if(m!=start) {
               //Add to priority list if it doesn't get its most optimal solution in the selection
               priority.add(priority.get(0));
             }
             //Increase enrollment number
             ++studentNumber[choices[priority.get(0)][m]];
             //Set the courseID in the appropriate time slot
             actualSchedule[priority.get(0)][value];
             //Set the choice thing to filled
             choices[priority.get(0)][m]=-1;
             break;
           } else {
             choices[priority.get(0)][m]=-1;
           }
         }
         priority.remove(0);
       }
       
     }

    
  }
  public int availability(int classID, int person) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    //Stores the place or locations of the class on the schedule
    for(int i=0; i<numTime; ++i) {
      for(int j=0; j<numClass; ++j) {
        if(classSchedule[i][j]==classID) {
          //Store time slot only
          list.add(i);
        }
      }
    }

    //Check if the participant size is greater than 16
    if(studentNumber[classID]==16) {
      return -1;
    }
    //Check if there is a bell is compatible
    for(int i=0; i<list.size(); ++i) {
      if(actualSchedule[person][list.get(i)]!=-1) {
        //Returns the compatible time slot
        return list.get(i);
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



  public void secondOptim() {
  //Special built-in optimization to switch to duplicate first class to optimize second class selection
    
  } */
}
