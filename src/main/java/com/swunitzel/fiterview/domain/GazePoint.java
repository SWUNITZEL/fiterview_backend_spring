package com.swunitzel.fiterview.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GazePoint {
    private Integer x;
    private Integer y;
    private LocalDateTime time;
}