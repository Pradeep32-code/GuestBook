package com.project.guestbook.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.guestbook.entity.User;
import com.project.guestbook.service.UserService;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping(value={"/adminlogin"})
    public ModelAndView adminlogin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminlogin");
        return modelAndView;
    }
    
    @GetMapping(value={"/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @GetMapping(value="/registrationuser")
    public ModelAndView registrationUser(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registrationuser");
        return modelAndView;
    }
    
    @GetMapping(value="/registrationadmin")
    public ModelAndView registrationAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registrationadmin");
        return modelAndView;
    }

    @PostMapping(value = "/registrationadmin")
    public ModelAndView createNewAdmin(@Valid User user,BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registrationadmin");
        } else {
            
            userService.saveUserAdmin(user);
            
            modelAndView.addObject("successMessage", "Admin has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registrationadmin");

        }
        return modelAndView;
    }
    
    @PostMapping(value = "/registrationuser")
    public ModelAndView createNewUser(@Valid User user,BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registrationuser");
        } else {
            
            userService.saveUserRegular(user);
            
            modelAndView.addObject("successMessage", "Guest User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registrationuser");

        }
        return modelAndView;
    }    
    @GetMapping(value="/admin/home")
    public ModelAndView homeAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/adminhome");
        return modelAndView;
    }
    
    @GetMapping(value="/user/home")
    public ModelAndView homeUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userid",user.getId());
        modelAndView.addObject("userMessage","Content Available Only for Users with User Role");
        modelAndView.setViewName("user/home");
        return modelAndView;
    }
    
    


}
