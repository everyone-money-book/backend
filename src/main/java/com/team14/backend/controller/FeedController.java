package com.team14.backend.controller;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.model.Record;
import com.team14.backend.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return new ResponseDto("success",null, feeds);
    }
}
