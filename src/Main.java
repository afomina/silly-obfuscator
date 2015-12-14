import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by alexa on 15.12.2015.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path to obfuscate (default is Main.java):");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            input = "Main.java";
        }
        System.out.println("Enter output file name (default is obfuscated.java):");
        String output = scanner.nextLine();
        if (output.isEmpty()) {
            output = "obfuscated.java";
        }
        try {
            new Obfuscator().obfuscate(new File(input), new File(output));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
