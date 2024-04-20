package com.flacko.appeal;

import com.flacko.appeal.exception.AppealNotFoundException;
import com.flacko.auth.spring.ServiceLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public List<Appeal> list() {
        return StreamSupport.stream(appealRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Appeal get(String id) throws AppealNotFoundException {
        return appealRepository.findById(id)
                .orElseThrow(() -> new AppealNotFoundException(id));
    }

    @Override
    public AppealBuilder create() {
        return serviceLocator.create(InitializableAppealBuilder.class)
                .initializeNew();
    }

    @Override
    public AppealBuilder update(String id) throws AppealNotFoundException {
        Appeal existingAppeal = get(id);
        return serviceLocator.create(InitializableAppealBuilder.class)
                .initializeExisting(existingAppeal);
    }

}
