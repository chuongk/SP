package sp.chuongk.socialnetwork.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Person {
	@Id
	private String email;
	
	@ManyToMany
	private List<Person> friendList;
	
	@ManyToMany
	private List<Person> blockList;
	
	@ManyToMany
	private List<Person> subscribers;
	
	public Person() {
		this.email = "default";
		this.friendList = new ArrayList<>();
		this.blockList = new ArrayList<>();
	}
	
	public Person(String email_) {
		this.email = email_;
		this.friendList = new ArrayList<>();
		this.blockList = new ArrayList<>();
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public List<Person> getFriendList() {
		return friendList;
	}


	public void setFriendList(List<Person> friendList) {
		this.friendList = friendList;
	}


	public List<Person> getBlockList() {
		return blockList;
	}


	public void setBlockList(List<Person> blockList) {
		this.blockList = blockList;
	}

	public List<Person> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Person> subscribers) {
		this.subscribers = subscribers;
	}
	
	
}
