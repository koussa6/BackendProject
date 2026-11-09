package com.library.com.service;

import com.library.com.exception.GenreException;
import com.library.com.payload.dto.GenreDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GenreService {

    GenreDto createGenre(GenreDto genre);
    List<GenreDto>getAllGenres();
    GenreDto getGenreById(Long genreId) throws GenreException;
    GenreDto updateGenre(Long genreId,GenreDto genre) throws GenreException;
    void deleteGenre(Long genreId) throws GenreException;
    void hardDeleteGenre(Long genreId) throws GenreException;
    List<GenreDto> getTotalActiveGenres();
    List<GenreDto>getTopLevelGenres();
//    Page<GenreDto> searchGenres(String searchTerm, Pageable pageable);
    long getAllActiveGenres();
    long getBookCountByGenres(Long genreId);
}
