package com.sleepy4k.practice.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sleepy4k.practice.api.model.Book;
import com.sleepy4k.practice.service.BookService;

@RestController
public class BookController {
  private BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping(value="/book")
  public List<Book> getBook() {
    return bookService.getBook();
  }

  @PostMapping(value="/book")
  public Book addBook(@RequestBody Book body) {
    return bookService.addBook(body);
  }

  @GetMapping(value="/book/{id}")
  public Book findBook(@PathVariable("id") int id) {
    Optional<Book> book = bookService.findBook(id);

    if (book.isPresent()) {
      return (Book) book.get();
    } else {
      return new Book();
    }
  }

  @PutMapping(value="book/{id}")
  public Book editBook(@PathVariable("id") int id, @RequestBody Book body) {
    Optional<Book> book = bookService.editBook(id, body);

    if (book.isPresent()) {
      return (Book) book.get();
    } else {
      return new Book();
    }
  }

  @DeleteMapping(value="book/{id}")
  public Book deleteBook(@PathVariable("id") int id) {
    Optional<Book> book = bookService.deleteBook(id);

    if (book.isPresent()) {
      return (Book) book.get();
    } else {
      return new Book();
    }
  }
}
