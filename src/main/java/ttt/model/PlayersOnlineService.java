package ttt.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlayersOnlineService {
	
	List<User> hostUsers;
	List<User> joinUsers;
	List<Game> games;	
	List<DeferredResult<String>> hostResults;
	List<DeferredResult<String>> joinResults;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public PlayersOnlineService(){
		
		hostUsers = new ArrayList<>();
		joinUsers = new ArrayList<>();
		games = new ArrayList<>();
		
		hostResults = new ArrayList<>();
		joinResults = new ArrayList<>();

	}
	
	public void addHostResult(DeferredResult<String> result){
		hostResults.add(result);
		
	}

	public void addJoinResult(DeferredResult<String> result){
		joinResults.add(result);
	}
	
	public List<User> getHostUsers() {
		return hostUsers;
	}
	
	public User getHostUser(){
		
		return hostUsers.get(0);
	}
	
	public void removeUser(User user){
		if(hostUsers.contains(user)){
			hostUsers.remove(user);
		}
		if(joinUsers.contains(user)){
			joinUsers.remove(user);
		}
	}
	
	public void removeHostUser(User joinUser){
		setHostResult(joinUser);
		hostUsers.remove(0);
		((ArrayList<User>) hostUsers).trimToSize();
	}
	
	public void addHostUser(User user){
		if(!hostUsers.contains(user)){
			hostUsers.add(user);
		}
	}
	
	public List<User> getJoinUsers() {
		return joinUsers;
	}
	
	public User getJoinUser(){
		
		return joinUsers.get(0);
	}
	
	public void removeJoinUser(User hostUser){
		setJoinResult(hostUser);
		joinUsers.remove(0);
		((ArrayList<User>) joinUsers).trimToSize();
	}
	
	public void addJoinUser(User user){
		if(!joinUsers.contains(user)){
			joinUsers.add(user);
		}
	}
	
	public Game getGame(User user){
		for(Game game : games){
			if(game.getPlayer1().getUsername() == user.getUsername() 
					|| game.getPlayer2().getUsername() == user.getUsername()){
				return game;
			}
		}
		return null;
	}
	
	public void displayMoveToJoin(int posi, String outcome){
		setDisplayMoveToJoinResult(posi, outcome);
	}

	public void displayMoveToHost(int posi, String outcome){
		setDisplayMoveToHostResult(posi, outcome);
	}
	public List<Game> getGames() {
		return games;
	}
	
	public void addGame(Game game){
		games.add(game);
	}
	
	public void removeGame(Game game){
		if(games.contains(game)){
			games.remove(game);
		}	
	}

	private void setHostResult(User joinUser){
		
		StringWriter sw = new StringWriter();
		try {
			objectMapper.writeValue(sw, joinUser.getUsername());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = sw.toString();
	
		hostResults.get(0).setResult(json);
		hostResults.remove(0);
		((ArrayList<DeferredResult<String>>) hostResults).trimToSize();

	}

	private void setJoinResult(User hostUser){
		
		StringWriter sw = new StringWriter();
		try {
			objectMapper.writeValue(sw, hostUser.getUsername());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = sw.toString();
		
		joinResults.get(0).setResult(json);
		joinResults.remove(0);
		((ArrayList<DeferredResult<String>>) joinResults).trimToSize();
	}
	
	private void setDisplayMoveToJoinResult(int posi, String outcome){
		String[] result = {posi+"", outcome};
		StringWriter sw = new StringWriter();
		try {
			objectMapper.writeValue(sw, result);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = sw.toString();
	
		joinResults.get(0).setResult(json);
		joinResults.remove(0);
		((ArrayList<DeferredResult<String>>) joinResults).trimToSize();
	}
	
	private void setDisplayMoveToHostResult(int posi, String outcome){
		String[] result = {posi+"", outcome};
		StringWriter sw = new StringWriter();
		try {
			objectMapper.writeValue(sw, result);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = sw.toString();
	
		hostResults.get(0).setResult(json);
		hostResults.remove(0);
		((ArrayList<DeferredResult<String>>) hostResults).trimToSize();
	}
}
