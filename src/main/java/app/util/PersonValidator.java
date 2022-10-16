package app.util;

import app.models.Person;
import app.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

	private final PeopleService peopleService;

	@Autowired
	public PersonValidator(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;

		if (peopleService.checkPresence(person.getFullName(), person.getYearOfBirth()).isPresent()) {
			errors.rejectValue("fullName", "", "Этот человек уже является пользователем библитеки");
			errors.rejectValue("yearOfBirth", "", "Этот человек уже является пользователем библитеки");
		}
	}
}
