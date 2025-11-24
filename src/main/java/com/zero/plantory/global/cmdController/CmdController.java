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

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUpMember(MemberVO member){
        memberService.signUp(member);
        return "redirect:/login";
    }



    @GetMapping("/termsOfService")
    public String termsOfService(){
        return "termsOfService";
    }

    @GetMapping("/plantCalendar")
    public String plantCalendar(){
        return "plantCalendar";
    }

    @GetMapping("/reportManagement")
    public String reportManagement(){
        return "reportManagement";
    }

    @GetMapping("/readQuestion")
    public String readQuestion(){
        return "readQuestion";
    }

    @GetMapping("/sharingList")
    public String sharingList(){
        return "sharingList";
    }

    @GetMapping("/sharingCreate")
    public String sharingCreate(){
        return "sharingCreate";
    }

    @GetMapping("/sharingUpdate")
    public String sharingUpdate(){
        return "sharingUpdate";
    }

    @GetMapping("/sharingDetail")
    public String sharingDetail(){
        return "sharingDetail";
    }

    @GetMapping("/sharingDetail-other")
    public String sharingDetailOther(){
        return "sharingDetail-other";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @GetMapping("/messageList")
    public String messageList(){
        return "messageList";
    }

    @GetMapping("/messageDetail")
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

    @RequestMapping("/memberManagement")
    public String memberManagement(){
        return "memberManagement";
    }

    @RequestMapping("/myPlantManagement")
    public String myPlantManagement(){return "myPlantManagement";}

    @RequestMapping("/myProfile")
    public String myProfile(){return "myProfile";}

    @RequestMapping("/sharingHistory")
    public String sharingHistory(){return "sharingHistory";}

    @RequestMapping("/updateMyInfo")
    public String updateMyInfo(){return "updateMyInfo";}


}
