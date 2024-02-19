package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Folder;
import com.example.repository.FolderRepository;

@Service
public class FolderService {
	@Autowired
	FolderRepository folderRepository;
	
	public void saveFolder(Folder folder) {
        folderRepository.save(folder);
    }

	public Folder getFolderByName(String name) {
		return folderRepository.getFolderByName(name);
	}
	
	public List<Folder> getAllFolders() {
		return folderRepository.findAll();
	}
	
	public void deleteFolderById(Long id) {
		folderRepository.deleteFolderById(id);
	}
	
	public Folder getFolderById(Long id) {
		return folderRepository.findById(id).get();
	}
}
