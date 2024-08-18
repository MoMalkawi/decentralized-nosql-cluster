package malkawi.project.utilities.io.console.messages;

import malkawi.project.utilities.io.console.utils.AnsiColors;

public class Message {

    private final String text;

    private final AnsiColors[] effects;

    private final String start;

    private final String end;

    public Message(String text, String start, String end, AnsiColors[] effects) {
        this.text = text;
        this.start = start;
        this.end = end;
        this.effects = effects;
    }

    public enum Type {

        INFO(AnsiColors.WHITE_BRIGHT),
        SUCCESS(AnsiColors.GREEN_BOLD_BRIGHT),
        ERROR(AnsiColors.RED_BOLD_BRIGHT);

        private final AnsiColors color;

        Type(AnsiColors color) {
            this.color = color;
        }

        public AnsiColors getColor() {
            return color;
        }

    }

    public String getText() {
        return text;
    }

    public AnsiColors[] getEffects() {
        return effects;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

    @Override
    public String toString() {
        return start + AnsiColors.format(text, effects) + end;
    }

}
