package com.project.guestbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.guestbook.entity.Guestledger;

/**
 * This is Repository class is used to save Guest Entry in database.
 */

@Repository
public interface GuestRepository extends JpaRepository<Guestledger, Long> {
	
	@Query(value="select * from Guestledger where user_id=?1", 
			  nativeQuery = true)
	List<Guestledger> getbyEntereduser(int enteredby);
	
	//List<Guestledger> findbyUserId(long enteredby);
}