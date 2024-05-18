package webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record StatsUpdateRequest(@JsonProperty(TODAY_OUTGOING_TOTAL) BigDecimal todayOutgoingTotal,
                                 @JsonProperty(TODAY_INCOMING_TOTAL) BigDecimal todayIncomingTotal,
                                 @JsonProperty(ALL_TIME_OUTGOING_TOTAL) BigDecimal allTimeOutgoingTotal,
                                 @JsonProperty(ALL_TIME_INCOMING_TOTAL) BigDecimal allTimeIncomingTotal) {

    private static final String TODAY_OUTGOING_TOTAL = "today_outgoing_total";
    private static final String TODAY_INCOMING_TOTAL = "today_incoming_total";
    private static final String ALL_TIME_OUTGOING_TOTAL = "all_time_outgoing_total";
    private static final String ALL_TIME_INCOMING_TOTAL = "all_time_incoming_total";
}