package com.example.fantreehouse.domain.feed.entity;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import lombok.Getter;
import java.util.Objects;

@Getter
public enum FeedEnumCategory {
    // 게시글 종류
    COMMUNITY("community"),
    ARTIST("artist"),
    NOTICE("notice"),
    SCHEDULE("schedule");

    private final String category;

    FeedEnumCategory(String category) {
        this.category = category;
    }

    public static FeedEnumCategory getByCategory(String category) {
        if (Objects.equals(category, COMMUNITY.category)) {
            return COMMUNITY;
        } else if (Objects.equals(category, ARTIST.category)) {
            return ARTIST;
        } else if (Objects.equals(category, NOTICE.category)) {
            return NOTICE;
        } else if (Objects.equals(category, SCHEDULE.category)) {
            return SCHEDULE;
        } else
            throw new MismatchException(ErrorType.CONTENT_TYPE_MISMATCH);
    }
}
