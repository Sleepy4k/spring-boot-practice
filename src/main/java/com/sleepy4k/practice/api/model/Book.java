package com.sleepy4k.practice.api.model;

public class Book {
  private int id;
  private String title;
  private String author;
  private int yearPublished;

  public Book(int id, String title, String author, int yearPublished) {
    this.setId(id);
    this.setTitle(title);
    this.setAuthor(author);
    this.setYearPublished(yearPublished);
  }

  public Book() {
    this.setId(0);
    this.setTitle("");
    this.setAuthor("");
    this.setYearPublished(0);
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("id must be greater than 0");
    }

    this.id = id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    if (title == null) {
      throw new IllegalArgumentException("title must not be null");
    }

    this.title = title;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String author) {
    if (author == null) {
      throw new IllegalArgumentException("author must not be null");
    }

    this.author = author;
  }

  public int getYearPublished() {
    return this.yearPublished;
  }

  public void setYearPublished(int yearPublished) {
    if (yearPublished < 0) {
      throw new IllegalArgumentException("yearPublished must be greater than 0");
    }

    this.yearPublished = yearPublished;
  }

  @Override
  public String toString() {
    return "Book{" +
      "id=" + this.id +
      ", title='" + this.title + '\'' +
      ", author='" + this.author + '\'' +
      ", yearPublished=" + this.yearPublished +
      '}';
  }
}
