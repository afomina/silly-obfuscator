import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexa on 15.12.2015.
 */
public class Obfuscator {
    private static final String[] VAR_TYPES = {"int", "double", "long", "float", "char", "String"};
    private static final Pattern[] patterns = new Pattern[VAR_TYPES.length + 1];

    static {
        for (int i = 0; i < VAR_TYPES.length; i++) {
            String varType = VAR_TYPES[i];
            patterns[i] = Pattern.compile("\\s*" + varType + "\\s*(\\w+[\\w\\d]*).*;.*");
        }
//        patterns[patterns.length - 1] = Pattern.compile(".*\\s*(\\w+[\\w\\d]*)\\s*=\\s*new.*");
    }

    public void obfuscate(File source, File obfuscated) throws FileNotFoundException {
        Scanner in = null;
        PrintWriter writer = null;
        try {
            in = new Scanner(source);

            writer = new PrintWriter(new FileOutputStream(obfuscated));
            Map<String, String> renames = new HashMap<String, String>();
            while (in.hasNextLine()) {
                String line = in.nextLine();
                line = replaceNames(line, renames);
                Matcher matcher = matchVarDefinition(line);
                if (matcher != null) {
                    String name = matcher.group(1);

                    String newName = RandomUtils.randomString();
                    line = line.replace(name, newName);
                    renames.put(name, newName);
                }
                writer.println(line);
            }
        } finally {
            in.close();
            writer.close();
        }

    }

    private Matcher matchVarDefinition(String s) {
        for (Pattern pattern : patterns) {
            if (pattern != null) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches()) {
                    return matcher;
                }
            }
        }
        return null;
    }

    private String replaceNames(String line, Map<String, String> renames) {
        for (Map.Entry<String, String> entry : renames.entrySet()) {
            Pattern p = Pattern.compile("([=\\(\\+\\-\\*/,]\\s*)" + entry.getKey());
            Matcher m = p.matcher(line);
            if (m.find()) {
                line = m.replaceAll(m.group(1) + entry.getValue());
            }

            p = Pattern.compile(entry.getKey() + "\\\\s*([=\\(\\+\\-\\*/,\\.])");
            m = p.matcher(line);
            if (m.find()) {
                line = m.replaceAll(m.group(1) + entry.getValue());
            }
        }
        return line;
    }
}
