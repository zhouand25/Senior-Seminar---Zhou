import java.util.Scanner;
public class Tester {
  public static void main(String[] args) {
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
