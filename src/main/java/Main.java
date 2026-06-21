import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc=new Scanner(System.in);
        while (true) { 
            System.out.print("$ ");
            String s=sc.next();
            if (s.equals("exit")){
                break;
            }
            else if (s.startsWith("echo ")){
                System.out.println(s.substring(5));
            }
            System.out.println(s+": command not found");
        }
    }
}
