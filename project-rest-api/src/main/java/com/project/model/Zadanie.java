package com.project.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

	@NotBlank(message = "Pole opis nie może być puste!")
	@Size(max = 500, message = "Pole opis może zawierać maksymalnie {max} znaków!")
	@Column(length = 500)
	private String opis;

	@NotNull(message = "Pole kolejność nie może być puste!")
	@Min(value = 1, message = "Kolejność musi być większa lub równa {value}!")
	@Column(nullable = false)
	private Integer kolejnosc;

	@CreatedDate
	@Column(name = "dataczas_dodania", nullable = false, updatable = false)
	private LocalDateTime dataczasDodania;

	@NotNull(message = "Projekt musi być wybrany!")
	@ManyToOne
	@JoinColumn(name = "projekt_id", nullable = false)
	@JsonIgnoreProperties({ "zadania" })
	private Projekt projekt;

	public Integer getZadanieId() {
		return zadanieId;
	}

	public void setZadanieId(Integer zadanieId) {
		this.zadanieId = zadanieId;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public Integer getKolejnosc() {
		return kolejnosc;
	}

	public void setKolejnosc(Integer kolejnosc) {
		this.kolejnosc = kolejnosc;
	}

	public LocalDateTime getDataczasDodania() {
		return dataczasDodania;
	}

	public void setDataczasDodania(LocalDateTime dataczasDodania) {
		this.dataczasDodania = dataczasDodania;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}
} 
