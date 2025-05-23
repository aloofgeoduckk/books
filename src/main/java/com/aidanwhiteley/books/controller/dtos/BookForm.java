package com.aidanwhiteley.books.controller.dtos;

import com.aidanwhiteley.books.domain.Book;
import com.aidanwhiteley.books.domain.Comment;
import com.aidanwhiteley.books.service.dtos.GoogleBookSearchResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookForm {

    private String bookId;
    private List<Comment> comments = new ArrayList<>();
    @Size(min = 1, max = 100, message
            = "A book title between 1 and 100 characters is required")
    private String title;
    @Size(min = 1, max = 100, message = "The name of the author must be specified")
    private String author;
    @Size(min = 1, max = 30, message = "The book's genre must be specified and be less than 30 characters")
    private String genre;
    @Size(min = 10, max = 5000, message
            = "A review of the book is required. Anything less than 10 character is too brief and more than 5k is too loquacious")
    private String summary;
    @NotBlank(message = "The book must be given a rating")
    private String rating;
    private int index = -1;
    private String googleBookId;
    private GoogleBookSearchResult googleBookSearchResult;

    public static BookForm getBookFormFromBook(Book book) {
        var bookForm = new BookForm();
        bookForm.setBookId(book.getId());
        bookForm.setComments(book.getComments());
        bookForm.setTitle(book.getTitle());
        bookForm.setAuthor(book.getAuthor());
        bookForm.setGenre(book.getGenre());
        bookForm.setSummary(book.getSummary());
        bookForm.setRating(book.getRating().toString());
        bookForm.setGoogleBookId(book.getGoogleBookId());

        var googleBookSearchResult = new GoogleBookSearchResult();
        googleBookSearchResult.setItem(book.getGoogleBookDetails());
        bookForm.setGoogleBookSearchResult(googleBookSearchResult);

        return bookForm;
    }

    public Book getBookFromBookForm() {
        var buildingBook = Book.builder().title(title).author(author).genre(genre).summary(summary).
                rating(Book.Rating.getRatingByString(rating)).googleBookId(googleBookId);

        // Its important that we don't set an empty string for the id of the Book as that
        // could result inserting a Book with an id of "" rather than creating a new entry.
        if (bookId == null || bookId.isBlank()) {
            return buildingBook.build();
        } else {
            return buildingBook.id(bookId).build();
        }
    }
}
