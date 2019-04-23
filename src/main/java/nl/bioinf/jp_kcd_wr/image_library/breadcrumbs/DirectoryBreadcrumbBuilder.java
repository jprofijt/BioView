package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

import nl.bioinf.jp_kcd_wr.image_library.model.Breadcrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DirectoryBreadcrumbBuilder implements BreadcrumbBuilder{
    private final Path rootLocation;

    @Autowired
    public DirectoryBreadcrumbBuilder(Environment environment) {
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
    }

    @Override
    public List<Breadcrumb> getBreadcrumbs(String directory) throws IllegalArgumentException{
        final List<Breadcrumb> breadcrumbs = new ArrayList<>();
        if (directory != null) {
            String[] crumbs = directory.split(File.separator);
            List<String> crumbList = Arrays.asList(crumbs);
            for (int i = 0; i < crumbList.size()+1; i++) {
                List<String> crumbSubList = crumbList.subList(0, i);
                Breadcrumb breadcrumb = getFolderBreadCrumb(crumbSubList);
                breadcrumbs.add(breadcrumb);
            }
        }

        return breadcrumbs;
    }
//    @Override
//    public Breadcrumb getParentDirectoryBreadcrumb(File folder){
//        final String directoryURL = "/nextfolder/" + folder.getParentFile();
//        System.out.println(directoryURL);
//        System.out.println(folder.getParentFile());
//        System.out.println(folder.getParent());
//        return new Breadcrumb(folder.getParent(), directoryURL);
//    }

    @Override
    public Breadcrumb getFolderBreadCrumb(List<String> crumbSubList){
        String crumbPath = String.join(File.separator, crumbSubList);
        final String directoryURL = "/nextfolder?folder=" + crumbPath;
        File folder = new File(crumbPath);
        return new Breadcrumb(folder.getName(), directoryURL);
    }
}
