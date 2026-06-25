package com.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Projekt {

    @Id
    private Integer projektId;

    @NotBlank(message = "Pole nazwa nie może być puste!")
    @Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
    private String nazwa;

    @NotBlank(message = "Pole opis nie może być puste!")
    @Size(max = 500, message = "Pole opis może zawierać maksymalnie {max} znaków!")
    private String opis;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataOddania;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dataczasUtworzenia;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dataczasModyfikacji;

    public Projekt() {
    }

    public Projekt(Integer projektId, String nazwa, String opis, LocalDate dataOddania,
            LocalDateTime dataczasUtworzenia, LocalDateTime dataczasModyfikacji) {
        this.projektId = projektId;
        this.nazwa = nazwa;
        this.opis = opis;
        this.dataOddania = dataOddania;
        this.dataczasUtworzenia = dataczasUtworzenia;
        this.dataczasModyfikacji = dataczasModyfikacji;
    }

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

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDate getDataOddania() {
        return dataOddania;
    }

    public void setDataOddania(LocalDate dataOddania) {
        this.dataOddania = dataOddania;
    }

    public LocalDateTime getDataczasUtworzenia() {
        return dataczasUtworzenia;
    }

    public void setDataczasUtworzenia(LocalDateTime dataczasUtworzenia) {
        this.dataczasUtworzenia = dataczasUtworzenia;
    }

    public LocalDateTime getDataczasModyfikacji() {
        return dataczasModyfikacji;
    }

    public void setDataczasModyfikacji(LocalDateTime dataczasModyfikacji) {
        this.dataczasModyfikacji = dataczasModyfikacji;
    }
}
