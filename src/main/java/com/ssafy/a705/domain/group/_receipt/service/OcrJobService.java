package com.ssafy.a705.domain.group._receipt.service;

import com.ssafy.a705.domain.group._receipt.dto.OcrStatus;
import com.ssafy.a705.domain.group._receipt.dto.request.FlaskOcrReq;
import com.ssafy.a705.domain.group._receipt.dto.request.OcrExecuteReq;
import com.ssafy.a705.domain.group._receipt.dto.response.FlaskOcrRes;
import com.ssafy.a705.domain.group._receipt.dto.response.OcrExecuteRes;
import com.ssafy.a705.domain.group._receipt.dto.response.ReceiptItem;
import com.ssafy.a705.domain.group._receipt.exception.FlaskApiException;
import com.ssafy.a705.domain.group._receipt.exception.FlaskException;
import com.ssafy.a705.domain.group._receipt.exception.FlaskTimeoutException;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrJobService {

    private final RestTemplate restTemplate;
    private final S3PresignedUploader presignedUploader;

    @Value("${ocr.downstream.flask-base-url}")
    private String flaskBaseUrl;

    @Value("${ocr.downstream.flask-internal-token}")
    private String internalToken;

    public OcrExecuteRes execute(OcrExecuteReq ocrExecuteReq, CustomUserDetails userDetails) {
        String receiptPresignedUrl = presignedUploader.generatePresignedGetUrl(
                ocrExecuteReq.receiptImageUrl());
        FlaskOcrRes flaskOcrRes = callFlaskOcr(receiptPresignedUrl);
        if (!Objects.isNull(flaskOcrRes) && flaskOcrRes.ocrStatus()
                .equals(OcrStatus.SUCCEEDED)) {
            List<ReceiptItem> receiptItems = parseReceiptItems(flaskOcrRes.ocrResult());
            return OcrExecuteRes.of(receiptItems);
        }
        return OcrExecuteRes.of(List.of());
    }

    private FlaskOcrRes callFlaskOcr(String presignedImageUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", internalToken);
        FlaskOcrReq flaskOcrReq = FlaskOcrReq.of(presignedImageUrl);
        HttpEntity<FlaskOcrReq> entity = new HttpEntity<>(flaskOcrReq, headers);
        try {
            ResponseEntity<FlaskOcrRes> flaskOcrResResponseEntity = restTemplate.postForEntity(
                    flaskBaseUrl +
                            "/v1/ai/ocr",
                    entity, FlaskOcrRes.class);
            return !Objects.isNull(flaskOcrResResponseEntity.getBody())
                    ? flaskOcrResResponseEntity.getBody() : FlaskOcrRes.of(OcrStatus.FAILED, null);
        } catch (ResourceAccessException e) {
            throw new FlaskTimeoutException();
        } catch (RestClientException e) {
            throw new FlaskApiException();
        } catch (Exception e) {
            throw new FlaskException();
        }
    }

    private List<ReceiptItem> parseReceiptItems(Object ocrResult) {
        if (!(ocrResult instanceof Map<?, ?> map)) {
            return List.of();
        }
        Object itemsObject = map.get("items");
        if (!(itemsObject instanceof List<?> ocrItems)) {
            return List.of();
        }
        List<ReceiptItem> receiptItems = new ArrayList<>();
        for (Object ocrItem : ocrItems) {
            if (!(ocrItem instanceof Map<?, ?> receiptItem)) {
                continue;
            }
            String name =
                    !Objects.isNull(receiptItem.get("name")) ? receiptItem.get("name").toString()
                            : null;
            Integer price =
                    (receiptItem.get("price") instanceof Number) ? ((Number) receiptItem.get(
                            "price")).intValue() : null;
            if (!Objects.isNull(name) && !Objects.isNull(price) && price >= 0) {
                receiptItems.add(ReceiptItem.of(name, price));
            }
        }
        return receiptItems;
    }
}
