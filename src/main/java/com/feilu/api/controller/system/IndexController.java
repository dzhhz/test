package com.feilu.api.controller.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("index")
public class IndexController {

	@GetMapping("index")
	public String index() {
		log.info("Test log4j2 info");
        log.warn("Test log4j2 warn");
        log.error("Test log4j2 error");
		return "api";
	}
}



