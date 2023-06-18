package com.fdmgroup.TuitionProjectSpring;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	Logger logger = LogManager.getLogger(UserController.class);
	
	public UserController() {
		
	}
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String goToIndexPage() {
		logger.trace("Go to home page");
		return "index";
	}
	
	@GetMapping("/register")
	public String goToRegisterPage() {
		logger.trace("Go to registration page");
		return "register";
	}
	
	@GetMapping("/login")
	public String goToLogInPage() {
		logger.trace("Go to login page");
		return "login";
	}
	
	@GetMapping("parent/addstudent")
	public String goToAddStudentsUnderParent(HttpSession session, Model model) {
		Parent parent = (Parent) session.getAttribute("current_parent");
		logger.trace("Go to add student page");
		model.addAttribute("parent",parent);
		return "addstudent";
	}
	
	@GetMapping("student/addtuitionclass")
	public String goToAddTuitionClassesPage(HttpSession session, Model model) {
		List<TuitionClass> tc = userService.listTuitionClass();
		Student student = (Student) session.getAttribute("current_student");
		model.addAttribute("student",student);
		model.addAttribute("tuitionclasses",tc);
		logger.trace("Go to add tuition class page");
		return "addtuitionclass";
	}
	
	@PostMapping("parent/addstudent")
	public String addStudentUnderParent(@RequestParam String firstName, @RequestParam String lastName, HttpSession session) {
		Parent parent = (Parent) session.getAttribute("current_parent");
		Parent parentDb = userService.findUser(parent.parentId).get();
		Student student = new Student(firstName, lastName);
		student.setParent(parentDb);
		logger.trace("Student: " + student.getFirstName() + " " + student.getLastName() + " will be added to " + parent.getFirstName() + " " + parent.getLastName() + "'s account");
		userService.registerNewStudent(student);
		logger.trace("transaction saved");
		String redir = "redirect:/parent/" + parent.getParentId();
		return redir;
	}
	
	@PostMapping("/register")
	public String registerUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam long contactNumber, @RequestParam long bankAccountNumber ,@RequestParam String username, @RequestParam String password, Model model) {
		if (userService.registerNewUser(new Parent(firstName, lastName, contactNumber, bankAccountNumber, username, password))) {
			logger.trace("transaction saved : " + firstName + " " + lastName + " is an official member now");
			return "redirect:/login";
		} else {
			model.addAttribute("error", true);
			logger.error("A used username has been attempted to be registered : " + username);
			return "register";
		}
	}
	
//	@PostMapping("/login")
//	public String logIn(@RequestParam String username, @RequestParam String password, HttpSession session) {
//		if (userService.getLogIn(username, password).equals("correct")) {
//			if (username.equals("admin")) {
//				return "admin";
//			} else {
//				Parent parent = userService.findByUserPostValidation(username);
//				session.setAttribute("current_parent", parent);
//				String redir = "redirect:/parent/" + parent.getParentId();
//				return redir;
//			}
//		} else {
//			return "login";
//		}
//	}
	
	@GetMapping("/intermediate")
	public String logIn1(Principal principal, HttpSession session) {
		String name = principal.getName();
		if(name.equals("admin")) {
			logger.trace("logging in to admin");
			return "admin";
		} else {
			Parent parentNonAdmin = userService.findByUserPostValidation(name);
			logger.trace("logging in to: " + parentNonAdmin.getFirstName() + " " + parentNonAdmin.getLastName());
			session.setAttribute("current_parent", parentNonAdmin);
			String redir = "redirect:/parent/" + parentNonAdmin.getParentId();
			return redir;
		}
	}
	
	@GetMapping("/errorlogin")
	public String noLogIn(Model model) {
		model.addAttribute("error", true);
		logger.error("Failed login!");
		return "login";
	}
	
	@PostMapping("/setclass")
	public String setClass(@RequestParam String className, @RequestParam String teacher, @RequestParam Integer tuitionFeeMonthly, Model model) {
		TuitionClass tc = new TuitionClass(className, teacher, tuitionFeeMonthly);
		if (userService.adminSetClass(tc)) {
			logger.trace("Class created: " + tc.getClassName());
			return "admin";
		} else {
			logger.error("Class already exists!");
			model.addAttribute("errora", true);
			return "admin";
		}
	}
	
	@PostMapping("/setgrade")
	public String setGrade(@RequestParam Long studentId, @RequestParam Long tuitionId, @RequestParam Integer month, @RequestParam Integer score) {
		Student student = userService.findStudent(studentId);
		TuitionClass tc = userService.findTuitionClass(tuitionId);
		Grade grade = new Grade(student, tc, month, score);
		userService.saveGrade(grade);
		logger.trace("Grade created: " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName() + " for: " + grade.getTuitionClass().getClassName());
		return "admin";
	}
	
	@GetMapping("/paybills")
	public String goToPayBillsPage(HttpSession session, Model model) {
		Parent parent = (Parent) session.getAttribute("current_parent");
		Parent parentDb = userService.findUser(parent.parentId).get();
		model.addAttribute("parent",parentDb);
		logger.trace("Going to pay bills page");
		return "paybills";
	}
	
	@PostMapping("/paybills")
	public String payBills(@RequestParam Integer payment, HttpSession session) {
		Parent parent = (Parent) session.getAttribute("current_parent");
		Parent parentDb = userService.findUser(parent.parentId).get();
		parentDb.payBill(payment);
		userService.saveParentBill(parentDb);
		logger.trace("Paying bills for " + parentDb.getFirstName() + " " + parentDb.getLastName() + " amount for: " + payment);
		String redir = "redirect:/parent/" + parentDb.parentId;
		return redir;
	}
	
	@GetMapping("/parent/{id}")
	public String getUser(@PathVariable("id") long parentId, Model model) {
		Optional<Parent> parentOpt = userService.findUser(parentId);
		if (parentOpt.isEmpty()) {
			return "redirect:/register";
		} else {
			model.addAttribute("parent",parentOpt.get());
			model.addAttribute("students",parentOpt.get().getStudents());
			return "profile";
		}
	}
	
	@GetMapping("/student/{id}")
	public String goStudentProfile(@PathVariable("id") long studentId, Model model, HttpSession session) {
		Student student = userService.findStudent(studentId);
		model.addAttribute("student",student);
		model.addAttribute("tuitionClasses",student.getTuitionClasses());
		session.setAttribute("current_student", student);
		logger.trace("Going to student profile: " + student.getFirstName() + " " + student.getLastName());
		return "studentprofile";
	}
	
	@GetMapping("/tuitionclass/{id}")
	public String addClassesToStudent(@PathVariable("id") long tuitionId, HttpSession session) {
		TuitionClass tc = userService.findTuitionClass(tuitionId);
		Student student = (Student) session.getAttribute("current_student");
		Parent parent = (Parent) session.getAttribute("current_parent");
		long studId = student.studentId;
		Parent parentDb = userService.findUser(parent.parentId).get();
		Student studentEdit = userService.findStudent(studId);
		studentEdit.setTuitionClasses(tc);
		logger.trace("Adding tuition: " + tc.getClassName() + " to student: " + studentEdit.getFirstName());
		userService.saveStudent(studentEdit);
		parentDb.setDueAmount(tc.getTuitionMonthlyFee());
		logger.trace("Hence, adding " + tc.getTuitionMonthlyFee() + " to Parent: " + parent.getFirstName() + " " + parent.getLastName());
		userService.saveParentBill(parentDb);
		String redir = "redirect:/student/" + student.studentId;
		return redir;
	}
	
	@GetMapping("/report/tuitionclass/{id}")
	public String viewGrades(@PathVariable("id") long tuitionId, HttpSession session, Model model) {
		Student student = (Student) session.getAttribute("current_student");
		logger.trace("Going to profile of: " + student.getFirstName() + " " + student.getLastName());
		List<Grade> studentGrades = new ArrayList<Grade>();
		for (Grade i : student.getGrades()) {
			if (i.getTuitionClass().tuitionId == tuitionId) {
				studentGrades.add(i);
			}
		}
		model.addAttribute("student",student);
		model.addAttribute("grades", studentGrades);
		return "studentreport";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		logger.trace("sayonara!");
		session.invalidate();
		return "index";
	}
	
	@PostMapping("/student/removestudent")
	public String removeStudent(HttpSession session) {
		Student student = (Student)session.getAttribute("current_student");
		Student studentDb = userService.findStudent(student.studentId);
		Parent parent = (Parent)session.getAttribute("current_parent");
		Parent parentDb = userService.findUser(parent.parentId).get();
//		System.out.println(parentDb.getStudents().size());
//		for (Student i : parentDb.getStudents()) {
//			System.out.println(i.getFirstName());
//		}
		userService.removeStudent(studentDb);
		logger.trace("removing student: " + studentDb.getFirstName());
		if (parentDb.removeStudent(studentDb)) {
			for (Student i : parentDb.getStudents()) {
				System.out.println(i.getFirstName());
			}
		} else {
			System.out.println("not removed");
		}
		for (Student i : parentDb.getStudents()) {
			System.out.println(i.getFirstName());
		}
		userService.saveParentBill(parentDb);
		System.out.println(parentDb.getStudents().size());
		String redir = "redirect:/parent/" + parent.parentId;
		return redir;
	}
	
	@PostMapping("/parent/revoke")
	public String removeMembership(HttpSession session, Model model) {
		Parent parent = (Parent)session.getAttribute("current_parent");
		Parent parentDb = userService.findUser(parent.parentId).get();
		String redirdef = "redirect:/parent/" + parent.parentId;
		model.addAttribute("parent",parentDb);
		model.addAttribute("students", parentDb.getStudents());
		if (parentDb.getStudents().size()==0) {
			if (parentDb.getDueAmount()==0) {
				userService.removeParent(parentDb);
				logger.trace("User removed, see you next time!");
				String redir = "redirect:/logout";
				return redir;
			} else {
				logger.error("Still owe money!");
				model.addAttribute("errora", true);
				return "profile";
			}
		} else {
			logger.error("Still have students under you!");
			model.addAttribute("error", true);
			return "profile";
		}
	}
	
	
}
