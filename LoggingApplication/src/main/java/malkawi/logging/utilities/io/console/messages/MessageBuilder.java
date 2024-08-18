package malkawi.logging.utilities.io.console.messages;

import malkawi.logging.utilities.io.console.utils.AnsiColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageBuilder {

    private String text;

    private List<AnsiColors> effects;

    private String end = "\n";

    private String start = "";

    public Message build() {
        return new Message(text, start, end, effects != null ? effects.toArray(new AnsiColors[0]) : null);
    }

    public MessageBuilder text(String text) {
        this.text = this.text != null ? this.text + text : text;
        return this;
    }

    public MessageBuilder effects(AnsiColors... effects) {
        if(this.effects == null) this.effects = new ArrayList<>();
        this.effects.addAll(Arrays.asList(effects));
        return this;
    }

    public MessageBuilder end(String end) {
        this.end = end;
        return this;
    }

    public MessageBuilder start(String start) {
        this.start= start;
        return this;
    }

}
