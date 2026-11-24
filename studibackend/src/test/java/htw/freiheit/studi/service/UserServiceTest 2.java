package htw.freiheit.studi.service;

import htw.freiheit.studi.dto.UserRequestDTO;
import htw.freiheit.studi.dto.UserResponseDTO;
import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.entity.User;
import htw.freiheit.studi.repository.RoleRepository;
import htw.freiheit.studi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Role admin;
    private UserRequestDTO request;

    @BeforeEach
    void setUp() {
        admin = new Role("admin");
        admin.setId(2L);
        request = new UserRequestDTO("anna", "geheim123", "anna@htw-berlin.de", 2L);
    }

    @Test
    void create_WithNewUser_ShouldHashPasswordAndSaveUser() {
        // Arrange
        when(userRepository.existsByUsername("anna")).thenReturn(false);
        when(userRepository.existsByEmail("anna@htw-berlin.de")).thenReturn(false);
        when(roleRepository.findById(2L)).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode("geheim123")).thenReturn("HASH");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // Act
        UserResponseDTO response = userService.create(request);

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getPassword()).isEqualTo("HASH");
        assertThat(saved.getRole()).isSameAs(admin);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("anna");
        assertThat(response.role().name()).isEqualTo("admin");
    }

    @Test
    void create_WithExistingUsername_ShouldThrowConflict() {
        // Arrange
        when(userRepository.existsByUsername("anna")).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        verify(userRepository, never()).save(any());
    }

    @Test
    void create_WithUnknownRole_ShouldThrowNotFound() {
        // Arrange
        when(userRepository.existsByUsername("anna")).thenReturn(false);
        when(userRepository.existsByEmail("anna@htw-berlin.de")).thenReturn(false);
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(e -> ((ResponseStatusException) e).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_WithBlankPassword_ShouldKeepOldPassword() {
        // Arrange
        User existing = new User();
        existing.setId(1L);
        existing.setUsername("anna");
        existing.setEmail("anna@htw-berlin.de");
        existing.setPassword("ALTER_HASH");
        existing.setRole(admin);

        UserRequestDTO updateRequest = new UserRequestDTO("anna", "", "anna.neu@htw-berlin.de", 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByUsernameAndIdNot("anna", 1L)).thenReturn(false);
        when(userRepository.existsByEmailAndIdNot("anna.neu@htw-berlin.de", 1L)).thenReturn(false);
        when(roleRepository.findById(2L)).thenReturn(Optional.of(admin));
        when(userRepository.save(existing)).thenReturn(existing);

        // Act
        UserResponseDTO response = userService.update(1L, updateRequest);

        // Assert
        assertThat(existing.getPassword()).isEqualTo("ALTER_HASH");
        assertThat(response.email()).isEqualTo("anna.neu@htw-berlin.de");
        verify(passwordEncoder, never()).encode(any());
    }
}
