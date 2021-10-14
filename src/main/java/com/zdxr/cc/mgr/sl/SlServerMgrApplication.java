package com.zdxr.cc.mgr.sl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("com.zdxr.cc.mgr.sl.mapper")
public class SlServerMgrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlServerMgrApplication.class, args);
    }

}
