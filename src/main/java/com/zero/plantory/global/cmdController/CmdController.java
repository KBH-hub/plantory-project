package com.zero.plantory.global.cmdController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CmdController {

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

    @RequestMapping("/termsOfService")
    public String termsOfService(){
        return "termsOfService";
    }

    @RequestMapping("/plantCalendar")
    public String plantCalendar(){
        return "plantCalendar";
    }

    @RequestMapping("/reportManagement")
    public String reportManagement(){
        return "reportManagement";
    }

    @RequestMapping("/sharingCommunityList")
    public String sharingCommunityList(){
        return "sharingCommunityList";
    }

    @RequestMapping("/sharingPostRegist")
    public String sharingPostRegist(){
        return "sharingPostRegist";
    }

}
