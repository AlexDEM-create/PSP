package com.flacko.terminal;

import com.flacko.auth.spring.ServiceLocator;
import com.flacko.terminal.exception.TerminalNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
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

    @Override
    public TerminalBuilder create() {
        return serviceLocator.create(InitializableTerminalBuilder.class)
                .initializeNew();
    }

    @Override
    public TerminalBuilder update(String id) throws TerminalNotFoundException {
        Terminal existingTerminal = get(id);
        return serviceLocator.create(InitializableTerminalBuilder.class)
                .initializeExisting(existingTerminal);
    }

}
