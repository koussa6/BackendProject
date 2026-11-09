package com.library.com.service.impl;

import com.library.com.service.GenreService;
import com.library.com.exception.GenreException;
import com.library.com.mapper.GenreMapper;
import com.library.com.modal.Genre;
import com.library.com.payload.dto.GenreDto;
import com.library.com.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImplementation implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDto createGenre(GenreDto genreDto) {
//        return genreRepository.save(genreDto);
        Genre genre=genreMapper.toEntity(genreDto);
        Genre savedGenre= genreRepository.save(genre);
        //GenreDto dto= GenreMapper.toDto(savedGenre);
        return  genreMapper.toDto(savedGenre);
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream()
//                .map(genre->GenreMapper.toDto(genre))
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto getGenreById(Long genreId) throws GenreException {
      Genre genre= genreRepository.findById(genreId).orElseThrow(
                ()->new GenreException("genre not found")
        );
       return genreMapper.toDto(genre);
    }

    @Override
    public GenreDto updateGenre(Long genreId, GenreDto dto) throws GenreException {

        Genre existingGenre=genreRepository.findById(genreId)
                .orElseThrow(()->new GenreException("genre not found"));
         genreMapper.updateEntityFromDto(dto,existingGenre);
        Genre updateGenre= genreRepository.save(existingGenre);
        return genreMapper.toDto(updateGenre);
    }

    @Override
    public void deleteGenre(Long genreId) throws GenreException {
        Genre existingGenre=genreRepository.findById(genreId)
                .orElseThrow(()->new GenreException("genre not found"));
        existingGenre.setActive(false);
        genreRepository.save(existingGenre);
    }

    @Override
    public void hardDeleteGenre(Long genreId) throws GenreException {
        Genre existingGenre=genreRepository.findById(genreId)
                .orElseThrow(()->new GenreException("genre not found"));
        genreRepository.delete(existingGenre);

    }

    @Override
    public List<GenreDto> getTotalActiveGenres() {
        List<Genre> topLevelGenres=genreRepository
                .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.toDoList(topLevelGenres);
    }

    @Override
    public List<GenreDto> getTopLevelGenres() {
        List<Genre> topLevelGenres=genreRepository
                .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.toDoList(topLevelGenres);

    }

    @Override
    public long getAllActiveGenres() {
        return genreRepository.countByActiveTrue();
    }

    @Override
    public long getBookCountByGenres(Long genreId) {
        return 0;
    }
}