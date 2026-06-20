package com.project.service;

import java.util.Optional; 
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import com.project.model.Projekt;
import com.project.model.Student;
import com.project.model.Zadanie; 


public interface ProjektService { 
   
	//projekt
    Optional<Projekt> getProjekt(Integer projektId); 
    Projekt setProjekt(Projekt projekt); 
    void deleteProjekt(Integer projektId); 
    Page<Projekt> getProjekty(Pageable pageable); 
    Page<Projekt> searchByNazwa(String nazwa, Pageable pageable);
   
    //student
    Optional<Student> getStudent(Integer studentId); 
    Student setStudent(Student student); 
    void deleteStudent(Integer studentId); 
    Page<Student> getStudenci(Pageable pageable); 
    Page<Student> searchByNrIndeksu(String nrIndeksu, Pageable pageable);
    
    //zadanie
    Optional<Zadanie> getZadanie(Integer zadanieId); 
    Zadanie setZadanie(Zadanie zadanie); 
    void deleteZadanie(Integer zadanieId); 
    Page<Zadanie> getZadania(Pageable pageable); 
    Page<Zadanie> searchByNazwaZadania(String nazwa, Pageable pageable);
   
} 