package com.team14.backend.controller;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping("/api/feed/all")
    public ResponseDto getAllFeeds(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ){
        page = page -1;
        Page<Record> feeds = feedService.getAllFeeds(page,size,sortBy,isAsc);
        return new ResponseDto("success","", feeds);
    }

    @GetMapping("/api/feed/")
    public ResponseDto getFollowFeeds(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ){
        Long userId = userDetails.getUser().getId();
        page = page -1;
        Page<Record> feeds = feedService.getFollowFeeds( page, size, sortBy, isAsc, userId);

        return new ResponseDto("success","",feeds);
    }
}
