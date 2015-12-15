import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexa on 15.12.2015.
 */
public class Obfuscator {
    private static final String[] VAR_TYPES = {"int", "double", "long", "float", "char", "string"};
    private static final Pattern[] patterns = new Pattern[VAR_TYPES.length + 1];
    private List<String> initializedVars = new ArrayList<>();
    private Map<Integer, String> jumps = new HashMap<>();

    static {
        for (int i = 0; i < VAR_TYPES.length; i++) {
            String varType = VAR_TYPES[i];
            patterns[i] = Pattern.compile("\\s*" + varType + "\\s*(\\w+[\\w\\d]*).*;.*");
        }
        patterns[patterns.length - 1] = Pattern.compile("\\s*[A-Z]*[\\w0-9]*\\s+(\\w+[\\w\\d]*)\\s*=\\s*.*;.*");
    }

    public void obfuscate(File source, File obfuscated) throws FileNotFoundException {
        Scanner in = null;
        PrintWriter writer = null;
        try {
            in = new Scanner(source);

            writer = new PrintWriter(new FileOutputStream(obfuscated));
            Map<String, String> renames = new HashMap<String, String>();

            StringBuilder begin = new StringBuilder();
            StringBuilder otherPart = new StringBuilder();
            int cnt = -1;
            while (in.hasNextLine()) {
                if (jumps.keySet().contains(cnt)) {
                    otherPart.append(jumps.get(cnt));
                }
                cnt++;
                String line = in.nextLine();

                if (firstLines(line)) {
                    begin.append(line);
                    begin.append('\n');
                    continue;
                }

                line = replaceNames(line, renames);
                Matcher matcher = matchVarDefinition(line);
                if (matcher != null) {
                    String name = matcher.group(1);

                    if (!initializedVars.contains(name)) {
                        String newName = RandomUtils.randomString();
                        line = line.replace(name, newName);
                        renames.put(name, newName);

                        initializedVars.add(name);
                        begin.append(line);
                        begin.append('\n');
                        continue;
                    }
                }
//                writer.println(line);
                otherPart.append(line);
                otherPart.append('\n');

                if (cnt != 0 && cnt % 15 == 0) {
                    otherPart.append(generateDummy());
                    otherPart.append('\n');
                }

                if (cnt != 0 && cnt % 25 == 0) {
                    jumps.put(cnt *2, line);
                    otherPart.append("\tgoto " + cnt*2 + ";");
                    otherPart.append('\n');
                }

            }

            writer.print(begin.toString());
            writer.print(otherPart.toString());
        } finally {
            in.close();
            writer.close();
        }

    }

    private boolean firstLines(String s) {
        Pattern imp = Pattern.compile("\\s*#\\s*include\\s*.*\\s*");
        Pattern def = Pattern.compile("\\s*#\\s*define\\s*.*\\s*");
        Pattern cl = Pattern.compile("\\s*.*\\s*class\\s*[A-Z][\\w0-9]*\\s*\\{\\s*");
//        Pattern main = Pattern.compile("\\s*void\\s+.+\\s+\\(.*\\)\\s*\\{");
        Pattern us = Pattern.compile("\\s*using\\s+.+\\s*;\\s*");
        Matcher m1 = imp.matcher(s);
        Matcher m2 = cl.matcher(s);
        Matcher m3 = def.matcher(s);
        Matcher m4 = us.matcher(s);
        return m1.matches() || m2.matches() || m3.matches() || m4.matches();
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

    private String generateDummy() {
        Random rnd = new Random();
        String type = VAR_TYPES[rnd.nextInt(VAR_TYPES.length - 2)];
        String name1 = RandomUtils.randomString();
        String value1 = "";
        String value2 = "";
        long a = 1;
        switch (type) {
            case "int":
                value1 += rnd.nextInt();
                value2 += rnd.nextInt();
                break;
            case "double":
                value1 += rnd.nextDouble();
                value2 += rnd.nextDouble();
                break;
            case "long":
                value1 += rnd.nextLong();
                value2 += rnd.nextLong();
                break;
            case "float":
                value1 += rnd.nextFloat();
                value2 += rnd.nextFloat();
                break;
            case "char":
                value1 += (char) (rnd.nextInt(122 - 97) + 97);
                value2 += (char) (rnd.nextInt(122 - 97) + 97);
                break;
        }
        String name2 = RandomUtils.randomString();
        String res = String.format("\t%s %s = %s;\n", type, name1, value1);
        res += String.format("\t%s %s = %s;\n", type, name2, value2);
        if (!"char".equals(type)) {
            res += String.format("\t%s = %s + %s;\n", name1, name1, name2);
        }
        return res;
    }


}
