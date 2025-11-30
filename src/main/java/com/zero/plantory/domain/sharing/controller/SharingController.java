package com.zero.plantory.domain.sharing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SharingController {
    @GetMapping("/readSharing/{sharingId}")
    public String readSharing(@PathVariable Long sharingId, Model model){
        model.addAttribute("sharingId", sharingId);
        return "sharing/readSharing";
    }

    @GetMapping("/updateSharing/{sharingId}")
    public String updateSharing(@PathVariable Long sharingId, Model model){
        model.addAttribute("sharingId", sharingId);
        return "sharing/createSharing";
    }

    @GetMapping("/updateReview")
    public String updateReview(@RequestParam Long sharingId, Model model) {
        model.addAttribute("sharingId", sharingId);
        return "sharing/updateReview";
    }



}
