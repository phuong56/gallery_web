package com.example.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Folder;
import com.example.entity.Image;
import com.example.entity.User;
import com.example.service.ImageService;
import com.example.service.UserService;
import com.example.service.FolderService;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	FolderService FolderService;
	@GetMapping
	public String home(@RequestParam(name ="folder", required=false) String Pfolder,Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user =userService.getUserByUsername(username).get();
		System.out.println("folder: " + Pfolder);
		if (Pfolder == null) {
			Pfolder = "default";
			Folder folder1 = user.getFolderByName(Pfolder);
			if (folder1 == null) {
				Folder folder = new Folder();
				folder.setName(Pfolder);
				FolderService.saveFolder(folder);
				user.getFolders().add(folder);
			}

		}
	
		Folder folder = user.getFolderByName(Pfolder);
		System.out.println("folder: " + folder);
		List<Folder> folders = user.getFolders();
		List<Image> images= folder.getImages();

		model.addAttribute("images", images);
		model.addAttribute("folders", folders);
		return "home";
	}
	@DeleteMapping("/{imageId}")
	public String home(@PathVariable("imageId") int imageId, Model model) {
		System.out.println("delete image id: " + imageId);
		Image image = imageService.getImageById((long) imageId);
		String name = image.getName();
		imageService.deleteImageById((long) imageId);
		List<Image> images= imageService.GetAllImages();
		model.addAttribute("images", images);
//		String imagePath = "/static/images/"+name;
//		String absolutePath = System.getProperty("user.dir") + "/src/main/resources" + imagePath;
//		File fileToDelete = new File(absolutePath);
//
//		if (fileToDelete.delete()) {
//		    System.out.println("Đã xóa tệp thành công!");
//		} else {
//		    System.out.println("Không thể xóa tệp.");
//		}

		return "home";
	}
	
	@DeleteMapping("/folder/{folderId}")
	public String deleteFolder(@PathVariable("folderId") int folderId, Model model) {
		System.out.println("delete folder id: " + folderId);
		Folder folder = FolderService.getFolderById((long) folderId);
		for (Image image : folder.getImages()) {
			String name = image.getName();
            if(image.getFolders().size() <= 1) {
            	System.out.println("size image have folder: " + image.getFolders().size());
    			imageService.deleteImageById(image.getId());
    			String imagePath = "/static/images/" + name;
    			String absolutePath = System.getProperty("user.dir") + "/src/main/resources" + imagePath;
    			File fileToDelete = new File(absolutePath);

    			if (fileToDelete.delete()) {
    				System.out.println("Đã xóa tệp thành công!");
    			} else {
    				System.out.println("Không thể xóa tệp.");
    			}
            }
		}
		String name = folder.getName();
		FolderService.deleteFolderById((long) folderId);
		List<Folder> folders = FolderService.getAllFolders();
		model.addAttribute("folders", folders);
		String folderPath = "/static/images/" + name;
		String absolutePath = System.getProperty("user.dir") + "/src/main/resources" + folderPath;
		File fileToDelete = new File(absolutePath);

		if (fileToDelete.delete()) {
			System.out.println("Đã xóa tệp thành công!");
		} else {
			System.out.println("Không thể xóa tệp.");
		}

		return "home";
	}
	
	@PutMapping("favorite/{imageId}")
	public String favoriteImage(@PathVariable("imageId") int imageId, Model model) {
		System.out.println("favorite image id: " + imageId);
		Image image = imageService.getImageById((long) imageId);
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user =userService.getUserByUsername(username).get();
		System.out.println("user: " + user.getUsername());
		Folder folder = user.getFolderByName("favorite");
		System.out.println("folder favorite: " + folder);
		if (folder == null) {
			folder = new Folder();
			folder.setName("favorite");
			folder.setUser(user);
			user.getFolders().add(folder);
			
		}
		folder.getImages().add(image);
		image.getFolders().add(folder);
		FolderService.saveFolder(folder);
		userService.saveUser(user);
		imageService.saveImage(image);
		List<Image> images = imageService.GetAllImages();
		model.addAttribute("images", images);
		return "home";
	}
	
	
}
