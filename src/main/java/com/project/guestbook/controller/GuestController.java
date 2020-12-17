package com.project.guestbook.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.project.guestbook.entity.Guestledger;
import com.project.guestbook.service.GuestService;


@Controller
public class GuestController {
	
	@Autowired
    private GuestService guestService;
	
	@PostMapping("/saveguestEntry")
    public String saveGuestEntry(@ModelAttribute("guestledger") Guestledger guestledger,@RequestParam("image") MultipartFile multipartFile)throws IOException {
        // save guest entry to database
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		guestledger.setFilename(fileName);
		guestService.saveGuestEntry(guestledger);
		String uploadDir = "user-photos/" + guestledger.getEntryId();
		 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        
        return "home";
    }
	
	/* @GetMapping("/showFormForApproval/{entryId}")
	    public String showFormForApproval(@PathVariable(value = "entryId") long entryId, Model model) {

	        // get Guests from the service
	    	Guestledger guestledger = guestService.getGuestById(entryId);
	    	
	    	//guestledger.setApprovalStatus("Approved");
	    	
	    	guestService.saveGuestEntry(guestledger);

	        // set Guest as a model attribute to pre-populate the form
	        model.addAttribute("guestledger", guestledger);
	        return "adminhome";
	    }*/
	 
	// display list of Guests
	    @GetMapping("/guest")
	    public String viewHomePage(Model model) {
	        model.addAttribute("listGuests", guestService.getAllGuestEntry());
	        return "approve_guest";
	    }
	
	    /*@GetMapping("/removeGuestEntry/{entryId}")
	    public String removeGuestEntry(@PathVariable(value = "entryId") long entryId) {

	        // call delete employee method 
	        this.guestService.removeGuestEntryById(entryId);
	        return "adminhome";
	    }*/
	    
	    @GetMapping("/showMyEntry")
	    public String showMyEntry(@ModelAttribute("guestledger") int userid,Model model) {
	    	
            List<Guestledger> guestledger= guestService.getbyEntereduser(userid);
	    	model.addAttribute("guestledger", guestledger);
	        return "home";
	    }
	

}
