import java.util.Scanner;
public class Tester {
  public static void main(String[] args) {
  System.out.print("\nFile Name: ");
  Scanner filename = new Scanner(System.in);
  String file = filename.nextLine();
  filename.close();
  Seminar sem1 = new Seminar(file);
  }
}
