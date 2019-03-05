package nl.bioinf.jp_kcd_wr.image_library.imagebrowser;

import java.util.ArrayList;

public interface FolderStructureProvider {
    ArrayList<String> getNextFolders(String nextFolder);


}
