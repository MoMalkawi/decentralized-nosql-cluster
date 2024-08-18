package malkawi.project.net.cluster.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import malkawi.project.net.cluster.Cluster;
import malkawi.project.utilities.Sleep;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ClusterCleaner implements Runnable {

    private final @NonNull Cluster cluster;

    private @Setter boolean enabled = true;

    @Override
    public void run() {
        while (enabled) {
            clean();
            Sleep.sleep(10000);
        }
    }

    private void clean() {
        cluster.getNodes().forEach((k, v) -> {
            if(v.isAlive() &&
                    getMinutesDifference(v.getConnection().getLatestActivityTimeMillis()) >= 10)
                v.stop();
        });
    }

    private double getMinutesDifference(long lastAccess) {
        return TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastAccess);
    }

}
