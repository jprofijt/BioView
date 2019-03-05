package nl.bioinf.jp_kcd_wr.image_library.imagebrowser;

import java.io.File;
import java.util.ArrayList;

public class FolderSearcher implements FolderStructureProvider {

    @Override
    public ArrayList<String> getNextFolders(String nextFolders){
        File[] directories = new File(nextFolders).listFiles(File::isDirectory);
        ArrayList<String> folders = new ArrayList<>();
        for (File folder: directories) {
            folders.add(folder.getPath());
        }
        return folders;
    }


}
