package app.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	@NotEmpty(message = "Это поле не должно быть пустым")
	@Size(min = 2, max = 100, message = "Это поле должно содержать 2-100 символов")
	@Column(name = "title")
	private String title;
	@NotEmpty(message = "Это поле не должно быть пустым")
	@Size(min = 2, max = 100, message = "Это поле должно содержать 2-100 символов")
	@Column(name = "author")
	private String author;
	@Min(value = 1500, message = "Год публикации должен быть не раньше 1500")
	@Column(name = "year_of_publishing")
	private int yearOfPublishing;

	@Column(name = "taken_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date takenAt;

	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "id")
	private Person owner;

	@Transient
	private boolean expired;

	public Book(String name, String author, int yearOfPublishing) {
		this.title = name;
		this.author = author;
		this.yearOfPublishing = yearOfPublishing;
	}

	public Book() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYearOfPublishing() {
		return yearOfPublishing;
	}

	public void setYearOfPublishing(int yearOfPublishing) {
		this.yearOfPublishing = yearOfPublishing;
	}

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public Date getTakenAt() {
		return takenAt;
	}

	public void setTakenAt(Date takenAt) {
		this.takenAt = takenAt;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
}
