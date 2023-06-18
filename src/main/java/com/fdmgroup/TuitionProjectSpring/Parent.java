package com.fdmgroup.TuitionProjectSpring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
@Component
public class Parent{
	@Id
	@SequenceGenerator(name="ug1", sequenceName="us1",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ug1")
	public long parentId;
	private String firstName;
	private String lastName;
	private long contactNumber;
	private long bankAccountNumber;
	private String username;
	private String password;
	private Integer dueAmount = 0;
	
	@OneToMany (mappedBy = "parent", fetch=FetchType.EAGER)
	
	private List<Student> students = new ArrayList<Student>();
	public List<Student> getStudents() {
		return this.students;
	}
	
	public void addStudents(Student student) {
		this.students.add(student);
	}
	
	public boolean removeStudent(Student student) {
		return this.students.remove(student);
	}
	
	public Parent() {
		super();
	}
	
	public Parent(String firstName, String lastName, long contactNumber, long bankAccountNumber, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNumber = contactNumber;
		this.bankAccountNumber = bankAccountNumber;
		this.username = username;
		this.password = password;
	}
	
	public long getParentId() {
		return this.parentId;
	}
	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public long getContactNumber() {
		return this.contactNumber;
	}
	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}
	public long getBankAccountNumber() {
		return this.bankAccountNumber;
	}
	public void setBankAccountNumber(long bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getDueAmount() {
		return this.dueAmount;
	}

	public void setDueAmount(Integer dueAmount) {
		this.dueAmount = this.dueAmount + dueAmount;
	}
	
	public void payBill(Integer amount) {
		this.dueAmount = this.dueAmount - amount;
	}

}
