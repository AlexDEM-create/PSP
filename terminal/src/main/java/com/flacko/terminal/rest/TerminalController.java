package com.flacko.terminal.rest;

import com.flacko.terminal.Terminal;
import com.flacko.terminal.TerminalBuilder;
import com.flacko.terminal.TerminalService;
import com.flacko.terminal.exception.TerminalMissingRequiredAttributeException;
import com.flacko.terminal.exception.TerminalNotFoundException;
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
    public List<TerminalResponse> list() {
        return terminalService.list()
                .stream()
                .map(terminalRestMapper::mapModelToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{terminalId}")
    public TerminalResponse get(@PathVariable String terminalId) throws TerminalNotFoundException {
        return terminalRestMapper.mapModelToResponse(terminalService.get(terminalId));
    }

    @PostMapping
    public TerminalResponse create(@RequestBody TerminalCreateRequest terminalCreateRequest)
            throws TerminalMissingRequiredAttributeException {
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
            throws TerminalNotFoundException, TerminalMissingRequiredAttributeException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withArchived();
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

    @PostMapping("/{terminalId}/verify")
    public TerminalResponse verify(@PathVariable String terminalId)
            throws TerminalNotFoundException, TerminalMissingRequiredAttributeException {
        TerminalBuilder builder = terminalService.update(terminalId);
        builder.withVerified();
        Terminal terminal = builder.build();
        return terminalRestMapper.mapModelToResponse(terminal);
    }

}
