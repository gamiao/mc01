package com.ehealth.mc.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired 
	private ServletContext servletContext;

	public static final String UPLOAD_ROOT = "uploadedImg";

	private final ResourceLoader resourceLoader;

	@Autowired
	public FileUploadController(ResourceLoader resourceLoader) {		
		if(!Paths.get(UPLOAD_ROOT).toFile().exists()){
			Paths.get(UPLOAD_ROOT).toFile().mkdirs();
		}
		this.resourceLoader = resourceLoader;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/uploadedImg/")
	public String provideUploadInfo(Model model) throws IOException {

		model.addAttribute("files", Files.walk(Paths.get(UPLOAD_ROOT))
				.filter(path -> !path.equals(Paths.get(UPLOAD_ROOT)))
				.map(path -> Paths.get(UPLOAD_ROOT).relativize(path))
				.map(path -> linkTo(methodOn(FileUploadController.class).getFile(path.toString())).withRel(path.toString()))
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/uploadedImg/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filename) {

		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(UPLOAD_ROOT, filename).toString()));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadImg")
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {

		try {
			
            String fileName = FilenameUtils.getName(file.getOriginalFilename());

            String extension = FilenameUtils.getExtension(fileName);

            String base = servletContext.getRealPath("/");

            File dir = new File(base + File.separator + UPLOAD_ROOT);                

            if (!dir.exists()) {
                dir.mkdirs();
            }

//            Date date = new Date();
//            String year = DateTimeUtility.getInstance().getYear(date);
//            String month = DateTimeUtility.getInstance().getMonth(date);
//            String uniqueFileName = DateTimeUtility.getInstance().getDateTime(date);
//
//            File dateDir = new File(base + File.separator + UPLOAD_ROOT + File.separator + year + File.separator + month);
//
//            if (!dateDir.exists()) {
//                dateDir.mkdirs();
//            }
//
//            File uploadedFile = new File(dateDir.getAbsolutePath() + File.separator + uniqueFileName + WordCollections.UNDERBAR +  fileName);
//			
			
			
			
			Files.copy(file.getInputStream(),
					Paths.get(dir.getAbsolutePath(), file.getOriginalFilename()));
		} catch (IOException | RuntimeException e) {
		}

		return null;
	}

}
