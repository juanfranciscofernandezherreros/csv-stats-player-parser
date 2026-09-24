package com.example.csvstats.mapper;

import com.example.csvstats.avro.StatsPlayerKey;
import com.example.csvstats.avro.StatsPlayerValue;
import com.example.csvstats.dto.StatsPlayerDTO;
import org.springframework.stereotype.Component;

@Component
public class StatsPlayerMessageMapper {
    public StatsPlayerKey toKey(String eventId, StatsPlayerDTO d) {
        return StatsPlayerKey.newBuilder().setSourceEventId(eventId).setMatchId(d.getMatchId()).setName(d.getName()).setTeam(d.getTeam()).build();
    }
    public StatsPlayerValue toValue(String eventId, StatsPlayerDTO d) {
        return StatsPlayerValue.newBuilder()
            .setSourceEventId(eventId).setMatchId(d.getMatchId()).setName(d.getName()).setTeam(d.getTeam())
            .setPts(d.getPts()).setReb(d.getReb()).setAst(d.getAst()).setMin(d.getMin())
            .setFgm(d.getFgm()).setFga(d.getFga()).setTwopm(d.getTwopm()).setTwopa(d.getTwopa())
            .setThreepm(d.getThreepm()).setThreepa(d.getThreepa()).setFtm(d.getFtm()).setFta(d.getFta())
            .setPlusMinus(d.getPlusMinus()).setOr(d.getOr()).setDr(d.getDr()).setPf(d.getPf())
            .setSt(d.getSt()).setTo(d.getTo()).setBs(d.getBs()).setBa(d.getBa()).setTfs(d.getTfs()).build();
    }
}
