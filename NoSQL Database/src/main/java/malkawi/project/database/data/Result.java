package malkawi.project.database.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class Result implements Iterator<ResultRow> {

    public static final Result FAILURE = new ResultBuilder().fail().build();
    public static final Result AUTH_FAILURE = new ResultBuilder().fail().message("You are unauthorized to access this db.").build();

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

}
