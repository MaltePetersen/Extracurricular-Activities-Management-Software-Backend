package com.main.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Veraltet, da auf Konstruktoren verzichtet werden sollte
 *
 * @author Markus
 */

@Component
public class UserData {

    PasswordEncoder encoder;

    @Autowired
    public UserData(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public Map<User, String> getUserData() {
        User.UserBuilder<User> builder = User.UserBuilder.next();


        User parent = builder.withName("Parent_Test").withPassword(encoder.encode("password")).withFullname("Craig Walls").withEmail("craig@walls.com").withPhoneNumber("112").build();
        builder = User.UserBuilder.next();
        User parent2 = builder.withName("Parent_Test2").withPassword(encoder.encode("password")).withFullname("Craig Walls 2").withEmail("craig2@walls.com").withPhoneNumber("112").build();
        builder = User.UserBuilder.next();
        User parent3 = builder.withName("Parent_Test3").withPassword(encoder.encode("password")).withFullname("Andreas Müller").withEmail("andreas@müller.com").withPhoneNumber("112").build();
        builder = User.UserBuilder.next();
        User management = builder.withName("Management_Test").withPassword(encoder.encode("password")).withFullname("Malte Petersen")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").build();
        builder = User.UserBuilder.next();
        User employee = builder.withName("Employee_Test").withPassword(encoder.encode("password")).withFullname("Malte Petersen")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").build();
        builder = User.UserBuilder.next();
        User employee2 = builder.withName("Employee_Test2").withPassword(encoder.encode("password")).withFullname("Mitarbeiter Malte 2")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").build();
        builder = User.UserBuilder.next();
        User teacher = builder.withName("Teacher_Test").withPassword(encoder.encode("password")).withFullname("Malte Petersen")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").build();
        builder = User.UserBuilder.next();
        User child = builder.withName("Child_Test").withPassword(encoder.encode("password")).withFullname("Malte Petersen")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").withSchoolClass("7a").build();
        builder = User.UserBuilder.next();
        User child2 = builder.withName("Child_Test2").withPassword(encoder.encode("password")).withFullname("Malte Petersen 2")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").withSchoolClass("7b").build();
        builder = User.UserBuilder.next();
        User child3 = builder.withName("Child_Test3").withPassword(encoder.encode("password")).withFullname("Benno Müller")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").withSchoolClass("7b").build();
        builder = User.UserBuilder.next();
        User child4 = builder.withName("Child_Test4").withPassword(encoder.encode("password")).withFullname("Merlin Wizard")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").withSchoolClass("3b").build();
        builder = User.UserBuilder.next();
        User child5 = builder.withName("Child_Test5").withPassword(encoder.encode("password")).withFullname("Nathalie Müller")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").withSchoolClass("3b").build();
        builder = User.UserBuilder.next();
        User schoolCoordinator =  builder.withName("School_Coordinator_Test").withPassword(encoder.encode("password")).withFullname("Malte Petersen")
                .withEmail("malte.petersen@web.de").withPhoneNumber("123-123-1234").withAddress("Geschichte").withSubject("Geschichte").withIban("1374816241982437").build();

        parent.setVerified(true);
        parent2.setVerified(true);
        parent3.setVerified((true));
        employee.setVerified(true);
        employee2.setVerified(true);
        teacher.setVerified(true);
        child.setVerified(true);
        child2.setVerified(true);
        child3.setVerified(true);
        child4.setVerified(true);
        child5.setVerified(true);
        management.setVerified(true);
        schoolCoordinator.setVerified(true);
        Map<User, String> users = new HashMap<>();
        users.put(employee, "EMPLOYEE");
        users.put(employee2, "EMPLOYEE");
        users.put(teacher, "TEACHER");
        users.put(child, "CHILD");
        users.put(child2, "CHILD");
        users.put(child3, "CHILD");
        users.put(child4, "CHILD");
        users.put(child5, "CHILD");
        users.put(parent, "PARENT");
        users.put(parent2, "PARENT");
        users.put(parent3, "PARENT");
        users.put(management, "MANAGEMENT");
        users.put(schoolCoordinator, "SCHOOLCOORDINATOR");
        return users;
    }
}
