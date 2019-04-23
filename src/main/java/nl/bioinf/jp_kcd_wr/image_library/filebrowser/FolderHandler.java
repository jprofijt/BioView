package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

import nl.bioinf.jp_kcd_wr.image_library.model.Directory;
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
    public ArrayList<Directory> getNextFolders(String nextFolders){
        File[] directories = new File(String.valueOf(this.rootLocation.resolve(nextFolders))).listFiles(File::isDirectory);

        ArrayList<Directory> directoryList = new ArrayList<>();
        if(directories != null){
            for (File directory : directories){
                Directory newDirectory = new Directory();
                newDirectory.setName(directory.getName());
                newDirectory.setPath(rootLocation.relativize(Paths.get(directory.getPath())));
                directoryList.add(newDirectory);
            }
        }
        return directoryList;
    }

    @Override
    public void createNewFolder(String directoryName, String currentPath) throws DirectoryExistsException {
        String path = this.rootLocation.resolve(currentPath) + File.separator + directoryName;
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
