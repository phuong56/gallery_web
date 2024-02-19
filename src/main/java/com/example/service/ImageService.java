package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Image;
import com.example.repository.ImageRepository;

@Service
public class ImageService {
	@Autowired
	ImageRepository imageRepository;
	
	public void saveImage(Image image) {
		imageRepository.save(image);
	}
	
	public List<Image> GetAllImages() {
		return imageRepository.getAllImages();
	}
	
	public void deleteImageById(Long id) {
		imageRepository.deleteImageById(id);
	}
	
	public Image getImageById(Long id) {
		return imageRepository.getImageById(id);
	}
}
