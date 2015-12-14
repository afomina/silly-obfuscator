import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by alexa on 15.12.2015.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path to obfuscate:");
        String input = scanner.next();
        System.out.println("Enter output file name");
        String output = scanner.next();
        try {
            new Obfuscator().obfuscate(new File(input), new File(output));
        } catch (FileNotFoundException e) {
            System.out.println("No such file");
        }
    }
}
