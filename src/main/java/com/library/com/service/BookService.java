package com.library.com.service;

import com.library.com.exception.BookException;
import com.library.com.payload.dto.BookDto;
import com.library.com.payload.request.BookSearchRequest;
import com.library.com.payload.response.PageResponse;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto bookDTO) throws Exception;
    List<BookDto> createBooksBulk(List<BookDto>bookDto) throws BookException;
    BookDto getBookById(Long bookId) throws BookException;
    BookDto getBookByISBN(String isbn) throws BookException;
    BookDto updateBook(Long bookId, BookDto bookDTO) throws BookException;
    void deleteBook(Long bookId) throws BookException;
    void hardDeleteBook(Long bookId) throws BookException;

    PageResponse<BookDto> searchBooksWithFilters(
    BookSearchRequest searchRequest
    );

    long getTotalActiveBooks(); 

    long getTotalAvailableBooks(); 
}
