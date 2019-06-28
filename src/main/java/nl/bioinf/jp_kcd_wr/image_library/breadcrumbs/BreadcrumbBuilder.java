package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */

import java.util.List;

public interface BreadcrumbBuilder {
    List<Breadcrumb> getBreadcrumbs(String directory);
}
