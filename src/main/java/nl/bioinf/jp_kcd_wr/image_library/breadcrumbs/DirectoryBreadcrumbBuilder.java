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

    /**
     * constructor
     */
    @Autowired
    public DirectoryBreadcrumbBuilder() {
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
            Iterator<Path> pathIterator = buildPathIterator(directory);
            StringBuilder crumbPath = new StringBuilder();
            while (pathIterator.hasNext()){
                if(crumbPath.length() != 0) {
                    crumbPath.append("/");
                }
                crumbPath.append(pathIterator.next());
                Breadcrumb breadcrumb = getFolderBreadCrumb(crumbPath);
                breadcrumbs.add(breadcrumb);
            }
        }
        return breadcrumbs;
    }

    /**
     * Build a path iterator
     * @param directory directory path
     * @return path iterator
     */
    private Iterator<Path> buildPathIterator(String directory){
        Path path = Paths.get(directory);
        return path.iterator();
    }

    /**
     * Creates breadcrumb object
     * @param crumbPath relative directory of the breadcrumb
     * @return breadcrumb object
     */

    private Breadcrumb getFolderBreadCrumb(StringBuilder crumbPath){
        final String directoryURL = "/imageview?location=" + crumbPath;
        File folder = new File(String.valueOf(crumbPath));
        return new Breadcrumb(folder.getName(), directoryURL);
    }
}
