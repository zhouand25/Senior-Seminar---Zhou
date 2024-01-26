import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Seminar {

    final int numClass = 5;
    final int numTime = 5;
    final int maxSize = 16;

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> courses = new ArrayList<String>();
    ArrayList<int[]> choices = new ArrayList<int[]>();

    ArrayList<Integer> rank = new ArrayList<Integer>();
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
      for(int i=0; i<choices.size(); ++i) {
        for(int j=0; j<5; ++j) {
            System.out.print(choices.get(i)[j]+" ");
        }
        System.out.println("");
      }
      s1.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } 
	popularRank();
	}

  public void popularRank() {
     int numDup = numClass*numTime - courses.size();
     for(int i=0; i<courses.size(); ++i) {
        rank.add(i);
        coursevote.add(0);
     }
     for(int i=0; i<courses.size(); ++i) {
        for(int j=0; j<5; ++j) {
          coursevote.set(choices.get(i)[j], coursevote.get(choices.get(i)[j])+(5-j));
        }
     }
     for(int i=0; i<coursevote.size(); ++i) {
      //Modulus 16*5
     }
  }
}
