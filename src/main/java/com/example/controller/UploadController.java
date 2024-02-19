package com.example.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Folder;
import com.example.entity.Image;
import com.example.entity.User;
import com.example.service.FolderService;
import com.example.service.ImageService;
import com.example.service.UserService;

@Controller
@RequestMapping("/upload")
public class UploadController {
	private final String uploadLocation = "D:" + File.separator + "Gallery_Web" + File.separator + "gallery_web" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images";
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private FolderService FolderService;

    @GetMapping
    public String showUploadForm() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	System.out.println("name: " + auth.getName());
        return "upload"; // Return the name of the Thymeleaf template or JSP file
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("nameFolder") String nameFolder ,RedirectAttributes redirectAttributes) {
        try {
        	
        	String username = SecurityContextHolder.getContext().getAuthentication().getName();
        	User user = userService.getUserByUsername(username).get();
        	Image image = new Image();
        	image.setName(file.getOriginalFilename());
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadLocation, fileName);
            System.out.println("name folder: " + nameFolder);
            List<Folder> folders = user.getFolders();
            Folder folder = null;
			for (Folder f : folders) {
				if (f.getName().equals(nameFolder)) {
					folder = f;
					break;
				}
			}
            System.out.println("folder: " + folder);
			if (folder == null) {
				folder = new Folder();
				folder.setName(nameFolder);
				FolderService.saveFolder(folder);
				user.getFolders().add(folder);
				folder.setUser(user);
			}
			image.getFolders().add(folder);
			folder.getImages().add(image);
			userService.saveUser(user);
			imageService.saveImage(image);
			FolderService.saveFolder(folder);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File uploaded successfully!");
            redirectAttributes.addFlashAttribute("successMessage", "Image uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image. Please try again.");
        }
        return "redirect"; // Redirect to the home page or wherever you want to go after upload
    }
}
