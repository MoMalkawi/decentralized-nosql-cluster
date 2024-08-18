package malkawi.logging.database.requests;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import malkawi.logging.database.DatabaseSession;
import malkawi.logging.database.data.result.Result;
import malkawi.logging.net.global.data.Packet;
import malkawi.logging.net.global.data.PacketBuilder;
import malkawi.logging.utilities.Sleep;
import malkawi.logging.utilities.io.console.Console;

@RequiredArgsConstructor
public class DatabaseRequest implements Runnable {

    private final @NonNull DatabaseSession session;

    private final @NonNull Object[] requestData;

    private @Getter boolean completedTasks;

    private @Getter Result result;

    @Override
    public void run() {
        try {
            Packet requestPacket = new PacketBuilder().typeName("API")
                    .values(requestData).generateIdentifier().build();
            Console.info("[DatabaseRequest] sent (" + requestPacket.getContent()[0] + ") request. Identifier: " + requestPacket.getIdentifier());
            session.getClient().getConnection().send(requestPacket);
            if (Sleep.sleepUntil(() -> session.getClient().getDatabaseConnection()
                    .getResultsCache().containsKey(requestPacket.getIdentifier()), 20000, 50))
                fetchResult(requestPacket.getIdentifier());
            Console.info("[DatabaseRequest] finished tasks.");
            completedTasks = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchResult(long identifier) {
        Console.success("[DatabaseRequest] received result with identifier: " + identifier);
        result = session.getClient().getDatabaseConnection()
                .getResultsCache().get(identifier);
        removeResult(identifier);
    }

    private void removeResult(long identifier) {
        session.getClient().getDatabaseConnection().getResultsCache().remove(identifier);
    }

}
