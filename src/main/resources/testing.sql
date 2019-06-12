insert into image_roi (roi_id, image_id, x1, y1, x2, y2)
values (1, 1, 1, 2, 3, 4);
insert into image_roi (roi_id, image_id, x1, y1, x2, y2)
values (2, 1, 1, 2, 3, 4);

insert into ROI_STATE ( roi_id, ph, T, o2, co2)
values (1,7.5,5,50,20);

insert into ROI_STATE (roi_id, ph, T, o2, co2)
values (2,7.5,5,50,20);



insert into ROI_TAGS (roi_id, tag)
values (1, 'BloodCell');

insert into ROI_TAGS (roi_id, tag)
values (1, 'Human');

insert into ROI_TAGS (roi_id, tag)
values (1, 'lowPH');

insert into ROI_TAGS (roi_id, tag)
values (2, 'BloodCell');

insert into ROI_TAGS (roi_id, tag)
values (2, 'Mouse');

insert into ROI_TAGS (roi_id, tag)
values (2, 'HighPH');
