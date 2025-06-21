package org.ezcode.codetest.application.submission.dto.response.review;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코드 리뷰 응답 DTO")
public record CodeReviewResponse(

    @Schema(description = "리뷰 내용", example = "변수명이 명확하지 않습니다. 의미 있는 이름을 사용해주세요.")
    String reviewContent

) {
}
