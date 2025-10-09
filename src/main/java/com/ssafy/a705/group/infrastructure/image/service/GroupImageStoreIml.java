package com.ssafy.a705.group.infrastructure.image.service;

import com.ssafy.a705.group.domain.image.entity.GroupImage;
import com.ssafy.a705.group.domain.image.repository.GroupImageRepository;
import com.ssafy.a705.group.domain.image.service.GroupImageStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupImageStoreIml implements GroupImageStore {
    private final GroupImageRepository groupImageRepository;
    @Override
    public void saveGroupImage(GroupImage groupImage) {
        groupImageRepository.save(groupImage);
    }
}
