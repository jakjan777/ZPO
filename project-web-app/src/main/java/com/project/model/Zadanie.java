package com.project.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Zadanie {

    @Id
    private Integer zadanieId;

    @NotBlank(message = "Pole nazwa nie może być puste!")
    @Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
    private String nazwa;

    @NotNull(message = "Pole kolejność nie może być puste!")
    @Min(value = 1, message = "Kolejność musi być większa lub równa {value}!")
    private Integer kolejnosc;

    @NotBlank(message = "Pole opis nie może być puste!")
    @Size(max = 500, message = "Pole opis może zawierać maksymalnie {max} znaków!")
    private String opis;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dataczasDodania;

    @NotNull(message = "Projekt musi być wybrany!")
    @JsonIgnoreProperties({ "projekt" })
    private Projekt projekt;

    public Zadanie() {
    }

    public Zadanie(Integer zadanieId, String nazwa, Integer kolejnosc, String opis,
            LocalDateTime dataczasDodania, Projekt projekt) {
        this.zadanieId = zadanieId;
        this.nazwa = nazwa;
        this.kolejnosc = kolejnosc;
        this.opis = opis;
        this.dataczasDodania = dataczasDodania;
        this.projekt = projekt;
    }

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

    public Integer getKolejnosc() {
        return kolejnosc;
    }

    public void setKolejnosc(Integer kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
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
