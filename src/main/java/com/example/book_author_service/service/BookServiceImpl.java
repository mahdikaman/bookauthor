package com.example.book_author_service.service;

import com.example.book_author_service.model.Author;
import com.example.book_author_service.model.Book;
import com.example.book_author_service.repository.AuthorRepository;
import com.example.book_author_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book createBook(Book book) {
        // Kontrollera om författaren finns
        if (book.getAuthor() != null && book.getAuthor().getId() != null) {
            Optional<Author> authorOptional = authorRepository.findById(book.getAuthor().getId());
            if (authorOptional.isPresent()) {
                book.setAuthor(authorOptional.get());
            } else {
                // Hantera om författaren inte finns
                throw new RuntimeException("Author not found with id: " + book.getAuthor().getId());
            }
        } else {
            // Hantera om ingen författare är satt
            throw new RuntimeException("Author information is required");
        }
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Optional<Book> existingBookOptional = bookRepository.findById(id);
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setTitle(book.getTitle());

            // Uppdatera författaren om nödvändigt
            if (book.getAuthor() != null && book.getAuthor().getId() != null) {
                Optional<Author> authorOptional = authorRepository.findById(book.getAuthor().getId());
                if (authorOptional.isPresent()) {
                    existingBook.setAuthor(authorOptional.get());
                } else {
                    throw new RuntimeException("Author not found with id: " + book.getAuthor().getId());
                }
            }

            return bookRepository.save(existingBook);
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
