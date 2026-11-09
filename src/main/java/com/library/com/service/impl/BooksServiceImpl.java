package com.library.com.service.impl;

import com.library.com.service.BookService;
import com.library.com.exception.BookException;
import com.library.com.mapper.BookMapper;
import com.library.com.modal.Book;
import com.library.com.payload.dto.BookDto;
import com.library.com.payload.request.BookSearchRequest;
import com.library.com.payload.response.PageResponse;
import com.library.com.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BookService {
    private final BooksRepository booksRepository;
    private final BookMapper bookMapper;
    @Override
    public BookDto createBook(BookDto bookDTO) throws BookException {
        if(booksRepository.existsByIsbn(bookDTO.getIsbn())){
            throw new BookException("book with isbn"+bookDTO.getIsbn()+"already exists");
        }
        Book book=bookMapper.toEntity(bookDTO);
        book.isAvailableCopiesValid();
        Book savedBook=booksRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> createBooksBulk(List<BookDto>bookDtos) throws BookException {
        List<BookDto>books=new ArrayList<>();
        for(BookDto bookDto:bookDtos){
            BookDto book=createBook(bookDto);
            books.add(book);
        }
        return books;
    }

    @Override
    public BookDto getBookById(Long bookId) throws BookException {
        Book book=booksRepository.findById(bookId)
                .orElseThrow(()->new BookException("book wasn't found by this id"));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto getBookByISBN(String isbn) throws BookException {
        Book book=booksRepository.findByIsbn(isbn)
                .orElseThrow(()->new BookException("book wasn't found by this id"));
        return bookMapper.toDto(book);

    }

    @Override
    public BookDto updateBook(Long bookId, BookDto bookDTO) throws BookException {
        Book existingBook=booksRepository.findById(bookId)
                .orElseThrow(()->new BookException("book not found"));
        bookMapper.updateEntityFromDTO(bookDTO,existingBook);
        existingBook.isAvailableCopiesValid();
        Book book=booksRepository.save(existingBook);
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteBook(Long bookId) throws BookException {
        Book existingBook=booksRepository.findById(bookId)
                .orElseThrow(()->new BookException("book not found"));
        existingBook.setActive(false);
        booksRepository.save(existingBook);
    }

    @Override
    public void hardDeleteBook(Long bookId) throws BookException {
        Book existingBook=booksRepository.findById(bookId)
                .orElseThrow(()->new BookException("book not found"));
        booksRepository.delete(existingBook);

    }

    @Override
    public PageResponse<BookDto> searchBooksWithFilters(BookSearchRequest searchRequest) {
        Pageable pageable = createPageable(searchRequest.getPage(),
                searchRequest.getSize(),
                searchRequest.getSortBy(),
                searchRequest.getSortDirection());

        Page<Book> bookPage = booksRepository.searchBooksWithFilters(
                searchRequest.getSearchTerm(),
                searchRequest.getGenreId(),
                searchRequest.getAvailableOnly(),
                pageable
        );
        
        return convertToPageResponse(bookPage);    }

    @Override
    public long getTotalActiveBooks() {
        return booksRepository.countByActiveTrue();
    }

    @Override
    public long getTotalAvailableBooks() {
        return booksRepository.countAvailableBooks();
    }
    private Pageable createPageable(int page,int size,String sortBy,String sortDirection){
        if(size>0){
        size=Math.min(size,10);
        size=Math.max(size,1);
}
        Sort sort=sortDirection.equalsIgnoreCase("ASC")
                ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        return PageRequest.of(page,size,sort);
    }
    private PageResponse<BookDto> convertToPageResponse(Page<Book> books) {
        List<BookDto> bookDTOS = books.getContent()
                .stream()
//                .map(book->bookMapper.toDto(book))
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookDTOS,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty()
        );
    }
}
