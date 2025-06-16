package com.swunitzel.fiterview.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Objects;


@Document(collection = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    private String id;

    @Field("interview_id")
    private String interviewId;

    @Field("question_id")
    private String questionId;

    private String answer;

    private String summary;

    @Field("aiAnalysis_comment")
    private String aiAnalysisComment;

    @Field("improved_answer")
    private String improvedAnswer;

    @Field("blind_rule_adherence")
    private String blindRuleAdherence;

    @Field("smile_ratio")
    private Float smileRatio;

    @Field("gaze_down_count")
    private Integer gazeDownCount;

    @Field("gaze_points")
    private List<List<Integer>> gazePoints;

    @Field("blinks_per_minute")
    private Float blinksPerMinute;

    @Field("shoulder_tilt_count")
    private Integer shoulderTiltCount;

    @Field("turn_left_count")
    private Integer turnLeftCount;

    @Field("turn_right_count")
    private Integer turnRightCount;

    @Field("video_url")
    private String videoUrl;

    @Field("speaking_speed")
    private Float speakingSpeed;

    @Field("pitch_mean")
    private Float pitchMean;

    @Field("frequently_used_words")
    private List<Object> frequentlyUsedWords;

    @Field("hesitant_list")
    private List<String> hesitantList;

    @Field("hesitant_score")
    private Integer hesitantScore;
}
