package com.example.lujuria.stream.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.stream.dto.LiveStreamRequest;
import com.example.lujuria.stream.dto.LiveStreamResponse;
import com.example.lujuria.stream.entity.StreamStatus;
import com.example.lujuria.stream.service.StreamService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/streams")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping
    public List<LiveStreamResponse> listStreams(
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String tag,
        @RequestParam(required = false) StreamStatus status
    ) {
        return streamService.listStreams(country, category, tag, status);
    }

    @PostMapping
    public LiveStreamResponse createStream(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @Valid @RequestBody LiveStreamRequest request
    ) {
        return streamService.createStream(principal, request);
    }

    @PatchMapping("/{streamId}/finish")
    public LiveStreamResponse finishStream(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @PathVariable Long streamId
    ) {
        return streamService.finishStream(principal, streamId);
    }
}
