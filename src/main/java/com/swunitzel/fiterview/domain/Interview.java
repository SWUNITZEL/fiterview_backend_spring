package com.swunitzel.fiterview.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

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

    @Field("gaze_points_list")
    private List<List<List<Integer>>> gazePointsList;

    @Field("avg_hesitant_Score")
    private float avgHesitantScore;

    @Field("avg_pitch_score")
    private float avgPitchScore;

    @Field("avg_speed_score")
    private float avgSpeedScore;

    @Field("frequently_used_words")
    private List<List<Object>> frequentlyUsedWords;

    @Field("hesitant_list")
    private List<List<String>> hesitantList;

    public Interview updateNonverbalCommunicationReport(float avgPostureScore, float avgFacialScore, float avgGazeScore,
                                      float avgShoulderTiltCount, float avgTurnLeftCount, float avgTurnRightCount, List<List<List<Integer>>> gazePointsList) {
        this.avgPostureScore = avgPostureScore;
        this.avgFacialScore = avgFacialScore;
        this.avgGazeScore = avgGazeScore;
        this.avgShoulderTiltCount = avgShoulderTiltCount;
        this.avgTurnLeftCount = avgTurnLeftCount;
        this.avgTurnRightCount = avgTurnRightCount;
        this.gazePointsList = gazePointsList;
        return this;
    }

    public Interview updateTransmissionReport(float avgHesitantScore, float avgPitchScore, float avgSpeedScore,
                                              List<List<Object>> frequentlyUsedWords, List<List<String>> hesitantList) {
        this.avgHesitantScore = avgHesitantScore;
        this.avgPitchScore = avgPitchScore;
        this.avgSpeedScore = avgSpeedScore;
        this.frequentlyUsedWords = frequentlyUsedWords;
        this.hesitantList = hesitantList;
        return this;
    }
}
