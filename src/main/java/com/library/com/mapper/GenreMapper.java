package com.library.com.mapper;

import com.library.com.modal.Genre;
import com.library.com.payload.dto.GenreDto;
import com.library.com.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenreMapper {

    private final GenreRepository genreRepository;

    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }

        GenreDto dto = GenreDto.builder()
                .id(genre.getId())
                .code(genre.getCode())
                .name(genre.getName())
                .description(genre.getDescription())
                .displayOrder(genre.getDisplayOrder())
                .active(genre.getActive())
                .createdAt(genre.getCreatedAt())
                .updatedAt(genre.getUpdatedAt())
                .build();

        if (genre.getParentGenre() != null) {
            dto.setParentGenreId(genre.getParentGenre().getId());
            dto.setParentGenreName(genre.getParentGenre().getName());
        }

        if (genre.getSubGenres() != null && !genre.getSubGenres().isEmpty()) {
            dto.setSubGenre(
                    genre.getSubGenres()
                            .stream()
                            .map(this::toDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Genre toEntity(GenreDto dto) {
        if (dto == null) {
            return null;
        }

        Genre genre = Genre.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .active(dto.getActive() != null ? dto.getActive() : true)
                .build();

        if (dto.getParentGenreId() != null) {
            genreRepository.findById(dto.getParentGenreId())
                    .ifPresent(genre::setParentGenre);
        }

        if (dto.getSubGenre() != null && !dto.getSubGenre().isEmpty()) {
            List<Genre> children = dto.getSubGenre()
                    .stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());

            children.forEach(child -> child.setParentGenre(genre));

            genre.setSubGenres(children);
        }

        return genre;
    }

    public void updateEntityFromDto(GenreDto dto, Genre genre) {
        if (dto == null || genre == null) {
            return;
        }

        genre.setCode(dto.getCode());
        genre.setName(dto.getName());
        genre.setDescription(dto.getDescription());
        genre.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);

        if (dto.getActive() != null) {
            genre.setActive(dto.getActive());
        }

        if (dto.getParentGenreId() != null) {
            genreRepository.findById(dto.getParentGenreId())
                    .ifPresent(genre::setParentGenre);
        } else {
            genre.setParentGenre(null);
        }
    }


    public List<GenreDto> toDoList(List<Genre>genreList){
        return genreList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
