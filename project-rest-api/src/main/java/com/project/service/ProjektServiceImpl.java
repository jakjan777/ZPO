package com.project.service;

import java.util.Optional; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.Projekt;
import com.project.model.Student;
import com.project.model.Zadanie;
import com.project.repository.ProjektRepository;
import com.project.repository.StudentRepository;
import com.project.repository.ZadanieRepository;
    

@Service  
public class ProjektServiceImpl implements ProjektService { 
   private final ProjektRepository projektRepository; 
   private final ZadanieRepository zadanieRepository; 
   private final StudentRepository studentRepository;
   
   @Autowired
   public ProjektServiceImpl(ProjektRepository projektRepository, ZadanieRepository zadanieRepository,
		   StudentRepository studentRepository) { 
	   this.projektRepository = projektRepository; 
	   this.zadanieRepository = zadanieRepository; 
	   this.studentRepository = studentRepository;
   } 
 
   @Override 
   public Optional<Projekt> getProjekt(Integer projektId) { 
      return projektRepository.findById(projektId); 
   } 
 
   @Override 
   public Projekt setProjekt(Projekt projekt) { 
      return projektRepository.save(projekt);    
  } 
 
   @Override 
   @Transactional 
   public void deleteProjekt(Integer projektId) { 
      for (Zadanie zadanie : zadanieRepository.findZadaniaProjektu(projektId)) { 
         zadanieRepository.delete(zadanie); 
      } 
      projektRepository.deleteById(projektId); 
   }  
 
  @Override 
  public Page<Projekt> getProjekty(Pageable pageable) { 
      return projektRepository.findAll(pageable);    
  } 
    
   @Override 
   public Page<Projekt> searchByNazwa(String nazwa, Pageable pageable) { 
     return projektRepository.findByNazwaContainingIgnoreCase(nazwa, pageable);    
   }

   @Override
   public Optional<Student> getStudent(Integer studentId) {
	   return studentRepository.findById(studentId); 
   }

   @Override
   public Student setStudent(Student student) {
	return studentRepository.save(student);
   }

   @Override
   public void deleteStudent(Integer studentId) {
	studentRepository.deleteById(studentId);
   }

   @Override
   public Page<Student> getStudenci(Pageable pageable) {
	return studentRepository.findAll(pageable);
   }

   @Override
   public Page<Student> searchByNrIndeksu(String nrIndeksu, Pageable pageable) {
	return studentRepository.findByNrIndeksuStartsWith(nrIndeksu, pageable);
   }

   @Override
   public Optional<Zadanie> getZadanie(Integer zadanieId) {
	return zadanieRepository.findById(zadanieId);
   }

   @Override
   public Zadanie setZadanie(Zadanie zadanie) {
	return zadanieRepository.save(zadanie);
   }

   @Override
   public void deleteZadanie(Integer zadanieId) {
	zadanieRepository.deleteById(zadanieId);
   }

   @Override
   public Page<Zadanie> getZadania(Pageable pageable) {
	return zadanieRepository.findAll(pageable);
   }

   @Override
   public Page<Zadanie> searchByNazwaZadania(String nazwa, Pageable pageable) {
	return zadanieRepository.findByNazwaContainingIgnoreCase(nazwa, pageable);
   } 
} 