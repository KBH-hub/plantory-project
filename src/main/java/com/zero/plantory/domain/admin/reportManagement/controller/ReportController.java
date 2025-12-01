package com.zero.plantory.domain.admin.reportManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReportController {

    @RequestMapping("/admin")
    public String admin(){
        return "admin/reportManagement";
    }
}
