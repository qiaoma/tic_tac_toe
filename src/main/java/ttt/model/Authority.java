package ttt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class Authority implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue
    private Integer id;

	@Column(nullable = false, unique = true)
    private String username;
	
	private String authority;
	
	public Authority(){}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
