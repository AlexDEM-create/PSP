package com.flacko.terminal.webapp.rest;

import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.terminal.service.Terminal;
import com.flacko.terminal.service.TerminalBuilder;
import com.flacko.terminal.service.TerminalListBuilder;
import com.flacko.terminal.service.TerminalService;
import com.flacko.terminal.service.exception.TerminalMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terminals")
public class TerminalController {

    private static final String TRADER_TEAM_ID = "trader_team_id";
    private static final String VERIFIED = "verified";
    private static final String ENABLED = "enabled";
    private static final String ONLINE = "online";
    private static final String ARCHIVED = "archived";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private final TerminalService terminalService;
    private final TerminalRestMapper terminalRestMapper;

    @GetMapping
    public List<TerminalResponse> list(@RequestParam(TRADER_TEAM_ID) Optional<String> traderTeamId,
                                       @RequestParam(VERIFIED) Optional<Boolean> verified,
                                       @RequestParam(ENABLED) Optional<Boolean> enabled,
                                       @RequestParam(ONLINE) Optional<Boolean> online,
                                       @RequestParam(ARCHIVED) Optional<Boolean> archived,
                                       @RequestParam(value = LIMIT, defaultValue = "10") Integer limit,
                                       @RequestParam(value = OFFSET, defaultValue = "0") Integer offset) {
        TerminalListBuilder builder = terminalService.list();
        traderTeamId.ifPresent(builder::withTraderTeamId);
        verified.ifPresent(builder::withVerified);
        enabled.ifPresent(builder::withEnabled);
        online.ifPresent(builder::withOnline);
        archived.ifPresent(builder::withArchived);
        return builder.build()
                .stream()
                .map(terminalRestMapper::mapModelToResponse)
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @GetMapping("/{terminalId}")
    public TerminalResponse get(@PathVariable String terminalId) throws TerminalNotFoundException {
        return terminalRestMapper.mapModelToResponse(terminalService.get(terminalId));
    }

    @PostMapping
    public TerminalResponse create(@RequestBody TerminalCreateRequest terminalCreateRequest)
            throws TerminalMissingRequiredAttributeException, TraderTeamNotFoundException {
        TerminalBuilder builder = terminalService.create();
        builder.withTraderTeamId(terminalCreateRequest.traderTeamId());
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

    @DeleteMapping("/{terminalId}")
    public TerminalResponse archive(@PathVariable String terminalId)
            throws TerminalNotFoundException, TerminalMissingRequiredAttributeException, TraderTeamNotFoundException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withArchived();
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

    @PatchMapping("/{terminalId}/verify")
    public TerminalResponse verify(@PathVariable String terminalId, TerminalVerifyRequest terminalVerifyRequest)
            throws TerminalNotFoundException, TerminalMissingRequiredAttributeException, TraderTeamNotFoundException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withVerified();
        if (terminalVerifyRequest.model().isPresent()) {
            builder.withModel(terminalVerifyRequest.model().get());
        }
        if (terminalVerifyRequest.operatingSystem().isPresent()) {
            builder.withOperatingSystem(terminalVerifyRequest.operatingSystem().get());
        }
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

    @PatchMapping("/{terminalId}/enable")
    public TerminalResponse enable(@PathVariable String terminalId)
            throws TerminalNotFoundException, TerminalMissingRequiredAttributeException, TraderTeamNotFoundException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withEnabled(true);
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

    @PatchMapping("/{terminalId}/disable")
    public TerminalResponse disable(@PathVariable String terminalId)
            throws TerminalNotFoundException, TerminalMissingRequiredAttributeException, TraderTeamNotFoundException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withEnabled(false);
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

    @PatchMapping("/{terminalId}/healthcheck")
    public TerminalResponse healthCheck(@PathVariable String terminalId) throws TerminalNotFoundException,
            TraderTeamNotFoundException, TerminalMissingRequiredAttributeException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withOnline(true);
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

}
