package app.services;

import app.models.Book;
import app.models.Person;
import app.repositories.BooksRepository;
import app.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

	private final PeopleRepository peopleRepository;

	private final int EXPIRATION_TIME = 864000000;

	@Autowired
	public PeopleService(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	public List<Person> index() {
		return peopleRepository.findAll();
	}

	public Person findById(int id) {
		return peopleRepository.findById(id).orElse(null);
	}

	public Optional<Person> checkPresence(String fullName, int yearOfBirth) {
		return peopleRepository.findByFullNameAndYearOfBirth(fullName, yearOfBirth);
	}

	@Transactional
	public void save(Person person) {
		peopleRepository.save(person);
	}

	@Transactional
	public void update(int id, Person updatedPerson) {
		updatedPerson.setId(id);
		peopleRepository.save(updatedPerson);
	}

	@Transactional
	public void delete(int id) {
		peopleRepository.deleteById(id);
	}

	@Transactional
	public List<Book> getBooksByPersonId(int id) {
		Optional<Person> person = peopleRepository.findById(id);

		if (person.isPresent()) {
			Hibernate.initialize(person.get().getBooks());

			person.get().getBooks().forEach(
					book -> {
						long diffInMillies = new Date().getTime() - book.getTakenAt().getTime();

						if (diffInMillies > EXPIRATION_TIME) {
							book.setExpired(true);
						}
					}
			);

			return person.get().getBooks();
		}

		return Collections.emptyList();
	}
}
