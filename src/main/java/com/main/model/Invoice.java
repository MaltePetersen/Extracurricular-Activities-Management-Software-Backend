package com.main.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
class Invoice{
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private LocalDateTime time; 
    
    private String data;
}