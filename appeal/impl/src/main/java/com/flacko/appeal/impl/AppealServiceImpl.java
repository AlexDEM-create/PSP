package com.flacko.appeal.impl;

import com.flacko.appeal.service.Appeal;
import com.flacko.appeal.service.AppealBuilder;
import com.flacko.appeal.service.AppealListBuilder;
import com.flacko.appeal.service.AppealService;
import com.flacko.common.exception.AppealNotFoundException;
import com.flacko.common.spring.ServiceLocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final ServiceLocator serviceLocator;

    @Override
    public AppealListBuilder list() {
        return serviceLocator.create(AppealListBuilder.class);
    }

    @Override
    public Appeal get(String id) throws AppealNotFoundException {
        return appealRepository.findById(id)
                .orElseThrow(() -> new AppealNotFoundException(id));
    }

    @Transactional
    @Override
    public AppealBuilder create() {
        return serviceLocator.create(InitializableAppealBuilder.class)
                .initializeNew();
    }

    @Transactional
    @Override
    public AppealBuilder update(String id) throws AppealNotFoundException {
        Appeal existingAppeal = get(id);
        return serviceLocator.create(InitializableAppealBuilder.class)
                .initializeExisting(existingAppeal);
    }

}
