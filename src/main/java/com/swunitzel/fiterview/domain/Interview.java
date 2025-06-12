package com.swunitzel.fiterview.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "interview")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interview {

    @Id
    private String id;

    @Field("avg_posture_score")
    private float avgPostureScore;

    @Field("avg_facial_score")
    private float avgFacialScore;

    @Field("avg_gaze_score")
    private float avgGazeScore;

    @Field("avg_shoulder_tilt_count")
    private float avgShoulderTiltCount;

    @Field("avg_turn_left_count")
    private float avgTurnLeftCount;

    @Field("avg_turn_right_count")
    private float avgTurnRightCount;

    public Interview updateTotalScore(float avgPostureScore, float avgFacialScore, float avgGazeScore,
                                      float avgShoulderTiltCount, float avgTurnLeftCount, float avgTurnRightCount) {
        this.avgPostureScore = avgPostureScore;
        this.avgFacialScore = avgFacialScore;
        this.avgGazeScore = avgGazeScore;
        this.avgShoulderTiltCount = avgShoulderTiltCount;
        this.avgTurnLeftCount = avgTurnLeftCount;
        this.avgTurnRightCount = avgTurnRightCount;
        return this;
    }
}
