package malkawi.project.database.data;

import lombok.Getter;
import lombok.Setter;

public class ResultRow {

    private @Setter @Getter Object[] data;

    public ResultRow(Object... data) {
        this.data = data;
    }

    public Object get(int index) {
        return data[index];
    }

    public int getInt(int index) {
        return (int) get(index);
    }

    public double getDouble(int index) {
        return (double) get(index);
    }

    public String getString(int index) {
        return (String) get(index);
    }

    public char getChar(int index) {
        return (char) get(index);
    }

}
