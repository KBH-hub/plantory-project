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

    @RequestMapping("/readQuestion")
    public String readQuestion(){
        return "readQuestion";
    }

    @RequestMapping("/sharingList")
    public String sharingList(){
        return "sharingList";
    }

    @RequestMapping("/sharingCreate")
    public String sharingCreate(){
        return "sharingCreate";
    }

    @RequestMapping("/sharingUpdate")
    public String sharingUpdate(){
        return "sharingUpdate";
    }

    @RequestMapping("/sharingDetail")
    public String sharingDetail(){
        return "sharingDetail";
    }

    @RequestMapping("/sharingDetail-other")
    public String sharingDetailOther(){
        return "sharingDetail-other";
    }

    @RequestMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @RequestMapping("/messageList")
    public String messageList(){
        return "messageList";
    }

    @RequestMapping("/messageDetail")
    public String messageDetail(){
        return "messageDetail";
    }

    @RequestMapping("/dryPlantDictionary")
    public String dryPlantDictionary(){
        return "dryPlantDictionary";
    }

    @RequestMapping("/updateReview")
    public String updateReview(){ return "updateReview"; }
    
    @RequestMapping("/updateReviewTargetMember")
    public String updateReviewTargetMember(){
        return "updateReviewTargetMember";
    }

    @RequestMapping("/createQuestion")
    public String createQuestion(){
        return "createQuestion";
    }

    @RequestMapping("/questionList")
    public String questionList(){
        return "questionList";
    }

    @RequestMapping("/readDictionary")
    public String readDictionary(){
        return "readDictionary";
    }


}
