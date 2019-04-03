package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

import nl.bioinf.jp_kcd_wr.image_library.model.Breadcrumb;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class BreadcrumbBuilder {
    public List<Breadcrumb> getBreadcrumbs(String directory, File folder) throws IllegalArgumentException{
        final List<Breadcrumb> breadcrumbs = new ArrayList<>();
        Breadcrumb parent = getParentDirectoryBreadcrumb(folder);
        breadcrumbs.add(parent);
        Breadcrumb currentFolder = getfolderBreadCrumb(folder);
        breadcrumbs.add(currentFolder);

        return breadcrumbs;
    }

    protected Breadcrumb getParentDirectoryBreadcrumb(File folder){
        final String directoryURL = "/nextfolder/" + folder.getParentFile();
        return new Breadcrumb(folder.getParent(), directoryURL);
    }

    protected Breadcrumb getfolderBreadCrumb(File folder){
        final String folderURL = "/nextfolder/" + folder;
        return new Breadcrumb(folder.getName(), folderURL);
    }
}
