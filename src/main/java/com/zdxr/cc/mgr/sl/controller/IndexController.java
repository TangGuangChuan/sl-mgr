package com.zdxr.cc.mgr.sl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {
    /*@GetMapping(value = "/")
    public ModelAndView defaultPath(Model model) {
        return new ModelAndView("redirect:/index.html");
    }*/

    @GetMapping(value = "/")
    public void defaultPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        response.sendRedirect(basePath + "index.html");
    }

}
