package com.flacko.terminal.impl;

import com.flacko.common.exception.TerminalNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import com.flacko.terminal.service.Terminal;
import com.flacko.terminal.service.TerminalBuilder;
import com.flacko.terminal.service.TerminalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {

    private final TerminalRepository terminalRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<Terminal> list() {
        return StreamSupport.stream(terminalRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Terminal get(String id) throws TerminalNotFoundException {
        return terminalRepository.findById(id)
                .orElseThrow(() -> new TerminalNotFoundException(id));
    }

    @Transactional
    @Override
    public TerminalBuilder create() {
        return serviceLocator.create(InitializableTerminalBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public TerminalBuilder update(String id) throws TerminalNotFoundException {
        Terminal existingTerminal = get(id);
        return serviceLocator.create(InitializableTerminalBuilder.class)
                .initializeExisting(existingTerminal);
    }

}
