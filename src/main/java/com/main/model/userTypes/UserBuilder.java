package com.main.model.userTypes;

import com.main.dto.UserDTO;
import com.main.model.userTypes.interfaces.IUser;

/**
 * 
 * Eine Builder-Klasse, welche genutzt werden kann <br>
 * um Objekte von einer Klasse, die das Interface {@link IUser} implementiert, <br>
 * zu erzeugen. <br>
 * 
 * 
 * @author Markus
 * @since 18.10.2019
 * @version 1.0
 * @param <T extends IUser>
 */

public class UserBuilder<T extends IUser> {
	
	private User user;

	private UserBuilder() {
		user = new User();
	}
	
	public UserBuilder<T> withName(String name) {
		user.setUsername(name);
		return this;
	}
	
	public UserBuilder<T> withPassword(String password){
		user.setPassword(password);
		return this;
	}
	
	public UserBuilder<T> withFullname(String fullname){
		user.setFullname(fullname);
		return this;
	}
	
	public UserBuilder<T> withRole(String role){
		user.setRole(role);
		return this;
	}
	
	public UserBuilder<T> withEmail(String email){
		user.setEmail(email);
		return this;
	}
	
	public UserBuilder<T> withPhoneNumber(String phoneNumber){
		user.setPhoneNumber(phoneNumber);
		return this;
	}
	
	public UserBuilder<T> withAddress(String address){
		user.setAddress(address);
		return this;
	}
	
	public UserBuilder<T> withSubject(String subject){
		user.setSubject(subject);
		return this;
	}
	
	public UserBuilder<T> withIban(String iban) {
		user.setIban(iban);
		return this;
	}
	
	public UserBuilder<T> withSchoolClass(String schoolClass){
		user.setSchoolClass(schoolClass);
		return this;
	}
	
	public UserBuilder<T> isSchoolCoordinator(boolean isSchoolCoordinator){
		user.setSchoolCoordinator(isSchoolCoordinator);
		return this;
	}
	
	public UserBuilder<T> isEnabled(boolean isEnabled){
		user.setEnabled(isEnabled);
		return this;
	}
	
	public UserBuilder<T> withDto(UserDTO userDTO){
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
	

	public T build() {
		return (T) user;
	}
	
	public UserDTO toDto(String type) {
		UserDTO dto = new UserDTO();
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
	 * Gibt eine neue Instanz der Klasse
	 * {@link UserBuilder} zurück.
	 * 
	 * @param <U extends IUser>
	 * @return Neue Instanz
	 */
	
	public static<U extends IUser> UserBuilder<U> next() {
		return new UserBuilder<>();
	}

	
}
