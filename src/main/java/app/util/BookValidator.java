package app.util;

import app.models.Book;
import app.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

	private final BooksService booksService;

	@Autowired
	public BookValidator(BooksService booksService) {
		this.booksService = booksService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Book book = (Book) target;

		if (booksService.checkPresence(book.getAuthor(), book.getTitle(), book.getYearOfPublishing()).isPresent()) {
			errors.rejectValue("author", "", "Эта книга уже есть в библиотеке");
			errors.rejectValue("title", "", "Эта книга уже есть в библиотеке");
			errors.rejectValue("yearOfPublishing", "", "Эта книга уже есть в библиотеке");
		}
	}
}
