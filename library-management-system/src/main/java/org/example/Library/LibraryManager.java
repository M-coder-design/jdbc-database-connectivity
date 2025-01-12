package org.example.Library;

import org.example.connection.DatabaseConnection;
import org.example.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Library Management System class that handles all database operations
public class LibraryManager {
    private Connection connection;

    public LibraryManager() {
        // Get the database connection from our singleton
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Method to add a new book using PreparedStatement
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";

        // try-with-resources ensures automatic resource closure
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set values for the PreparedStatement
            // This prevents SQL injection and handles data type conversion
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());

            // Execute the insert statement
            pstmt.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get all books using Statement
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        // Using Statement for simple queries without parameters
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Process the ResultSet
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setYear(rs.getInt("year"));
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Method to update a book using PreparedStatement
    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());
            pstmt.setInt(4, book.getId());

            pstmt.executeUpdate();
            System.out.println("Book updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a book
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            System.out.println("Book deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method demonstrating transaction management
    public void transferBook(int bookId, int oldLibraryId, int newLibraryId) {
        // Set auto-commit to false to start a transaction
        try {
            connection.setAutoCommit(false);

            // First operation: Remove book from old library
            String sql1 = "UPDATE library_inventory SET quantity = quantity - 1 " +
                    "WHERE library_id = ? AND book_id = ?";

            // Second operation: Add book to new library
            String sql2 = "UPDATE library_inventory SET quantity = quantity + 1 " +
                    "WHERE library_id = ? AND book_id = ?";

            // Execute first update
            try (PreparedStatement pstmt1 = connection.prepareStatement(sql1)) {
                pstmt1.setInt(1, oldLibraryId);
                pstmt1.setInt(2, bookId);
                pstmt1.executeUpdate();
            }

            // Execute second update
            try (PreparedStatement pstmt2 = connection.prepareStatement(sql2)) {
                pstmt2.setInt(1, newLibraryId);
                pstmt2.setInt(2, bookId);
                pstmt2.executeUpdate();
            }

            // If both operations successful, commit the transaction
            connection.commit();
            System.out.println("Book transferred successfully!");

        } catch (SQLException e) {
            // If any operation fails, rollback the transaction
            try {
                connection.rollback();
                System.out.println("Transaction rolled back!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                // Reset auto-commit to true
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Example of using CallableStatement for a stored procedure
    public void getBooksByYear(int year) {
        // Assuming we have a stored procedure named 'get_books_by_year'
        String sql = "{CALL get_books_by_year(?)}";

        try (CallableStatement cstmt = connection.prepareCall(sql)) {
            cstmt.setInt(1, year);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Book: " + rs.getString("title"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
