import java.util.Random;

/**
 * Created by alexa on 15.12.2015.
 */
public class RandomUtils {
    public static String randomString() {
        Random random = new Random();
        int len = random.nextInt(40) + 10;
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = random.nextInt(26) + 97;
            res.append((char)n);
        }
        return res.toString();
    }
}
