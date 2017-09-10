package com.apec.service.impl;

import com.apec.service.MyService;
import org.springframework.stereotype.Service;

/**
 * Created by wubi on 2017/8/2.
 */
@Service
public class MyServiceImpl implements MyService {
    @Override
    public void test() {
        System.out.println("call MyService#test Method");
    }
}
