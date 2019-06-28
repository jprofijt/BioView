# BioView - High quantity image library

BioView is  a simple to use, user friendly and easy to understand application for researchers in the biological Imaging field.

Researchers need somewhere to keep track of their work and that's where this product comes in. Save, tag and edit your images and share them with your fellow researchers within one big library.


## Getting Started

```
git clone https://bitbucket.org/JoukeProfijt/image-library/src/master/
```

Clone the project, open gradle project in Intellij

And select Java version 8
### Prerequisites

For this library to work you need the Bioview metadata API to also be installed

```
git clone https://bitbucket.org/Whreitsma/bioview-metadata-api/src/master/
1. clone api
2. run with intellij or build with gradle using correct application.properties
3. Make sure api port is set to 8081 in properties file
```

### Installing


```
git clone https://bitbucket.org/JoukeProfijt/image-library/src/master/
```

Clone the project, open gradle project in Intellij

And select Java version 8

Set the following application properties:

* library.upload: storage location for image library

* library.sym: can be left default, must be in application context. symbolic link location for serving images

* library.sym.thumbnail: can be left default, must be in application context. symbolic link location for serving thumbnails

* cache-location: storage space for thumbnails

* spring.datasource.url: mysql database url

* spring.datasource.username: mysql database username

* spring.datasource.password: mysql database user password

* spring.profiles.active: uses diffrent application.properties file e.g.:
`spring.profiles.active=dev` uses `application-dev.properties`

## Deployment

```
use gradle for building

and run jar file.
```

## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Spring](https://spring.io/) - Java Web framework
* [Bootstrap](https://getbootstrap.com/) - html, css, js framework
* [thymeleaf](https://www.thymeleaf.org) - templating framework

## Authors

* **Jouke Profijt** - [JoukeProfijt](https://bitbucket.org/JoukeProfijt/)
* **Kim Chau Duong** - [kcduong](https://bitbucket.org/kcduong/)
* **Wietse Reitsma** - [Whreitsma](https://bitbucket.org/Whreitsma/)


## Acknowledgments

* Michiel Noback - giving example code, Code reviews and feedback
* Lambert instruments - Providing the project
