package malkawi.logging.database.data.result;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ResultRow {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private @Setter @Getter Object[] data;

    public ResultRow(Object... data) {
        this.data = data;
    }

    public Object get(int index) {
        return data[index];
    }

    public Number getNumber(int index) {
        return (Number) get(index);
    }

    public int getInt(int index) {
        return getNumber(index).intValue();
    }

    public double getDouble(int index) {
        return getNumber(index).doubleValue();
    }

    public String getString(int index) {
        return (String) get(index);
    }

    public char getChar(int index) {
        return (char) get(index);
    }

    public LocalDate getDate(int index) {
        return LocalDate.parse((String) get(index), formatter);
    }

    public LocalDate getDateFromList(int listIndex, int dateIndex) {
        return LocalDate.parse((String) getList(listIndex).get(dateIndex), formatter);
    }

    public Object[] getArray(int index) {
        return (Object[]) get(index);
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList(int index) {
        return (ArrayList<Object>) get(index);
    }

}
