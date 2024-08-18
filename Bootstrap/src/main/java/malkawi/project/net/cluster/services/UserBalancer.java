package malkawi.project.net.cluster.services;

import malkawi.project.data.Config;
import malkawi.project.net.global.data.ServerInfo;

public class UserBalancer {

    private final int[] distributionFrequencies;

    public UserBalancer() {
        this.distributionFrequencies = new int[Config.get().getServerInfos().size()];
    }

    public ServerInfo getMostFreeNode() {
        int minimumIndex = getMinimumDistribution();
        ServerInfo serverInfo = Config.get().getServerInfos().get(minimumIndex);
        distributionFrequencies[minimumIndex]++;
        return serverInfo;
    }

    private int getMinimumDistribution() {
        int minIndex = 0;
        int minFrequency = Integer.MAX_VALUE;
        for(int i = 0; i < distributionFrequencies.length; i++) {
            int frequency = distributionFrequencies[i];
            if(frequency < minFrequency) {
                minFrequency = frequency;
                minIndex = i;
            }
        }
        return minIndex;
    }

}
