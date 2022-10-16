package app.services;

import app.models.Book;
import app.models.Person;
import app.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

	private final BooksRepository booksRepository;

	@Autowired
	public BooksService(BooksRepository booksRepository) {
		this.booksRepository = booksRepository;
	}

	public List<Book> index(boolean sortByYear) {
		if (sortByYear) {
			return booksRepository.findAll(Sort.by("yearOfPublishing"));
		} else {
			return booksRepository.findAll();
		}
	}

	public Book findById(int id) {
		return booksRepository.findById(id).orElse(null);
	}

	public List<Book> searchByTitle(String query) {
		return booksRepository.findByTitleStartingWith(query);
	}

	public List<Book> showPage(int page, int booksPerPage, boolean sortByYear) {
		if (sortByYear) {
			return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfPublishing"))).getContent();
		} else {
			return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
		}
	}

	public Optional<Book> checkPresence(String author, String title, int yearOfPublishing) {
		return booksRepository.findByAuthorAndTitleAndYearOfPublishing(author, title, yearOfPublishing);
	}

	@Transactional
	public void save(Book book) {
		booksRepository.save(book);
	}

	@Transactional
	public void update(int id, Book updatedBook) {
		Book bookToBeUpdated = booksRepository.findById(id).get();

		updatedBook.setId(id);
		updatedBook.setOwner(bookToBeUpdated.getOwner());

		booksRepository.save(updatedBook);
	}

	@Transactional
	public void delete(int id) {
		booksRepository.deleteById(id);
	}

	public Person getBookOwner(int id) {
		return booksRepository.findById(id).map(Book::getOwner).orElse(null);
	}

	@Transactional
	public void release(int id) {
		booksRepository.findById(id).ifPresent(
				book -> {
					book.setOwner(null);
					book.setTakenAt(null);
				}
		);
	}

	@Transactional
	public void assign(int id, Person chosenPerson) {
		booksRepository.findById(id).ifPresent(
				book -> {
					book.setOwner(chosenPerson);
					book.setTakenAt(new Date());
				}
		);
	}
}
