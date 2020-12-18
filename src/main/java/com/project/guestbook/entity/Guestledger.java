package com.project.guestbook.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "guestledger")
public class Guestledger {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long entryId;
	    
		public void setEntryId(long entryId) {
			this.entryId = entryId;
		}
		
		@Column(name = "approval_Status")
	    private String approvalStatus;
	    
	    @OneToOne
	    @JoinColumn(name="user_id")
	    private User enteredby;
	    
	    @Column(name = "picByte", length = 1000)
		private byte[] picByte;
	    
	    public byte[] getPicByte() {
			return picByte;
		}


		public void setPicByte(byte[] picByte) {
			this.picByte = picByte;
		}

		@Column(name = "image",nullable = true, length = 64)
	    private String filename;
	    
	    private String photosImagePath;
	    
	    
	    @Transient
		public String getPhotosImagePath() {
	    	if (filename == null)return null;
	         
	        return "/user-photos/"+ filename;
		}


		public void setPhotosImagePath(String photosImagePath) {
			this.photosImagePath = photosImagePath;
		}


		public String getFilename() {
			return filename;
		}


		public void setFilename(String filename) {
			this.filename = filename;
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
