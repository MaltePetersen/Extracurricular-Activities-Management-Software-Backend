package com.main.controller;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.ChildDTO;
import com.main.dto.SchoolDTO;
import com.main.dto.WorkingGroupDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.dto.converters.SchoolConverter;
import com.main.model.Attendance;
import com.main.model.School;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.interfaces.IUser;
import com.main.service.implementations.AfterSchoolCareService;
import com.main.service.implementations.SchoolService;
import com.main.service.implementations.UserService;
import org.aspectj.lang.annotation.After;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/sc")
public class SchoolCoordinatorController {
    private SchoolService schoolService;
    private AfterSchoolCareService afterSchoolCareService;

    private UserService userService;

    @Autowired
    public SchoolCoordinatorController(SchoolService schoolService, AfterSchoolCareService afterSchoolCareService, UserService userService) {
        this.schoolService = schoolService;
        this.afterSchoolCareService = afterSchoolCareService;
        this.userService = userService;
    }

    //Add Working Group
    @PostMapping("/ag")
    public AfterSchoolCareDTO addAWorkingGroup(@RequestBody WorkingGroupDTO workingGroupDTO) {
        if (workingGroupDTO.getType() != 2) {
            return null;
        }
        AfterSchoolCareDTO afterSchoolCareDTO = afterSchoolCareService.createNew(workingGroupDTO);
        return workingGroupDTO;
    }

    //Add Child
    @PostMapping("/ag/child")
    public ResponseEntity<String> updateChild(@PathVariable Long id,
                                              @RequestBody ChildDTO childDTO,
                                              Authentication authentication) {
        if (checkIfUserIsAuth(id, authentication) == null)
            return null;

        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        IUser user = userService.findByUsername(childDTO.getUsername());
        attendance.setChild((User) user);
        afterSchoolCare.addAttendance(attendance);
        return ResponseEntity.ok("Child was successfully added");
    }


    //Remove Working Group
    @DeleteMapping("/ag")
    public ResponseEntity<String> removeWorkingGroup(@PathVariable Long id, Authentication authentication) {
        if (checkIfUserIsAuth(id, authentication) == null)
            return null;

        afterSchoolCareService.deleteById(id);
        return ResponseEntity.ok("");
    }

    //Get All Working Groups
    @GetMapping("/ags")
    public List<AfterSchoolCareDTO> getMyWorkingGroup(Authentication authentication) {
        String username = authentication.getName();
        return afterSchoolCareService.findAllByTypeWorkingGroup()
                .stream()
                .filter(each -> each.getOwner().getUsername().equals(username))
                .collect(Collectors.toList());
    }


    //Get Working Group by Id
    @GetMapping("/ag")
    public AfterSchoolCareDTO getWorkingGroupById(@RequestParam("id") Long id, Authentication authentication) {
        if (id == null)
            return null;
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);
        if (afterSchoolCare == null)
            return null;
        Long userId = userService.findByUsername(authentication.getName()).getId();
        if (!afterSchoolCare.getOwner().getId().equals(userId))
            return null;
        return AfterSchoolCareConverter.toDto(afterSchoolCare);
    }


    @PatchMapping("/ag")
    public ResponseEntity<String> updateAg(@PathVariable Long id,
                                           @RequestBody AfterSchoolCareDTO afterSchoolCareDTO,
                                           Authentication authentication) {
        if (checkIfUserIsAuth(id, authentication) == null)
            return ResponseEntity.badRequest().build();
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);
        afterSchoolCareService.update(afterSchoolCare, afterSchoolCareDTO);
        return ResponseEntity.ok().build();
    }

    protected ResponseEntity<Object> checkIfUserIsAuth(@PathVariable Long id, Authentication authentication) {
        if (id == null)
            return ResponseEntity.badRequest().build();
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);
        if (!afterSchoolCare.getType().equals(AfterSchoolCare.Type.WORKING_GROUP))
            return ResponseEntity.badRequest().build();
        if (!afterSchoolCare.getOwner().getUsername().equals(authentication.getName()))
            return ResponseEntity.badRequest().build();
        return null;
    }

    //Anwesendheit verwalten??


    @GetMapping("/schools")
    public List<SchoolDTO> getSchools() {
        return schoolService.getAll().stream().map(SchoolConverter::toDto).collect(Collectors.toList());
    }

    @PostMapping("/school")
    @ResponseStatus(HttpStatus.CREATED)
    SchoolDTO createSchool(@RequestBody School newSchool) {
        return SchoolConverter.toDto((School) schoolService.save(newSchool));
    }

    @GetMapping("/school/{id}")
    SchoolDTO getSchool(@PathVariable @Min(1) Long id) {
        return SchoolConverter.toDto(schoolService.findOne(id));
    }

    @PatchMapping("/school/{id}")
    SchoolDTO changeSchool(@RequestBody SchoolDTO newSchool, @PathVariable Long id) {
        School school = schoolService.findOne(id);

        school.setName(newSchool.getName());
        school.setAddress(newSchool.getAddress());
        school.setEmail(newSchool.getEmail());
        school.setPhoneNumber(newSchool.getPhoneNumber());

        schoolService.save(school);

        return SchoolConverter.toDto(school);
    }

    @DeleteMapping("/school/{id}")
    void deleteSchool(@PathVariable Long id) {
        schoolService.deleteById(id);
    }
}
