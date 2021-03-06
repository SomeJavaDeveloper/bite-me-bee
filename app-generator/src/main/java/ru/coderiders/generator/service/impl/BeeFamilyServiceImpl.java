package ru.coderiders.generator.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coderiders.commons.rest.dto.BeeFamilySnapshotDto;
import ru.coderiders.commons.rest.dto.GeneratorFamilyRqDto;
import ru.coderiders.commons.rest.exception.BadRequestException;
import ru.coderiders.commons.rest.exception.NotFoundException;
import ru.coderiders.generator.entity.BeeFamily;
import ru.coderiders.generator.repository.BeeFamilyRepository;
import ru.coderiders.generator.service.BeeFamilyService;
import ru.coderiders.generator.service.HiveService;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeeFamilyServiceImpl implements BeeFamilyService {
    private static final String BEE_FAMILY_NOT_FOUND = "Пчелиная семья с id=%s не найдена";
    private static final String HIVE_IS_OCCUPIED = "Улей занят другой пчелиной семьёй";
    private final BeeFamilyRepository beeFamilyRepository;
    private final HiveService hiveService;

    @Override
    @Transactional
    public BeeFamilySnapshotDto createBeeFamilySnapshot(BeeFamily beeFamily) {
        String snapshotTime = Instant.now().toString();
        long dronePopulationIncrease = (long) (((ThreadLocalRandom.current()
                .nextDouble(0.008, 0.013)) * beeFamily.getDronePopulation()) * beeFamily.getDelta()) + 1;
        long workerPopulationIncrease = (long) ((ThreadLocalRandom.current()
                .nextDouble(0.008, 0.013) * beeFamily.getWorkerPopulation()) * beeFamily.getDelta()) + 1;
        long queenPopulationIncrease = (long) (ThreadLocalRandom.current()
                .nextDouble(0.00000001, 1.0) * beeFamily.getDelta() * 0.01);
        if (beeFamily.getIsInfected()) {
            if(beeFamily.getDronePopulation() - dronePopulationIncrease < 0) {
                dronePopulationIncrease = -beeFamily.getDronePopulation();
            } else {
                dronePopulationIncrease = -dronePopulationIncrease;
            }
            if(beeFamily.getWorkerPopulation() - workerPopulationIncrease < 0) {
                workerPopulationIncrease = -beeFamily.getWorkerPopulation();
            } else {
                workerPopulationIncrease = -workerPopulationIncrease;
            }
            if(beeFamily.getQueenPopulation() - queenPopulationIncrease < 0) {
                queenPopulationIncrease = -beeFamily.getQueenPopulation();
            } else {
                queenPopulationIncrease = -queenPopulationIncrease;
            }
        }
        long populationIncrease = dronePopulationIncrease + workerPopulationIncrease + queenPopulationIncrease;
        beeFamily.setDronePopulation(beeFamily.getDronePopulation() + dronePopulationIncrease);
        beeFamily.setWorkerPopulation(beeFamily.getWorkerPopulation() + workerPopulationIncrease);
        beeFamily.setQueenPopulation(beeFamily.getQueenPopulation() + queenPopulationIncrease);
        beeFamily.setPopulation(beeFamily.getPopulation() + populationIncrease);
        double activity = ThreadLocalRandom.current().nextDouble(0.7, 1.3);
        double mood = ThreadLocalRandom.current().nextDouble(0.7, 1.3);
        beeFamily.setActivity(activity);
        beeFamily.setMood(mood);
        beeFamilyRepository.save(beeFamily);
        return BeeFamilySnapshotDto.builder()
                .familyId(beeFamily.getId())
                .createdAt(snapshotTime)
                .populationIncrease(populationIncrease)
                .dronePopulationIncrease(dronePopulationIncrease)
                .workerPopulationIncrease(workerPopulationIncrease)
                .queenPopulationIncrease(queenPopulationIncrease)
                .activity(activity)
                .mood(mood).build();
    }

    @Override
    @Transactional
    public List<BeeFamily> findAll() {
        return beeFamilyRepository.findAll();
    }

    @Override
    @Transactional
    public void create(GeneratorFamilyRqDto generatorFamilyRqDto) {
        log.debug("Запрос на подселение семьи в генераторе, id = {}", generatorFamilyRqDto.getId());
        var hiveId = generatorFamilyRqDto.getHiveId();
        if (hiveService.isOccupied(hiveId)) {
            log.warn("Улей уже занят, id = {}", hiveId);
            throw new BadRequestException(HIVE_IS_OCCUPIED);
        }
        BeeFamily toCreate = BeeFamily.builder()
                .id(generatorFamilyRqDto.getId())
                .isDeleted(false)
                .dronePopulation(generatorFamilyRqDto.getDronePopulation())
                .workerPopulation(generatorFamilyRqDto.getWorkerPopulation())
                .queenPopulation(generatorFamilyRqDto.getQueenPopulation())
                .population(generatorFamilyRqDto.getPopulation())
                .diseaseResistance(generatorFamilyRqDto.getDiseaseResistance())
                .honeyProductivity(generatorFamilyRqDto.getHoneyProductivity())
                .eggProductivity(generatorFamilyRqDto.getEggProductivity())
                .build();
        BeeFamily toSaveInHive = beeFamilyRepository.save(toCreate);
        hiveService.saveFamily(generatorFamilyRqDto.getHiveId(), toSaveInHive);
    }

    @Override
    @Transactional
    public void delete(@NonNull Long id) {
        log.debug("Запрос на выселение семьи в генераторе, id = {}", id);
        beeFamilyRepository.findById(id)
                .map(found -> {
                    found.setIsDeleted(true);
                    return found;
                })
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
        hiveService.releaseFamily(id);
    }

    @Override
    @Transactional
    public void updateInfectedStatus(@NonNull Long id, @NonNull Boolean isInfected) {
        log.debug("Запрос на изменение здоровья семьи в генераторе, id = {}, isInfected = {}", id, isInfected);
        beeFamilyRepository.findById(id)
                .map(found -> {
                    found.setIsInfected(isInfected);
                    return found;
                })
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public void updateDelta(@NonNull Long id, @NonNull Double delta) {
        log.debug("Запрос на изменение дельты семьи в генераторе, id = {}", id);
        beeFamilyRepository.findById(id)
                .map(found -> {
                    found.setDelta(delta);
                    return found;
                })
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public void removeExtraQueens(@NonNull Long id) {
        log.debug("Запрос на удаление лишних маток в семье в генераторе, id = {}", id);
        beeFamilyRepository.findById(id)
                .map(found -> {
                    found.setQueenPopulation(1L);
                    return found;
                })
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
    }
}
