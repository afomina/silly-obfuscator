import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path to obfuscate (default is source.c):");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            input = "source.c";
        }
        System.out.println("Enter output file name (default is obfuscated.c):");
        String output = scanner.nextLine();
        if (output.isEmpty()) {
            output = "obfuscated.c";
        }
        try {
            new Obfuscator().obfuscate(new File(input), new File(output));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
