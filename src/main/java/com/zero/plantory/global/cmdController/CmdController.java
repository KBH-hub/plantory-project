package com.zero.plantory.global.cmdController;

import com.zero.plantory.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class CmdController {

    final MemberService memberService;

    @RequestMapping("/")
    public String index(){
        return "member/login";
    }

    @GetMapping("/signUp")
    public String signUp(){
        return "member/signUp";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @RequestMapping("/dryPlantDictionary")
    public String dryPlantDictionary(){
        return "dictionary/dryPlantDictionary";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }

    @RequestMapping("/memberManagement")
    public String memberManagement(){
        return "admin/memberManagement";
    }

    @GetMapping("/messageDetail")
    public String messageDetail(){
        return "message/messageDetail";
    }

    @GetMapping("/messageList")
    public String messageList(){
        return "message/messageList";
    }

    @RequestMapping("/myPlantManagement")
    public String myPlantManagement(){return "myProfile/myPlantManagement";}

    @RequestMapping("/myProfile")
    public String myProfile(){return "myProfile/myProfile";}

    @GetMapping("/plantCalendar")
    public String plantCalendar(){
        return "myPlant/plantCalendar";
    }

    @RequestMapping("/createQuestion")
    public String createQuestion(){
        return "question/createQuestion";
    }

    @RequestMapping("/questionList")
    public String questionList(){
        return "question/questionList";
    }

    @RequestMapping("/readDictionary")
    public String readDictionary(){
        return "dictionary/readDictionary";
    }

    @GetMapping("/readQuestion")
    public String readQuestion(){
        return "question/readQuestion";
    }

    @GetMapping("/reportManagement")
    public String reportManagement(){
        return "admin/reportManagement";
    }

    @GetMapping("/sharingCreate")
    public String sharingCreate(){
        return "sharing/sharingCreate";
    }

    @GetMapping("/sharingDetail")
    public String sharingDetail(){
        return "sharing/sharingDetail";
    }

    @GetMapping("/sharingDetail-other")
    public String sharingDetailOther(){
        return "sharing/sharingDetail-other";
    }

    @RequestMapping("/sharingHistory")
    public String sharingHistory(){return "myProfile/mySharingHistory";}

    @GetMapping("/sharingList")
    public String sharingList(){
        return "sharing/sharingList";
    }

    @GetMapping("/sharingUpdate")
    public String sharingUpdate(){
        return "sharing/sharingUpdate";
    }

    @GetMapping("/termsOfService")
    public String termsOfService(){
        return "member/termsOfService";
    }

    @RequestMapping("/updateMyInfo")
    public String updateMyInfo(){return "myProfile/updateMyInfo";}

    @RequestMapping("/updateReview")
    public String updateReview(){ return "sharing/updateReview"; }

    @RequestMapping("/updateReviewTargetMember")
    public String updateReviewTargetMember(){
        return "sharing/updateReviewTargetMember";
    }

}
