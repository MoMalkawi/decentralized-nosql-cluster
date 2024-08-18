package malkawi.logging.database.requests;

import malkawi.logging.database.DatabaseSession;
import malkawi.logging.database.data.result.Result;
import malkawi.logging.utilities.Sleep;
import malkawi.logging.utilities.io.console.Console;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseRequester {

    private static final ExecutorService requestPool = Executors.newCachedThreadPool();

    public static Result request(DatabaseSession session, Object... requestData) {
        DatabaseRequest request = createRequest(session, requestData);
        if(request != null) {
            Sleep.sleepUntil(request::isCompletedTasks, 5000, 50);
            return request.getResult();
        }
        return null;
    }

    public static DatabaseRequest createRequest(DatabaseSession session, Object... requestData) {
        if (!session.isConnected())
            return null;
        Console.info("[DatabaseRequester] submitting request (" + requestData[0] + ")");
        DatabaseRequest request = new DatabaseRequest(session, requestData);
        requestPool.submit(request);
        return request;
    }

    public static void closeRequests() {
        requestPool.shutdown();
    }

    private DatabaseRequester() {}

}
