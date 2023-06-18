package com.fdmgroup.TuitionProjectSpring;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private TuitionRepository tcRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private GradeRepository gradeRepo;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
//	UserService (StudentRepository studentRepo, UserRepository userRepo) {
//		this.studentRepo = studentRepo;
//		this.userRepo = userRepo;
//	}
	
	UserService() {
	}
	
	public boolean registerNewUser(Parent parent) {
		String parentUserName = parent.getUsername();
		Optional<Parent> parentOpt = userRepo.findByUsername(parentUserName);
		if (parentOpt.isEmpty()) {
			String pw = parent.getPassword();
			String encodedPw = passwordEncoder.encode(pw);
			parent.setPassword(encodedPw);
			userRepo.save(parent);
			return true;
		} else {
			return false;
		}	
	}
	
	public void removeStudent(Student student) {
		studentRepo.delete(student);
	}
	
	public void removeParent(Parent parent) {
		userRepo.delete(parent);
	}
	
	public void registerNewStudent(Student student) {
		studentRepo.save(student);
	}
	
	public void saveStudent(Student student) {
		studentRepo.save(student);
	}
	
	public void saveGrade(Grade grade) {
		gradeRepo.save(grade);
	}
	
	public void saveParentBill(Parent parent) {
		userRepo.save(parent);
	}
	
	public void saveParent(Parent parent) {
		userRepo.save(parent);
	}
	
	public String getLogIn(String username, String password) {
		Optional<Parent> parentOpt = userRepo.findByUsername(username);
		if (parentOpt.isEmpty()) {
			return "account does not exist";
		} else {
			if (parentOpt.get().getPassword().equals(password)) {
				return "correct";
			} else {
				return "password is wrong";
			}
		}
	}
	
	public Parent findByUserPostValidation (String username) {
		Optional<Parent> parentOpt = userRepo.findByUsername(username);
		Parent parent = parentOpt.get();
		return parent;
	}
	
	public boolean adminSetClass(TuitionClass tuitionClass) {
		Optional<TuitionClass> tcOpt = tcRepo.findByClassName(tuitionClass.getClassName());
		if (tcOpt.isEmpty()) {
			tcRepo.save(tuitionClass);
			return true;
		} else {
			return false;
		}
	}
	
	public List<TuitionClass> listTuitionClass() {
		return tcRepo.findAll();
	}
	
	
	public TuitionClass findTuitionClass(long id) {
		TuitionClass tc = tcRepo.findById(id).get();
		return tc;
	}
	
	public Optional<Parent> findUser(long id) {
		Optional<Parent> parentOpt = userRepo.findById(id);
		return parentOpt;
	}
	
	public Student findStudent(long id) {
		Optional<Student> student = studentRepo.findById(id);
		return student.get();
	}
	
	
}
