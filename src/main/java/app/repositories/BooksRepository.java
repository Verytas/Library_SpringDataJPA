package app.repositories;

import app.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
	List<Book> findByTitleStartingWith(String title);
	Optional<Book> findByAuthorAndTitleAndYearOfPublishing(String author, String title, int yearOfPublishing);
}
