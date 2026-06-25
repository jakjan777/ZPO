package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    @Id
    private Integer studentId;

    @NotBlank(message = "Pole imię nie może być puste!")
    @Size(max = 50, message = "Imię może zawierać do {max} znaków!")
    private String imie;

    @NotBlank(message = "Pole nazwisko nie może być puste!")
    @Size(max = 50, message = "Nazwisko może zawierać do {max} znaków!")
    private String nazwisko;

    @NotBlank(message = "Pole nr indeksu nie może być puste!")
    @Size(max = 20, message = "Nr indeksu może zawierać do {max} znaków!")
    private String nrIndeksu;

    @NotBlank(message = "Pole email nie może być puste!")
    @Email(message = "Adres email jest niepoprawny!")
    @Size(max = 50, message = "Email może zawierać do {max} znaków!")
    private String email;

    private boolean stacjonarny;

    public Student() {
    }

    public Student(Integer studentId, String imie, String nazwisko, String nrIndeksu, String email,
            boolean stacjonarny) {
        this.studentId = studentId;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrIndeksu = nrIndeksu;
        this.email = email;
        this.stacjonarny = stacjonarny;
    }

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
}
