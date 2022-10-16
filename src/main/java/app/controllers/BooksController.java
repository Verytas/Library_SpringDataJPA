package app.controllers;

import app.models.Book;
import app.models.Person;
import app.services.BooksService;
import app.services.PeopleService;
import app.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

	private final BooksService booksService;
	private final PeopleService peopleService;
	private final BookValidator bookValidator;

	@Autowired
	public BooksController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
		this.booksService = booksService;
		this.peopleService = peopleService;
		this.bookValidator = bookValidator;
	}

	@GetMapping()
	public String index(@RequestParam(name="page", required = false) Integer page,
						@RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
						@RequestParam(name = "sort_by_year", required = false) boolean sortByYear,
						Model model) {
		if (page != null && page >= 0 && booksPerPage != null && booksPerPage > 0) {
			model.addAttribute("books", booksService.showPage(page, booksPerPage, sortByYear));
		} else {
			model.addAttribute("books", booksService.index(sortByYear));
		}

		return "/books/index";
	}

	@GetMapping("/{id}")
	public String show(Model model, @PathVariable("id") int id,
					   @ModelAttribute("person") Person person) {
		model.addAttribute("book", booksService.findById(id));

		Person bookOwner = booksService.getBookOwner(id);

		if (bookOwner != null) {
			model.addAttribute("owner", bookOwner);
		} else {
			model.addAttribute("people", peopleService.index());
		}

		return "/books/show";
	}

	@GetMapping("/search")
	public String searchBook() {
		return "/books/search";
	}

	@PostMapping("/search")
	public String makeSearch(Model model, @RequestParam("query") String query) {
		model.addAttribute("books", booksService.searchByTitle(query));
		return "/books/search";
	}

	@GetMapping("/new")
	public String newBook(@ModelAttribute("book") Book book) {
		return "/books/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("book") @Valid Book book,
						 BindingResult bindingResult) {

		bookValidator.validate(book, bindingResult);

		if (bindingResult.hasErrors()) {
			return "books/new";
		}

		booksService.save(book);
		return "redirect:/books";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("book", booksService.findById(id));
		return "books/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("book") @Valid Book book,
						 BindingResult bindingResult, @PathVariable("id") int id) {

		bookValidator.validate(book, bindingResult);

		if (bindingResult.hasErrors()) {
			return "books/edit";
		}

		booksService.update(id, book);
		return "redirect:/books";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		booksService.delete(id);
		return "redirect:/books";
	}

	@PatchMapping("/{id}/release")
	public String release(@PathVariable("id") int id) {
		booksService.release(id);
		return "redirect:/books/" + id;
	}

	@PatchMapping("/{id}/assign")
	public String assign(@ModelAttribute("person") Person chosenPerson,
						 @PathVariable("id") int id) {
		booksService.assign(id, chosenPerson);
		return "redirect:/books/" + id;
	}
}
