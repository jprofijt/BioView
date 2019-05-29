INSERT INTO users (name, email, password, role, enabled)VALUES
              ("Admin_User", "Admin.example@example.com", "$2a$10$c8BI3BFm2tyD6FlTldS3v.ibJIM9hQZPIiHwAz7nXApYLotbPqQiO", "ADMIN", 1),
              ("Lisa","Lisa67@smoething.nl","bLAbla","USER",1),
              ("piet", "piet@example.nl", "$2a$10$mLIwwhNS.gpxuktYoMZKAefml/6y0cvvdOUDA3SJjaNomgWwrXnHO", "USER", 1);



#insert into images_meta (id, path, date, size, type) values (1, 'root/image.tiff', '2019-05-12 23:47:22', 222.2, 'TIFF');
#insert into images_meta (id, path, date, size, type) values (2, 'root/image.png', '2019-05-12 23:47:25', 3.1, 'PNG');
#insert into images_meta (id, path, date, size, type) values (3, 'root/image.jpeg', '2019-05-12 23:47:29', 55.1, 'JPEG');


insert into tags(tag) value ("human");
insert into tags(tag) value ("Bloodcell");

#insert into image_tags(image_id, image_tag) values (1, "Human");
#INSERT into image_tags(image_id, image_tag) values (1, "Bloodcell");