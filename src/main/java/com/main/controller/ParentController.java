package com.main.controller;

import com.main.dto.*;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.dto.converters.SchoolConverter;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.interfaces.IUser;
import com.main.service.AfterSchoolCareService;
import com.main.service.AttendanceService;
import com.main.service.SchoolService;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;
import com.main.util.events.OnRegistrationCompleteEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
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
            if (roles.contains("ROLE_PARENT")) {
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/schools")
    public List<SchoolDTO> getSchools() {
        return schoolService.getAll().stream().map(SchoolConverter::toDto).collect(Collectors.toList());
    }

    @GetMapping("/school/{id}")
    SchoolDTO getSchool(@PathVariable @Min(1) Long id) {
        return SchoolConverter.toDto(schoolService.findOne(id));
    }

    @GetMapping("/after_school_cares/types")
    Map<Integer, String> getAfterSchoolCaresTypes() {
        return AfterSchoolCare.getTypes();
    }

    @GetMapping("/booked_after_school_cares")
    public List<AfterSchoolCareDTO> getBookedAfterSchoolCares(Authentication auth) {
        return afterSchoolCareService.getAllByParent(auth.getName()).stream().map(AfterSchoolCareConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/after_school_cares")
    public List<AfterSchoolCareDTO> getAfterSchoolCares(
            @RequestParam(required = false, name = "school") Long schoolId,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return afterSchoolCareService.getAll().stream()
                .filter(afterSchoolCare -> schoolId == null || afterSchoolCare.getParticipatingSchool().getId().equals(schoolId))
                .filter(afterSchoolCare -> type == null || afterSchoolCare.getType().getId() == type)
                .filter(afterSchoolCare -> startDate == null || afterSchoolCare.getEndTime().isAfter(startDate))
                .filter(afterSchoolCare -> endDate == null || afterSchoolCare.getStartTime().isBefore(endDate))
                .map(AfterSchoolCareConverter::toDto)
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
    @Transactional
    public List<UserDTO> getChilds(Authentication auth) {
        return ((User) userService.findByUsername(auth.getName())).getChildren().stream().map((child) -> {
            User.UserBuilder<IUser> builder = User.UserBuilder.next();
            builder.withUser(child);
            return (UserDTO) builder.toDto("CHILD");
        }).collect(Collectors.toList());
    }

    @PostMapping("/child")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity createChild(@RequestBody ChildDTO childDTO, Authentication auth, Errors errors,
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

        if (auth.getName() != null && !auth.getName().isEmpty()) {
            User parent = (User) userService.findByUsername(auth.getName());
            registered.setParent(parent);
            userService.update(registered);
            parent.addChild(registered);
            userService.update(parent);
        }
        User.UserBuilder builder = User.UserBuilder.next();
        builder.withUser(registered);
//        return new ResponseEntity<>("Created: " + registered.getRoles(), HttpStatus.CREATED);
//        return new ResponseEntity<>("Created: " + registered.getId(), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body((UserDTO) builder.toDto("CHILD"));
    }

    @PatchMapping("/child/{username}")
    @Transactional
    IUserDTO updateChild(@RequestBody Map<String, String> update, @PathVariable String username) {
        User child = (User) userService.findByUsername(username);

        Long school;

        String fullname = update.get("fullname");
        if (fullname != null && !fullname.isEmpty()) {
            child.setFullname(fullname);
        }

        String schoolClass = update.get("schoolClass");
        if (schoolClass != null && !schoolClass.isEmpty()) {
            child.setSchoolClass(schoolClass);
        }

        if (update.get("school") != null) {
            school = Long.parseLong(update.get("school"));
            child.setChildSchool(schoolService.findOne(school));
        } else{
            school = child.getChildSchool().getId();
        }

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

    @GetMapping("/child/{username}")
    UserDTO getChild(@PathVariable @Min(1) String username) {
        User.UserBuilder builder = User.UserBuilder.next();
        builder.withUser((User) userService.findByUsername(username));
        return (UserDTO) builder.toDto("CHILD");
    }

    //Funktioniert noch nciht richtig, zum Schutz vor unerwarteten Auswirkungen erst einmal deaktiviert
    @DeleteMapping("/child/{username}")
    @Transactional
    ResponseEntity<String> deleteChild(@PathVariable String username, Authentication auth) {
        User parent = (User) userService.findByUsername(auth.getName());
        User child = (User) userService.findByUsername(username);

        parent.removeChild(child);
        userService.deleteUser(child);


        userService.update(parent);

        return new ResponseEntity<>("Das Kind wurde erfolgreich gelöscht!", HttpStatus.OK);

    }

    private String createErrorString(Errors errors) {
        return errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
    }

}

