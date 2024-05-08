package com.flacko.terminal.webapp.rest;

import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.exception.TraderTeamNotFoundException;
import com.flacko.terminal.service.Terminal;
import com.flacko.terminal.service.TerminalBuilder;
import com.flacko.terminal.service.TerminalListBuilder;
import com.flacko.terminal.service.TerminalService;
import com.flacko.terminal.service.exception.TerminalMissingRequiredAttributeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terminals")
public class TerminalController {

    private final TerminalService terminalService;
    private final TerminalRestMapper terminalRestMapper;


    @GetMapping
    public List<TerminalResponse> list(TerminalFilterRequest terminalFilterRequest) {
        TerminalListBuilder builder = terminalService.list();
        terminalFilterRequest.traderTeamId().ifPresent(builder::withTraderTeamId);
        terminalFilterRequest.verified().ifPresent(builder::withVerified);
        terminalFilterRequest.online().ifPresent(builder::withOnline);
        return builder.build()
                .stream()
                .map(terminalRestMapper::mapModelToResponse)
                .skip(terminalFilterRequest.offset())
                .limit(terminalFilterRequest.limit())
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
        if (terminalCreateRequest.model().isPresent()) {
            builder.withModel(terminalCreateRequest.model().get());
        }
        if (terminalCreateRequest.operatingSystem().isPresent()) {
            builder.withOperatingSystem(terminalCreateRequest.operatingSystem().get());
        }
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
    public TerminalResponse verify(@PathVariable String terminalId)
            throws TerminalNotFoundException, TerminalMissingRequiredAttributeException, TraderTeamNotFoundException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withVerified();
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
