package ru.coderiders.bitemebee.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.coderiders.bitemebee.entity.BeeFamily;
import ru.coderiders.bitemebee.entity.BeeType;
import ru.coderiders.bitemebee.entity.Hive;
import ru.coderiders.bitemebee.mapper.BeeFamilyMapper;
import ru.coderiders.bitemebee.repository.BeeFamilyRepository;
import ru.coderiders.bitemebee.rest.dto.BeeFamilyNoteRqDto;
import ru.coderiders.bitemebee.rest.dto.BeeFamilyRqDto;
import ru.coderiders.bitemebee.rest.dto.BeeFamilyRsDto;
import ru.coderiders.bitemebee.rest.dto.BeeTypeRsDto;
import ru.coderiders.bitemebee.service.BeeFamilyService;
import ru.coderiders.bitemebee.service.BeeTypeService;
import ru.coderiders.bitemebee.service.HiveService;
import ru.coderiders.commons.rest.api.generator.BeeFamilyFeignApi;
import ru.coderiders.commons.rest.dto.GeneratorFamilyRqDto;
import ru.coderiders.commons.rest.exception.BadRequestException;
import ru.coderiders.commons.rest.exception.NotFoundException;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeeFamilyServiceImpl implements BeeFamilyService {
    private static final String BEE_FAMILY_NOT_FOUND = "Пчелиная семья с id=%s не найдена";
    private static final String HIVE_IS_OCCUPIED = "Улей занят другой пчелиной семьёй";
    private static final String BEE_TYPE_IS_DELETED = "Карточка вида удалена";
    private final BeeFamilyRepository beeFamilyRepository;
    private final BeeFamilyMapper beeFamilyMapper;
    private final HiveService hiveService;
    private final BeeTypeService beeTypeService;
    private final BeeFamilyFeignApi beeFamilyFeignApi;

    @Override
    @Transactional
    public BeeFamilyRsDto create(@NonNull BeeFamilyRqDto beeFamilyRqDto) {
        log.debug("Запрос на создание новой пчелиной семьи, beeFamilyRqDto = {}", beeFamilyRqDto);
        var hiveId = beeFamilyRqDto.getHiveId();
        var beeTypeId = beeFamilyRqDto.getBeeTypeId();
        try {
            hiveService.getById(hiveId);
            beeTypeService.getById(beeTypeId);
        } catch (NotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
        if (hiveService.isOccupied(hiveId)) {
            log.warn("Улей уже занят, id = {}", hiveId);
            throw new BadRequestException(HIVE_IS_OCCUPIED);
        }
        if (beeTypeService.getById(beeTypeId).getIsDeleted()) {
            log.warn("Карточка вида уже удалена, beeTypeId = {}", beeTypeId);
            throw new BadRequestException(BEE_TYPE_IS_DELETED);
        }
        BeeType beeType = new BeeType(beeTypeId);
        Hive hive = new Hive(hiveId);
        long totalPopulation = beeFamilyRqDto.getDronePopulation() +
                beeFamilyRqDto.getQueenPopulation() +
                beeFamilyRqDto.getWorkerPopulation();
        BeeFamily toCreate = beeFamilyMapper.toEntity(beeFamilyRqDto);
        toCreate.setBeeType(beeType);
        toCreate.setHive(hive);
        toCreate.setPopulation(totalPopulation);
        toCreate.setCreatedAt(Instant.now());
        BeeFamily created = beeFamilyRepository.save(toCreate);
        BeeTypeRsDto newFamilyType = beeTypeService.getById(created.getBeeType().getId());
        log.debug("Пчелиная семья успешно добавлена в улей, id = {}, toCreate = {}", hiveId, toCreate);
        GeneratorFamilyRqDto generatorFamilyRqDto = GeneratorFamilyRqDto.builder()
                .id(created.getId())
                .hiveId(beeFamilyRqDto.getHiveId())
                .dronePopulation(created.getDronePopulation())
                .workerPopulation(created.getWorkerPopulation())
                .queenPopulation(created.getQueenPopulation())
                .population(created.getPopulation())
                .diseaseResistance(newFamilyType.getDiseaseResistance())
                .honeyProductivity(newFamilyType.getHoneyProductivity())
                .eggProductivity(newFamilyType.getEggProductivity())
                .build();
        beeFamilyFeignApi.addFamily(generatorFamilyRqDto);
        return beeFamilyMapper.toDto(created);
    }

    @Override
    @Transactional
    public BeeFamilyRsDto getById(@NonNull Long id) {
        log.debug("Запрос на получение пчелиной семьи по id = {}", id);
        return beeFamilyRepository.findById(id)
                .map(beeFamilyMapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public Page<BeeFamilyRsDto> getAll(@NonNull Pageable pageable) {
        log.debug("Запрос на получение всех пчелиных семьей, pageable = {}", pageable);
        return beeFamilyRepository.findAll(pageable)
                .map(beeFamilyMapper::toDto);
    }

    @Override
    @Transactional
    public BeeFamilyRsDto update(@NonNull Long id, @NonNull BeeFamilyNoteRqDto beeFamilyNoteRqDto) {
        log.debug("Запрос на получение обновление пчелиной семьи по id = {}, beeFamilyNoteRqDto = {}", id, beeFamilyNoteRqDto);
        return beeFamilyRepository.findById(id)
                .map(found -> {
                    found.setNote(beeFamilyNoteRqDto.getNote());
                    return found;
                })
                .map(beeFamilyMapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public void updatePopulation(@NonNull Long id, @NonNull Long dronePopulation,
                                 @NonNull Long workerPopulation, @NonNull Long queenPopulation,
                                 @NonNull Long population) {
        log.debug("Запрос на обновление популяции семьи по id = {}, population = {}", id, population);
        beeFamilyRepository.findById(id)
                .map(found -> {
                    found.setDronePopulation(found.getDronePopulation() + dronePopulation);
                    found.setWorkerPopulation(found.getWorkerPopulation() + workerPopulation);
                    found.setQueenPopulation(found.getQueenPopulation() + queenPopulation);
                    found.setPopulation(found.getPopulation() + population);
                    return found;
                })
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public void release(@NonNull Long id) {
        log.debug("Запрос на выселение пчелиной семьи по id = {}", id);
        beeFamilyRepository.findById(id)
                .ifPresentOrElse(found -> {
                            found.setIsAlive(false);
                            found.setIsDeleted(true);
                            beeFamilyFeignApi.deleteById(id);
                        },
                        () -> {
                            throw new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id));
                        });
    }

    @Override
    @Transactional
    public void deleteByBeeType(@NonNull Long beeTypeId) {
        log.debug("Запрос на удаление семей с beeTypeId = {}", beeTypeId);
        List<BeeFamily> beeFamiliesList = beeFamilyRepository.findByBeeTypeId(beeTypeId)
                .stream().peek(found -> {
                    found.setIsDeleted(true);
                    found.setIsAlive(false);
                    beeFamilyFeignApi.deleteById(found.getId());
                }).toList();
        beeFamilyRepository.saveAll(beeFamiliesList);
    }

    @Override
    @Transactional
    public boolean beeFamilyExists(@NonNull Long id) {
        log.debug("Запрос на проверку существование пчелиной семьи по id = {}", id);
        return beeFamilyRepository.existsById(id);
    }

    @Transactional
    public List<BeeFamily> getAllEntities() {
        return beeFamilyRepository.findAll();
    }

    @Override
    @Transactional
    public void removeExtraQueens(@NonNull Long id) {
        log.debug("Запрос на удаление лишних маток в семье, id = {}", id);
        beeFamilyRepository.findById(id)
                .map(found -> {
                    found.setQueenPopulation(1L);
                    return found;
                })
                .orElseThrow(() -> new NotFoundException(String.format(BEE_FAMILY_NOT_FOUND, id)));
    }
}
