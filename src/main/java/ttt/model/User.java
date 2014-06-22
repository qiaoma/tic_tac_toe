package ttt.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Integer id;

	@Column(nullable = false, unique = true)
    private String username;

    private String password;
    
    private String email;
    
    private boolean enabled = true;
     
    //List<Game> savedGames;
    //List<Game> playedGames;
    
    @OneToMany(mappedBy="player1", cascade=CascadeType.ALL)
    @OrderBy("saveTime asc")
    List<Game> savedGamesAsPlayer1;
    
    @OneToMany(mappedBy="player2", cascade=CascadeType.ALL)
    @OrderBy("saveTime asc")
    List<Game> savedGamesAsPlayer2;
    
    @OneToMany(mappedBy="player1", cascade=CascadeType.ALL)
    @OrderBy("gameType asc")
    List<Game> playedGamesAsPlayer1;
    
    @OneToMany(mappedBy="player2", cascade=CascadeType.ALL)
    @OrderBy("gameType asc")
    List<Game> playedGamesAsPlayer2;

    public User()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Game> getSavedGamesAsPlayer1() {
		return savedGamesAsPlayer1;
	}

	public void setSavedGamesAsPlayer1(List<Game> savedGamesAsPlayer1) {
		this.savedGamesAsPlayer1 = savedGamesAsPlayer1;
	}

	public List<Game> getSavedGamesAsPlayer2() {
		return savedGamesAsPlayer2;
	}

	public void setSavedGamesAsPlayer2(List<Game> savedGamesAsPlayer2) {
		this.savedGamesAsPlayer2 = savedGamesAsPlayer2;
	}

	public List<Game> getPlayedGamesAsPlayer1() {
		return playedGamesAsPlayer1;
	}

	public void setPlayedGamesAsPlayer1(List<Game> playedGamesAsPlayer1) {
		this.playedGamesAsPlayer1 = playedGamesAsPlayer1;
	}

	public List<Game> getPlayedGamesAsPlayer2() {
		return playedGamesAsPlayer2;
	}

	public void setPlayedGamesAsPlayer2(List<Game> playedGamesAsPlayer2) {
		this.playedGamesAsPlayer2 = playedGamesAsPlayer2;
	}

	
}
