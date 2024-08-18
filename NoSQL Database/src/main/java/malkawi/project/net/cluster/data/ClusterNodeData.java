package malkawi.project.net.cluster.data;

import lombok.Getter;
import lombok.Setter;
import malkawi.project.data.Config;
import malkawi.project.utilities.Utils;
import malkawi.project.utilities.io.JSONUtilities;

import java.io.File;

@Getter @Setter
public class ClusterNodeData { //sent with the data synchronization.

    private long timeLastModified = Integer.MIN_VALUE;

    public void updateDataFile() {
        JSONUtilities.overwriteJSONFile(this, new File(getNodeDataFilePath()));
    }

    public ClusterNodeData updateTime() {
        timeLastModified = System.currentTimeMillis();
        return this;
    }

    public static ClusterNodeData loadNodeData() {
        File nodeData = new File(getNodeDataFilePath());
        if(!nodeData.exists())
            return new ClusterNodeData();
        ClusterNodeData clusterNodeData = JSONUtilities.readJSONFile(getNodeDataFilePath(), ClusterNodeData.class);
        return clusterNodeData != null ? clusterNodeData : new ClusterNodeData();
    }

    private static String getNodeDataFilePath() {
        return Utils.buildPath(Config.get().getDatabasesRootPath(), "nodeData.cluster");
    }

}
