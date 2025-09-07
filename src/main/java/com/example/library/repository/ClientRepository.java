package com.example.library.repository;

import com.example.library.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(c.birthDate AS string) LIKE CONCAT('%', :query, '%')")
    Page<Client> searchClients(@Param("query") String query, Pageable pageable);

    Page<Client> findByFullName(String fullName, Pageable pageable);
    Page<Client> findByBirthDate(LocalDate birthDate, Pageable pageable);


}