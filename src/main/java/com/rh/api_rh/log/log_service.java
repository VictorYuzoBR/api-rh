package com.rh.api_rh.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class log_service {

    @Autowired
    private log_repository log_repository;

    public List<log_model> listar() {

        try {
            return log_repository.findAll();
        } catch (Exception e) {
            return null;
        }

    }

}
