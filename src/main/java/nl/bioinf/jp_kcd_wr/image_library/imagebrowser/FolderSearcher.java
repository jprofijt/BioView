package nl.bioinf.jp_kcd_wr.image_library.imagebrowser;

import java.io.File;
import java.util.ArrayList;

public class FolderSearcher {
    public ArrayList<String> findFolders(String nextFolders){
        File[] directories = new File(nextFolders).listFiles(File::isDirectory);
        ArrayList<String> folders = new ArrayList<>();
        for (File folder: directories) {
            folders.add(folder.getPath().replace(nextFolders + "/", ""));
        }
        return folders;
    }
}
