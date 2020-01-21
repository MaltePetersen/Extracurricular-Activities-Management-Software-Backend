package com.main.controller;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.InvoiceDTO;
import com.main.dto.SchoolDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.dto.converters.SchoolConverter;
import com.main.dto.converters.StringToLocalDatetimeConverter;
import com.main.model.Attendance;
import com.main.model.School;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.interfaces.ISchool;
import com.main.service.AfterSchoolCareService;
import com.main.service.AttendanceService;
import com.main.service.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("api/employee")
public class EmployeeController {
    SchoolService schoolService;
    AfterSchoolCareService afterSchoolCareService;
    AttendanceService attendanceService;
    List<InvoiceDTO> invoiceDTOs;

    EmployeeController(SchoolService schoolService, AfterSchoolCareService afterSchoolCareService, AttendanceService attendanceService) {
        this.schoolService = schoolService;
        this.afterSchoolCareService = afterSchoolCareService;
        this.attendanceService = attendanceService;
        generateInvoices();
    }

    private void generateInvoices() {
        invoiceDTOs = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            InvoiceDTO invoiceDTO = new InvoiceDTO();
            invoiceDTO.setId(new Long(i));
            invoiceDTO.setData("Abbrechnung Nr " + i);
            invoiceDTO.setTime(LocalDateTime.of(i, i, i, i, i));
            invoiceDTOs.add(invoiceDTO);
        }
    }

    @GetMapping("/schools")
    public List<SchoolDTO> getSchools() {
        return schoolService.getAll().stream().map(SchoolConverter::toDto).collect(Collectors.toList());
    }

    @PostMapping("/school")
    @ResponseStatus(HttpStatus.CREATED)
    ISchool createSchool(@RequestBody School newSchool) {
        return schoolService.save(newSchool);
    }

    @GetMapping("/school/{id}")
    SchoolDTO getSchool(@PathVariable @Min(1) Long id) {
        return SchoolConverter.toDto(schoolService.findOne(id));
    }

    @PatchMapping("/school/{id}")
    SchoolDTO changeSchool(@RequestBody School newSchool, @PathVariable Long id) {
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

    @GetMapping("/after_school_cares")
    public List<AfterSchoolCareDTO> getAfterSchoolCares() {
        return afterSchoolCareService.getAll().stream().map(AfterSchoolCareConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/after_school_cares")
    @ResponseStatus(HttpStatus.CREATED)
    AfterSchoolCareDTO createAfterSchoolCare(@RequestBody AfterSchoolCare newAfterSchoolCare) {
        return AfterSchoolCareConverter.toDto(afterSchoolCareService.save(newAfterSchoolCare));
    }

    @GetMapping("/after_school_cares/{id}")
    AfterSchoolCareDTO getAfterSchoolCare(@PathVariable @Min(1) Long id) {
        return AfterSchoolCareConverter.toDto(afterSchoolCareService.findOne(id));
    }

    @DeleteMapping("/after_school_cares/{id}")
    void deleteAfterSchoolCare(@PathVariable Long id) {
        afterSchoolCareService.deleteById(id);
    }

    /**
     * Aktualisiert ArrivalTime und / oder LeaveTime einer Attendance, kann auch auf null gesetzt werden
     * @param update Map mit Keys und Values
     * @param id Id der Attendance
     * @return Gibt die verknüpfte AfterSchoolCare als DTO zurück
     */
    @PatchMapping("/attendance/{id}")
    AfterSchoolCareDTO updateAttendance(@RequestBody Map<String, String> update, @PathVariable Long id) {
        Attendance attendance = attendanceService.findOne(id);

        String arrivalTimeString = update.get("arrivalTime");
        if (arrivalTimeString != null && !arrivalTimeString.isEmpty()) {
            LocalDateTime arrivalTime = (new StringToLocalDatetimeConverter()).convert(arrivalTimeString);
            if (arrivalTime != null) {
                attendance.setArrivalTime(arrivalTime);
            }
        } else {
            if (update.containsKey("arrivalTime") && arrivalTimeString == null) {
                attendance.setArrivalTime(null);
            }
        }

        String leaveTimeString = update.get("leaveTime");
        if (leaveTimeString != null && !leaveTimeString.isEmpty()) {
            LocalDateTime leaveTime = (new StringToLocalDatetimeConverter()).convert(leaveTimeString);
            if (leaveTime != null) {
                attendance.setLeaveTime(leaveTime);
            }
        } else {
            if(update.containsKey("leaveTime") && arrivalTimeString == null) {
                attendance.setLeaveTime(null);
            }
        }

        return AfterSchoolCareConverter.toDto(attendanceService.save(attendance).getAfterSchoolCare());
    }

    @GetMapping("/invoices")
    public List<InvoiceDTO> getInvoices() {
        return invoiceDTOs;
    }

    @PostMapping("/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    void createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        invoiceDTOs.add(invoiceDTO);
    }

    @GetMapping("/invoice/{id}")
    InvoiceDTO getInvoices(@PathVariable @Min(1) int id) {
        return invoiceDTOs.get(id - 1);
    }

    @PatchMapping("/invoice/{id}")
    void changeInvoice(@RequestBody InvoiceDTO invoiceDTO, @PathVariable int id) {
        invoiceDTOs.set(id - 1, invoiceDTO);
    }

    @DeleteMapping("/invoice/{id}")
    void deleteInvoice(@PathVariable int id) {
        invoiceDTOs.remove(id - 1);
    }

}
