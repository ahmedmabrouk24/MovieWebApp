
# Movie Management Dashboard

This project is a Movie Management System designed using **Java**, **Spring Boot**, and **Angular 19** to provide a dynamic and interactive platform for managing and exploring movies. The system is built to accommodate three distinct user roles: *Admin*, *Authenticated User*, and *Unauthenticated User*, each with different levels of access and functionalities.





## Admin Dashboard:

- **Login functionality** for admin users to access the admin panel.
- **Movie List:** The admin dashboard loads a list of added movies.
- **Search Movies:** Admins can search for movies by title. The results are dynamically fetched from the OMDB API.
- **Movie Details:** Admin users can view detailed information about each movie.  
- **Manage Movies:**    
    - **Individual Movie Management:** Admins can add or remove movies one by one.
    - **Batch Movie Management:** Admins can add or remove multiple movies at once, making it easier to manage the movie database in bulk.


## Authenticated User Dashboard:

- **Login functionality** for authenticated users to access the user dashboard.
- **Movie List:** Authenticated users can view a complete list of movies added by the admin, fetched directly from the database.
- **Search Movies:** Users can search for movies by title. The results are fetched from the database.
- **Movie Rating:** Each authenticated user can rate individual movies based on their preference.
- **Movie Details:** Authenticated users can view detailed information about each movie.   


## Unauthenticated User Dashboard

- **No Login Required:** Unauthenticated users can access the movie list without logging in.
- **Movie List:** Unauthenticated user can view a list of movies added by the admin, fetched directly from the database.
- **Search Movies:** Unauthenticated user can search for movies by title. The results are fetched from the database.
- **Movie Details:** Unauthenticated user can view detailed information about each movie.   


## Features 
- **Pagination:** Pagination is implemented for efficient viewing of large movie lists.
- A **search bar** for regular users to find specific movies.
- **Rating** a specific movie for each user.
- **Batch adding** or **batch removing** movies for admin users.


## Tech Stack
- **Backend:** Java 17, SpringBoot 3.4.3.
- **Frontend:** Angular 19, Node.js 22.14.0




## Steps to run locally
Clone the project

```bash
  git clone https://github.com/ahmedmabrouk24/MovieWebApp.git
```

Go to the project directory

```bash
  cd <project-directory>
```
### 1. Backend (Spring Boot Application)
Setup the Database:

- Ensure that **MySQL** is installed and running on your machine.
- Create a database named ```movies``` if it doesn't exist. You can either create it manually or the application will create it automatically.

```bash
  CREATE DATABASE movies;
```

Update the database connection details in "src/main/resources/application.properties".
These credentials (username: root, password: 1234) are used to connect to the MySQL database.

```bash 
spring.datasource.url=jdbc:mysql://localhost:3306/movies?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=1234
```


Build the Backend: Run the following Maven command to build the Spring Boot project:
```bash
  mvn clean install
```

Run the Backend Application: After the build is complete, run the application using the following command:
```bash
  mvn spring-boot:run
```
This will start the Spring Boot backend application. By default, it will be running on http://localhost:8080.


- Admin credentials:
```bash
Username: admin@gmail.com
Password: 123456
```

### 2. Frontend (Angular Application)
Prerequisites:
- Node.js 22.14.0 installed on your machine.

- Angular CLI installed globally. 
To install Angular CLI globally, run:
```bash
npm install -g @angular/cli
```

**Install the Dependencies:** Install all the necessary npm packages required by the Angular project.
```bash
npm install
```

**Run the Angular Application:** Start the Angular development server with the following command:
```bash
ng serve 
```

This will run the Angular frontend on http://localhost:4200.
You should see the application’s homepage and be able to interact with the movie management system.




### Unauthantecatied user dashboard
Unauthantecatied user dashboard.
![Unauthantecatied user dashboard](/MovieWebApp%20screanshoots/unloggedin%20user.PNG)

### Admin View - Batch Adding Movies
**Movie Details Page** of the Movie Web App, where users can view detailed information about a specific movie, including title, description, and ratings.
![Admin View - Batch Adding Movies](/MovieWebApp%20screanshoots/Movie%20details%20page.PNG)

### Login form
Login form.
![Login form](/MovieWebApp%20screanshoots/loginForm.PNG)


### Signup form
signup form.
![Signup form](/MovieWebApp%20screanshoots/sign%20up%20form.PNG)


### User view - show all existing movies
**User View** of the Movie Web App, where users can browse and view all existing movies available in the system. Additionally, users can **rate any movie** they choose based on their preference.
![User view - show all existing movies](/MovieWebApp%20screanshoots/User%20view%20-%20show%20all%20existing movies.PNG)


### User view - show all existing movies
**User View** of the Movie Web App, where users can search for specific movies in the database.
![User view - show all existing movies](/MovieWebApp%20screanshoots/User%20view%20-%20show%20all%20existing movies.PNG)

### Admin view - remove existing movies
**Admin View** of the Movie Web App, where admins have the ability to **remove existing movies** from the system.
![Admin view - remove existing movies](/MovieWebApp%20screanshoots/Admin%20view%20-%20remove%20existing%20movies.PNG)


### Admin view - batch removing
**Admin View** of the Movie Web App, where admins can **batch remove multiple movies** from the system at once, streamlining the process of managing the movie database.
![Admin view - batch removing](/MovieWebApp%20screanshoots/Admin%20view%20-%20batch%20removing.PNG)


## Admin view - batch adding
**Admin View** of the Movie Web App, where admins can **batch add multiple movies** to the system at once, allowing for efficient management and addition of movies to the database.
![Admin view - batch adding](/MovieWebApp%20screanshoots/Admin%20view%20-%20batch%20adding.PNG)





