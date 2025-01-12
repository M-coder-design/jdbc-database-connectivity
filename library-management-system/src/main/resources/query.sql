CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    year INT
);

CREATE TABLE library_inventory (
    library_id INT,
    book_id INT,
    quantity INT,
    PRIMARY KEY (library_id, book_id)
);

------------------
#stored procedures

DELIMITER $$

CREATE PROCEDURE library_db.get_books_by_year(IN year INT)
BEGIN
    SELECT
        b.id,
        b.title,
        b.author,
        b.year
    FROM
        books b
    WHERE
        b.year = year;
END$$

DELIMITER ;