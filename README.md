# Extracurricular Activities Management Software Backend

This repository is part of the Extracurricular Activities Management Software.
It provides a REST API for the project.
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Maven 
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
mvn spring-boot:run
```

The system can be opened in localhost:8080
```
localhost:8080
```
## Running the tests

Explain how to run the automated tests for this system
## Example Users
Header should include Content-Type application/json 
You will need to verify the use before using it(if it isnt a CHILD or a USER.
 
There are following test user to play around with: 
 * Parent_Test 
 * Employee_Test 
 * Employee_SchoolCoordinator_Test 
 * Management_Test 
 * Teacher_Test 
 * Teacher_SchoolCoordinator_Test 
 * User_Test 
 * Child_Test
 
The password is always password
One Paragraph of project description goes here

## Example Body User Creation with Rest Endpoint /register

```
{
"userType":"TEACHER",
"username": "REST_Teacher",
"password": "password",
"email": "malte.petersen11@gmail.com",
"fullname": "Rest_Test",
"phoneNumber":"13141",
"subject":"Geschichte",
"iban":"23465732456",
"address":"Hans-Detlev-Prien Str. 8", 
"schoolClass":"7a",
"isSchoolCoordinator" : false
}
```

## Deployment

Deployment with maven: 
```
mvn clean install
```
## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors
* **Malte Petersen** - *Project Lead* - [MaltePetersen](https://github.com/MaltePetersen)
* **Markus** - *Backend developer* - [Oberstrike](https://github.com/Oberstrike)
* **Bendix** - *Backend developer* - [TaskPlays](https://github.com/TaskPlays)
* **Timo** - *Backend developer* - [Sibo0](https://github.com/Sibo0)
* **Hendrik** - *Frontend developer* - [PeterPan86](https://github.com/PeterPan86)
* **Daniel Last** - *Frontend developer* - [DanielLast1997](https://github.com/DanielLast1997)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* DHSH
* Fjoerde

