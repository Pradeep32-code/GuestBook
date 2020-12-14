package com.project.guestbook.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "guestledger")
public class Guestledger {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long entryId;

	    @Column(name = "approval_Status")
	    private String approvalStatus;
	    
	    @OneToOne
	    @JoinColumn(name="user_id")
	    private User enteredby;
	    
	    @Lob 
	    @Basic(fetch = FetchType.LAZY)
	    @Column(name = "image",nullable = true, length = 64)
	    private String image;
	    
	    
	    public String getImage() {
			return image;
		}


		public void setImage(String image) {
			this.image = image;
		}


		@Column(name = "text",columnDefinition = "MEDIUMTEXT")
	    private String text;


		public Long getEntryId() {
			return entryId;
		}


		public void setEntryId(Long entryId) {
			this.entryId = entryId;
		}


		public String getApprovalStatus() {
			return approvalStatus;
		}


		public void setApprovalStatus(String approvalStatus) {
			this.approvalStatus = approvalStatus;
		}


		public User getEnteredby() {
			return enteredby;
		}


		public void setEnteredby(User enteredby) {
			this.enteredby = enteredby;
		}

		public String getText() {
			return text;
		}


		public void setText(String text) {
			this.text = text;
		}
	

}
