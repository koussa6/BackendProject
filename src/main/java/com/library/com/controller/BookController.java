package com.library.com.controller;


import com.library.com.service.BookService;
import com.library.com.exception.BookException;
import com.library.com.payload.dto.BookDto;
import com.library.com.payload.request.BookSearchRequest;
import com.library.com.payload.response.ApiResponse;
import com.library.com.payload.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
@Tag(name = "Book controller")
public class BookController {
    private final BookService bookService;


    @GetMapping("/get/{bookId}")
    @Operation(summary = "Get any book by its id")
    public ResponseEntity<BookDto> getByBookId(@PathVariable Long bookId) throws BookException {
        BookDto book=bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a book by filters")
    public ResponseEntity<PageResponse<BookDto>> advancedSearch(
            @RequestBody BookSearchRequest searchRequest) {

        PageResponse<BookDto> books = bookService.searchBooksWithFilters(searchRequest);
        return ResponseEntity.ok(books);
    }
    @GetMapping
    public ResponseEntity<PageResponse<BookDto>> searchBooks(
            
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false,defaultValue = "false") Boolean availableOnly,
            @RequestParam(defaultValue = "true") boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        // Build search request from query parameters
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setGenreId(genreId);
        searchRequest.setAvailableOnly(availableOnly);
        searchRequest.setActiveOnly(activeOnly);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<BookDto> books = bookService.searchBooksWithFilters(searchRequest);
        return ResponseEntity.ok(books);
    }
    @GetMapping("/stats")
    @Operation(summary = "Get any book stats")
    public ResponseEntity<BookStatsResponse>getBookStats(){
        long totalActive=bookService.getTotalActiveBooks();
        long totalAvailable=bookService.getTotalAvailableBooks();

        BookStatsResponse stats=new BookStatsResponse(totalActive,totalAvailable);
        return ResponseEntity.ok(stats);
    }

    public static class BookStatsResponse {
        public long totalActiveBooks;
        public long totalAvailableBooks;

        public BookStatsResponse(long totalActiveBooks, long totalAvailableBooks) {
            this.totalActiveBooks = totalActiveBooks;
            this.totalAvailableBooks = totalAvailableBooks;
        }
    }
}
