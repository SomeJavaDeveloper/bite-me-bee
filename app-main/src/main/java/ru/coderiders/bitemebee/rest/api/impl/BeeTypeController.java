package ru.coderiders.bitemebee.rest.api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.coderiders.bitemebee.rest.api.BeeTypeApi;
import ru.coderiders.bitemebee.rest.dto.BeeTypeRqDto;
import ru.coderiders.bitemebee.rest.dto.BeeTypeRsDto;
import ru.coderiders.bitemebee.service.BeeTypeService;

@RestController
@RequiredArgsConstructor
public class BeeTypeController implements BeeTypeApi {
    private final BeeTypeService beeTypeService;

    @Override
    public Page<BeeTypeRsDto> getAll(Pageable pageable) {
        return beeTypeService.getAll(pageable);
    }

    @Override
    public BeeTypeRsDto getById(Long id) {
        return beeTypeService.getById(id);
    }

    @Override
    public ResponseEntity<BeeTypeRsDto> create(BeeTypeRqDto beeTypeRqDto) {

        BeeTypeRsDto created = beeTypeService.create(beeTypeRqDto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Override
    public BeeTypeRsDto update(Long id, BeeTypeRqDto beeTypeRqDto) {
        return beeTypeService.update(id, beeTypeRqDto);
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        beeTypeService.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
