package com.main.model.userTypes;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.School;
import com.main.model.userTypes.interfaces.IChild;
import com.main.model.userTypes.interfaces.IEmployee;
import com.main.model.userTypes.interfaces.IManagement;
import com.main.model.userTypes.interfaces.IParent;
import com.main.model.userTypes.interfaces.ITeacher;
import com.main.model.userTypes.interfaces.IUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class User implements UserDetails, IChild, IEmployee, IManagement, IParent, IUser, ITeacher {
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

	@NotBlank(message = "Role is mandatory")
	private String role;
	private String email;
	private String phoneNumber;
	private String address;
	private String subject;
	private String iban;
	private String schoolClass;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "employee_school", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "school_id"))
	private List<School> employeesSchools = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "schoolCoordinator_school", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "school_id"))
	private List<School> schoolCoordinatorsSchools = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id")
	private School childSchool;

	private boolean isSchoolCoordinator;

	@Column(name = "enabled")
	private boolean enabled;

	// Constructur normal User
	User(String username, String password, String fullname) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		role = "USER";
		enabled = true;
	}

	// Constructor Child
	User(String username, String password, String fullname, String schoolClass) {
		this(username, password, fullname);
		this.schoolClass = schoolClass;
		role = "CHILD";
		enabled = true;
	}

	// Constructer Parent
	User(String username, String password, String fullname, String email, String phoneNumber) {
		this(username, password, fullname);
		this.email = email;
		this.phoneNumber = phoneNumber;
		role = "PARENT";
		enabled = false;
	}

	// Constructor Teacher and SchoolCoordinator
	User(String username, String password, String fullname, String email, String phoneNumber, String subject,
			boolean isSchoolCoordinator) {
		this(username, password, fullname, email, phoneNumber);
		this.subject = subject;
		role = "TEACHER";
		this.isSchoolCoordinator = isSchoolCoordinator;
		enabled = false;
	}

	// Constructor Employee and SchoolCoordinator
	User(String username, String password, String fullname, String email, String phoneNumber, String address,
			String subject, String iban, boolean isSchoolCoordinator) {
		this(username, password, fullname, email, phoneNumber, address);
		this.subject = subject;
		this.iban = iban;
		this.isSchoolCoordinator = isSchoolCoordinator;
		role = "EMPLOYEE";
		enabled = false;
	}

	// Constructor Management
	User(String username, String password, String fullname, String email, String phoneNumber, String address) {
		this(username, password, fullname, email, phoneNumber);
		this.address = address;
		role = "MANAGEMENT";
		enabled = true;
	}

	User() {
		username = null;
		password = null;
		fullname = null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (isSchoolCoordinator)
			return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role),
					new SimpleGrantedAuthority("ROLE_SCHOOLCOORDINATOR"));
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 
	 * Eine Builder-Klasse, welche genutzt wird <br>
	 * um Objekte von einer Klasse, die das Interface {@link IUser} implementiert,
	 * <br>
	 * zu erzeugen. <br>
	 * 
	 * 
	 * @author Markus
	 * @since 18.10.2019
	 * @version 1.2
	 * @param <T extends IUser>
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

		public UserBuilder<T> withRole(String role) {
			user.setRole(role);
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

		public UserBuilder<T> isEnabled(boolean isEnabled) {
			user.setEnabled(isEnabled);
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
			return withSubject(userDTO.getSubject());
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
			dto.setUserType(type);
			dto.setSchoolCoordinator(user.isSchoolCoordinator());
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

}