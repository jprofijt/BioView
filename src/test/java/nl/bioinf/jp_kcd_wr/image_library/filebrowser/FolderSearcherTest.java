package nl.bioinf.jp_kcd_wr.image_library.imagebrowser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FolderSearcherTest {

    @Test
    void findFolders() {
        FolderController folderSearcher = new FolderController();
        String[] folders = folderSearcher.findFolders("testdata").toArray(new String[0]);

        for (String folder: folders) {
            System.out.printf("directory: " + folder + "\n");
        }
    }
}