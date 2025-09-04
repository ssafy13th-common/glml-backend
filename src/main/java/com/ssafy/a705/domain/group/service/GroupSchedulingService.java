package com.ssafy.a705.domain.group.service;

import com.ssafy.a705.domain.group.entity.GroupStatus;
import com.ssafy.a705.domain.group.repository.GroupRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupSchedulingService {

    private final GroupRepository groupRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateTravelStatus() {
        LocalDate today = LocalDate.now();
        log.info(today.toString());
        log.info(today.minusDays(1).toString());
        groupRepository.updateStatusByEndAt(GroupStatus.DONE, today.minusDays(1));
        groupRepository.updateStatusByStartAt(GroupStatus.IN_PROGRESS, today);
    }

}
