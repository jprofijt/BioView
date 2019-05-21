package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

import nl.bioinf.jp_kcd_wr.image_library.model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class that implements folderStructureProvider
 * Handles folder interaction by users
 *
 * @author Jouke Profijt, Kim Chau Duong
 */
@Service
public class FolderHandler implements FolderStructureProvider {
    private final Path rootLocation;

    @Autowired
    public FolderHandler(Environment environment){
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
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
     * Creates directory object
     * @param directory directory path
     * @return directory object
     *
     * @author Kim Chau Duong
     */
    private Directory createDirectoryObject(File directory){
        Directory newDirectory = new Directory();
        newDirectory.setName(directory.getName());
        Path relativeDirectory = getRelativePath(directory.getPath());
        newDirectory.setPath(relativeDirectory);
        return newDirectory;
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
        File[] directories = new File(String.valueOf(this.rootLocation.resolve(nextFolders))).listFiles(File::isDirectory);

        ArrayList<Directory> directoryList = new ArrayList<>();
        if(directories != null){
            for (File directory : directories){
                directoryList.add(createDirectoryObject(directory));
            }
        }
        return directoryList;
    }

    /**
     * Create a new directory
     * @param directoryName name of the new directory
     * @param currentPath path where directory should be located
     * @throws DirectoryExistsException when directory already exists
     *
     * @author Jouke Profijt
     */
    @Override
    public void createNewFolder(String directoryName, String currentPath) throws DirectoryExistsException {
        String path = this.rootLocation.resolve(currentPath) + File.separator + directoryName;
        File newDir = new File(path);
        try {
            Files.createDirectory(newDir.toPath());
            newDir.setWritable(true, false);
            newDir.setReadable(true, false);
            newDir.setExecutable(true, false);

        } catch (IOException e){
            e.printStackTrace();
            throw new DirectoryExistsException("Directory " + directoryName + " already exists");
        }

        }



    @Override
    public void removeFolder(File directory) {
       return;
    }

    /**
     * Creates a directory wht the current date as name
     * @param currentPath path where directory should be located
     * @throws DirectoryExistsException when directory exists
     *
     * @author Jouke Profijt
     */
    public void createDateDirectory(String currentPath) throws DirectoryExistsException{
        String date = LocalDate.now().toString();
        try {
            this.createNewFolder(date, currentPath);
        } catch (DirectoryExistsException e){
            throw new DirectoryExistsException(date + " already has a directory in " + currentPath);
        }
    }


}
