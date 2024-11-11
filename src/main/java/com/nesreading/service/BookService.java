package com.nesreading.service;

import com.nesreading.domain.Book;
import com.nesreading.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;
    private final UploadService uploadService;

    public BookService(BookRepository bookRepository, UploadService uploadService) {
        this.bookRepository = bookRepository;
        this.uploadService = uploadService;
    }

    public boolean handleCheckBookExist(int id) {
        return !bookRepository.existsById(id);
    }

    public List<Book> handleFetchAllBooks() {
        return bookRepository.findAll();
    }

    public Book handleFetchBookById(int id) {
        return bookRepository.getReferenceById(id);
    }

    public Book findBookById(int bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public void handleCreateBook(Book newBook) {
        newBook.setStatus(0);
        bookRepository.save(newBook);
    }

    public void handleCreateBook(Book newBook, MultipartFile file) {
        String bookImage = uploadService.handleSaveUploadFile(file, "book");
        
        newBook.setImageUrl(bookImage);
        newBook.setSold(0);
        newBook.setStatus(0);
        
        bookRepository.save(newBook);
    }

    public void handleDeleteBookById(int id) {
        Book tempBook = bookRepository.getReferenceById(id);

        if(tempBook.getImageUrl() != null && !tempBook.getImageUrl().isEmpty()) {
            uploadService.handleDeleteFile(tempBook.getImageUrl(), "book");
        }

        bookRepository.deleteById(id);
    }

    public void handleUpdateBook(Book tempBook, MultipartFile file) {
        Book dbBook = handleFetchBookById(tempBook.getId());

        dbBook.setTitle(tempBook.getTitle());
        dbBook.setAuthor(tempBook.getAuthor());
        dbBook.setPublisher(tempBook.getPublisher());
        dbBook.setPublicationYear(tempBook.getPublicationYear());
        dbBook.setShortDescription(tempBook.getShortDescription());
        dbBook.setDetailDescription(tempBook.getDetailDescription());
        dbBook.setPrice(tempBook.getPrice());
        dbBook.setStock(tempBook.getStock());
        dbBook.setCategory(tempBook.getCategory());

        if(!file.isEmpty()) {
            String bookImage = uploadService.handleSaveUploadFile(file, "book");
            dbBook.setImageUrl(bookImage);
        }

        bookRepository.save(dbBook);
    }
}
