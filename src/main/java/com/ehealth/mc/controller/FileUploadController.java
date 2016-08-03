package com.ehealth.mc.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ehealth.mc.service.OverallService;
import com.ehealth.mc.service.util.FormatUtil;

@Controller
public class FileUploadController {

	private static final Logger log = LoggerFactory
			.getLogger(FileUploadController.class);

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private OverallService overallService;

	public static final String UPLOAD_ROOT = "uploadedImg";

	private final ResourceLoader resourceLoader;

	@Autowired
	public FileUploadController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	private File getUploadRootDir() {
		String base = servletContext.getRealPath("/");
		File dir = new File(base + File.separator + UPLOAD_ROOT);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadImg/{entityType}/{entityID}/{method}")
	public String handleFileUpload(@PathVariable String entityType,
			@PathVariable String entityID, @PathVariable String method,
			@RequestParam("file") MultipartFile file) {
		try {
			File uploadDir = getUploadRootDir();

			String fileName = FilenameUtils.getName(file.getOriginalFilename());
			String fileExtension = FilenameUtils.getExtension(fileName);
			String fileNameNoExtension = FilenameUtils
					.removeExtension(fileName);

			File targetFile = new File(uploadDir.getAbsolutePath()
					+ File.separator + fileName);

			if (targetFile.exists()) {
				fileName = fileNameNoExtension + FormatUtil.getFileSuffix()
						+ FilenameUtils.EXTENSION_SEPARATOR_STR + fileExtension;
				targetFile = new File(uploadDir.getAbsolutePath()
						+ File.separator + fileName);
			}

			long fileSize = Files.copy(file.getInputStream(),
					Paths.get(targetFile.getAbsolutePath()));

			if (fileSize > 0) {
				overallService.updateEntityAfterFileUploaded(entityType,
						entityID, method, fileName);
			}

		} catch (IOException | RuntimeException e) {
		}
		return null;
	}

}
