package ru.coderiders.bitemebee.rest.api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.coderiders.bitemebee.rest.api.BeeFamilyAPI;
import ru.coderiders.bitemebee.rest.dto.BeeFamilyNoteRqDto;
import ru.coderiders.bitemebee.rest.dto.BeeFamilyRqDto;
import ru.coderiders.bitemebee.rest.dto.BeeFamilyRsDto;
import ru.coderiders.bitemebee.service.BeeFamilyService;
import ru.coderiders.bitemebee.service.BeeFamilySnapshotService;
import ru.coderiders.commons.rest.dto.BeeFamilySnapshotDto;
import ru.coderiders.commons.rest.dto.BeeFamilySnapshotRqDto;

@RequiredArgsConstructor
@RestController
public class BeeFamilyController implements BeeFamilyAPI {
    private final BeeFamilyService beeFamilyService;
    private final BeeFamilySnapshotService beeFamilySnapshotService;

    @Override
    public Page<BeeFamilySnapshotDto> getSnapshots(Pageable pageable, BeeFamilySnapshotRqDto beeFamilySnapshotRqDto) {
        return beeFamilySnapshotService.getSnapshots(pageable, beeFamilySnapshotRqDto);
    }

    @Override
    public ResponseEntity<BeeFamilyRsDto> create(BeeFamilyRqDto beeFamilyRqDto) {
        BeeFamilyRsDto created = beeFamilyService.create(beeFamilyRqDto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public BeeFamilyRsDto getById(Long id) {
        return beeFamilyService.getById(id);
    }

    @Override
    public Page<BeeFamilyRsDto> getAll(Pageable pageable) {
        return beeFamilyService.getAll(pageable);
    }

    @Override
    public BeeFamilyRsDto update(Long id, BeeFamilyNoteRqDto beeFamilyNoteRqDto) {
        return beeFamilyService.update(id, beeFamilyNoteRqDto);
    }

    @Override
    public ResponseEntity<Void> release(Long id) {
        beeFamilyService.release(id);
        return ResponseEntity.accepted().build();
    }
}
