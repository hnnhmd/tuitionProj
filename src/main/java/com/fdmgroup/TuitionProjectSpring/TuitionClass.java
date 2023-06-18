package com.fdmgroup.TuitionProjectSpring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class TuitionClass {
	@Id
	@SequenceGenerator(name="ug2", sequenceName="us2",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ug2")
	public long tuitionId;
	private String className;
	private String teacher;
	private Integer tuitionMonthlyFee;
	
	@ManyToMany(mappedBy="tuitionClasses", fetch=FetchType.EAGER)
	@Autowired
	private List<Student> students = new ArrayList<Student>();
	public void setStudents (Student...students) {
		for (Student i : students) {
			this.students.add(i);
		}
	}
	public List<Student> getStudents() {
		return this.students;
	}
	
	@OneToMany(mappedBy="tuitionClass", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Autowired
	public List<Grade> grades = new ArrayList<Grade>();
	public List<Grade> getGrades() {
		return this.grades;
	}
	
	public TuitionClass() {
		super();
	}
	
	public TuitionClass(String className, String teacher, Integer tuitionMonthlyFee) {
		this.setClassName(className);
		this.setTeacher(teacher);
		this.setTuitionMonthlyFee(tuitionMonthlyFee);
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTeacher() {
		return this.teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public Integer getTuitionMonthlyFee() {
		return this.tuitionMonthlyFee;
	}

	public void setTuitionMonthlyFee(Integer tuitionMonthlyFee) {
		this.tuitionMonthlyFee = tuitionMonthlyFee;
	}

}
