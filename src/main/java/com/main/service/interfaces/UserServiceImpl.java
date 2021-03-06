package com.main.service.interfaces;

import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.School;
import com.main.model.User;
import com.main.model.VerificationToken;
import com.main.model.interfaces.IContactDetails;
import com.main.model.interfaces.IUser;
import com.main.model.interfaces.IVerificationToken;
import com.main.model.user.UserRole;
import com.main.repository.RoleRepository;
import com.main.repository.UserRepository;
import com.main.repository.VerificationTokenRepository;
import com.main.service.implementations.SchoolService;
import com.main.service.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository verificationTokenRepository;
    private RoleRepository roleRepository;
    private SchoolService schoolService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           VerificationTokenRepository verificationTokenRepository, RoleRepository roleRepository, SchoolService schoolService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.schoolService = schoolService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    private ResponseEntity<String> saveUser(@Valid User user) {
        User newUser = userRepository.save(user);
        return new ResponseEntity<String>("Created", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> save(IUserDTO userDTO, Authentication auth) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Role role = null;
        List<String> roles = new ArrayList<>();
        if (auth != null) {
            auth.getAuthorities().forEach((a) -> {
                roles.add(a.getAuthority());
            });
        } else {
            if (userDTO.getUserType().equals("PARENT")) {
                role = roleRepository.findByName("ROLE_PARENT");
                return saveUser(User.UserBuilder.<User>next().withDto(userDTO).withRole(role).build());
            }
        }

        if (userRepository.findByUsername(userDTO.getUsername()) != null)
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);

        role = roleRepository.findByName(UserRole.byRole(userDTO.getUserType().toString()).toString());

        if (roles.contains("ROLE_MANAGEMENT")) {
            User user = User.UserBuilder.<User>next().withDto(userDTO).withRole(role).build();
            user.setVerified(true);
            return saveUser(user);
        } else if (roles.contains("ROLE_PARENT") && userDTO.getUserType().equals("CHILD")) {
            return saveUser(User.UserBuilder.<User>next().withDto(userDTO).withRole(role).build());
        } else if (roles.contains("ROLE_SCHOOLCOORDINATOR")
                && (userDTO.getUserType().equals("USER") || userDTO.getUserType().equals("TEACHER"))) {
            return saveUser(User.UserBuilder.<User>next().withDto(userDTO).withRole(role).build());
        }

        return new ResponseEntity<>("Role not valid", HttpStatus.CONFLICT);
    }

    public User update(User user) {
        return this.update(user, null);
    }

    @Override
    public User update(User user, UserRole roleName) {
        if (roleName != null) {
            Role role = roleRepository.findByName(roleName.toString());
            if (role != null) {
                user.addRole(role);
            }
        }
        return userRepository.save(user);

    }

    @Override
    public IUser createAccount(UserDTO userDTO, Authentication auth) {
        ResponseEntity<String> response = save(userDTO, auth);
        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            Role role = roleRepository.findByName("ROLE_NEW_USER");
            IUser iuser = findByEmail(userDTO.getEmail());
            if (iuser != null) {
                User user = (User) iuser;
                user.addRole(role);
                if (userDTO.getSchool() != null) {
                    School childSchool = schoolService.findOne(userDTO.getSchool());
                    if (childSchool != null)
                        user.setChildSchool(childSchool);
                }
                update(user);
            }
            return iuser;
        }
        return null;
    }

    @Override
    public IVerificationToken createVerificationToken(User user, String token) {
        return verificationTokenRepository.save(new VerificationToken(token, user));

    }

    @Override
    public IUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean emailExist(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public void deleteUser(User user) {
        user.setChildSchool(null);
        user.setParent(null);


        user.setAfterSchoolCares(null);
        user.setAttendances(null);
        user.setEmployeesSchools(null);

        user.getParent().removeChild(user);

        userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public IUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllByVerified(boolean verified) {
        return userRepository.findAllByVerified(verified);
    }

    @Override
    public UserDTO toDto(IUser user) {
        return UserDTO.builder().fullname(user.getFullname()).username(user.getUsername()).email(((IContactDetails) user).getEmail()).build();
    }

    public User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user id not found: " + id));
    }

    @Override
    public String changePassword(IUser user) {
        String newPassword = UUID.randomUUID().toString();
        user.setPassword(passwordEncoder.encode(newPassword));
        update((User) user);
        return newPassword;
    }

    @Override
    public void deleteByName(String username) {
        User user = (User) findByUsername(username);
        userRepository.delete(user);
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.delete(findOne(id));
    }

    @Override
    public void changePassword(IUser user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save((User)user);

    }

    @Override
    public void changeEmail(IUser iUser, String email) {
        User user = (User) iUser;
        user.setEmail(email);
        update(user);
    }

    @Override
    public void changeFullname(IUser iUser, String fullname) {
        User user = (User) iUser;
        user.setFullname(fullname);
        update(user);
    }
}
