package com.main.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
class Invoice{
    
	private Long id;

    private LocalDateTime time; 
    
    private String data;
}