package com.main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.AttendanceDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.dto.converters.AttendanceConverter;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.service.implementations.AfterSchoolCareService;
import com.main.service.implementations.AttendanceService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.main.dto.UserDTO;
import com.main.model.User;
import com.main.service.implementations.UserService;
import com.main.util.UserDTOValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/management")
public class ManagementController {


    private UserService userService;
    private UserDTOValidator userDTOValidator;
    private AttendanceService attendanceService;
    private AfterSchoolCareService afterSchoolCareService;

    public ManagementController(UserService userService, UserDTOValidator userDTOValidator, AttendanceService attendanceService, AfterSchoolCareService afterSchoolCareService) {
        this.userService = userService;
        this.userDTOValidator = userDTOValidator;
        this.attendanceService = attendanceService;
        this.afterSchoolCareService = afterSchoolCareService;
    }


    @GetMapping("/not_enabled_users")
    public List<UserDTO> allNotEnabledUser() {
        List<UserDTO> allUserDTOsNotVerified = new ArrayList<>();
        for (User user : userService.findAllByVerified(false)) {
            allUserDTOsNotVerified.add(UserDTO.builder().userType(user.getRoles().toString()).username(user.getUsername()).password(user.getPassword()).fullname(user.getFullname()).schoolClass(user.getSchoolClass()).phoneNumber(user.getPhoneNumber()).subject(user.getSubject()).
                    iban(user.getIban()).address(user.getAddress()).build());
        }
        return allUserDTOsNotVerified;
    }


    @GetMapping("/users")
    public List<UserDTO> getAllUsers(@RequestParam(name = "type", required = false) Integer type) {

        List<UserDTO> userDTOS;

        if (type == null) {
            userDTOS = userService.findAll().stream()
                    .map(each -> (UserDTO) User.UserBuilder.next().withUser(each).toDto("USER"))
                    .collect(Collectors.toList());
        } else {
            userDTOS = userService.findAll().stream()
                    .filter(each -> each.getRoles().get(0).getId() == type)
                    .map(each -> (UserDTO) User.UserBuilder.next().withUser(each).toDto("User"))
                    .collect(Collectors.toList());
        }
        return userDTOS;
    }


    //Attendance
    @GetMapping("/attendanceLists")
    public List<AttendanceDTO> getAllAttendanceLists() {
        return attendanceService.getAll().stream().map(AttendanceConverter::toDto).collect(Collectors.toList());
    }

    @GetMapping("/attendanceList/{id}")
    public AttendanceDTO getAttendanceListById(@PathVariable long id) {
        return AttendanceConverter.toDto(attendanceService.findOne(id));
    }

    @PatchMapping("/attendanceList/{id}")
    public AttendanceDTO patchAttendanceListById(@RequestBody AttendanceDTO attendanceDTO, @PathVariable int id) {
        //TODO Siehe AttendanceService
        return null;
    }

    @DeleteMapping("/attendanceList/{id}")
    public ResponseEntity<String> deleteAttendanceListById(@RequestBody(required = false) AttendanceDTO newAttendanceList, @PathVariable long id) {
        try {
            attendanceService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {

        }
        return ResponseEntity.badRequest().build();
    }
    //-----------------

    //AfterSchoolCares
    @GetMapping("/afterSchoolCare")
    public List<AfterSchoolCareDTO> getAllAfterSchoolCareServices() {
        return afterSchoolCareService.getAll().stream().map(AfterSchoolCareConverter::toDto).collect(Collectors.toList());
    }

    @GetMapping("/afterSchoolCare/{id}")
    public AfterSchoolCareDTO geAfterSchoolCareById(@PathVariable long id) {
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);
        if (afterSchoolCare != null)
            return AfterSchoolCareConverter.toDto(afterSchoolCare);
        return null;
    }

    @PatchMapping("/afterSchoolCare/{id}")
    public AfterSchoolCareDTO patchAfterSchoolCare(@RequestBody AfterSchoolCareDTO afterSchoolCareDTO, @PathVariable long id) {
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);
        if (afterSchoolCare == null)
            return null;
        if (afterSchoolCareDTO.getStartTime() != null)
            afterSchoolCare.setStartTime(afterSchoolCareDTO.getStartTime());
        if (afterSchoolCareDTO.getEndTime() != null)
            afterSchoolCare.setEndTime(afterSchoolCareDTO.getEndTime());
        if (afterSchoolCareDTO.getName() != null)
            afterSchoolCare.setName(afterSchoolCareDTO.getName());
        if (afterSchoolCareDTO.getType() != 0)
            afterSchoolCare.setType(AfterSchoolCare.Type.getById(afterSchoolCareDTO.getType()));

        afterSchoolCareService.save(afterSchoolCare);
        return AfterSchoolCareConverter.toDto(afterSchoolCare);
    }

    @DeleteMapping("/afterSchoolCare/{id}")
    public ResponseEntity<String> deleteAfterSchoolCare(@RequestBody(required = false) AfterSchoolCareDTO afterSchoolCareDTO, @PathVariable long id) {
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);

        if (afterSchoolCare == null) {
            return ResponseEntity.unprocessableEntity().build();
        } else {
            afterSchoolCareService.deleteById(id);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/afterSchoolCare")
    public ResponseEntity<String> addAfterSchoolCare(@RequestBody(required = false) AfterSchoolCareDTO afterSchoolCareDTO){
        AfterSchoolCareDTO response = afterSchoolCareService.createNew(afterSchoolCareDTO);
        if(response == null){
            return ResponseEntity.unprocessableEntity().build();
        }else{
            return ResponseEntity.ok().build();
        }

    }
    //-------------------


    @GetMapping("/number_of_participants")
    public int getNumberOfParticipants() {
        return 0;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO, Authentication authentication, Errors errors) {
        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            return new ResponseEntity<>(createErrorString(errors), HttpStatus.BAD_REQUEST);
        }
        if (userService.createAccount(userDTO, authentication) != null)
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        return new ResponseEntity<>("Fehler in Anfrage", HttpStatus.BAD_REQUEST);
    }

    private String createErrorString(Errors errors) {
        return errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
    }

    //Anwesenheitsliste
    @GetMapping("/list")
    public byte[] getAttendanceList() throws Exception {
        byte[] data = attendanceService.getAttendanceList();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/csv"));
        headers.setContentDispositionFormData("attendanceList.csv", "attendanceList.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(data, headers, HttpStatus.OK).getBody();
    }

    //DurchschnittlicheTeilnehmerListe
    @GetMapping("/averageList")
    public byte[] getAverageParticipantsList() throws Exception {
        byte [] data = attendanceService.getAverageParticipantsList();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/csv"));
        headers.setContentDispositionFormData("averageParticipants.csv", "averageParticipants.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(data, headers, HttpStatus.OK).getBody();
    }

}
