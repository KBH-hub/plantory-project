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

    @RequestMapping("/plantDictionary")
    public String plantDictionary(){
        return "dictionary/plantDictionary";
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
    public String myPlantManagement(){return "myPlant/myPlantManagement";}

    @RequestMapping("/profileInfo")
    public String profile(){return "profile/profileInfo";}

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

    @GetMapping("/createSharing")
    public String createSharing(){
        return "sharing/createSharing";
    }

    @GetMapping("/readSharing")
    public String readSharing(){
        return "sharing/readSharing";
    }

    @GetMapping("/readSharing-other")
    public String readSharingOther(){
        return "sharing/readSharing-other";
    }

    @RequestMapping("/mySharingHistory")
    public String mySharingHistory(){return "profile/mySharingHistory";}

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
    public String updateMyInfo(){return "profile/updateProfileInfo";}

    @RequestMapping("/updateReview")
    public String updateReview(){ return "sharing/updateReview"; }

    @RequestMapping("/updateReviewTargetMember")
    public String updateReviewTargetMember(){
        return "sharing/updateReviewTargetMember";
    }

}
