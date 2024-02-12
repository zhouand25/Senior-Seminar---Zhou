import java.util.Scanner;
public class Tester {
  /**
   * Tester.java 
   * @author Andrew Zhou
   * @since 2/11/24
   * @version 1.0.1
   * The class shown below essentially acts as the "brain"/headquarters of the entire program. It takes in input from the user as the name of the file (String)
   * and then runs the various methods of Seminar.java from this file (like popularRank, wildcard, placement and more), ultimately leading to a finished product.
   */
  public static void main(String[] args) {
	  /* The purpose of the function is to initiate the start of the program (the default method which is run "on-load"), take
	   * in input of the file name from the user, and act as a command center to activate methods in seminar.java. The input parameter
	   * of this function is an array of strings which are essentially from the command line. However, there is no return type for this method.
	   * */
  System.out.print("\nFile Name: ");
  Scanner filename = new Scanner(System.in);
  String file = filename.nextLine();
  Seminar sem1 = new Seminar(file);
  sem1.popularRank();
  sem1.selectionSort();
  sem1.synthesis(sem1.courseID);
  sem1.placement();
  sem1.wildcard();
  sem1.gather();
  sem1.finish();
  System.out.println("FINAL END");
  }
}
