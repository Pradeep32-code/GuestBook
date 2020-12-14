package com.project.guestbook.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.guestbook.entity.Guestledger;
import com.project.guestbook.repository.GuestRepository;
@Service
public class GuestService {
	@Autowired
	 private GuestRepository guestRepository;
	
	public void saveGuestEntry(Guestledger guestledger) {
        this.guestRepository.save(guestledger);
    }
	
    public List < Guestledger > getAllGuestEntry() {
        return guestRepository.findAll();
    }
    
    public Guestledger getGuestById(long entryId) {
        Optional < Guestledger > optional = guestRepository.findById(entryId);
        Guestledger guestledger = null;
        if (optional.isPresent()) {
        	guestledger = optional.get();
        } else {
            throw new RuntimeException(" Guest not found for id :: " + entryId);
        }
        return guestledger;
    }
  
    public void removeGuestEntryById(long entryId) {
        this.guestRepository.deleteById(entryId);
    }
    
    public List<Guestledger> getbyEntereduser(int userid) {
        return guestRepository.getbyEntereduser(userid);
    }

}
