insert into users (id, name, email, password, role, enabled) values (1, 'admin', 'admin@admin', '$2a$10$exszT/QxUsV7b/eXhRURT.3A.BRCLFGb67EavWoWp3G/dHnkxkxlG', 'ADMIN', 1);
insert into images (id, path, orig_name, new_name) values (1, 'root/image.tiff', 'image.tiff', 'image.tiff');
insert into images (id, path, orig_name, new_name) values (2, 'root/image.png', 'image.png', 'image.png');
insert into images (id, path, orig_name, new_name) values (3, 'root/image.jpeg', 'image.jpeg', 'image.jpeg');
insert into images (id, path, orig_name, new_name) values (4, 'root/image1.tiff', 'image1.tiff', 'image1.tiff');
insert into images (id, path, orig_name, new_name) values (5, 'root/image2.png', 'image2.png', 'image2.png');
insert into images (id, path, orig_name, new_name) values (6, 'root/image3.jpeg', 'image3.jpeg', 'image3.jpeg');
insert into images (id, path, orig_name, new_name) values (7, 'root/image4.tiff', 'image4.tiff', 'image4.tiff');
insert into images (id, path, orig_name, new_name) values (8, 'root/image5.png', 'image5.png', 'image5.png');
insert into images (id, path, orig_name, new_name) values (9, 'root/image6.jpeg', 'image6.jpeg', 'image6.jpeg');

insert into image_attributes (id, name,  path, filepath, date, size, type) values (1, 'image.tiff', 'root', 'root/image.tiff','2019-05-12 23:47:22', 222.2, 'TIFF');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (2, 'image.png', 'root', 'root/image.png','2019-05-12 23:47:25', 3.1, 'PNG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (3, 'image.jpeg', 'root', 'root/image.jpeg','2019-05-12 23:47:29', 55.1, 'JPEG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (4, 'test.jpeg', 'root/folder1', 'root/image1.tiff', '2019-05-13 23:47:29', 90.4, 'JPEG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (5, 'dummy.png', 'root/folder1', 'root/image2.png','2019-05-13 23:47:55', 66, 'PNG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (6, 'tiff.tiff', 'root/folder2', 'root/image3.jpeg', '2019-05-14 23:11:22', 215.4, 'TIFF');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (7, 'image3.tiff', 'root/folder2/folder3', 'root/image4.tiff', '2019-05-14 23:13:22', 55.1, 'TIFF');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (8, 'image2.png', 'root/folder2/folder3', 'root/image5.png', '2019-05-14 23:21:55', 75.1, 'PNG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (9, 'hello.jpeg', 'root/folder1/folder3', 'root/image6.jpeg', '2019-05-14 23:11:17', 75.1, 'JPEG');


insert into images (id, orig_name, new_name, path) VALUES (10, 'image.tiff', 'image.tiff', 'root/image10.tiff');
insert into images (id, orig_name, new_name, path) VALUES (11, 'image.png', 'image.png', 'root/image11.png');
insert into images (id, orig_name, new_name, path) VALUES (12, 'image.jpeg', 'image.jpg', 'root/image12.jpg');
insert into images (id, orig_name, new_name, path) VALUES (13, 'test.jpeg', 'test.jpg', 'root/folder1/test.jpg');
insert into images (id, orig_name, new_name, path) VALUES (14, 'dummy.png', 'dummy.png', 'root/folder1/dummy.png');

insert into image_attributes (id, name,  path, filepath, date, size, type) values (10, 'image.tiff', 'root', 'root/image10.tiff','2019-05-12 23:47:22', 222.2, 'TIFF');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (11, 'image.png', 'root', 'root/image11.png','2019-05-12 23:47:25', 3.1, 'PNG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (12, 'image.jpeg', 'root', 'root/image12.jpg','2019-05-12 23:47:29', 55.1, 'JPG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (13, 'test.jpeg', 'root/folder1', 'root/folder1/test.jpg', '2019-05-13 23:47:29', 90.4, 'JPG');
insert into image_attributes (id, name,  path, filepath, date, size, type) values (14, 'dummy.png', 'root/folder1', 'root/folder1/dummy.png','2019-05-13 23:47:55', 66, 'PNG');


# insert into tags(tag) value ("human");
# insert into tags(tag) value ("Bloodcell");

#insert into image_tags(image_id, image_tag) values (1, "Human");
#INSERT into image_tags(image_id, image_tag) values (1, "Bloodcell");

insert into image_roi (roi_id, image_id)
values (1, 1);
insert into image_roi (roi_id, image_id)
values (2, 3);
insert into image_roi (roi_id, image_id)
values (3, 2);
insert into image_roi (roi_id, image_id)
values (4, 5);
insert into image_roi (roi_id, image_id)
values (5, 2);
insert into image_roi (roi_id, image_id)
values (6, 6);
insert into image_roi (roi_id, image_id)
values (7, 9);
insert into image_roi (roi_id, image_id)
values (8, 2);
insert into image_roi (roi_id, image_id)
values (9, 5);
insert into image_roi (roi_id, image_id)
values (10, 7);
insert into image_roi (roi_id, image_id)
values (11, 8);
insert into image_roi (roi_id, image_id)
values (12, 4);

insert into image_roi (roi_id, image_id)
values (13, 4);
insert into image_roi (roi_id, image_id)
values (14, 4);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (1, 7.5, 2, 70, 30);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (2, 6.2, 2, 50, 50);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (3, 7.5, 2, 70, 30);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (4, 6.2, 2, 50, 50);
insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (5, 7.5, 2, 70, 30);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (6, 6.2, 2, 50, 50);
insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (7, 7.5, 2, 70, 30);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (8, 6.2, 2, 50, 50);
insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (9, 7.5, 2, 70, 30);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (10, 6.2, 2, 50, 50);
insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (11, 7.5, 2, 70, 30);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (12, 6.2, 2, 50, 50);
insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (13, 7.5, 2, 70, 30);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (14, 6.2, 2, 50, 50);

insert into ROI_TAGS (roi_id, tag)
values (1, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (1, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (1, 'Bloodcell');

insert into ROI_TAGS (roi_id, tag)
values (2, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (2, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (2, 'Bloodcell');

insert into ROI_TAGS (roi_id, tag)
values (3, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (3, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (3, 'Bloodcell');

insert into ROI_TAGS (roi_id, tag)
values (4, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (4, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (4, 'Bloodcell');

insert into ROI_TAGS (roi_id, tag)
values (5, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (5, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (5, 'Bloodcell');

insert into ROI_TAGS (roi_id, tag)
values (6, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (6, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (6, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (7, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (7, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (7, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (8, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (8, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (8, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (9, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (9, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (9, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (10, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (10, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (10, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (11, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (11, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (11, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (12, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (12, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (12, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (13, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (13, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (13, 'Bloodcell');
insert into ROI_TAGS (roi_id, tag)
values (14, 'LowPh');

insert into ROI_TAGS (roi_id, tag)
values (14, 'testTag1');

insert into ROI_TAGS (roi_id, tag)
values (14, 'Bloodcell');


insert into ROI_TAGS (roi_id, tag)
values (2, 'testTag2');


