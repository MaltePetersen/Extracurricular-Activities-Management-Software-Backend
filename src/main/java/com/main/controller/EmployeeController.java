package com.main.controller;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.InvoiceDTO;
import com.main.dto.SchoolDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.dto.converters.SchoolConverter;
import com.main.dto.converters.StringToLocalDatetimeConverter;
import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.service.implementations.AfterSchoolCareService;
import com.main.service.implementations.AttendanceService;
import com.main.service.implementations.SchoolService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
            invoiceDTO.setId((long) i);
            invoiceDTO.setData("Abbrechnung Nr " + i);
            invoiceDTO.setTime(LocalDateTime.of(i, i, i, i, i));
            invoiceDTOs.add(invoiceDTO);
        }
    }

    @GetMapping("/schools")
    public List<SchoolDTO> getSchools() {
        return schoolService.getAll().stream().map(SchoolConverter::toDto).collect(Collectors.toList());
    }

    @GetMapping("/school/{id}")
    public SchoolDTO getSchool(@PathVariable @Min(1) Long id) {
        return SchoolConverter.toDto(schoolService.findOne(id));
    }

    /**
     * Gibt alle AfterSchoolCares zurück, bei denen der Employee Owner ist
     *
     * @param schoolId Die Id der Schule, auf die gefiltert werden soll (optional)
     * @return Liste an AfterSchoolCareDTOs
     */
    @GetMapping("/after_school_cares")
    public List<AfterSchoolCareDTO> getAfterSchoolCares(
            Authentication auth,
            @RequestParam(required = false, name = "school") Long schoolId,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) boolean showOnlyOwn) {
        return afterSchoolCareService.getAll().stream()
                .filter(afterSchoolCare -> !showOnlyOwn || afterSchoolCare.getOwner() != null && afterSchoolCare.getOwner().getUsername().equals(auth.getName()))
                .filter(afterSchoolCare -> schoolId == null || afterSchoolCare.getParticipatingSchool() != null && afterSchoolCare.getParticipatingSchool().getId().equals(schoolId))
                .filter(afterSchoolCare -> type == null || afterSchoolCare.getType().getId() == type)
                .filter(afterSchoolCare -> startDate == null || afterSchoolCare.getEndTime() != null && afterSchoolCare.getEndTime().isAfter(startDate))
                .filter(afterSchoolCare -> endDate == null || afterSchoolCare.getStartTime() != null && afterSchoolCare.getStartTime().isBefore(endDate))
                .map(AfterSchoolCareConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/after_school_cares")
    @ResponseStatus(HttpStatus.CREATED)
    public AfterSchoolCareDTO createAfterSchoolCare(@RequestBody AfterSchoolCare newAfterSchoolCare) {
        return AfterSchoolCareConverter.toDto(afterSchoolCareService.save(newAfterSchoolCare));
    }

    @GetMapping("/after_school_cares/{id}")
    public AfterSchoolCareDTO getAfterSchoolCare(@PathVariable @Min(1) Long id) {
        return AfterSchoolCareConverter.toDto(afterSchoolCareService.findOne(id));
    }

    @DeleteMapping("/after_school_cares/{id}")
    public void deleteAfterSchoolCare(@PathVariable Long id) {
        afterSchoolCareService.deleteById(id);
    }

    @PatchMapping("/after_school_cares/{id}/close")
    @Transactional
    public ResponseEntity closeAfterSchoolCare(@PathVariable @Min(1) Long id) {
        AfterSchoolCare afterSchoolCare = afterSchoolCareService.findOne(id);

        if (afterSchoolCare.getAttendances().stream().allMatch(attendance -> attendance.determineStatus() == 4)) {
            afterSchoolCare.setClosed(true);
            afterSchoolCareService.save(afterSchoolCare);
            for (Attendance attendance : afterSchoolCare.getAttendances()) {
                attendance.setClosed(true);
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Closing an AfterSchoolCare is only allowed when all children have left.");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AfterSchoolCareConverter.toDto(afterSchoolCare));
    }

    /**
     * Aktualisiert ArrivalTime und / oder LeaveTime einer Attendance, kann auch auf null gesetzt werden
     *
     * @param update Map mit Keys und Values
     * @param id     Id der Attendance
     * @return Gibt die verknüpfte AfterSchoolCare als DTO oder eine Fehlermeldung zurück
     */
    @PatchMapping("/attendance/{id}")
    public ResponseEntity updateAttendance(@RequestBody Map<String, String> update, @PathVariable Long id) {
        Attendance attendance = attendanceService.findOne(id);

        if (!attendance.getAfterSchoolCare().isClosed()) {
            String arrivalTimeString = update.get("arrivalTime");
            if (arrivalTimeString != null && !arrivalTimeString.isEmpty()) {
                LocalDateTime arrivalTime = (new StringToLocalDatetimeConverter()).convert(arrivalTimeString);
                if (arrivalTime != null) {
                    attendance.setArrivalTime(arrivalTime);
                }
            } else {
                if (update.containsKey("arrivalTime") && arrivalTimeString == null) {
                    // arrivalTime darf nur null gesetzt werden, wenn keine leaveTime gesetzt ist
                    if (attendance.getLeaveTime() == null) {
                        attendance.setArrivalTime(null);
                    } else {
                        return ResponseEntity
                                .badRequest()
                                .body("Error: Setting arrival time to null is only allowed if no leave time is set.");
                    }
                }
            }

            String leaveTimeString = update.get("leaveTime");
            if (leaveTimeString != null && !leaveTimeString.isEmpty()) {
                LocalDateTime leaveTime = (new StringToLocalDatetimeConverter()).convert(leaveTimeString);
                if (leaveTime != null) {
                    // leaveTime darf nur gesetzt werden, wenn eine arrivalTime gesetzt ist
                    if (attendance.getArrivalTime() != null) {
                        attendance.setLeaveTime(leaveTime);
                    } else {
                        return ResponseEntity
                                .badRequest()
                                .body("Error: Setting a leave time is only allowed if an arrival time is already set.");
                    }
                }
            } else {
                if (update.containsKey("leaveTime") && leaveTimeString == null) {
                    attendance.setLeaveTime(null);
                }
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Modifying a closed AfterSchoolCare is not allowed.");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AfterSchoolCareConverter.toDto(attendanceService.save(attendance).getAfterSchoolCare()));
    }

    @GetMapping("/invoices")
    public List<InvoiceDTO> getInvoices() {
        return invoiceDTOs;
    }

    @PostMapping("/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public void createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        invoiceDTOs.add(invoiceDTO);
    }

    @GetMapping("/invoice/{id}")
    public InvoiceDTO getInvoices(@PathVariable @Min(1) int id) {
        return invoiceDTOs.get(id - 1);
    }

    @PatchMapping("/invoice/{id}")
    public void changeInvoice(@RequestBody InvoiceDTO invoiceDTO, @PathVariable int id) {
        invoiceDTOs.set(id - 1, invoiceDTO);
    }

    @DeleteMapping("/invoice/{id}")
    public void deleteInvoice(@PathVariable int id) {
        invoiceDTOs.remove(id - 1);
    }

}
