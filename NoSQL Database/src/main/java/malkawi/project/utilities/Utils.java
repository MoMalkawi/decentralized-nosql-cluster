package malkawi.project.utilities;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Utils() {}

}
