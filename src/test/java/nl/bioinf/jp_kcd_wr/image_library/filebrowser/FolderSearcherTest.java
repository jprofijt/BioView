package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

import org.junit.jupiter.api.Test;

class FolderSearcherTest {

    @Test
    void getNextFolders() {
        FolderHandler folderSearcher = new FolderHandler();
        String[] folders = folderSearcher.getNextFolders("testdata").toArray(new String[0]);

        for (String folder: folders) {
            System.out.printf("directory: " + folder + "\n");
        }
    }
}