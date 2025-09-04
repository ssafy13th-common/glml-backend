package com.ssafy.a705.domain.chat.repository;

import com.ssafy.a705.domain.chat.dto.ChatMessageWithReadCount;
import com.ssafy.a705.domain.chat.dto.request.ReadLogUpdateReq;
import com.ssafy.a705.domain.chat.entity.ChatMessage;
import com.ssafy.a705.domain.chat.entity.ReadLog;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadLogRepositoryImpl implements
        ReadLogCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateLastReadMessage(String roomId, String memberId, String lastReadMessageId) {
        Query query = new Query(
                Criteria.where("roomId").is(roomId).and("memberId").is(memberId));
        Update update = new Update().set("lastReadMessageId", lastReadMessageId).set("updatedAt",
                LocalDateTime.now());
        mongoTemplate.upsert(query, update, ReadLog.class);
    }

    @Override
    public int countUsersWhoReadMessage(String roomId, String messageId) {
        ChatMessage message = mongoTemplate.findById(messageId, ChatMessage.class);
        Date timestamp = Date.from(
                message.getTimestamp().atZone(ZoneId.of("Asia/Seoul")).toInstant());
        if (message == null || message.getTimestamp() == null) {
            return 0;
        }

        List<Document> pipeline = Arrays.asList(
                new Document("$match", new Document("roomId", roomId).append("lastReadTimestamp",
                        new Document("$gte", timestamp))),
                new Document("$group", new Document("_id", "$memberId")),
                new Document("$count", "count")
        );

        Document result = mongoTemplate.getDb()
                .getCollection("read_logs")
                .aggregate(pipeline)
                .first();

        return result == null ? 0 : ((Number) result.get("count")).intValue();
    }

    @Override
    public List<ChatMessageWithReadCount> countUsersWhoReadMessages(String roomId, int page,
            int size) {
        MatchOperation match = Aggregation.match(Criteria.where("roomId").is(roomId));
        SortOperation sort = Aggregation.sort(Sort.by(Direction.DESC, "timestamp"));
        SkipOperation skip = Aggregation.skip((long) page * size);
        LimitOperation limit = Aggregation.limit(size);

        Document expr = new Document("$expr",
                new Document("$and", Arrays.asList(
                        new Document("$eq", Arrays.asList("$roomId", roomId)),
                        new Document("$gte", Arrays.asList("$$timestamp", "$timestamp"))
                        // 메시지.timestamp <= lastReadTimestamp
                ))
        );

        Document lookupDoc = new Document("$lookup",
                new Document("from", "read_logs")
                        .append("let",
                                new Document("msgId", "$_id").append("timestamp", "$timestamp"))
                        .append("pipeline", Arrays.asList(new Document("$match", expr)))
                        .append("as", "readLogs")
        );

        AggregationOperation lookupWithExpr = context -> lookupDoc;

        ProjectionOperation projection = Aggregation.project().and("_id").as("messageId")
                .andInclude("roomId", "senderId", "content", "timestamp").and("readLogs")
                .size().as("readCount");

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                sort,
                skip,
                limit,
                lookupWithExpr,
                projection
        );

        return mongoTemplate.aggregate(aggregation, "chat_messages", ChatMessageWithReadCount.class)
                .getMappedResults();
    }

    @Override
    public long countMessages(String roomId) {
        return mongoTemplate.count(new Query(Criteria.where("roomId").is(roomId)), "chat_messages");
    }

    public void updateAndInsert(ReadLogUpdateReq request, String memberId) {
        Query query = new Query(
                Criteria.where("roomId").is(request.roomId()).and("memberId").is(memberId));
        Update update = new Update()
                .set("lastReadMessageId", request.lastReadMessageId())
                .set("lastReadTimestamp", LocalDateTime.now())
                .set("updatedAt", LocalDateTime.now());
        mongoTemplate.upsert(query, update, ReadLog.class);
    }


}
