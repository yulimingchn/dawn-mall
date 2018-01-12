package com.dawn.manage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {

    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH;

    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL;
    
    @Value("${DAWN_WEB_URL}")
    public String DAWN_WEB_URL;

}
