package com.librarymanagement.librarymanagement.services.Impl;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.librarymanagement.librarymanagement.enums.BooksFieldsEnum;
import com.librarymanagement.librarymanagement.enums.OrderEnum;
import com.librarymanagement.librarymanagement.enums.SortingFields;
import com.librarymanagement.librarymanagement.modals.FilterBooksModal;
import com.librarymanagement.librarymanagement.utils.ResponseStatusUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.librarymanagement.librarymanagement.constants.BooksErrorMessages;
import com.librarymanagement.librarymanagement.entities.Books;
import com.librarymanagement.librarymanagement.repositories.BooksRepository;
import com.librarymanagement.librarymanagement.repositories.StudentBookMappingRepository;
import com.librarymanagement.librarymanagement.services.BooksService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BooksServiceImpl implements BooksService {

    @Autowired
    public BooksRepository booksRepository;

    @Autowired
    public ResponseStatusUtility responseStatusUtility;

    @Autowired
    public StudentBookMappingRepository studentBookMappingRepo;

    /**
     * Get All Books
     *
     * @return All book records
     */
    @Override
    public List<Books> getAllBooks() {
        try {
            return booksRepository.findAll();
        } catch (Exception e) {
            log.error("Error occurred while fetching the books with error message : {}", e.getMessage());
            throw responseStatusUtility.internalServerErrorStatusException("Error occurred while fetching the books");
        }
    }

    /**
     * Get Book Details by id
     *
     * @param id Id to fetch books
     * @return Book Records
     */
    @Override
    public Books getBooksDetailsById(long id) {
        if (!booksRepository.existsById(id)) {
            throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
        } else {
            try {
                return booksRepository.findById(id).get();
            } catch (Exception e) {
                log.error("Error occurred while fetching the book with error message : {}", e.getMessage());
                throw responseStatusUtility.internalServerErrorStatusException("Error occurred while fetching the book");
            }
        }
    }

    /**
     * Update Books
     *
     * @param id   Id to update book
     * @param book Book record to update
     * @return Updated Book record
     */
    @Override
    public Books updateBooksDetails(long id, Books book) {
        if (!booksRepository.existsById(id))
            throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
        else {
            if (isBookNameInvalid(book))
                throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_NAME);
            else if (isBookAuthorInvalid(book))
                throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_AUTHOR);
            else if (isBookDepartmentInvalid(book))
                throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_DEPARTMENT);
            else {
                try {
                    book.setId(id);
                    Books books = booksRepository.findById(id).get();
                    book.setCreatedAt(books.getCreatedAt());
                    long updatedAt = Instant.now().toEpochMilli();
                    book.setUpdatedAt(updatedAt);
                    return booksRepository.save(book);
                } catch (Exception e) {
                    log.error("Error occurred while saving the book with error message:{}", e.getMessage());
                    throw responseStatusUtility
                            .internalServerErrorStatusException("Error occurred while updating the book");
                }
            }
        }
    }

    /**
     * Mark book as inactive
     *
     * @param id Book Id to mark the book inactive
     * @return Book response
     */
    @Override
    public Books markBookAsInactive(long id) {
        if (!booksRepository.existsById(id)) {
            throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
        } else if (Objects.nonNull(studentBookMappingRepo.findByBookIdAndBookIssued(id, true)))
            throw responseStatusUtility.conflictStatusException(BooksErrorMessages.CAN_NOT_INACTIVE_BOOK);
        else {
            try {
                Books book = booksRepository.findById(id).get();
                long updatedAt = Instant.now().toEpochMilli();
                book.setUpdatedAt(updatedAt);
                book.setStatus(false);
                return booksRepository.save(book);
            } catch (Exception e) {
                log.error("Error occurred while marking the book inactive with error message : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while marking the student as inactive");
            }
        }
    }

    /**
     * Add a book
     *
     * @param book Book Request
     * @return Book Response
     */
    @Override
    public Books addBook(Books book) {
        if (isBookNameInvalid(book))
            throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_NAME);
        else if (isBookAuthorInvalid(book))
            throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_AUTHOR);
        else if (isBookDepartmentInvalid(book))
            throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_DEPARTMENT);
        else if (!booksRepository.findBooksByBookId(book.getBookId()).isEmpty())
            throw responseStatusUtility.conflictStatusException(BooksErrorMessages.BOOK_ID_DUPLICATE);
        else {
            try {
                long time = Instant.now().toEpochMilli();
                book.setCreatedAt(time);
                book.setUpdatedAt(time);
                book.setStatus(true);
                return booksRepository.save(book);
            } catch (Exception e) {
                log.error("Error occurred while adding the book with error message : {}", e.getMessage());
                throw responseStatusUtility.internalServerErrorStatusException("Error occurred while adding the book");
            }
        }
    }

    /**
     * Add list of books
     *
     * @param books List of books to add
     * @return Books Response
     */
    @Override
    public List<Books> addBooks(List<Books> books) {
        List<Books> booksList = Lists.newArrayList();
        List<Books> booksListResponse = Lists.newArrayList();
        List<String> booksId = Lists.newArrayList();
        for (Books book : books) {
            if (isBookDataValid(book))
                booksList.add(book);
            else
                throw responseStatusUtility.badRequestStatusException(
                        String.format(BooksErrorMessages.INVALID_BOOK_DATA, book.toString()));
        }
        for (Books book : booksList) {
            if (booksId.contains(book.getBookId()))
                throw responseStatusUtility.conflictStatusException(
                        String.format(BooksErrorMessages.DUPLICATE_BOOK_IDS, book.getBookId()));
            else
                booksId.add(book.getBookId());
        }
        for (Books book : booksList) {
            try {
                booksListResponse.add(addBook(book));
            } catch (Exception e) {
                log.error("Error occurred while adding the books with error message : {}", e.getMessage());
                throw responseStatusUtility.internalServerErrorStatusException("Error occurred while adding the book");
            }
        }
        return booksListResponse;
    }

    /**
     * Validate book data
     *
     * @param book Book
     * @return
     */
    private boolean isBookDataValid(Books book) {
        if (isBookNameInvalid(book))
            return false;
        else if (isBookAuthorInvalid(book))
            return false;
        else if (isBookDepartmentInvalid(book))
            return false;
        else return booksRepository.findBooksByBookId(book.getBookId()).isEmpty();
    }

    /**
     * Validate book name
     *
     * @param book
     * @return
     */
    private boolean isBookNameInvalid(Books book) {
        if (Objects.isNull(book.getBookName()))
            return true;
        else if (book.getBookName().isEmpty())
            return true;
        else return book.getBookName().isBlank();
    }

    /**
     * Validate book author
     *
     * @param book
     * @return
     */
    private boolean isBookAuthorInvalid(Books book) {
        if (Objects.isNull(book.getBookAuthor()))
            return true;
        else if (book.getBookAuthor().isEmpty())
            return true;
        else return book.getBookAuthor().isBlank();
    }

    /**
     * Validate book department
     *
     * @param book
     * @return
     */
    private boolean isBookDepartmentInvalid(Books book) {
        if (Objects.isNull(book.getDepartment()))
            return true;
        else if (book.getDepartment().isEmpty())
            return true;
        else return book.getDepartment().isBlank();
    }

    /**
     * Delete book
     *
     * @param id BookId to delete
     * @return
     */
    @Override
    public Map<String, String> deleteBook(long id) {
        Map<String, String> hashMap = Maps.newConcurrentMap();
        if (!booksRepository.existsById(id)) {
            throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
        } else if (Objects.nonNull(studentBookMappingRepo.findByBookIdAndBookIssued(id, true)))
            throw responseStatusUtility.conflictStatusException(BooksErrorMessages.CAN_NOT_DELETE_BOOK);
        else {
            try {
                booksRepository.deleteById(id);
                hashMap.put("message", "Success");
                return hashMap;
            } catch (Exception e) {
                log.error("Error occurred while deleting the book with error message : {}", e.getMessage());
                throw responseStatusUtility.internalServerErrorStatusException("Error occurred while deleting the book");
            }
        }
    }

    /**
     * Search a book
     *
     * @param bookName Book Name to search
     * @return Searched result
     */
    @Override
    public List<Books> searchBooks(String bookName) {
        if (Objects.isNull(bookName) || bookName.isEmpty() || bookName.isBlank()) {
            throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_NAME);
        } else {
            try {
                List<Books> books = booksRepository.findAll();
                return books.stream().filter(x -> x.getBookName().contains(bookName)).collect(Collectors.toList());
            } catch (Exception e) {
                log.error("Error while searching the book : {} with error message : {}", bookName, e.getMessage());
                throw responseStatusUtility.internalServerErrorStatusException("Error while searching the book ");
            }
        }
    }

    /**
     * Filter books
     *
     * @param filterBooksModal
     * @return
     */
    @Override
    public List<Books> filterBooks(int pageNo, int pageSize, FilterBooksModal filterBooksModal) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<Books> books = booksRepository.findAll(pageable);
            List<Books> booksList = books.toList();
            for (FilterBooksModal.Filters filters : filterBooksModal.getFilters()) {
                booksList = filterBookByFields(filters.getField(), filters.getValue(), booksList);
            }
            return sortBooks(filterBooksModal.getSort().getField(), filterBooksModal.getSort().getOrder(), booksList);
        } catch (Exception e) {
            log.error("Error occurred while filtering the records with error message : {}", e.getMessage());
            throw responseStatusUtility.internalServerErrorStatusException("Error occurred while filtering the records");
        }
    }

    public List<Books> filterBookByFields(BooksFieldsEnum booksFieldsEnum, String value, List<Books> booksList) {
        List<Books> books = Lists.newArrayList();
        switch (booksFieldsEnum) {
            case bookName:
                if (Objects.isNull(value) || value.isEmpty())
                    return booksList;
                else
                    return booksList.stream().filter(book -> book.getBookName().equals(value)).collect(Collectors.toList());
            case bookAuthor:
                if (Objects.isNull(value) || value.isEmpty())
                    return booksList;
                else
                    return booksList.stream().filter(book -> book.getBookAuthor().equals(value)).collect(Collectors.toList());
            case department:
                if (Objects.isNull(value) || value.isEmpty())
                    return booksList;
                else
                    return booksList.stream().filter(book -> book.getDepartment().equals(value)).collect(Collectors.toList());
            case status:
                if (value.equalsIgnoreCase("active"))
                    return booksList.stream().filter(Books::isStatus).collect(Collectors.toList());
                else
                    return booksList.stream().filter(book -> !book.isStatus()).collect(Collectors.toList());
        }
        return books;
    }

    public List<Books> sortBooks(@NotNull SortingFields sortingFields, OrderEnum orderEnum, List<Books> booksList) {
        switch (sortingFields) {
            case createdAt:
                switch (orderEnum) {
                    case ASC:
                        return booksList.stream().sorted(Comparator.comparingLong(Books::getCreatedAt)).collect(Collectors.toList());
                    case DESC:
                        return booksList.stream().sorted(Comparator.comparingLong(Books::getCreatedAt).reversed()).collect(Collectors.toList());
                }
            case updatedAt:
                switch (orderEnum) {
                    case ASC:
                        return booksList.stream().sorted(Comparator.comparingLong(Books::getUpdatedAt)).collect(Collectors.toList());
                    case DESC:
                        return booksList.stream().sorted(Comparator.comparingLong(Books::getUpdatedAt).reversed()).collect(Collectors.toList());
                }
            case rollNo:
                return booksList;
        }
        return booksList;
    }

}
