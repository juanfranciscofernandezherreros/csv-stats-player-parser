package com.example.csvstats.dto;

import lombok.Data;

@Data
public class StatsPlayerDTO {
    private String matchId;
    private String name;
    private String team;
    private Integer pts;
    private Integer reb;
    private Integer ast;
    private String min;
    private Integer fgm;
    private Integer fga;
    private Integer twopm;
    private Integer twopa;
    private Integer threepm;
    private Integer threepa;
    private Integer ftm;
    private Integer fta;
    private Integer plusMinus;
    private Integer or;
    private Integer dr;
    private Integer pf;
    private Integer st;
    private Integer to;
    private Integer bs;
    private Integer ba;
    private Integer tfs;
}
