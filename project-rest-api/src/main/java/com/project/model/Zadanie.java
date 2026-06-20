package com.project.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zadanie")
@EntityListeners(AuditingEntityListener.class)
public class Zadanie {

	@Id
	@GeneratedValue
	@Column(name = "zadanie_id")
	private Integer zadanieId;

	@NotBlank(message = "Pole nazwa nie może być puste!")
	@Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
	@Column(nullable = false, length = 50)
	private String nazwa;

	@Column(length = 500)
	private String opis;

	@Column(nullable = false)
	private Integer kolejnosc;

	@CreatedDate
	@Column(name = "dataczas_dodania", nullable = false, updatable = false)
	private LocalDateTime dataczasDodania;

	@ManyToOne
	@JoinColumn(name = "projekt_id")
	@JsonIgnoreProperties({ "zadania" })
	private Projekt projekt;

	public Integer getZadanieId() {
		return zadanieId;
	}

	public void setZadanieId(Integer zadanieId) {
		this.zadanieId = zadanieId;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
} 
