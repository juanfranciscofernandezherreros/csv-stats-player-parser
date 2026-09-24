package com.example.csvstats.constants;

import java.util.List;

public final class CsvConstants {
    public static final String EXPECTED_FILENAME = "player_stats.csv";
    public static final List<String> EXPECTED_HEADER = List.of(
        "match_id","Player","Team","PTS","REB","AST","MIN","FGM","FGA","2PM","2PA",
        "3PM","3PA","FTM","FTA","+/-","OR","DR","PF","ST","TO","BS","BA","TFS");
    private CsvConstants() {}
}
