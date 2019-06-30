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

* spring.datasource.initialization-mode: needs to be `always` for the first run of the application then set to `never` to prevent data loss
* spring.profiles.active: uses diffrent application.properties file e.g.:
`spring.profiles.active=dev` uses `application-dev.properties`

## Deployment

```
use gradle for building

and run jar file.
```

## Testing

Running the development version of the application you can login using:

* username: `piet`
* password: `password`


## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* [Spring](https://spring.io/) - Java Web framework
* [thymeleaf](https://www.thymeleaf.org) - templating framework

#### JS libraries
```
| Library              | Version |
|----------------------|---------|
| Bootstrap            | 4.3.1   |
| Bootstrap-tagsinput  | 0.8.0   |
| Fabric.js            | 3.0.0   |
| Fancytree            | 2.30.2  |
| jQuery               | 3.3.1   |
| jQuery contextMenu   | 2.8.0   |
| jQuery UI - position | 1.12.1  |
| Popper.js            | 1.14.6  |
| TinySort             | 3.1.4   |
| toastr               | 2.1.3   |
| typeahead.js         | 0.11.1  |
```

## Authors

* **Jouke Profijt** - [JoukeProfijt](https://bitbucket.org/JoukeProfijt/)
* **Kim Chau Duong** - [kcduong](https://bitbucket.org/kcduong/)
* **Wietse Reitsma** - [Whreitsma](https://bitbucket.org/Whreitsma/)


## Acknowledgments

* Michiel Noback - giving example code, Code reviews and feedback
* Lambert instruments - Providing the project
