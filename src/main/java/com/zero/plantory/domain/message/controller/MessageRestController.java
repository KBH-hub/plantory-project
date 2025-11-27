package com.zero.plantory.domain.message.controller;

import com.zero.plantory.domain.message.dto.MessageListResponse;
import com.zero.plantory.domain.message.dto.MessageResponse;
import com.zero.plantory.domain.message.dto.MessageSearchRequest;
import com.zero.plantory.domain.message.service.MessageService;
import com.zero.plantory.domain.message.service.MessageServiceImpl;
import com.zero.plantory.global.dto.BoxType;
import com.zero.plantory.global.dto.TargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping("/{memberId}/{boxType}")
    public ResponseEntity<List<MessageListResponse>> getMessageList(
            @PathVariable Long memberId,
            @PathVariable BoxType boxType,
            @RequestParam(required = false) TargetType targetType,
            @RequestParam(required = false) String title,
            @RequestParam int offset,
            @RequestParam int limit
    ) {

        MessageSearchRequest req = new MessageSearchRequest(
                memberId, boxType, targetType, title, offset, limit
        );
        return ResponseEntity.ok().body(messageService.getMessageList(req));
    }


    @DeleteMapping("/deleteMessages")
    public ResponseEntity<Map<String, String>> deleteMessages(@RequestBody List<Long> messageIds) {
        int result = messageService.removeMessages(messageIds);
        if (result > 0) {
            return ResponseEntity.ok().body(Map.of("message", "delete message success"));
        }
        return ResponseEntity.status(400).body(Map.of("message", "delete message fail"));
    }

    @DeleteMapping("/deleteSenderMessages")
    public ResponseEntity<Map<String, String>> deleteSenderMessages(@RequestBody List<Long> messageIds) {
        int result = messageService.removeSenderMessages(messageIds);
        if (result > 0) {
            return ResponseEntity.ok().body(Map.of("message", "delete message success"));
        }
        return ResponseEntity.status(400).body(Map.of("message", "delete message fail"));
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable Long messageId, @RequestParam Long viewerId) {
        MessageResponse result = messageService.findMessageDetail(messageId, viewerId);
        if(result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
