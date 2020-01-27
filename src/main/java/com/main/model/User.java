package com.main.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

import com.main.dto.ChildDTO;
import com.main.dto.SimpleUserDTO;
import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.interfaces.IChild;
import com.main.model.interfaces.IEmployee;
import com.main.model.interfaces.IManagement;
import com.main.model.interfaces.IParent;
import com.main.model.interfaces.ITeacher;
import com.main.model.interfaces.IUser;

import lombok.Data;

@Entity
@Data
public class User implements IChild, IEmployee, IManagement, IParent, IUser, ITeacher {
    private static final long serialVersionUID = 1337L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Fullname is mandatory")
    private String fullname;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    private String email;
    private String phoneNumber;
    private String address;
    private String subject;
    private String iban;
    private String schoolClass;

    @ManyToMany(cascade = {CascadeType.ALL, CascadeType.MERGE})
    @JoinTable(name = "employee_school", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "school_id"))
    private List<School> employeesSchools = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL, CascadeType.MERGE})
    @JoinTable(name = "participatingSchool", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "school_id"))
    private List<School> schoolCoordinatorsSchools = new ArrayList<>();

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AfterSchoolCare> afterSchoolCares = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationToken verificationTokens;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "school_id")
    private School childSchool;

    public void setChildSchool(School school){
        if(school!= null) {
            school.addChild(this);
            this.childSchool = school;
        } else this.childSchool = null;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private User parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> children = new ArrayList<>();

    private boolean isSchoolCoordinator;

    @Column(name = "verified")
    private boolean verified;

    public void addRole(Role role) {
        this.roles.add(role);
    }

	public void addChild(User child) {this.children.add(child);}

	public void removeChild(User child) {this.children.remove(child);}



	// Constructur normal User
	User(String username, String password, String fullname) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
	}

    // Constructor Child
    User(String username, String password, String fullname, String schoolClass) {
        this(username, password, fullname);
        this.schoolClass = schoolClass;
    }

    // Constructer Parent
    User(String username, String password, String fullname, String email, String phoneNumber) {
        this(username, password, fullname);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Constructor Teacher and SchoolCoordinator
    User(String username, String password, String fullname, String email, String phoneNumber, String subject,
         boolean isSchoolCoordinator) {
        this(username, password, fullname, email, phoneNumber);
        this.subject = subject;
        this.isSchoolCoordinator = isSchoolCoordinator;

    }

    // Constructor Employee and SchoolCoordinator
    User(String username, String password, String fullname, String email, String phoneNumber, String address,
         String subject, String iban, boolean isSchoolCoordinator) {
        this(username, password, fullname, email, phoneNumber, address);
        this.subject = subject;
        this.iban = iban;
        this.isSchoolCoordinator = isSchoolCoordinator;
    }

    // Constructor Management
    User(String username, String password, String fullname, String email, String phoneNumber, String address) {
        this(username, password, fullname, email, phoneNumber);
        this.address = address;

    }

    public User() {
        username = null;
        password = null;
        fullname = null;
        verified = false;
    }

    /**
     * Eine Builder-Klasse, welche genutzt wird <br>
     * um Objekte von einer Klasse, die das Interface {@link IUser} implementiert,
     * <br>
     * zu erzeugen. <br>
     *
     * @param <T extends IUser>
     * @author Markus
     * @version 1.2
     * @since 18.10.2019
     */

    public static class UserBuilder<T extends IUser> {

        private User user;

        private UserBuilder() {
            user = new User();
        }

        public UserBuilder<T> withName(String name) {
            user.setUsername(name);
            return this;
        }

        public UserBuilder<T> withPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public UserBuilder<T> withFullname(String fullname) {
            user.setFullname(fullname);
            return this;
        }

        public UserBuilder<T> withRole(Role role) {
            role.addUser(user);
            user.addRole(role);
            return this;
        }

        public UserBuilder<T> withEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public UserBuilder<T> withPhoneNumber(String phoneNumber) {
            user.setPhoneNumber(phoneNumber);
            return this;
        }

        public UserBuilder<T> withAddress(String address) {
            user.setAddress(address);
            return this;
        }

        public UserBuilder<T> withSubject(String subject) {
            user.setSubject(subject);
            return this;
        }

        public UserBuilder<T> withIban(String iban) {
            user.setIban(iban);
            return this;
        }

        public UserBuilder<T> withSchoolClass(String schoolClass) {
            user.setSchoolClass(schoolClass);
            return this;
        }

        public UserBuilder<T> isSchoolCoordinator(boolean isSchoolCoordinator) {
            user.setSchoolCoordinator(isSchoolCoordinator);
            return this;
        }

        public UserBuilder<T> isVerified(boolean isEnabled) {
            user.setVerified(isEnabled);
            return this;
        }

        public UserBuilder<T> withUser(User user) {
            this.user = user;
            return this;
        }


		public UserBuilder<T> withDto(IUserDTO userDTO) {
			withName(userDTO.getUsername());
			withAddress(userDTO.getAddress());
			withEmail(userDTO.getEmail());
			withFullname(userDTO.getFullname());
			withIban(userDTO.getIban());
			withPassword(userDTO.getPassword());
			withSchoolClass(userDTO.getSchoolClass());
			withPhoneNumber(userDTO.getPhoneNumber());
			withSubject(userDTO.getSubject());
			return this;
		}

        @SuppressWarnings("unchecked")
        public T build() {
            return (T) user;
        }


        public IUserDTO toDto(String type) {
            IUserDTO dto = UserDTO.builder().build();
            dto.setAddress(user.getAddress());
            dto.setEmail(user.getEmail());
            dto.setFullname(user.getFullname());
            dto.setUsername(user.getUsername());
            dto.setIban(user.getIban());
            dto.setPassword(user.getPassword());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setSchoolClass(user.getSchoolClass());
            dto.setSubject(user.getSubject());
            if (user.getChildSchool() != null) {
                dto.setSchool(user.getChildSchool().getId());
            }
//			if (user.getRoles() != null) {
//				if (user.getRoles().size() != 0) {
//					dto.setUserType(user.getRoles().get(0).getName());
//				}
//			} else {
            dto.setUserType(type);
//			}
            dto.setSchoolCoordinator(user.isSchoolCoordinator());
            return dto;
        }

        public SimpleUserDTO toSimpleDto(String type) {
            SimpleUserDTO dto = new SimpleUserDTO();
            dto.setEmail(user.getEmail());
            dto.setFullname(user.getFullname());
            dto.setUsername(user.getUsername());
            dto.setSchoolClass(user.getSchoolClass());
//			if (user.getRoles() != null) {
//				if (user.getRoles().size() != 0) {
//					dto.setUserType(user.getRoles().get(0).getName());
//				}
//			} else {
            dto.setUserType(type);
//			}

            return dto;
        }

        /**
         * Gibt eine neue Instanz der Klasse {@link UserBuilder} zurück.
         *
         * @param <U extends IUser>
         * @return Neue Instanz
         */

        public static <U extends IUser> UserBuilder<U> next() {
            return new UserBuilder<>();
        }

    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + "]";
    }

}