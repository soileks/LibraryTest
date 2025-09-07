package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "b.isbn LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Book> searchBooks(@Param("query") String query, Pageable pageable);

    Page<Book> findByTitle(String title, Pageable pageable);
    Page<Book> findByAuthor(String author, Pageable pageable);
    Page<Book> findByIsbn(String isbn, Pageable pageable);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
    boolean existsByIsbn(String isbn);
}
