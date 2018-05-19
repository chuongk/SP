package sp.chuongk.socialnetwork;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import sp.chuongk.socialnetwork.entity.Person;
import sp.chuongk.socialnetwork.repository.PersonRepository;
import sp.chuongk.socialnetwork.response.CommonFriendResponse;
import sp.chuongk.socialnetwork.response.ConnectionResponse;
import sp.chuongk.socialnetwork.response.UpdateRespone;
import sp.chuongk.socialnetwork.service.PersonService;
import sp.chuongk.socialnetwork.service.impl.PersonServiceImpl;


@RunWith(SpringRunner.class)
public class PersonServiceTest {
	
    @TestConfiguration
    static class PersonServiceTestContextConfiguration {
  
        @Bean
        public PersonService personService() {
            return new PersonServiceImpl();
        }
    }
	
	@Autowired
	PersonService personService;
	
	@MockBean
    private PersonRepository personRepository;
	
	@Before
	public void setUp() {
	    Person p1 = new Person("email1@co.in");
	    Person p2 = new Person("email2@co.in");
	    Person p3 = new Person("email3@co.in");
	    p2.getBlockList().add(p3);
	    Person p4 = new Person("email4@co.in");
	    p3.getFriendList().add(p4);
	    p4.getFriendList().add(p3);
	    
	    Person p5 = new Person("email5@co.in");
	    
	    Mockito.when(personRepository.findById(p1.getEmail()))
	      .thenReturn(Optional.of(p1));
	    Mockito.when(personRepository.findById(p2.getEmail()))
	      .thenReturn(Optional.of(p2));
	    Mockito.when(personRepository.findById(p3.getEmail()))
	      .thenReturn(Optional.of(p3));
	    Mockito.when(personRepository.findById(p4.getEmail()))
	      .thenReturn(Optional.of(p4));
	    Mockito.when(personRepository.findById(p5.getEmail()))
	      .thenReturn(Optional.of(p5));
	}
	
	@Test
	public void testAddConnectionSuccess() {
		String email1 = "email1@co.in";
		String email2 = "email2@co.in";
		List<String> connections = Arrays.asList(email1, email2);
		ConnectionResponse response = personService.addConnection(connections);
		assertTrue(response.isSuccess());
		Person person1 = personService.getByEmail(email1);
		Person person2 = personService.getByEmail(email2);
		assertTrue(person1.getFriendList().contains(person2));
	}
	
	@Test
	public void testAddConnectionErrors() {
		String email1 = "email1@co.in";
		String email2 = "email2@co.in";
		List<String> connections = Arrays.asList(email1);
		ConnectionResponse response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "Fiend list size is not 2!");
		
		String email3 = "email3@co.in";
		connections = Arrays.asList(email2, email3);
		response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "email2@co.in blocked email3@co.in");
		
		String email4 = "email4@co.in";
		connections = Arrays.asList(email3, email4);
		response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "Friend connection already exist");
		
		connections = Arrays.asList(email4, email4);
		response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "Both emails are the same");
	}
	
	@Test
	public void testGetCommonFriendList() {
		String email1 = "email1@co.in";
		String email2 = "email2@co.in";
		String email4 = "email4@co.in";
		String email5 = "email5@co.in";
		List<String> connections = Arrays.asList(email1, email2);
		personService.addConnection(connections);
		
		List<String> commonList12 = personService.getCommonFriends(Arrays.asList(email1, email2)).getFriends();
		assertEquals(commonList12.size(), 0);
		connections = Arrays.asList(email2, email5);
		personService.addConnection(connections);
		connections = Arrays.asList(email1, email5);
		personService.addConnection(connections);
		connections = Arrays.asList(email2, email4);
		personService.addConnection(connections);
		connections = Arrays.asList(email1, email4);
		personService.addConnection(connections);
		
		commonList12 = personService.getCommonFriends(Arrays.asList(email1, email2)).getFriends();
		assertEquals(commonList12.size(), 2);
		assertTrue(commonList12.contains(email4));
		assertTrue(commonList12.contains(email5));
		
		List<String> commonList15 = personService.getCommonFriends(Arrays.asList(email1, email5)).getFriends();
		assertEquals(commonList15.size(), 1);
		assertTrue(commonList15.contains(email2));
		assertFalse(commonList15.contains(email4));
		
		CommonFriendResponse errorResponse = personService.getCommonFriends(Arrays.asList(email1));
		assertFalse(errorResponse.isSuccess());
		assertEquals(errorResponse.getMessage(), "Fiend list size is not 2!");
	}
	
	@Test
	public void testAddSubscriber() {
		String email1 = "email1@co.in";
		String email5 = "email5@co.in";
		ConnectionResponse response = personService.addSubscribe(email1, email5);
		assertTrue(response.isSuccess());
		
		Person person1 = personService.getByEmail(email1);
		Person person5 = personService.getByEmail(email5);
		assertTrue(person5.getSubscribers().contains(person1));
		response = personService.addSubscribe(email1, email5);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "Subcribers already exist!");
		
		response = personService.addSubscribe(email5, email1);
		assertTrue(response.isSuccess());
		person1.getSubscribers().contains(person5);
	}
	
	@Test
	public void testAddBlock() {
		String email1 = "email1@co.in";
		String email5 = "email5@co.in";
		ConnectionResponse response = personService.addBlock(email1, email5);
		assertTrue(response.isSuccess());
		
		Person person1 = personService.getByEmail(email1);
		Person person5 = personService.getByEmail(email5);
		assertTrue(person1.getBlockList().contains(person5));
		response = personService.addBlock(email1, email5);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "Target already blocked!");
		
		response = personService.addBlock(email5, email1);
		assertTrue(response.isSuccess());
		person5.getBlockList().contains(person1);
		
		List<String> connections = Arrays.asList(email1, email5);
		response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "email1@co.in blocked email5@co.in");
	}
	
	@Test
	public void testNotify() {
		String email1 = "email1@co.in";
		String email2 = "email2@co.in";
		List<String> connections = Arrays.asList(email1, email2);
		personService.addConnection(connections);
		
		String email5 = "email5@co.in";
		personService.addSubscribe(email5, email2);
		
		String email4 = "email4@co.in";
		
		connections = Arrays.asList(email2, email4);
		personService.addConnection(connections);
		String email3 = "email3@co.in";
		UpdateRespone updateResponse = personService.doUpdate(email2, "Mention email3@co.in");
		assertTrue(updateResponse.isSuccess());
		assertEquals(updateResponse.getRecepients().size(), 4);
		assertTrue(updateResponse.getRecepients().containsAll(Arrays.asList(email1, email3, email4, email5)));
		
		personService.addBlock(email1, email2);
		personService.addBlock(email4, email2);
		
		updateResponse = personService.doUpdate(email2, "Mention email3@co.in");
		assertTrue(updateResponse.isSuccess());
		assertEquals(updateResponse.getRecepients().size(), 2);
		assertTrue(updateResponse.getRecepients().containsAll(Arrays.asList(email3, email5)));
		
	}
}
