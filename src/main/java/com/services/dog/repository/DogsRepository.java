package com.services.dog.repository;

import com.services.dog.entity.Dogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DogsRepository extends JpaRepository<Dogs, Long> {
    @Query("SELECT d FROM Dogs d WHERE (COALESCE(?1, '') = '' OR d.breed LIKE %?1%)")
    Page<Dogs> findByQuery(String q, Pageable pageable);

    @Query("SELECT d FROM Dogs d WHERE d.breed = ?1")
    Optional<Dogs> findByBreed(String breed);
}