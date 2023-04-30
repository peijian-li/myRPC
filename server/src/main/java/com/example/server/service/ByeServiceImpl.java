package com.example.server.service;


import com.example.common.api.ByeService;
import com.example.server.annotation.Service;

@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, " + name;
    }

}
