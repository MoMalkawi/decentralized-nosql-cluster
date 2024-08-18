package malkawi.project.utilities;

import java.io.File;
import java.util.regex.Pattern;

public class Utils {

    public static final Pattern NUMBERS_ONLY = Pattern.compile("^\\d+$");

    public static String buildPath(String... directories) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for(String directory : directories) {
            if(index != 0)
                builder.append(File.separator);
            builder.append(directory);
            index++;
        }
        return builder.toString();
    }

    private Utils() {}

}
