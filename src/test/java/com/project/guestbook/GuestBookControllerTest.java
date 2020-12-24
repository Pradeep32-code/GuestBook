package com.project.guestbook;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.project.guestbook.controller.GuestBookController;
import com.project.guestbook.entity.Guestledger;
import com.project.guestbook.entity.Role;
import com.project.guestbook.entity.User;
import com.project.guestbook.repository.GuestRepository;
import com.project.guestbook.service.GuestService;
import com.project.guestbook.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GuestBookControllerTest {
	
	private MockMvc mockMvc;
	 
	@MockBean
    private GuestService guestServiceMock;
    
    @Autowired
	private GuestBookController guestBookController;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    GuestRepository guestRepository;
    
    String username="Pradeep";

    @Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(guestBookController).build();
	}
    
    @Test
	public void registrationcheck() throws Exception {
    	ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        mockMvc.perform(post("/registration"))
        .andExpect(status().isOk())
        .andExpect(view().name("registration"));
	}
    
    @Test
	public void logincheck() throws Exception {
    	ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        mockMvc.perform(post("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"));
	}
    
    @Test
	public void inviteCreationOk() throws Exception {
    	Guestledger guestledgerEntity = this.sampleGuestledgerEntity();
    	User user = this.sampleUserEntity();
    	MultipartFile multipartFile=null;
    	Mockito.when(userService.findUserByUserName(username)).thenReturn(user);
    	guestledgerEntity.setEnteredby(user);
    	guestledgerEntity.setApprovalStatus("Pending");
    	String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
    	guestledgerEntity.setFilename(fileName);
    	guestledgerEntity.setPicByte(multipartFile.getBytes());
    	guestRepository.save(guestledgerEntity);
    	
    	mockMvc.perform(post("/welcome"))
        .andExpect(status().isOk())
        .andExpect(view().name("welcome"));
	}
    
    @Test
   	public void ApproveEntrycheck() throws Exception {
    	long inviteid=50L;
        Guestledger guestledger = guestServiceMock.getGuestById(inviteid);
     	
     	guestledger.setApprovalStatus("Approved");
     	
     	guestServiceMock.saveGuestEntry(guestledger);
     	
   	}
    @Test
   	public void EditEntrycheck() throws Exception {
    	long inviteid=12L;
    	ModelAndView mav = new ModelAndView("edit_guest");
    	guestServiceMock.getGuestById(inviteid);
    	mockMvc.perform(post("/edit/{entryId}"))
        .andExpect(status().isOk())
        .andExpect(view().name("welcome"));
   	}
    
    @Test
   	public void deleteEntrycheck() throws Exception {
    	long inviteid=12L;
    	 
    	guestServiceMock.removeGuestEntryById(inviteid);
   	}
 
    
    public Role sampleRoleEntity() {
    	Role roleEntity = new Role();
    	roleEntity.setId(1);
    	roleEntity.setRole("Admin");
    	roleEntity.setId(2);
    	roleEntity.setRole("USER");
		return roleEntity;
	}
    
    public User sampleUserEntity() {
    	User userEntity = new User();
    	userEntity.setUserName("PK00580943");
    	userEntity.setActive(true);
    	userEntity.setEmail("PK00580943@techmahindra.com");
    	userEntity.setId(57);
    	userEntity.setLastName("Kumar");
    	userEntity.setName("Pradeep");
    	userEntity.setPassword("Admin@123");
		return userEntity;
	}
    public Guestledger sampleGuestledgerEntity() {
    	Guestledger guestledgerEntity = new Guestledger();
    	User usr=new User();
    	usr.setUserName("Pradeep");
    	guestledgerEntity.setApprovalStatus("Approved");
    	guestledgerEntity.setEnteredby(usr);
		
		return guestledgerEntity;
	}
    @After
	public void tearDown() throws Exception {
	}

}
