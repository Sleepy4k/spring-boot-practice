package com.sleepy4k.practice.service;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.sleepy4k.practice.api.model.Book;

@Service
public class BookService {
  private List<Book> bookList;

  public BookService() {
    bookList = new ArrayList<Book>();

    Book book1 = new Book(1, "The Lord of the Rings", "J.R.R. Tolkien", 1954);
    Book book2 = new Book(2, "The Little Prince", "Antoine de Saint-Exupéry", 1943);
    Book book3 = new Book(3, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", 1997);
    Book book4 = new Book(4, "And Then There Were None", "Agatha Christie", 1939);
    Book book5 = new Book(5, "Dream of the Red Chamber", "Cao Xueqin", 1791);

    bookList.addAll(Arrays.asList(book1, book2, book3, book4, book5));
  }

  public List<Book> getBook() {
    return bookList;
  }

  public Book addBook(Book body) {
    int id = bookList.size() + 1;
    String title = body.getTitle();
    String author = body.getAuthor();
    int year = body.getYearPublished();

    Book book = new Book(id, title, author, year);

    bookList.add(book);

    return book;
  }

  public Optional<Book> findBook(int id) {
    Optional<Book> optional = Optional.empty();

    for (Book book : bookList) {
      if (book.getId() == id) {
        optional = Optional.of(book);
        break;
      }
    }

    return optional;
  }

  public Optional<Book> editBook(int id, Book body) {
    Optional<Book> optional = Optional.empty();

    for (Book book : bookList) {
      if (book.getId() == id) {
        String title = body.getTitle();
        String author = body.getAuthor();
        int year = body.getYearPublished();

        book.setTitle(title);
        book.setAuthor(author);
        book.setYearPublished(year);

        optional = Optional.of(book);
        break;
      }
    }

  return optional;
}

  public Optional<Book> deleteBook(int id) {
    Optional<Book> optional = Optional.empty();

    for (Book book : bookList) {
      if (book.getId() == id) {
        bookList.remove(book);
        optional = Optional.of(book);
        break;
      }
    }

    return optional;
  }
}
