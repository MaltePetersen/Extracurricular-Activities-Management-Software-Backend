package com.main.controller;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.afterSchoolCare.WorkingGroup;
import com.main.model.interfaces.IUser;
import com.main.service.AfterSchoolCareService;
import com.main.service.UserService;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/sc")
public class SchoolCoordinatorController {


    private AfterSchoolCareService afterSchoolCareService;

    private UserService userService;

    @Autowired
    public SchoolCoordinatorController(AfterSchoolCareService afterSchoolCareService,
                                       UserService userService) {
        this.afterSchoolCareService = afterSchoolCareService;
        this.userService = userService;
    }

    //Add Working Group
    @PostMapping("/ag")
    public AfterSchoolCareDTO addAWorkingGroup(@RequestBody AfterSchoolCareDTO afterSchoolCareDTO) {
        if (afterSchoolCareDTO.getType() != 2) {
            return null;
        }
        afterSchoolCareDTO = afterSchoolCareService.createNew(afterSchoolCareDTO);
        return afterSchoolCareDTO;
    }


    //Remove Working Group
    @GetMapping("/ag")
    public ResponseEntity<String> removeWorkingGroup(@PathVariable Long id, Authentication authentication) {
        if (id == null)
            return ResponseEntity.badRequest().build();
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);
        if (!afterSchoolCare.getType().equals(AfterSchoolCare.Type.WORKING_GROUP))
            return ResponseEntity.badRequest().build();
        if (!afterSchoolCare.getOwner().getUsername().equals(authentication.getName()))
            return ResponseEntity.badRequest().build();
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


    //Update


    //Anwesendheit verwalten??


}
