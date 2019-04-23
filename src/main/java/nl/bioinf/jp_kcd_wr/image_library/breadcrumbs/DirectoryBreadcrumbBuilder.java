package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

import nl.bioinf.jp_kcd_wr.image_library.model.Breadcrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryBreadcrumbBuilder implements BreadcrumbBuilder{
    private final Path rootLocation;

    @Autowired
    public DirectoryBreadcrumbBuilder(Environment environment) {
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
    }

    @Override
    public List<Breadcrumb> getBreadcrumbs(String directory, File folder) throws IllegalArgumentException{
        final List<Breadcrumb> breadcrumbs = new ArrayList<>();
        Breadcrumb parent = getParentDirectoryBreadcrumb(folder);
        breadcrumbs.add(parent);
        Breadcrumb currentFolder = getFolderBreadCrumb(folder);
        breadcrumbs.add(currentFolder);

        return breadcrumbs;
    }
    @Override
    public Breadcrumb getParentDirectoryBreadcrumb(File folder){
        final String directoryURL = "/nextfolder/" + folder.getParentFile();
        System.out.println(directoryURL);
        System.out.println(folder.getParentFile());
        System.out.println(folder.getParent());
        return new Breadcrumb(folder.getParent(), directoryURL);
    }

    @Override
    public Breadcrumb getFolderBreadCrumb(File folder){
        final String folderURL = "/nextfolder/" + folder;
        return new Breadcrumb(folder.getName(), folderURL);
    }
}
