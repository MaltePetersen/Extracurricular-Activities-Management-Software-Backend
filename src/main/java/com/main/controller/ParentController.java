package com.main.controller;

import com.main.dto.*;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.dto.converters.SchoolConverter;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.service.AfterSchoolCareService;
import com.main.service.AttendanceService;
import com.main.service.SchoolService;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;
import com.main.util.events.OnRegistrationCompleteEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parent")
@CrossOrigin
public class ParentController {
    SchoolService schoolService;
    AfterSchoolCareService afterSchoolCareService;
    AttendanceService attendanceService;
    List<User> childs;
    private UserDTOValidator userDTOValidator;
    private UserService userService;
    private ApplicationEventPublisher eventPublisher;
    private PasswordEncoder encoder;

    ParentController(AfterSchoolCareService afterSchoolCareService, AttendanceService attendanceService, UserService userService, UserDTOValidator userDTOValidator, ApplicationEventPublisher eventPublisher, PasswordEncoder encoder, SchoolService schoolService) {
        this.afterSchoolCareService = afterSchoolCareService;
        this.attendanceService = attendanceService;
        this.userService = userService;
        this.userDTOValidator = userDTOValidator;
        this.eventPublisher = eventPublisher;
        this.encoder = encoder;
        this.schoolService = schoolService;
        childs = new ArrayList<>();
        //generateChildren();
    }

   /* private void generateChildren(){
        childs = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            UserDTO child = UserDTO.builder().address("Adresse" + i).fullname("Kind "+ i).userType("ROLE_CHILD").schoolClass(i + "b").username("Kind " + i).build();
            childs.add(child);
        }
    }*/

    @GetMapping("/authority")
    public ResponseEntity<Void> isParent(Authentication auth) {
        if (auth != null) {
            List<String> roles = new ArrayList<>();
            auth.getAuthorities().forEach((a) -> {
                roles.add(a.getAuthority());
            });
            if(roles.contains("ROLE_PARENT")) {
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/schools")
    public List<SchoolDTO> getSchools() {
        return schoolService.getAll().stream().map(SchoolConverter::toDto).collect(Collectors.toList());
    }

    @GetMapping("/booked_after_school_cares")
    public List<AfterSchoolCareDTO> getBookedAfterSchoolCares() {
        // TODO: filtern auf gebuchte AfterSchoolCares fehlt noch
        return afterSchoolCareService.getAll().stream().map(AfterSchoolCareConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/after_school_cares")
    public List<AfterSchoolCareDTO> getAfterSchoolCares() {
        return afterSchoolCareService.getAll().stream().map(AfterSchoolCareConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/after_school_cares/{id}")
    public AfterSchoolCareDTO getAfterSchoolCare(@PathVariable Long id) {
        return AfterSchoolCareConverter.toDto(afterSchoolCareService.findOne(id));
    }

    @PostMapping("/after_school_cares/{afterSchoolCareId}/attendance")
    @ResponseStatus(HttpStatus.CREATED)
    AfterSchoolCareDTO addAttendance(@RequestBody AttendanceInputDTO attendanceInputDTO, @PathVariable Long afterSchoolCareId) {
        // TODO: Eltern sollten nur Attendances für eigene Kinder erstellen dürfen
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(afterSchoolCareId);

        attendanceService.saveByInputDTO(attendanceInputDTO, afterSchoolCare);

        return AfterSchoolCareConverter.toDto(afterSchoolCare);
    }

    @DeleteMapping("/attendance/{id}")
    ResponseEntity<String> deleteAttendance(@PathVariable Long id) {
        // TODO: Eltern sollten nur Attendances von eigenen Kindern entfernen dürfen
        attendanceService.deleteById(id);
        return new ResponseEntity<>("Deleted attendance: " + id, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/children")
    public List<UserDTO> getChilds(Authentication auth) {
        return ((User) userService.findByUsername(auth.getName())).getChildren().stream().map((child)-> {
            User.UserBuilder builder = User.UserBuilder.next();
            builder.withUser(child);
            return (UserDTO) builder.toDto("CHILD");
        }).collect(Collectors.toList());
    }

    @PostMapping("/child")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createChild(@RequestBody ChildDTO childDTO, Authentication auth, Errors errors,
                                              WebRequest request) {
        UUID username = UUID.randomUUID();
        //childDTO.setUsername(username.toString());
        UUID email = UUID.randomUUID();
        //childDTO.setEmail(email.toString() + "@test.de");
        UserDTO userDTO = (UserDTO) childDTO.toUserDTO(username.toString(), encoder.encode("password"), email.toString() + "@test.de");
        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            return new ResponseEntity<>(createErrorString(errors), HttpStatus.BAD_REQUEST);
        }

        User registered = (User) userService.createAccount(userDTO, auth);

        if (registered == null)
            return new ResponseEntity<>("Error creating the account", HttpStatus.BAD_REQUEST);

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Fehler beim Versenden", HttpStatus.BAD_REQUEST);
        }

        if(auth.getName() != null && !auth.getName().isEmpty()) {
            User parent = (User) userService.findByUsername(auth.getName());
            registered.setParent(parent);
            parent.addChild(registered);
            userService.update(parent);
        }
//        return new ResponseEntity<>("Created: " + registered.getRoles(), HttpStatus.CREATED);
        return new ResponseEntity<>("Created: " + registered.getId(), HttpStatus.CREATED);
    }

    @PatchMapping("/child/{id}")
    IUserDTO updateChild(@RequestBody Map<String, String> update, @PathVariable Long id){
        User child = userService.findOne(id);

        String fullname = update.get("fullname");
        if(fullname != null && !fullname.isEmpty()){
            child.setFullname(fullname);
        }

        String schoolClass = update.get("schoolClass");
        if(schoolClass != null && !schoolClass.isEmpty()){
            child.setSchoolClass(schoolClass);
        }

        Long school = Long.parseLong(update.get("school"));
        child.setChildSchool(schoolService.findOne(school));

        /*String schoolString = update.get("school");
        if(schoolString != null && !schoolString.isEmpty()){
            child.setChildSchool(schoolService.findOne(Long.parseLong(schoolString)));
        }*/

        userService.update(child);
        User.UserBuilder builder = User.UserBuilder.next();
        builder.withUser(child);
        IUserDTO dto = builder.toDto("CHILD");
        dto.setSchool(school);
        return dto;
    }

    @GetMapping("/child/{id}")
    UserDTO getChild(@PathVariable @Min(1) Long id) {
        User.UserBuilder builder = User.UserBuilder.next();
        builder.withUser(userService.findOne(id));
        return (UserDTO) builder.toDto("CHILD");
    }

    //Funktioniert noch nciht richtig, zum Schutz vor unerwarteten Auswirkungen erst einmal deaktiviert
   /* @DeleteMapping("/child/{id}")
    void deleteChild(@PathVariable Long id, Authentication auth) {
        User parent = (User) userService.findByUsername(auth.getName());
        User child = userService.findOne(id);

        parent.removeChild(child);
        userService.deleteUser(child);


        userService.update(parent);

    }*/

    private String createErrorString(Errors errors) {
        return errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
    }

}

