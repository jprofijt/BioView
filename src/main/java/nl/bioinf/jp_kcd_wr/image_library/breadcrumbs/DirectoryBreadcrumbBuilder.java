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
import java.util.Iterator;
import java.util.List;

/**
 * Class to create breadcrumbs manually given a (relative) directory path
 * It contains methods that create the breadcrumb and then add them to a list
 *
 * @author Kim Chau Duong
 * @version 1.0
 */
@Service
public class DirectoryBreadcrumbBuilder implements BreadcrumbBuilder{
    private final Path rootLocation;

    /**
     * constructor
     */
    @Autowired
    public DirectoryBreadcrumbBuilder(Environment environment) {
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
    }

    /**
     * Gathers all created breadcrumbs into a list
     * @param directory current directory
     * @return list of breadcrumbs
     * @throws IllegalArgumentException
     */
    @Override
    public List<Breadcrumb> getBreadcrumbs(String directory) throws IllegalArgumentException{
        final List<Breadcrumb> breadcrumbs = new ArrayList<>();
        if (directory != null) {
            Path path = Paths.get(directory);
            Iterator<Path> it= path.iterator();
            StringBuilder crumbPath = new StringBuilder();
            while (it.hasNext()){
                crumbPath.append(it.next()).append("/");
                Breadcrumb breadcrumb = getFolderBreadCrumb(crumbPath);
                breadcrumbs.add(breadcrumb);
            }

//            String[] crumbs = directory.split(File.separator);
//            List<String> crumbList = Arrays.asList(crumbs);
//            for (int i = 0; i < crumbList.size()+1; i++) {
//                List<String> crumbSubList = crumbList.subList(0, i);
//                Breadcrumb breadcrumb = getFolderBreadCrumb(crumbSubList);
//                breadcrumbs.add(breadcrumb);
//            }
        }
        return breadcrumbs;
    }

    /**
     * Creates breadcrumb object
     * @param crumbPath relative directory of the breadcrumb
     * @return breadcrumb object
     */
    @Override
    public Breadcrumb getFolderBreadCrumb(StringBuilder crumbPath){
//        String crumbPath = String.join(File.separator, crumbSubList);
        final String directoryURL = "/nextfolder?folder=" + crumbPath;
        File folder = new File(String.valueOf(crumbPath));
        return new Breadcrumb(folder.getName(), directoryURL);
    }
}
