package com.example.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
	@Query("SELECT f FROM Folder f WHERE f.name = ?1")
	Folder getFolderByName(String name);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Folder f WHERE f.id = ?1")
	void deleteFolderById(Long id);
}
