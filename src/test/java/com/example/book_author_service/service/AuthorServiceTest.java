package com.example.book_author_service.service;

import com.example.book_author_service.model.Author;
import com.example.book_author_service.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        when(authorRepository.findAll()).thenReturn(Arrays.asList(new Author(), new Author()));
        assertEquals(2, authorService.getAllAuthors().size());
    }

    @Test
    void testGetAuthorById() {
        Author author = new Author();
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        assertEquals(author, authorService.getAuthorById(1L));
    }

    @Test
    void testCreateAuthor() {
        Author author = new Author();
        author.setName("Test Author");
        when(authorRepository.save(author)).thenReturn(author);
        Author createdAuthor = authorService.createAuthor(author);
        assertEquals(author, createdAuthor);
    }

    @Test
    void testUpdateAuthor() {
        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        existingAuthor.setName("Old Name");

        Author updatedAuthor = new Author();
        updatedAuthor.setName("New Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(existingAuthor)).thenReturn(existingAuthor);

        Author result = authorService.updateAuthor(1L, updatedAuthor);

        assertEquals("New Name", result.getName());
    }

    @Test
    void testDeleteAuthor() {
        doNothing().when(authorRepository).deleteById(1L);
        authorService.deleteAuthor(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }
}
