package com.project.repository; 

import java.util.Optional; 
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.project.model.Student; 

public interface StudentRepository extends JpaRepository<Student, Integer> { 
   Optional<Student> findByNrIndeksu(String nrIndeksu); 
   Page<Student> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable); 
   Page<Student> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable); 
   @Modifying
   @Query(value = "DELETE FROM projekt_student WHERE student_id = :studentId", nativeQuery = true)
   void deleteProjektStudentByStudentId(@Param("studentId") Integer studentId);
  // Metoda findByNrIndeksuStartsWith definiuje zapytanie  
  //    SELECT s FROM Student s WHERE s.nrIndeksu LIKE :nrIndeksu% 
  // Metoda findByNazwiskoStartsWithIgnoreCase definiuje zapytanie  
  //    SELECT s FROM Student s WHERE upper(s.nazwisko) LIKE upper(:nazwisko%) 
} 