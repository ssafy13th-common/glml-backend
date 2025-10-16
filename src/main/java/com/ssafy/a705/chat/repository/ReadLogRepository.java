package com.ssafy.a705.chat.repository;

import com.ssafy.a705.chat.entity.ReadLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReadLogRepository extends MongoRepository<ReadLog, String>,
        ReadLogCustomRepository {

}
