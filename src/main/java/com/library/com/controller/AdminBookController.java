package com.library.com.controller;

import com.library.com.exception.BookException;
import com.library.com.payload.response.ApiResponse;
import com.library.com.service.BookService;
import com.library.com.payload.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin controller")

public class AdminBookController {
    private final BookService bookService;

    @PostMapping("/create-book")
    @Operation(summary = "Admin creates a new book")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto)
            throws Exception {

        BookDto book = bookService.createBook(bookDto);
        return ResponseEntity.ok(book);
    }
    @PostMapping("/bulk")
    @Operation(summary = "Create many books")
    public ResponseEntity<List<BookDto>>createBookBulks(@Valid @RequestBody List<BookDto> bookDto) throws BookException {
        List<BookDto> book=bookService.createBooksBulk(bookDto);
        return ResponseEntity.ok(book);
    }
    @PutMapping("/{bookId}")
    @Operation(summary = "Updating book")
    public ResponseEntity<BookDto>updateBook(@PathVariable Long bookId
            ,@RequestBody BookDto bookDto) throws BookException {

        BookDto dto=bookService.updateBook(bookId,bookDto);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft deleting a book")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) throws BookException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book deleted successfully", true));
    }
    @DeleteMapping("/{id}/permanent")
    @Operation(summary = "Hard deleting a book")
    public ResponseEntity<ApiResponse> hardDeleteBook(@PathVariable Long id) throws BookException {
        bookService.hardDeleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book permanently deleted", true));
    }
}
