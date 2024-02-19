package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Image;



@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	@Query("SELECT i FROM Image i")
	List<Image> getAllImages();

	@Modifying
	@Transactional
	@Query("DELETE FROM Image WHERE id = ?1")
	void deleteImageById(Long id);
	
	@Query("SELECT i FROM Image i WHERE i.id = ?1")
	Image getImageById(Long id);
}
