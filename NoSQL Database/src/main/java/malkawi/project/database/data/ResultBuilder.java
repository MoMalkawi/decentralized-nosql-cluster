package malkawi.project.database.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultBuilder {

    private final List<ResultRow> rows = new ArrayList<>();

    private boolean success;

    private String message;

    public ResultBuilder row(ResultRow row) {
        rows.add(row);
        return this;
    }

    public ResultBuilder row(Object... data) {
        rows.add(new ResultRow(data));
        return this;
    }

    public ResultBuilder rows(ResultRow... rows) {
        this.rows.addAll(Arrays.asList(rows));
        return this;
    }

    public ResultBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ResultBuilder success() {
        this.success = true;
        this.message = "Operation performed successfully!";
        return this;
    }

    public ResultBuilder fail() {
        this.success = false;
        this.message = "Something went wrong...";
        return this;
    }

    public Result build() {
        return new Result(message, success, rows);
    }

}
