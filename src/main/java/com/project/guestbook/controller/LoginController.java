package com.project.guestbook.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.project.guestbook.entity.Guestledger;
import com.project.guestbook.entity.User;
import com.project.guestbook.repository.GuestRepository;
import com.project.guestbook.service.GuestService;
import com.project.guestbook.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    
    @Autowired
    GuestRepository guestRepository;
    
    @Autowired
    GuestService guestService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "Guest User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.addObject("invites",guestRepository.findAll());
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
    
    @RequestMapping(value="/welcome", method = RequestMethod.GET)
    public ModelAndView welcome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("isAdmin",false);
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
        	modelAndView.addObject("isAdmin",true);
        }
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Logged in User Content");
        modelAndView.addObject("invites",guestRepository.findAll());
        modelAndView.setViewName("welcome");
        return modelAndView;
    }
    
    @RequestMapping(value="/welcome", method = RequestMethod.POST)
    public ModelAndView createinvite(Guestledger guestledger,@RequestParam("image") MultipartFile multipartFile) throws IOException{
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("isAdmin",false);
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
        	modelAndView.addObject("isAdmin",true);
        }
        User user = userService.findUserByUserName(auth.getName());
        guestledger.setEnteredby(user);
        guestledger.setApprovalStatus("Pending");
     
        //file upload
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        guestledger.setFilename(fileName);
        guestledger.setPicByte(multipartFile.getBytes());
        Guestledger guestledger1=guestRepository.save(guestledger);
        String uploadDir = "user-photos/";
        FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Logged in User Content");
        modelAndView.addObject("successMessage","Invite Created");
        modelAndView.addObject("invites",guestRepository.findAll());
        modelAndView.setViewName("welcome");
        return modelAndView;
    }
    
    @RequestMapping(value="/approve/{invite}", method = RequestMethod.GET)
    @ResponseBody
    public void ApproveInvitation(@PathVariable("invite") String invite){
       long inviteid=Long.parseLong(invite); 
       Guestledger guestledger = guestService.getGuestById(inviteid);
    	
    	guestledger.setApprovalStatus("Approved");
    	
    	guestService.saveGuestEntry(guestledger);
    	
    }
    
    
    
    @RequestMapping(value="/remove/{entryId}",method=RequestMethod.DELETE)
    @ResponseBody
	public void removeInvitation(@PathVariable String entryId) {
    	long inviteid=Long.parseLong(entryId); 
    	guestService.removeGuestEntryById(inviteid);
    		
	}
        
}