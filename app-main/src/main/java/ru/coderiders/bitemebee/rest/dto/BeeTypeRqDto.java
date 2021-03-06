package ru.coderiders.bitemebee.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Входной DTO вида пчелы")
public class BeeTypeRqDto {
    @Size(max = 100, message = "Длина названия вида должна быть не более 100 символов")
    @NotNull(message = "Не указано название вида")
    @Schema(description = "Название вида", example = "Медоносная пчела", maxLength = 100)
    private String title;
    @NotNull(message = "Не указано описание")
    @Schema(description = "Описание вида",
            example = "Люди разводят медоносных пчёл для получения продуктов пчеловодства: воска, мёда и других.")
    private String description;
    @Range(min = 300, max = 700, message = "Минимально комфортный уровень Co2 должен быть в промежутке от 300 до 700")
    @NotNull(message = "Не указан минимально комфортный уровень Co2")
    @Schema(description = "Минимально комфортный уровень Co2", example = "310", minimum = "300", maximum = "700")
    private Integer minCo2;
    @Range(min = 300, max = 700, message = "Максимально комфортный уровень Co2 должен быть в промежутке от 300 до 700")
    @NotNull(message = "Не указан максимально комфортный уровень Co2")
    @Schema(description = "Максимально комфортный уровень Co2", example = "640", minimum = "300", maximum = "700")
    private Integer maxCo2;
    @Range(min = 70, max = 85, message = "Минимально комфортный уровень влажности должен быть в промежутке от 70% до 85%")
    @NotNull(message = "Не указан минимально комфортный уровень влажности")
    @Schema(description = "Минимально комфортный уровень влажности", example = "75", minimum = "70", maximum = "85")
    private Integer minHumidity;
    @Range(min = 70, max = 85, message = "Максимально комфортный уровень влажности должен быть в промежутке от 70% до 85%")
    @NotNull(message = "Не указан максимально комфортный уровень влажности")
    @Schema(description = "Максимально комфортный уровень влажности", example = "83", minimum = "70", maximum = "85")
    private Integer maxHumidity;
    @Range(min = 25, max = 35, message = "Минимально комфортная температура должна быть в промежутке от 25°C до 35°C")
    @NotNull(message = "Не указана минимально комфортная температура")
    @Schema(description = "Минимально комфортная температура", example = "27", minimum = "25", maximum = "35")
    private Integer minTemperature;
    @Range(min = 25, max = 35, message = "Максимально комфортная температура должна быть в промежутке от 25°C до 35°C")
    @NotNull(message = "Не указана максимально комфортная температура")
    @Schema(description = "Максимально комфортная температура", example = "34", minimum = "25", maximum = "35")
    private Integer maxTemperature;
    @DecimalMin(value = "0.7", message = "Коэффициент устойчивости к заморозкам должен быть не меньше 0.7")
    @DecimalMax(value = "1.3", message = "Коэффициент устойчивости к заморозкам должен быть не больше 1.3")
    @NotNull(message = "Не указан коэффициент устойчивости к заморозкам")
    @Schema(description = "Коэффициент устойчивости к заморозкам", example = "0.9", minimum = "0.7", maximum = "1.3")
    private Double coldResistance;
    @DecimalMin(value = "0.7", message = "Коэффициент устойчивости к болезням должен быть не меньше 0.7")
    @DecimalMax(value = "1.3", message = "Коэффициент устойчивости к болезням должен быть не больше 1.3")
    @NotNull(message = "Не указан коэффициент устойчивости к болезням")
    @Schema(description = "Коэффициент устойчивости к болезням", example = "0.9", minimum = "0.7", maximum = "1.3")
    private Double diseaseResistance;
    @DecimalMin(value = "0.7", message = "Коэффициент производительности мёда должен быть не меньше 0.7")
    @DecimalMax(value = "1.3", message = "Коэффициент производительности мёда должен быть не больше 1.3")
    @NotNull(message = "Не указан коэффициент производительности мёда")
    @Schema(description = "Коэффициент производительности мёда", example = "0.9", minimum = "0.7", maximum = "1.3")
    private Double honeyProductivity;
    @DecimalMin(value = "0.7", message = "Коэффициент яйценоскости должен быть не меньше 0.7")
    @DecimalMax(value = "1.3", message = "Коэффициент яйценоскости должен быть не больше 1.3")
    @NotNull(message = "Не указан коэффициент яйценоскости")
    @Schema(description = "Коэффициент яйценоскости", example = "0.9", minimum = "0.7", maximum = "1.3")
    private Double eggProductivity;
    @DecimalMin(value = "0.7", message = "Коэффициент агрессии должен быть не меньше 0.7")
    @DecimalMax(value = "1.3", message = "Коэффициент агрессии должен быть не больше 1.3")
    @NotNull(message = "Не указан коэффициент агрессии")
    @Schema(description = "Уровень агрессии", example = "0.9", minimum = "0.7", maximum = "1.3")
    private Double aggressionLevel;
    @DecimalMin(value = "0.7", message = "Коэффициент склонности к роению должен быть не меньше 0.7")
    @DecimalMax(value = "1.3", message = "Коэффициент склонности к роению должен быть не больше 1.3")
    @NotNull(message = "Не указан коэффициент склонности к роению")
    @Schema(description = "Коэффициент склонности к роению", example = "0.9", minimum = "0.7", maximum = "1.3")
    private Double roilingLevel;
    @Builder.Default
    @Schema(description = "Массив с интервалами для типовых работ")
    private List<ScheduleRqDto> schedules = Collections.emptyList();
}
