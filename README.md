This is upgraded version of [my previous project](https://github.com/Verytas/Library_books_records). </br>

Changes:
- Spring Data JPA is used instead of JDBC temlplate;
- added search functionality (/books/search);
- added check if user not returned book in time (10 days) (will show such book in red text on user page).



Data is stored in database. SQL code for table creation is in file sql/library_db.sql.

You need to create file resources/hibernate.properties, which needed to connect to db.
Required fields to fill you will find in file resources/hibernate.properties.origin.

To acceess Users' data type in browser .../people, for books type .../books, where ... is address of your server.
(For example http://localhost:8080/books will bring you to books index page)
