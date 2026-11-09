package com.library.com.payload.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreDto {

    private Long id;

    @NotBlank(message = "Genre code is mandatory")
    private String code;

    @NotBlank(message = "Genre name is mandatory")
    private String name;

    @Size(max = 500, message = "Description shouldn't exceed 500 characters")
    private String description;

    @Min(value = 0, message = "Display order shouldn't be negative")
    private Integer displayOrder = 0;

    private Boolean active;

    private Long parentGenreId;

    private String parentGenreName;

    private List<GenreDto> subGenre;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}