package nl.bioinf.jp_kcd_wr.image_library.folder_manager;

import nl.bioinf.jp_kcd_wr.image_library.Model.Directory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that implements folderStructureProvider
 * Handles folder interaction by users
 *
 * @author Jouke Profijt, Kim Chau Duong
 */
@Service
public class FolderHandler implements FolderStructureProvider {
    private final Path rootLocation;
    private static final Logger logger = Logger.getLogger(FolderHandler.class.getName());


    @Autowired
    public FolderHandler(Environment environment){
        this.rootLocation = Paths.get(environment.getProperty("library.sym"));
        logger.log(Level.INFO, "Starting FolderHandler service using {0} as root location", new Object[] {this.rootLocation});
    }

    /**
     * Gets the path without the root location
     * @param directory full directory path
     * @return the relative path
     *
     * @author Kim Chau Duong
     */
    private Path getRelativePath(String directory){
        return rootLocation.relativize(Paths.get(directory));
    }

    /**
     * Retrieves the full path, that includes the rootlocation and the given directory string
     * @param directory a relative path
     * @return full directory path
     */
    private Path getFullPath(String directory) { return this.rootLocation.resolve(directory);}

    /**
     * Creates directory object
     * @param directory directory path
     * @return directory object
     *
     * @author Kim Chau Duong
     */
    private Directory createDirectoryObject(File directory){
        String relativeDirectory = getRelativePath(directory.getPath()).toString().replace("\\", "/");
        String directoryName = directory.getName();
        String dateModified = getDateModified(directory);
        long size = FileUtils.sizeOfDirectory(directory);

        return new Directory(relativeDirectory, directoryName, dateModified, size);
    }

    /**
     * Gets the 'last modified' date of a directory'
     * @param directory
     * @return date String in yyyy-MM-dd HH:mm:ss
     * @auhtor Jouke Profijt
     */
    private String getDateModified(File directory) {
        long lastModified = directory.lastModified();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(lastModified);
    }

    /**
     * Gathers all folders present in the current directory
     * @param nextFolders current directory path
     * @return list of directory objects
     *
     * @author Kim Chau duong, Jouke Profijt
     */
    @Override
    public ArrayList<Directory> getNextFolders(String nextFolders){
        File[] directories = new File(String.valueOf(getFullPath(nextFolders))).listFiles(File::isDirectory);

        ArrayList<Directory> directoryList = new ArrayList<>();
        if(directories != null){
            for (File directory : directories){
                directoryList.add(createDirectoryObject(directory));
            }
        }
        return directoryList;
    }


}
