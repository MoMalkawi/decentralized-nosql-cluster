package malkawi.project.utilities.io.console;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.utilities.io.console.messages.Message;
import malkawi.project.utilities.io.console.messages.MessageBuilder;
import malkawi.project.utilities.io.console.utils.AnsiColors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

    private static BufferedReader inputReader;

    @Getter @Setter
    private static boolean running = true;

    private Console() {}

    public static void init() {
        inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void close() {
        if(inputReader != null) {
            try {
                inputReader.close();
            } catch (IOException e) {
                send("[System] Fatal closing error: \n" + e.getMessage(),
                        Message.Type.ERROR);
            }
        }
    }

    public static void send(Message message) {
        System.out.print(message.toString());
    }

    public static void send(String message, Message.Type type) {
        send(new MessageBuilder().
                text(message).effects(type.getColor()).build());
    }

    public static void error(String message) {
        send(message, Message.Type.ERROR);
    }

    public static void info(String message) {
        send(message, Message.Type.INFO);
    }

    public static void success(String message) {
        send(message, Message.Type.SUCCESS);
    }

    public static void newLine() {
        System.out.println();
    }

    public static String receive(Message message) {
        send(message);
        try {
            return inputReader.readLine();
        } catch (IOException e) {
            send("[System] Fatal reading error: \n" + e.getMessage(),
                    Message.Type.ERROR);
        }
        return "";
    }

    public static String receive(String message) {
        return receive(new MessageBuilder().
                text(message).
                effects(AnsiColors.WHITE_BRIGHT).
                build());
    }

}
