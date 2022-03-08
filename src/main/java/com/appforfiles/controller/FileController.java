package com.appforfiles.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appforfiles.model.File;
import com.appforfiles.service.FileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FileController {

	private final FileService fileService;

	@RequestMapping(value = "/files", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<File> getAllFiles() {
		return fileService.getAll();
	}

}
