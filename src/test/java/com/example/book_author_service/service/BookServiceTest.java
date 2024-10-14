package com.example.book_author_service.service;

import com.example.book_author_service.model.Author;
import com.example.book_author_service.model.Book;
import com.example.book_author_service.repository.AuthorRepository;
import com.example.book_author_service.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(new Book(), new Book()));
        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertEquals(book, bookService.getBookById(1L));
    }

    @Test
    void testCreateBook() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor(author);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(1L); // Simulera att databasen sätter id
            return savedBook;
        });

        Book createdBook = bookService.createBook(book);

        assertNotNull(createdBook.getId());
        assertEquals("Test Book", createdBook.getTitle());
        assertEquals("Test Author", createdBook.getAuthor().getName());
    }

    @Test
    void testUpdateBook() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor(author);

        Book updatedBookData = new Book();
        updatedBookData.setTitle("New Title");
        updatedBookData.setAuthor(author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook(1L, updatedBookData);

        assertEquals("New Title", result.getTitle());
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
