package com.project.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity   //Indeksujemy kolumny, które są najczęściej wykorzystywane do wyszukiwania studentów 
@Table(name = "student",  
indexes = { @Index(name = "idx_nazwisko", columnList = "nazwisko", unique = false), 
@Index(name = "idx_nr_indeksu", columnList = "nr_indeksu", unique = true) }) 

@Getter 
@Setter 
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class Student { 
	@Id
	@GeneratedValue
	@Column(name = "student_id")
	private Integer studentId;

	@NotBlank(message = "Pole imię nie może być puste!")
	@Size(max = 50, message = "Imię może zawierać do {max} znaków!")
	@Column(nullable = false, length = 50)
	private String imie;

	@NotBlank(message = "Pole nazwisko nie może być puste!")
	@Size(max = 50, message = "Nazwisko może zawierać do {max} znaków!")
	@Column(nullable = false, length = 50)
	private String nazwisko;

	@NotBlank(message = "Pole nr indeksu nie może być puste!")
	@Size(max = 20, message = "Nr indeksu może zawierać do {max} znaków!")
	@Column(name = "nr_indeksu", nullable = false, unique = true, length = 20)
	private String nrIndeksu;

	@NotBlank(message = "Pole email nie może być puste!")
	@Email(message = "Adres email jest niepoprawny!")
	@Size(max = 50, message = "Email może zawierać do {max} znaków!")
	@Column(length = 50)
	private String email;

	@Column
	private boolean stacjonarny;

	@ManyToMany(mappedBy = "studenci") 
	@JsonIgnoreProperties({ "studenci" })
	private Set<Projekt> projekty; 

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getImie() {
		return imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public String getNrIndeksu() {
		return nrIndeksu;
	}

	public void setNrIndeksu(String nrIndeksu) {
		this.nrIndeksu = nrIndeksu;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isStacjonarny() {
		return stacjonarny;
	}

	public void setStacjonarny(boolean stacjonarny) {
		this.stacjonarny = stacjonarny;
	}

	public Set<Projekt> getProjekty() {
		return projekty;
	}

	public void setProjekty(Set<Projekt> projekty) {
		this.projekty = projekty;
	}
} 