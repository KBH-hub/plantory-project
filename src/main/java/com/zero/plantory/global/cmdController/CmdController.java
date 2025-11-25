package com.zero.plantory.global.cmdController;

import com.zero.plantory.domain.member.service.MemberService;
import com.zero.plantory.global.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class CmdController {

    final MemberService memberService;

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @RequestMapping("/createQuestion")
    public String createQuestion(){
        return "createQuestion";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @RequestMapping("/dryPlantDictionary")
    public String dryPlantDictionary(){
        return "dryPlantDictionary";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/memberManagement")
    public String memberManagement(){
        return "memberManagement";
    }

    @GetMapping("/messageDetail")
    public String messageDetail(){
        return "messageDetail";
    }

    @GetMapping("/messageList")
    public String messageList(){
        return "messageList";
    }

    @RequestMapping("/myPlantManagement")
    public String myPlantManagement(){return "myPlantManagement";}

    @RequestMapping("/myProfile")
    public String myProfile(){return "myProfile";}

    @GetMapping("/plantCalendar")
    public String plantCalendar(){
        return "plantCalendar";
    }

    @RequestMapping("/questionList")
    public String questionList(){
        return "questionList";
    }

    @RequestMapping("/readDictionary")
    public String readDictionary(){
        return "readDictionary";
    }

    @GetMapping("/readQuestion")
    public String readQuestion(){
        return "readQuestion";
    }

    @GetMapping("/reportManagement")
    public String reportManagement(){
        return "reportManagement";
    }

    @GetMapping("/sharingCreate")
    public String sharingCreate(){
        return "sharingCreate";
    }

    @GetMapping("/sharingDetail")
    public String sharingDetail(){
        return "sharingDetail";
    }

    @GetMapping("/sharingDetail-other")
    public String sharingDetailOther(){
        return "sharingDetail-other";
    }

    @RequestMapping("/sharingHistory")
    public String sharingHistory(){return "sharingHistory";}

    @GetMapping("/sharingList")
    public String sharingList(){
        return "sharingList";
    }

    @GetMapping("/sharingUpdate")
    public String sharingUpdate(){
        return "sharingUpdate";
    }

    @GetMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

    @GetMapping("/termsOfService")
    public String termsOfService(){
        return "termsOfService";
    }

    @RequestMapping("/updateMyInfo")
    public String updateMyInfo(){return "updateMyInfo";}

    @RequestMapping("/updateReview")
    public String updateReview(){ return "updateReview"; }

    @RequestMapping("/updateReviewTargetMember")
    public String updateReviewTargetMember(){
        return "updateReviewTargetMember";
    }

}
