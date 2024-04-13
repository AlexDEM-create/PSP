package com.flacko.terminal.rest;

import com.flacko.terminal.Terminal;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class TerminalRestMapper {

    TerminalResponse mapModelToResponse(Terminal terminal) {
        // add timezone from authorization
        return new TerminalResponse(terminal.getId(),
                terminal.getTraderId(),
                terminal.getModel(),
                terminal.getOperatingSystem(),
                terminal.getCreatedDate().atZone(ZoneId.systemDefault()),
                terminal.getUpdatedDate().atZone(ZoneId.systemDefault()));
    }

}
