package ru.coderiders.Main.mapper;

import ru.coderiders.Main.entity.Hive;
import ru.coderiders.Main.rest.dto.HiveRqDto;
import ru.coderiders.Main.rest.dto.HiveRsDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class HiveMapper {

    private final ModelMapper modelMapper;

    @PostConstruct
    private void init() {
        modelMapper.createTypeMap(HiveRqDto.class, Hive.class);
        modelMapper.createTypeMap(Hive.class, HiveRsDto.class);
    }

    public Hive toEntity(HiveRqDto hiveRqDto) {
        return modelMapper.map(hiveRqDto, Hive.class);
    }

    public HiveRsDto toDto(Hive hive) {
        return modelMapper.map(hive, HiveRsDto.class);
    }
}