package ru.coderiders.bitemebee.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.coderiders.bitemebee.rest.dto.JobNoteRqDto;
import ru.coderiders.bitemebee.rest.dto.JobRqDto;
import ru.coderiders.bitemebee.rest.dto.JobRsDto;

import javax.validation.Valid;

@Validated
@RequestMapping("/api/jobs")
@Tag(name = "Контроллер работ", description = "Позволяет управлять записями о работах на пасеке")
public interface JobAPI {
    @PostMapping
    @Operation(description = "Создание записи о работе", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED",
                content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = JobRsDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    ResponseEntity<JobRsDto> create(@Valid @RequestBody JobRqDto jobRqDto);

    @GetMapping("/{id}")
    @Operation(description = "Получение записи о работе по идентификатору", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JobRsDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    JobRsDto getById(@Parameter(required = true, description = "Идентификатор") @PathVariable(name = "id") Long id);

    @GetMapping
    @Operation(description = "Получение всех записей о работе", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    Page<JobRsDto> getAll(Pageable pageable);

    @PutMapping("/{id}")
    @Operation(description = "Изменение заметки о работе по идентификатору", method = "PUT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JobRsDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    JobRsDto update(@Parameter(required = true, description = "Идентификатор")
                    @PathVariable(name = "id") Long id,
                    @Valid @RequestBody JobNoteRqDto jobNoteRqDto);

    @DeleteMapping("/{id}")
    @Operation(description = "Удаление записи о работе по идентификатору", method = "DELETE")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    ResponseEntity<Void> complete(@Parameter(required = true, description = "Идентификатор")
                                  @PathVariable(name = "id") Long id);
}