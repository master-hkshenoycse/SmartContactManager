package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smart.entities.Contact;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	
	/*@GetMapping("/test")
	@ResponseBody
	public String test() {
		
		User user=new User();
		user.setName("Hari");
		user.setEmail("hkshenoy@gmail.com");
		
		Contact contact=new Contact();
		user.getContacts().add(contact);
		
		userRepository.save(user);
		
		return "working";
	}*/
	
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title","Home -Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About -Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register -Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	//Handler for registering User
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam(value="agreememt",defaultValue="false") boolean agreement,
			Model model,
			HttpSession session) {
		
		
		try {
			
			
			if(result.hasErrors()) {
				model.addAttribute("user",user);
				return "signup";
			}
			
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User savedResult=userRepository.save(user);
			System.out.println(savedResult);
			session.setAttribute("message", new Message("Sucefuly resgitered","alert-sucess"));
			model.addAttribute("user",savedResult);
			model.addAttribute("session",session);
			
			return "signup";
		}catch(Exception e) {
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("Something wrong "+e.getMessage(),"alert-error"));
			model.addAttribute("session",session);
			return "signup";
			
		}
		
		
		
		
		
	}
	
	@RequestMapping("/signin")
	public String signin(Model model) {
		model.addAttribute("title","Login  -Smart Contact Manager");
		return "signin";
	}
	

}
