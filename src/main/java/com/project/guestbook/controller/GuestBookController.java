package com.project.guestbook.controller;

import java.io.IOException;
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

/**
* <h1>Guest Book Application!</h1>
* Guest Book Application consists of 2 types of users.
* Guest and Administrator.
* Guests
User needs to login in order to write a new entry in the guestbook
Guestbook entry can be either a single image or a text

*Administrator
View all the entries posted by all the users
Approve the entries 
Remove the entries
Edit the entries

* @author  Pradeep Kumar
* @version 1.0
* @since   2020-12-14
*/

@Controller
public class GuestBookController {

    @Autowired
    private UserService userService;
    
    @Autowired
    GuestRepository guestRepository;
    
    @Autowired
    GuestService guestService;
    
    /**
     * This method is used to view login page. 
     * @return ModelView Object This returns login page.
     */
    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    /**
     * This method is used to view registration page. 
     * @return ModelView Object This returns registration page.
     */
    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }
    
    /**
     * This method is used to register on Guest Book Application.
     * @param User Object.This is the first paramter to createNewUser method
     * @param BindingResult Object.This is the second paramter to createNewUser method 
     * @return ModelView Object This returns registration page.
     */
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
    
    /**
     * This method is used to Show Admin Page on Guest Book Application. 
     * @return ModelView Object This will show admin home page.
     */
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
    
    /**
     * This method is used to Show Home Page on Guest Book Application. 
     * @return ModelView Object This will show User logged in home page.
     */
    @RequestMapping(value="/welcome", method = RequestMethod.GET)
    public ModelAndView welcome(@RequestParam(required = false) String modifyentry){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("isAdmin",false);
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
        	modelAndView.addObject("isAdmin",true);
        }
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Logged in User Content");
        modelAndView.addObject("modifyentry",modifyentry);
        modelAndView.addObject("invites",guestRepository.findAll());
        modelAndView.setViewName("welcome");
        return modelAndView;
    }
    
    /**
     * This method is used to Provide Guest Entry on Guest Book Application and save in DB and FileSystem. 
     * @param Guestledger Object.This is the first paramter to createinvite method
     * @param MultipartFile Object.This is the second paramter to createinvite method
     * @return ModelView Object This will show User logged in home page.
     * @exception IOException On input error.
     * @see IOException
     */
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
        
        boolean isFileAvailable=false;
       if(multipartFile!=null && !multipartFile.isEmpty()){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        guestledger.setFilename(fileName);
        guestledger.setPicByte(multipartFile.getBytes());
        isFileAvailable=true;
       }
        guestRepository.save(guestledger);
        if(isFileAvailable){
        String uploadDir = "user-photos/";
        
        FileUploadUtil.saveFile(uploadDir,guestledger.getFilename(),multipartFile);
        }
        
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Logged in User Content");
        modelAndView.addObject("successMessage","GuestEntry Done");
        modelAndView.addObject("invites",guestRepository.findAll());
        modelAndView.setViewName("welcome");
        return modelAndView;
    }
    
    /**
     * This method is used to Approve Guest Entry By Admin on Guest Book Application. 
     * @param String.This is the first paramter to ApproveInvitation method
     * @return Nothing.
     */
    @RequestMapping(value="/approve/{invite}", method = RequestMethod.GET)
    @ResponseBody
    public void ApproveInvitation(@PathVariable("invite") String invite){
       long inviteid=Long.parseLong(invite); 
       Guestledger guestledger = guestService.getGuestById(inviteid);
    	
    	guestledger.setApprovalStatus("Approved");
    	
    	guestService.saveGuestEntry(guestledger);
    	
    }
    /**
     * This method is used to Remove Guest Entry By Admin on Guest Book Application. 
     * @param String.This is the first paramter to removeInvitation method
     * @return Nothing.
     */
    @RequestMapping(value="/remove/{entryId}",method=RequestMethod.DELETE)
    @ResponseBody
	public void removeInvitation(@PathVariable String entryId) {
    	long inviteid=Long.parseLong(entryId); 
    	guestService.removeGuestEntryById(inviteid);
    		
	}
        
}