package com.library.com.controller;

import com.library.com.service.GenreService;
import com.library.com.exception.GenreException;
import com.library.com.payload.dto.GenreDto;
import com.library.com.payload.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
@Tag(name = "Genre controller")

public class GenreController {

    private final GenreService genreService;
    @PostMapping("/create")
    public ResponseEntity<GenreDto> addGenre(@RequestBody GenreDto genre){
        GenreDto createdGenre=genreService.createGenre(genre);
        return ResponseEntity.ok(createdGenre);
    }
    @GetMapping("/getAll")
    public ResponseEntity<?>getAllGenres(){
        List<GenreDto> genreDto=genreService.getAllGenres();
      return ResponseEntity.ok(genreDto);
    }
    @GetMapping("/get/{genreId}")
    public ResponseEntity<?>getGenreById(@PathVariable("genreId") Long genreId) throws GenreException {
       GenreDto genreDto=genreService.getGenreById(genreId);
        return ResponseEntity.ok(genreDto);
    }
    @PutMapping("/{genreId}")
    public ResponseEntity<?>updateGenre(@PathVariable("genreId") Long genreId,
                                        @RequestBody GenreDto dto) throws GenreException {
        GenreDto genreDto=genreService.updateGenre(genreId,dto);
        return ResponseEntity.ok(genreDto);
    }
    @DeleteMapping("/{genreId}")
    public ResponseEntity<?>deleteGenre(@PathVariable("genreId") Long genreId
                                        ) throws GenreException {
        genreService.deleteGenre(genreId);
        ApiResponse response=new ApiResponse("genre deleted - soft delete",true);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/hard/{genreId}")
    public ResponseEntity<?>hardDelete(@PathVariable("genreId") Long genreId
    ) throws GenreException {
        genreService.hardDeleteGenre(genreId);
        ApiResponse response=new ApiResponse("genre deleted - hard delete",true);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/top-level")
    public ResponseEntity<?>getTopLevelGenres()  {
        List<GenreDto> genres=genreService.getTopLevelGenres();
        return ResponseEntity.ok(genres);
    }
    @GetMapping("/count")
    public ResponseEntity<?>getTotalActiveGenres() throws GenreException {
        List<GenreDto> genres=genreService.getTotalActiveGenres();
        return ResponseEntity.ok(genres);
    }
    @GetMapping("/{id}/book-count")
    public ResponseEntity<?>getBookCountByGenres(@PathVariable Long id) throws GenreException {
        Long count=genreService.getBookCountByGenres(id);
        return ResponseEntity.ok(count);
    }

}

