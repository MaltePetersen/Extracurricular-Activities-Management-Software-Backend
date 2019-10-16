package com.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
public class Activeness {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private String name;
	
	private String date;
	
}
