package engine.Service;

import engine.DAO.UserRepository;
import engine.DTO.UserDTO;
import engine.Model.User;
import engine.security.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    //Should throw an UserAlreadyExistsException
    public User registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if(emailExists(userDTO.getEmail())) {
            System.err.println("hello");
            throw new UserAlreadyExistsException();
        }
        System.err.println("hello userService email dont exists");

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAuthorities(Arrays.asList("ROLE_USER"));
        return userRepository.save(user);
    }
    public void deleteAll() {
        userRepository.deleteAll();
    }
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    public User finUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
}
