package ru.coderiders.bitemebee.rest.api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.coderiders.bitemebee.rest.api.HiveApi;
import ru.coderiders.bitemebee.rest.dto.HiveRqDto;
import ru.coderiders.bitemebee.rest.dto.HiveRsDto;
import ru.coderiders.bitemebee.service.HiveService;
import ru.coderiders.bitemebee.service.HiveSnapshotService;
import ru.coderiders.commons.rest.dto.HiveSnapshotDto;
import ru.coderiders.commons.rest.dto.HiveSnapshotRqDto;

@RestController
@RequiredArgsConstructor
public class HiveController implements HiveApi {
    private final HiveService hiveService;
    private final HiveSnapshotService hiveSnapshotService;

    @Override
    public Page<HiveSnapshotDto> getSnapshots(Pageable pageable, HiveSnapshotRqDto hiveSnapshotRqDto) {
        return hiveSnapshotService.getSnapshots(pageable, hiveSnapshotRqDto);
    }

    @Override
    public Page<HiveRsDto> getAll(Pageable pageable) {
        return hiveService.getAll(pageable);
    }

    @Override
    public HiveRsDto getById(Long id) {
        return hiveService.getById(id);
    }

    @Override
    public ResponseEntity<HiveRsDto> create(HiveRqDto hiveRqDto) {
        HiveRsDto created = hiveService.create(hiveRqDto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public HiveRsDto update(Long id, HiveRqDto hiveRqDto) {
        return hiveService.update(id, hiveRqDto);
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        hiveService.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
