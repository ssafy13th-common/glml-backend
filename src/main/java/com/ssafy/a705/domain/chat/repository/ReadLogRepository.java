package com.ssafy.a705.domain.chat.repository;

import com.ssafy.a705.domain.chat.entity.ReadLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReadLogRepository extends MongoRepository<ReadLog, String>,
        ReadLogCustomRepository {

}
