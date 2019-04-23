package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;/*
 * Copyright (c) 2018 Kim Chau Duong
 * All rights reserved
 */

import nl.bioinf.jp_kcd_wr.image_library.model.Breadcrumb;

import java.io.File;
import java.util.List;

public interface BreadcrumbBuilder {
    List<Breadcrumb> getBreadcrumbs(String directory);

    Breadcrumb getFolderBreadCrumb(List<String> crumbSubList);
}
