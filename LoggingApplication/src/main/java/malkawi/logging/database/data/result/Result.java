package malkawi.logging.database.data.result;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class Result implements Iterator<ResultRow> {

    private @Getter @NonNull String message;

    private @Getter @NonNull boolean success;

    private @NonNull List<ResultRow> rows;

    private int index = -1;

    @Override
    public boolean hasNext() {
        return rows.size() > (index + 1);
    }

    @Override
    public ResultRow next() {
        return rows.get(++index);
    }

    public ResultRow getRow() {
        return rows.get(index);
    }

    public ResultRow getRow(int index) {
        return rows.get(index);
    }

}
