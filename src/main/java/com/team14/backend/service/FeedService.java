package com.team14.backend.service;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.dto.SafeFeedResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Follow;
import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import com.team14.backend.repository.FeedRepository;
import com.team14.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    //피드들 Page로 가져오기: 모든 피드
    public Page<SafeFeedResponseDto> getAllFeeds(int page, int size, String sortBy, boolean isAsc){
        Sort.Direction direction = isAsc ? Sort.Direction.ASC: Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Record> records = feedRepository.findAll(pageable);

        //Page<Record>로 보내면 Record객체 안에 있는 User의 모든 정보까지 가져오게 되므로 안전한 정보만 있는걸 리터하도록 하였음
        return convertToSafeResponseDto(records);
    }

    //피드들 Page로 가져오기:follow한 피드만
    public Page<SafeFeedResponseDto> getFollowFeeds(int page, int size, String sortBy, boolean isAsc, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new CustomErrorException("회원 정보가 업습니다."));
        List<Follow> followingList = user.getFollowings();
        List<User> userList = new ArrayList<>();
        for(Follow following : followingList){
            userList.add(following.getToUser());
        }

        Sort.Direction direction = isAsc ? Sort.Direction.ASC: Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Record> records = feedRepository.findAllByUserIn(userList,pageable);

        return convertToSafeResponseDto(records);
    }

    private Page<SafeFeedResponseDto> convertToSafeResponseDto(Page<Record> records) {
        return records.map((SafeFeedResponseDto::new));
    }
}
