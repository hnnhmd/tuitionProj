package com.fdmgroup.TuitionProjectSpring;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Grade {
	@Id
	@SequenceGenerator(name="ug4", sequenceName="us4",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ug4")
	private long gradeId;
	private int month;
	private int score;
	
	@ManyToOne
	@JoinColumn(name= "FK_student")
	@Autowired
	private Student student;
	public Student getStudent() {
		return this.student;
	}
	
	@ManyToOne
	@JoinColumn(name="FK_tuitionClass")
	@Autowired
	private TuitionClass tuitionClass;
	public TuitionClass getTuitionClass() {
		return this.tuitionClass;
	}
	
	public Grade(Student student, TuitionClass tuitionClass, int month, int score) {
		this.student = student;
		this.tuitionClass = tuitionClass;
		this.month = month;
		this.score = score;
	}
	
	public Grade() {
		super();
	}
	
	public long getGradeId() {
		return this.gradeId;
	}
	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}
	public int getMonth() {
		return this.month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getScore() {
		return this.score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
