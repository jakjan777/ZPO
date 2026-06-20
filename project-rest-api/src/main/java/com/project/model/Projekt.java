package com.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projekt")
@EntityListeners(AuditingEntityListener.class)
public class Projekt {
	@Id
	@GeneratedValue
	@Column(name = "projekt_id")
	private Integer projektId;

	@NotBlank(message = "Pole nazwa nie może być puste!")
	@Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
	@Column(nullable = false, length = 50)
	private String nazwa;

	@Column(length = 500)
	private String opis;

	@Column(name = "data_oddania")
	private LocalDate dataOddania;

	@CreatedDate
	@Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
	private LocalDateTime dataczasUtworzenia;

	@LastModifiedDate
	@Column(name = "dataczas_modyfikacji", insertable = false)
	private LocalDateTime dataczasModyfikacji;

	@OneToMany(mappedBy = "projekt")
	@JsonIgnoreProperties({ "projekt" })
	private List<Zadanie> zadania;

	@ManyToMany
	@JoinTable(name = "projekt_student",
			joinColumns = { @JoinColumn(name = "projekt_id") },
			inverseJoinColumns = { @JoinColumn(name = "student_id") })
	private Set<Student> studenci;

	public Integer getProjektId() {
		return projektId;
	}

	public void setProjektId(Integer projektId) {
		this.projektId = projektId;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public void setDataOddania(LocalDate dataOddania) {
		this.dataOddania = dataOddania;
	}
} 