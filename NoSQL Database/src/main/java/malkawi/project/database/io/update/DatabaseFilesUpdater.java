package malkawi.project.database.io.update;

import lombok.AllArgsConstructor;
import malkawi.project.data.Config;
import malkawi.project.database.components.databases.Database;
import malkawi.project.utilities.Utils;
import malkawi.project.utilities.io.IOUtils;
import malkawi.project.utilities.io.JSONUtilities;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DatabaseFilesUpdater {

    private final Database database;

    public void createCollections(List<Integer> collectionIndices) {
        collectionIndices.forEach(i -> IOUtils.createDirectory(getCollectionPath(i)));
    }

    public List<Integer> getNotRegisteredCollections() {
        HashSet<Integer> collectionIds = getCollectionIds();
        return database.getCollections().keySet().stream()
                .filter(i -> !collectionIds.contains(i)).collect(Collectors.toList());
    }

    public void removeCollections(List<Integer> collectionIndices) {
        collectionIndices.forEach(i -> IOUtils.deleteDirectory(getCollectionPath(i)));
    }

    public List<Integer> getZombieCollections() {
        HashSet<Integer> collectionIds = getCollectionIds();
        return collectionIds.stream().filter(i -> !database.getCollections().containsKey(i))
                .collect(Collectors.toList());
    }

    private HashSet<Integer> getCollectionIds() {
        return IOUtils.getFileNames(getDatabaseBaseFile(),
                        name -> Utils.NUMBERS_ONLY.matcher(name).find())
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(HashSet<Integer>::new));
    }

    public boolean createDatabaseBase() {
        File databaseDirectory = getDatabaseBaseFile();
        return (databaseDirectory.exists() && databaseDirectory.isDirectory())
                || databaseDirectory.mkdirs();
    }

    public void updateDatabaseMetaData() {
        File metaFile = getDatabaseMetaFile();
        JSONUtilities.overwriteJSONFile(database, metaFile);
    }

    public File getDatabaseBaseFile() {
        return new File(Utils.buildPath(Config.get().getDatabasesRootPath(),
                String.valueOf(database.getId())));
    }

    private File getDatabaseMetaFile() {
        return new File(Utils.buildPath(Config.get().getDatabasesRootPath(),
                String.valueOf(database.getId()), "meta.db"));
    }

    private File getCollectionPath(int index) {
        return new File(Utils.buildPath(Config.get().getDatabasesRootPath(),
                String.valueOf(database.getId()), String.valueOf(index)));
    }

}
