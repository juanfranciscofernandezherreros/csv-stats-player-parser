package com.example.csvstats.mapper;

import com.example.csvstats.dto.StatsPlayerDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatsPlayerMessageMapperTest {
 @Test void mapsDtoToAvro() {
  StatsPlayerDTO d = new StatsPlayerDTO(); d.setMatchId("m1"); d.setName("Player"); d.setTeam("Team"); d.setMin("30:00"); d.setPts(10);
  var mapper = new StatsPlayerMessageMapper();
  var key = mapper.toKey("e1", d); var value = mapper.toValue("e1", d);
  assertEquals("e1", key.getSourceEventId()); assertEquals("m1", key.getMatchId()); assertEquals("Player", value.getName()); assertEquals(10, value.getPts());
 }
}
