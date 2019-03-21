package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class FolderHandler implements FolderStructureProvider {
    private final Path rootLocation;

    @Autowired
    public FolderHandler(Environment environment){
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
    }

    @Override
    public ArrayList<String> getNextFolders(String nextFolders){
        File[] directories = new File(nextFolders).listFiles(File::isDirectory);
        ArrayList<String> folders = new ArrayList<>();
        for (File folder: directories) {
            folders.add(folder.getPath());
        }
        return folders;
    }

    @Override
    public void createNewFolder(String directoryName, String currentPath) throws DirectoryExistsException {
        String path = currentPath + "/" + directoryName;
        File newDir = new File(path);
        try {
            Files.createDirectory(newDir.toPath());
        } catch (IOException e){
            throw new DirectoryExistsException("Directory " + directoryName + " already exists");
        }

        }



    @Override
    public void removeFolder(File directory) {
        throw new NotImplementedException();

    }

    public void createDateDirectory(String currentPath) throws DirectoryExistsException{
        String date = LocalDate.now().toString();
        try {
            this.createNewFolder(date, currentPath);
        } catch (DirectoryExistsException e){
            throw new DirectoryExistsException(date + " already has a directory in " + currentPath);
        }
    }


}
