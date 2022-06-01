package com.ccsu.test;

import com.ccsu.rpc.annotation.Service;
import com.ccsu.rpc.api.ByeService;

@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}