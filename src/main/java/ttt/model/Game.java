//1. easy to use in application logic
//2. easy to save/load to/from database

package ttt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "games")
public class Game implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "game_type")
	private int gameType;//1 for one player game, 2 for two player game.
	
	@Column(name = "start_time")
	private Date startTime;
	
	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "save_time")
	private Date saveTime;
	
	@Transient
	private String[] positions;
	
	private String position;
	
	private String outcome;
			
	//@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToOne(cascade=CascadeType.ALL)
	private User player1;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private User player2;
	
	@Transient
	private int numVacancy;
	
	@Transient
	private int numUserMove;
	
	@Transient
	private int numAIMove;
	
	public Game(){
		positions = new String[9];
		
		for(int i = 0; i < positions.length; i++){
			positions[i] = "___";
		}
		
		outcome = "unfinish";
		numVacancy = positions.length;
		numUserMove = 0;
		numAIMove = 0;		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String[] getPositions() {
		
		return positions;
	}

	public void setPositions(String[] positions) {
		this.positions = positions;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public User getPlayer1() {
		return player1;
	}

	public void setPlayer1(User player1) {
		this.player1 = player1;
	}

	public User getPlayer2() {
		return player2;
	}

	public void setPlayer2(User player2) {
		this.player2 = player2;
	}
	
	public int getNumVacancy() {
		return numVacancy;
	}
	
	public void setNumVacancy(int numVacancy) {
		this.numVacancy = numVacancy;
	}

	public int getNumUserMove(){
		return numUserMove;
	}
	
	public void setNumUserMove(int numUserMove) {
		this.numUserMove = numUserMove;
	}

	public int getNumAIMove(){
		return numAIMove;
	}
	
	public void setNumAIMove(int numAIMove) {
		this.numAIMove = numAIMove;
	}
	
	public void setUserPositions(int pos) {
		positions[pos] = "_X_";
		numUserMove++;
		numVacancy--;
	}
	
	public void setAIPositions(int pos) {
		positions[pos] = "_O_";
		numAIMove++;
		numVacancy--;
	}

}
