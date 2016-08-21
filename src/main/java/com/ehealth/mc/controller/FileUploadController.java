package com.ehealth.mc.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

	private File getUploadRootDir() {
		String base = servletContext.getRealPath("/");
		File dir = new File(base + File.separator + UPLOAD_ROOT);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadImgBase64/{entityType}/{entityID}/{method}")
	@ResponseBody
	public String handleFileUploadBase64(@PathVariable String entityType,
			@PathVariable String entityID, @PathVariable String method,
			@RequestParam("fileData") String fileData,
			@RequestParam("fileName") String originalFileName) {

		String returnMsg = "{\"result\":\"E\"}";

		try {
			log.debug("--Upload image Base64 servlet begin.");
			File uploadDir = getUploadRootDir();

			if (fileData != null && originalFileName != null) {

				String[] strParts = fileData.split("base64,");
				String base64Content = null;
				if (strParts.length == 2) {
					base64Content = strParts[1];

					byte[] fileBytes = Base64.getDecoder()
							.decode(base64Content);
					String fileExtension = FilenameUtils
							.getExtension(originalFileName);
					String fileNameNoExtension = FilenameUtils
							.removeExtension(originalFileName);

					String fileName = fileNameNoExtension
							+ FormatUtil.getFileSuffix()
							+ FilenameUtils.EXTENSION_SEPARATOR_STR
							+ fileExtension;

					File targetFile = new File(uploadDir.getAbsolutePath()
							+ File.separator + fileName);

					BufferedOutputStream fileStream = new BufferedOutputStream(
							new FileOutputStream(targetFile));
					fileStream.write(fileBytes);
					fileStream.close();

					long fileSize = targetFile.length();

					if (fileSize > 0) {
						log.info("Uploaded file '"
								+ targetFile.getAbsolutePath()
								+ "' is created.");
						overallService.updateEntityAfterFileUploaded(
								entityType, entityID, method, fileName);
					}

					return "{\"result\":\"S\", \"fileName\": \"" + fileName
							+ "\"}";
				}
			}
			log.debug("==Upload image Base64 servlet end.");
		} catch (IOException | RuntimeException e) {
			return returnMsg;
		}
		return returnMsg;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadImg/{entityType}/{entityID}/{method}")
	@ResponseBody
	public String handleFileUpload(@PathVariable String entityType,
			@PathVariable String entityID, @PathVariable String method,
			@RequestParam("file") MultipartFile file) {

		String returnMsg = "{\"result\":\"E\"}";
		try {
			log.debug("--Upload image servlet begin.");
			File uploadDir = getUploadRootDir();

			if (file != null) {
				String originalFileName = FilenameUtils.getName(file
						.getOriginalFilename());
				String fileExtension = FilenameUtils
						.getExtension(originalFileName);
				String fileNameNoExtension = FilenameUtils
						.removeExtension(originalFileName);

				String fileName = fileNameNoExtension
						+ FormatUtil.getFileSuffix()
						+ FilenameUtils.EXTENSION_SEPARATOR_STR + fileExtension;

				File targetFile = new File(uploadDir.getAbsolutePath()
						+ File.separator + fileName);

				long fileSize = Files.copy(file.getInputStream(),
						Paths.get(targetFile.getAbsolutePath()));

				if (fileSize > 0) {
					log.info("Uploaded file '" + targetFile.getAbsolutePath()
							+ "' is created.");
					overallService.updateEntityAfterFileUploaded(entityType,
							entityID, method, fileName);
				}

				return "{\"result\":\"S\",\"fileName\": \"" + fileName + "\"}";
			}
			log.debug("==Upload image servlet end.");
		} catch (IOException | RuntimeException e) {
			return returnMsg;
		}
		return returnMsg;
	}

}
