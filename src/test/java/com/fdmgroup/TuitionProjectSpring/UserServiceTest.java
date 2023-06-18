package com.fdmgroup.TuitionProjectSpring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class UserServiceTest {
	@Autowired
	UserService userService;
	
	@MockBean
	GradeRepository gradeRepo;
	@MockBean
	StudentRepository studentRepo;
	@MockBean
	TuitionRepository tuitionRepo;
	@MockBean
	UserRepository userRepo;
	@MockBean
	BCryptPasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test_saveGrade() {
		Grade grade = new Grade();
		userService.saveGrade(grade);
		verify(gradeRepo).save(grade);
	}
	
	@Test
	void test_registerUser_noParent() {
		Parent parent = new Parent();
		parent.setUsername("yojamba");
		parent.setPassword("igothoes");
		Optional<Parent> nullParent = Optional.ofNullable(null);
		when(userRepo.findByUsername("yojamba")).thenReturn(nullParent);
		userService.registerNewUser(parent);
		verify(userRepo).findByUsername("yojamba");
		verify(passwordEncoder).encode("igothoes");
		verify(userRepo).save(parent);
	}
	
	@Test
	void test_registerUser_existingParent() {
		Parent parent = new Parent();
		parent.setUsername("yojamba");
		parent.setPassword("igothoes");
		Optional<Parent> nullParent = Optional.ofNullable(parent);
		when(userRepo.findByUsername("yojamba")).thenReturn(nullParent);
		userService.registerNewUser(parent);
		verify(userRepo).findByUsername("yojamba");
	}
	
	@Test
	void test_login_noAccount() {
		Optional<Parent> nullParent = Optional.ofNullable(null);
		when(userRepo.findByUsername("test")).thenReturn(nullParent);
		String noAccount = userService.getLogIn("test", null);
		assertEquals(noAccount, "account does not exist");
	}
	
	@Test
	void test_login_wrongPw() {
		Parent parent = new Parent();
		parent.setUsername("test");
		parent.setPassword("yeap");
		Optional<Parent> parentOpt = Optional.ofNullable(parent);
		when(userRepo.findByUsername("test")).thenReturn(parentOpt);
		String wrongPw = userService.getLogIn("test", "nope");
		assertEquals(wrongPw, "password is wrong");
	}
	
	@Test
	void test_login_correctPw() {
		Parent parent = new Parent();
		parent.setUsername("test");
		parent.setPassword("yeap");
		Optional<Parent> parentOpt = Optional.ofNullable(parent);
		when(userRepo.findByUsername("test")).thenReturn(parentOpt);
		String wrongPw = userService.getLogIn("test", "yeap");
		assertEquals(wrongPw, "correct");
	}
	

}
