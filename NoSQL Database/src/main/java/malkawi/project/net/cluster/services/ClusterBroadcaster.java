package malkawi.project.net.cluster.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.net.cluster.requests.BroadcastRequest;
import malkawi.project.utilities.Sleep;
import malkawi.project.utilities.io.console.Console;

import java.util.LinkedList;
import java.util.Queue;

@RequiredArgsConstructor
public class ClusterBroadcaster implements Runnable {

    private final @NonNull Cluster cluster;

    private final Queue<BroadcastRequest> requests = new LinkedList<>();

    private @Setter boolean enabled = true;

    @Override
    public void run() {
        while(enabled) {
            if(!requests.isEmpty())
                processQueue();
            Sleep.sleep(1000);
        }
    }

    private void processQueue() {
        BroadcastRequest request = requests.poll();
        if(request == null)
            return;
        Console.info("[Broadcaster] Broadcasting (" + request.getRequestData()[0] + ")");
        cluster.getServicePool().submit(request);
        Sleep.sleepUntil(request::isCompletedTasks, 15000, 100);
    }

    public synchronized void addRequest(BroadcastRequest request) {
        requests.add(request);
    }

}
