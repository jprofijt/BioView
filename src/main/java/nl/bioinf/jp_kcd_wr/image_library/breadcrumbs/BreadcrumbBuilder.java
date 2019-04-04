package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

import nl.bioinf.jp_kcd_wr.image_library.model.Breadcrumb;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class BreadcrumbBuilder {
    private final Path rootLocation;

    @Autowired
    public BreadcrumbBuilder(Environment environment) {
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
    }

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
        System.out.println(directoryURL);
        System.out.println(folder.getParentFile());
        System.out.println(folder.getParent());
        return new Breadcrumb(folder.getParent(), directoryURL);
    }

    protected Breadcrumb getfolderBreadCrumb(File folder){
        final String folderURL = "/nextfolder/" + folder;
        return new Breadcrumb(folder.getName(), folderURL);
    }
}
