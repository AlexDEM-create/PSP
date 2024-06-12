package com.flacko.stats.webapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flacko.stats.service.EntityType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record StatsResponse(@JsonProperty(ID) String id,
                            @JsonProperty(ENTITY_ID) String entityId,
                            @JsonProperty(ENTITY_TYPE) EntityType entityType,
                            @JsonProperty(TODAY_OUTGOING_TOTAL) BigDecimal todayOutgoingTotal,
                            @JsonProperty(TODAY_INCOMING_TOTAL) BigDecimal todayIncomingTotal,
                            @JsonProperty(ALL_TIME_OUTGOING_TOTAL) BigDecimal allTimeOutgoingTotal,
                            @JsonProperty(ALL_TIME_INCOMING_TOTAL) BigDecimal allTimeIncomingTotal,
                            @JsonProperty(CREATED_DATE) ZonedDateTime createdDate,
                            @JsonProperty(UPDATED_DATE) ZonedDateTime updatedDate) {

    private static final String ID = "id";
    private static final String ENTITY_ID = "entity_id";
    private static final String ENTITY_TYPE = "entity_type";
    private static final String TODAY_OUTGOING_TOTAL = "today_outgoing_total";
    private static final String TODAY_INCOMING_TOTAL = "today_incoming_total";
    private static final String ALL_TIME_OUTGOING_TOTAL = "all_time_outgoing_total";
    private static final String ALL_TIME_INCOMING_TOTAL = "all_time_incoming_total";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

}
