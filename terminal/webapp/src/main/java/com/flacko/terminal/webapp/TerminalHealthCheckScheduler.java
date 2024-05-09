package com.flacko.terminal.webapp;

import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.terminal.service.Terminal;
import com.flacko.terminal.service.TerminalService;
import com.flacko.terminal.service.exception.TerminalMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class TerminalHealthCheckScheduler {

    private final TerminalService terminalService;

    @Scheduled(fixedRate = 30000)
    public void performHealthCheck() {
        List<Terminal> terminals = terminalService.list()
                .withVerified(true)
                .build();
        for (Terminal terminal : terminals) {
            if (terminal.getUpdatedDate().plusSeconds(30).isBefore(Instant.now())) {
                try {
                    terminalService.update(terminal.getId())
                            .withOnline(false)
                            .build();
                } catch (TerminalMissingRequiredAttributeException | TraderTeamNotFoundException |
                        TerminalNotFoundException e) {
                    log.error(String.format("Could not mark terminal %s as offline", terminal.getId()), e);
                }
            }
        }
    }

}
