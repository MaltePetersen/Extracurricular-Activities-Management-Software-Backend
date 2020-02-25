package com.main.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InvoiceDTO {

	private Long id;

    private LocalDateTime time; 
    
    private String data;
}
