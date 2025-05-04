package com.example.backend.repository;

import com.example.backend.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    Optional<Size> findBySizeName(String sizeName); // <-- thêm dòng này
}
