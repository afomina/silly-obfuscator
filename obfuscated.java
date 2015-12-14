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
        String ztcccnwlemldhtb = scanner.next();
        System.out.println("Enter output file name");
        String czfyalnaycgao = scanner.next();
        try {
            new Obfuscator().obfuscate(new File(ztcccnwlemldhtb), new File(czfyalnaycgao));
        } catch (FileNotFoundException e) {
            System.out.println("No such file");
        }
    }
}
