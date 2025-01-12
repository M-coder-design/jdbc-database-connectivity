package org.example;

import org.example.Library.LibraryManager;
import org.example.entity.Book;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();

        // Adding a new book
        Book newBook = new Book();
        newBook.setTitle("The Great Gatsby");
        newBook.setAuthor("F. Scott Fitzgerald");
        newBook.setYear(1925);
        manager.addBook(newBook);
//
//        // Getting all books
        List<Book> allBooks = manager.getAllBooks();
        for (Book book : allBooks) {
            System.out.println(book.getTitle());
        }
////
//        // Updating a book
        newBook.setId(1); // Assuming the book got ID 1
        newBook.setYear(1926);
        manager.updateBook(newBook);
//
//        // Deleting a book
        manager.deleteBook(1);
//
//        // Using transaction to transfer a book
        manager.transferBook(2, 1, 2);

        // Using stored procedure
        manager.getBooksByYear(2000);
    }
}