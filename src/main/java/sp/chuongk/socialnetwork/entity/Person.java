package sp.chuongk.socialnetwork.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Person {
	@Id
	private String email;
	
	@ManyToMany
	private Set<Person> friendList;
	
	@ManyToMany
	private Set<Person> blockList;
	
	@ManyToMany
	private Set<Person> subscribers;
	
	@ManyToMany
	private List<Person> isBlockedList;
	
	public Person() {
		this.email = "default";
		initialize();
	}
	
	public Person(String email_) {
		this.email = email_;
		initialize();
	}
	
	private void initialize() {
		this.friendList = new HashSet<>();
		this.blockList = new HashSet<>();
		this.subscribers = new HashSet<>();
		this.isBlockedList = new ArrayList<>();
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Person> getFriendList() {
		return friendList;
	}

	public void setFriendList(Set<Person> friendList) {
		this.friendList = friendList;
	}

	public Set<Person> getBlockList() {
		return blockList;
	}

	public void setBlockList(Set<Person> blockList) {
		this.blockList = blockList;
	}

	public Set<Person> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<Person> subscribers) {
		this.subscribers = subscribers;
	}

	public List<Person> getIsBlockedList() {
		return isBlockedList;
	}

	public void setIsBlockedList(List<Person> isBlockedList) {
		this.isBlockedList = isBlockedList;
	}

}
