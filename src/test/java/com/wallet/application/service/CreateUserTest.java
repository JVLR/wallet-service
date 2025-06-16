//package com.wallet.application.usecase;
//
//import com.wallet.model.dto.CreateUserInput;
//import com.wallet.domain.model.User;
//import com.wallet.domain.repository.UsersRepository;
//import com.wallet.exception.ConflictDataException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class CreateUserTest {
//    private UsersRepository usersRepository;
//    private CreateUser createUser;
//
//    @BeforeEach
//    void setUp() {
//        usersRepository = mock(UsersRepository.class);
//        createUser = new CreateUser(usersRepository);
//    }
//
//    @Test
//    void shouldThrowConflictDataExceptionWhenUserAlreadyExists() {
//        var document = "12345678900";
//        CreateUserInput input = new CreateUserInput("Leo", document);
//        when(usersRepository.findByDocument(document)).thenReturn(Optional.of(new User("Leo", document)));
//
//        ConflictDataException exception = assertThrows(ConflictDataException.class, () -> {
//            createUser.execute(input);
//        });
//        assertTrue(exception.getMessage().contains("User already exists with document: 12345678900"));
//    }
//
//    @Test
//    void shouldSaveUserWhenUserDoesNotExist() {
//        var document = "12345678900";
//        CreateUserInput input = mock(CreateUserInput.class);
//        User user = new User("Leo", document);
//        when(usersRepository.findByDocument(document)).thenReturn(Optional.empty());
//        when(input.toUser()).thenReturn(user);
//        when(usersRepository.save(user)).thenReturn(user);
//        createUser.execute(input);
//
//        verify(usersRepository, times(1)).save(input.toUser());
//    }
//}
