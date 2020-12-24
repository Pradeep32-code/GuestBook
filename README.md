# Guest Book Application
#User
*Guests
*Administrator
#Action
Guests
*	User needs to login in order to write a new entry in the guestbook
*	Guestbook entry can be either a single image or a text

Administrator
*	View all the entries posted by all the users
*	Approve the entries 
*	Remove the entries

##Credential to login for spring security
username-admin
password-admin

##Credential to login to application with 2 different user

Admin User-Admin
Admin Password-Admin123

Guest User-Guest
Guest Password-Guest123

## Tools and Technologies used

* Java 8
* Spring boot
* MySQL
* Spring JPA
* Maven
* Thymeleaf 
* Spring Security

## Steps to install

**1. Clone the application**

git clone https://github.com/Pradeep32-code/GuestBook.git


**2. Create MySQL database**

CREATE DATABASE login

	
**3. Create table or Run the SQL script file**

CREATE TABLE `guestledger` (
  `entry_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approval_status` varchar(255) DEFAULT NULL,
  `image` varchar(64) DEFAULT NULL,
  `text` mediumtext,
  `user_id` int(11) DEFAULT NULL,
  `photos_image_path` varchar(255) DEFAULT NULL,
  `pic_byte` blob,
  PRIMARY KEY (`entry_id`),
  KEY `FKdgyh42uuer5ibsecsm39u9sp2` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
SELECT * FROM login.users;

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SELECT * FROM login.user_role;

CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'USER');
	
**4. Change MySQL Username and Password as per your MySQL Installation**
	
open `src/main/resources/application.properties` file.

change `spring.datasource.username` and `spring.datasource.password` as per your installation
	
**5. Run the app**

*Right click on Project and Select option 'Run As' then 'Spring Boot App'.

*We can also run the spring boot app by typing the below command -

mvn spring-boot:run


We can also package the application in the form of a `jar` file and then run it like so -

mvn package
java -jar target/guestbook-0.0.1-SNAPSHOT.jar


