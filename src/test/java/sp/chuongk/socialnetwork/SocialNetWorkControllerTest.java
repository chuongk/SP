package sp.chuongk.socialnetwork;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import sp.chuongk.socialnetwork.request.DirectRequest;
import sp.chuongk.socialnetwork.request.EmailRequest;
import sp.chuongk.socialnetwork.request.FriendListRequest;
import sp.chuongk.socialnetwork.request.UpdateRequest;
import sp.chuongk.socialnetwork.response.ConnectionResponse;
import sp.chuongk.socialnetwork.response.FriendResponse;
import sp.chuongk.socialnetwork.response.SuccessResponse;
import sp.chuongk.socialnetwork.response.UpdateSuccessResponse;
import sp.chuongk.socialnetwork.service.PersonService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SocialNetWorkControllerTest {
	
	private static final String ADD_CONNECTION = "/connection";
	private static final String GET_FRIEND_LIST = "/email";
	private static final String GET_COMMON_FRIEND = "/connection/common";
	private static final String SUBSCRIBE = "/email/subscribe";
	private static final String BLOCK = "/email/block";
	private static final String UPDATE = "/email/update";
	
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
	PersonService personService;
    
    static final Gson gson = new Gson();
    
    @Test
    public void testFlow() throws Exception {
    	MvcResult result = this.mockMvc.perform(get("/health")).andExpect(status().isOk())
        .andReturn();
    	assertEquals(result.getResponse().getContentAsString(), "ok");
    	
    	// 1: Setup friend request
    	String andyEmail = "andy@test.co";
    	String bobEmail = "bob@test.co";
    	FriendListRequest friendRequest = new FriendListRequest();
    	friendRequest.setFriends(Arrays.asList(andyEmail, bobEmail));
    	String json = gson.toJson(friendRequest);
    	
    	result = this.mockMvc.perform(post(ADD_CONNECTION).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	
    	SuccessResponse response = gson.fromJson(result.getResponse().getContentAsString(), SuccessResponse.class);
    	assertTrue(response.isSuccess());
    	
    	result = this.mockMvc.perform(post(ADD_CONNECTION).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	ConnectionResponse connectionResponse = gson.fromJson(result.getResponse().getContentAsString(), ConnectionResponse.class);
    	assertFalse(connectionResponse.isSuccess());
    	assertEquals(connectionResponse.getMessage(), "Friend connection already exist");
    	
    	String cateEmail = "cate@test.co";
    	friendRequest.setFriends(Arrays.asList(andyEmail, cateEmail));
    	json = gson.toJson(friendRequest);
    	result = this.mockMvc.perform(post(ADD_CONNECTION).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	response = gson.fromJson(result.getResponse().getContentAsString(), SuccessResponse.class);
    	assertTrue(response.isSuccess());
    	
    	// 2: Test Get Friend list
    	EmailRequest emailRequest = new EmailRequest();
    	emailRequest.setEmail(andyEmail);
    	json = gson.toJson(emailRequest);
    	result = this.mockMvc.perform(post(GET_FRIEND_LIST).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	FriendResponse friendResponse = gson.fromJson(result.getResponse().getContentAsString(), FriendResponse.class);
    	assertTrue(response.isSuccess());
    	assertEquals(friendResponse.getCount(), 2);
    	assertTrue(friendResponse.getFriends().containsAll(Arrays.asList(cateEmail, bobEmail)));
    	
    	emailRequest.setEmail(bobEmail);
    	json = gson.toJson(emailRequest);
    	result = this.mockMvc.perform(post(GET_FRIEND_LIST).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	friendResponse = gson.fromJson(result.getResponse().getContentAsString(), FriendResponse.class);
    	assertTrue(response.isSuccess());
    	assertEquals(friendResponse.getCount(), 1);
    	assertTrue(friendResponse.getFriends().containsAll(Arrays.asList(andyEmail)));
    	
    	// 3: Test Get Common Friend
    	friendRequest.setFriends(Arrays.asList(cateEmail, bobEmail));
    	json = gson.toJson(friendRequest);
    	result = this.mockMvc.perform(post(GET_COMMON_FRIEND).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	friendResponse = gson.fromJson(result.getResponse().getContentAsString(), FriendResponse.class);
    	assertTrue(response.isSuccess());
    	assertEquals(friendResponse.getCount(), 1);
    	assertTrue(friendResponse.getFriends().containsAll(Arrays.asList(andyEmail)));
    	
    	// 4: Test subscribe
    	String subscribeEmail = "subscribe@test.co";
    	DirectRequest directRequest = new DirectRequest();
    	directRequest.setRequestor(subscribeEmail);
    	directRequest.setTarget(andyEmail);
    	json = gson.toJson(directRequest);
    	result = this.mockMvc.perform(post(SUBSCRIBE).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	response = gson.fromJson(result.getResponse().getContentAsString(), SuccessResponse.class);
    	assertTrue(response.isSuccess());
    	
    	// 5: Test block
    	directRequest.setRequestor(bobEmail);
    	directRequest.setTarget(andyEmail);
    	json = gson.toJson(directRequest);
    	result = this.mockMvc.perform(post(BLOCK).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	response = gson.fromJson(result.getResponse().getContentAsString(), SuccessResponse.class);
    	assertTrue(response.isSuccess());
    	
    	// 6: test update    	
    	String mentionEmail = "mention@test.co";
    	UpdateRequest updateRequest = new UpdateRequest();
    	updateRequest.setSender(andyEmail);
    	updateRequest.setText("Testing update! " + mentionEmail);
    	json = gson.toJson(updateRequest);
    	result = this.mockMvc.perform(post(UPDATE).contentType(MediaType.APPLICATION_JSON)
    			.content(json)).andExpect(status().isOk()).andReturn();
    	UpdateSuccessResponse updateResponse = gson.fromJson(result.getResponse().getContentAsString(), UpdateSuccessResponse.class);
    	assertTrue(updateResponse.isSuccess());
    	assertEquals(updateResponse.getRecepients().size(), 3);
    	assertTrue(updateResponse.getRecepients().containsAll(Arrays.asList(mentionEmail, cateEmail, subscribeEmail)));
    }
}
