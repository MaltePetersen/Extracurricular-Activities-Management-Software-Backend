package com.main.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;

import com.main.dto.InvoiceDTO;
import com.main.dto.SchoolDTO;
import com.main.dto.converters.SchoolConverter;
import com.main.model.AfterSchoolCare;
import com.main.model.School;
import com.main.model.interfaces.ISchool;
import com.main.service.SchoolService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("api/employee")
public class EmployeeController {
    SchoolService schoolService;
    List<InvoiceDTO> invoiceDTOs;

    EmployeeController(SchoolService schoolService) {
        this.schoolService = schoolService;
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

    @GetMapping("/afterschoolcares")
    public String getAfterSchoolCares() {
        return "Not yet implemented";
    }

    @PostMapping("/afterschoolcare")
    @ResponseStatus(HttpStatus.CREATED)
    String createAfterSchoolCare(@RequestBody AfterSchoolCare AfterSchoolCare) {
        return "Not yet implemented";
    }

    @GetMapping("/afterschoolcare/{id}")
    String getAfterSchoolCare(@PathVariable @Min(1) Long id) {
        return "Not yet implemented";
    }

    @PatchMapping("/afterschoolcare/{id}")
    String changeAfterSchoolCare(@RequestBody AfterSchoolCare AfterSchoolCare, @PathVariable Long id) {
        return "Not yet implemented";

    }

    @DeleteMapping("/afterschoolcare/{id}")
    String deleteAfterSchoolCare(@PathVariable Long id) {
        return "Not yet implemented";
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
