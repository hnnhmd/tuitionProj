package com.fdmgroup.TuitionProjectSpring;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Student {
	@Id
	@SequenceGenerator(name="ug3", sequenceName="us3",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ug3")
	public long studentId;
	private String firstName;
	private String lastName;
	
	public Student() {
		super();
	}
	
	public Student(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
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
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="FK_parentId")
	@Autowired
	private Parent parent;
	public void setParent(Parent parent) {
		this.parent = parent;
	}
	public Parent getParent() {
		return parent;
	}
	
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "Student_Tuition",
			joinColumns = {@JoinColumn(name = "fk_student")},
			inverseJoinColumns = {@JoinColumn(name="fk_tuitionClass")})
	@Autowired
	private List<TuitionClass> tuitionClasses = new ArrayList<TuitionClass>();
	
	public void setTuitionClasses (TuitionClass...tuitionClasses) {
		for (TuitionClass i : tuitionClasses) {
//			System.out.println(i.getClassName());
			this.tuitionClasses.add(i);
		}
		for (TuitionClass i : this.tuitionClasses) {
//			System.out.println("......");
//			System.out.println(i.getClassName());
		}
	}
	public List<TuitionClass> getTuitionClasses() {
		return this.tuitionClasses;
	}
	
	@OneToMany(mappedBy = "student", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Autowired
	public List<Grade> grades = new ArrayList<Grade>();
	public List<Grade> getGrades() {
		return this.grades;
	}
}
