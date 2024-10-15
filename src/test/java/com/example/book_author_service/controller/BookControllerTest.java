package com.example.book_author_service.controller;

import com.example.book_author_service.model.Author;
import com.example.book_author_service.model.Book;
import com.example.book_author_service.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configure the ObjectMapper to match your application's configuration
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // Register any necessary modules
        // objectMapper.registerModule(new Hibernate5Module());

        // Set up MockMvc with the custom ObjectMapper
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setMessageConverters(messageConverter)
                .build();
    }

    @Test
    void testGetAllBooks() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book One");
        book1.setAuthor(author);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book Two");
        book2.setAuthor(author);

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[0].author.id").value(1))
                .andExpect(jsonPath("$[0].author.name").value("Author Name"))
                .andExpect(jsonPath("$[1].title").value("Book Two"))
                .andExpect(jsonPath("$[1].author.id").value(1))
                .andExpect(jsonPath("$[1].author.name").value("Author Name"));
    }

    @Test
    void testGetBookById() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor(author);

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.name").value("Author Name"));
    }

    @Test
    void testCreateBook() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor(author);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("New Book");
        savedBook.setAuthor(author);

        when(bookService.createBook(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.name").value("Author Name"));
    }

    @Test
    void testUpdateBook() throws Exception {
        Author author = new Author();
        author.setId(1L);
        author.setName("Author Name");

        Book book = new Book();
        book.setTitle("Updated Book");
        book.setAuthor(author);

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Book");
        updatedBook.setAuthor(author);

        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.name").value("Author Name"));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }
}
