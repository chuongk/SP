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
import sp.chuongk.socialnetwork.response.ConnectionResponse;
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
	    Person p1 = new Person("email1");
	    Person p2 = new Person("email2");
	    Person p3 = new Person("email3");
	    p2.getBlockList().add(p3);
	    Person p4 = new Person("email4");
	    p3.getFriendList().add(p4);
	    p4.getFriendList().add(p3);
	    
	    Person p5 = new Person("email5");
	    
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
		String email1 = "email1";
		String email2 = "email2";
		List<String> connections = Arrays.asList(email1, email2);
		ConnectionResponse response = personService.addConnection(connections);
		assertTrue(response.isSuccess());
		Person person1 = personService.getByEmail(email1);
		Person person2 = personService.getByEmail(email2);
		assertTrue(person1.getFriendList().contains(person2));
	}
	
	@Test
	public void testAddConnectionErrors() {
		String email1 = "email1";
		String email2 = "email2";
		List<String> connections = Arrays.asList(email1);
		ConnectionResponse response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "Fiend list size is not 2!");
		
		String email3 = "email3";
		connections = Arrays.asList(email2, email3);
		response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "email2 blocked email3");
		
		String email4 = "email4";
		connections = Arrays.asList(email3, email4);
		response = personService.addConnection(connections);
		assertFalse(response.isSuccess());
		assertEquals(response.getMessage(), "Friend connection already exist");
	}
	
	@Test
	public void testGetCommonFriendList() {
		String email1 = "email1";
		String email2 = "email2";
		String email4 = "email4";
		String email5 = "email5";
		List<String> connections = Arrays.asList(email1, email2);
		personService.addConnection(connections);
		
		List<String> commonList12 = personService.getCommonFriends(email1, email2);
		assertEquals(commonList12.size(), 0);
		connections = Arrays.asList(email2, email5);
		personService.addConnection(connections);
		connections = Arrays.asList(email1, email5);
		personService.addConnection(connections);
		connections = Arrays.asList(email2, email4);
		personService.addConnection(connections);
		connections = Arrays.asList(email1, email4);
		personService.addConnection(connections);
		
		commonList12 = personService.getCommonFriends(email1, email2);
		assertEquals(commonList12.size(), 2);
		assertTrue(commonList12.contains(email4));
		assertTrue(commonList12.contains(email5));
		
		List<String> commonList15 = personService.getCommonFriends(email1, email5);
		assertEquals(commonList15.size(), 1);
		assertTrue(commonList15.contains(email2));
		assertFalse(commonList15.contains(email4));
	}
}
