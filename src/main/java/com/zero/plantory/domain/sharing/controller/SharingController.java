package com.zero.plantory.domain.sharing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/sharing/{sharingId}/review")
    public String updateReview(@PathVariable Long sharingId, Model model) {
        model.addAttribute("sharingId", sharingId);
        return "sharing/updateReview";
    }



}
