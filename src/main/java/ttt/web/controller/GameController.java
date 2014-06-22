package ttt.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import ttt.model.Game;
import ttt.model.GameMonthHistory;
import ttt.model.User;
import ttt.model.dao.UserDao;


@Controller
public class GameController {

	@Autowired
    private UserDao userDao;

	
	@RequestMapping("/gameBoard.html")
	public String gameBoard( HttpSession session, Principal principal) {
		
		Game game = new Game();
		Date startTime = new Date();		
		game.setStartTime(startTime);
		game.setGameType(1);
		
		if(principal != null){
			String username = principal.getName();

			User player1 = userDao.getUser(username);
			
			session.setAttribute("user", player1);
			session.setAttribute("isAuthenticate", true);
			
			game.setPlayer1(player1);
	
		}else{
			session.setAttribute("isAuthenticate", false);
		}
				
		session.setAttribute("game", game);
		
	    return "gameBoard";
	}
	
	@RequestMapping("/playBoard.html")
	public String playBoard( @RequestParam String pos, HttpSession session) {		
		
		Game game = (Game)session.getAttribute("game");
		boolean isAuthenticate = (Boolean)session.getAttribute("isAuthenticate");
		
		String outcome = game.getOutcome();
		String[] positions = game.getPositions();		
		int posi = Integer.parseInt(pos);
		
		if(outcome.equals("unfinish") && positions[posi] == "___"){		
			game.setUserPositions(posi);			
			int numUserMove = game.getNumUserMove();
			
			if( numUserMove >= 3 && ifUserWin(positions, numUserMove)){
				//user win
				saveGameResult(game, "win", isAuthenticate);
			}else if(game.getNumVacancy() == 0){
				//game tied
				saveGameResult(game, "tie", isAuthenticate);
			}else{		
				int numVacancy = game.getNumVacancy();
				int numAIMove = game.getNumAIMove();
				int posAI = getAIPosition(positions, numVacancy);
				game.setAIPositions(posAI);				
				numAIMove = game.getNumAIMove();
				if( numAIMove >= 3 && ifAIWin(positions, numAIMove)){
					//AI win
					saveGameResult(game, "loss", isAuthenticate);
				}
			}	
		}
		
	    return "gameBoard";
	}
	
	@RequestMapping("/newGame.html")
	public String newGame( HttpSession session, Principal principal ) {
		
		Game game = (Game)session.getAttribute("game");
		boolean isAuthenticate = (Boolean)session.getAttribute("isAuthenticate");
		String outcome = game.getOutcome();
		
		if(outcome.equals("unfinish") && game.getId() == null){
			
			saveGameResult(game, "loss", isAuthenticate);
		}
		
		session.removeAttribute("game");
		
	    return gameBoard( session, principal );
	}
	
	@RequestMapping("/saveGame.html")
	public String saveGame( HttpSession session ) {
		
		User player1 = (User)session.getAttribute("user");
		Game game = (Game)session.getAttribute("game");
		
		Date saveTime = new Date();
		game.setSaveTime(saveTime);
		String[] positions = game.getPositions();
		String position = "";
		for(String p: positions){
			if( p.equals("_X_") ){
				position += "X"; 
			}else if(p.equals("_O_")){
				position += "O"; 
			}else if(p.equals("___")){
				position += "-";
			}
		}
		game.setPosition(position);
		
		userDao.saveGame(game);
		
		List<Game> savedGameList = userDao.getSavedGames(player1);
		Game lastSavedGame = savedGameList.get(savedGameList.size()-1);
		
		game.setId(lastSavedGame.getId());
					
	    return "gameBoard";
	}
	
	@RequestMapping("/resumeGame.html")
	public String resumeGame( HttpSession session, ModelMap models ) {
		
		User player1 = (User)session.getAttribute("user");
		
		models.put("savedGameList", userDao.getSavedGames(player1));
	    return "resume";
	}
	
	@RequestMapping("/resumeSingleGame.html")
	public String resumeSingleGame( @RequestParam String id, HttpSession session ) {
				
		int gameId = Integer.parseInt(id);
		
		Game game = userDao.getGame(gameId);
		
		String position = game.getPosition();
		String[] positions = new String[position.length()];
		int numUserMove = 0;
		int numAIMove = 0;
		int numVacancy = 0;
		
		for(int i = 0; i < positions.length; i++){
			if(position.charAt(i) == 'X'){
				positions[i] = "_X_";
				numUserMove++;
			}else if(position.charAt(i) == 'O'){
				positions[i] = "_O_";
				numAIMove++;
			}else{
				positions[i] = "___";
			}
		}	
		numVacancy = positions.length - numUserMove - numAIMove;
		game.setPositions(positions);
		game.setNumUserMove(numUserMove);
		game.setNumAIMove(numAIMove);
		game.setNumVacancy(numVacancy);

		session.setAttribute("game", game);
		
		return "gameBoard";
	}
	
	@RequestMapping("/gameHistory.html")
	public String gameHistory( HttpSession session, ModelMap models) {
		
		User player1 = (User)session.getAttribute("user");
		
		List<Game> completedGames = userDao.getCompletedGames(player1);		
		List<Game> completedGamesAI = userDao.getCompletedGamesAgainstAI(player1);		
		List<Game> completedGamesPlayer = userDao.getCompletedGamesAgainstPlayer(player1);		
		List<GameMonthHistory> monthHistoryList = new ArrayList<>();
		double numWinAI = 0;
		double numWinPlayer = 0;
		
		for(Game game : completedGamesAI){
			if(game.getOutcome().equals("win")){
				numWinAI++;
			}
		}
		
		for(Game game : completedGamesPlayer){
			if(game.getOutcome().equals("win")){
				numWinPlayer++;
			}
		}	
		
		Calendar calendar = Calendar.getInstance();	
		int currentMonth = calendar.get(Calendar.MONTH);
		
		for(Game game : completedGames){
			Date gameStartTime = game.getStartTime();			
			Calendar gameCalendar = Calendar.getInstance();
			gameCalendar.setTime(gameStartTime);			
			int gameMonth = gameCalendar.get(Calendar.MONTH);
			
			if(gameMonth == currentMonth){
				GameMonthHistory monthHistory = new GameMonthHistory();
				int gameType = game.getGameType();
				if (gameType == 1){
					monthHistory.setOpponentName("AI");
				}else if (gameType == 2){
					String oppUsername = game.getPlayer2().getUsername();
					monthHistory.setOpponentName(oppUsername);
				}
				monthHistory.setOutcome(game.getOutcome());
				long startTimeInMill = gameCalendar.getTimeInMillis();
				
				Date gameEndTime = game.getEndTime();
				gameCalendar.setTime(gameEndTime);
				long endTimeInMill = gameCalendar.getTimeInMillis();
				double gameLength = (double)(endTimeInMill - startTimeInMill)/1000;
				monthHistory.setGameLength(gameLength);	
				monthHistoryList.add(monthHistory);
			}
		}
	
		models.put("numCompletedGames", completedGames.size());		
		models.put("numCompletedGamesAI", completedGamesAI.size());		
		models.put("numCompletedGamesPlayer", completedGamesPlayer.size());	
		if(completedGamesAI.size() != 0){
			models.put("winRateAI", String.format("%.2f", numWinAI/completedGamesAI.size()) );
		}else{
			models.put("winRateAI", 0);
		}
		if(completedGamesPlayer.size() !=0){
			models.put("winRatePlayer", String.format("%.2f", numWinPlayer/completedGamesPlayer.size()) );
		}else{
			models.put("winRatePlayer", 0);
		}	
		models.put("monthHistoryList", monthHistoryList);
		
	    return "gameHistory";
	}
	
	 @RequestMapping(value="logout", method=RequestMethod.GET)
	 public String logout(SessionStatus sessionStatus, HttpSession session){
	    
		Game game = (Game)session.getAttribute("game");
		String outcome = game.getOutcome();
		if(outcome.equals("unfinish")){
			saveGameResult(game, "loss", true);
		}
	    sessionStatus.setComplete();
	    session.invalidate();
	    
	    //return "logoutConfirm";
	    return "redirect:/j_spring_security_logout";
	 }
	 
	void saveGameResult(Game game, String result, boolean isAuthenticate){
		Date endTime = new Date();
		game.setEndTime(endTime);	
		game.setOutcome(result);
		game.setPosition(null);
		game.setSaveTime(null);
		if(isAuthenticate){
			userDao.saveGame(game);
		}
	}
	
	//---------------------------Game Logic--------------------------------------
	public boolean ifWin(int pos1, int pos2, int pos3){
    	
    	if( (pos1 == 0 && pos2 == 1 && pos3 == 2) 
    		|| (pos1 == 3 && pos2 == 4 && pos3 == 5)
    		|| (pos1 == 6 && pos2 == 7 && pos3 == 8)
    		|| (pos1 == 0 && pos2 == 3 && pos3 == 6)
    		|| (pos1 == 1 && pos2 == 4 && pos3 == 7)
    		|| (pos1 == 2 && pos2 == 5 && pos3 == 8)
    		|| (pos1 == 0 && pos2 == 4 && pos3 == 8)
    		|| (pos1 == 2 && pos2 == 4 && pos3 == 6)
    		){   		
    		return true;
    	}
    	return false;
    }
    
    public boolean ifUserWin(String[] positions, int numUserMove){
    	int[] userPositions = new int[numUserMove];
		int index = 0;
		for(int i = 0; i < positions.length; i++){
			if(positions[i] == "_X_"){
				userPositions[index] = i;				
				index++;
			}
		}				
		switch (numUserMove){	
			case 3:
				if(ifWin(userPositions[0], userPositions[1], userPositions[2])){
					
					return true;
				}
				break;
		
			case 4:
				if( ifWin(userPositions[0], userPositions[1], userPositions[2])
					||ifWin(userPositions[0], userPositions[2], userPositions[3]) 
					|| ifWin(userPositions[1], userPositions[2], userPositions[3]) ){
					
					return true;
				}
				break;
		
			case 5:
				if( ifWin(userPositions[0], userPositions[1], userPositions[2])
					|| ifWin(userPositions[0], userPositions[2], userPositions[3]) 
					|| ifWin(userPositions[1], userPositions[2], userPositions[3])
					|| ifWin(userPositions[0], userPositions[1], userPositions[4]) 
					|| ifWin(userPositions[0], userPositions[2], userPositions[4]) 
					|| ifWin(userPositions[0], userPositions[3], userPositions[4])
					|| ifWin(userPositions[1], userPositions[2], userPositions[4])
					|| ifWin(userPositions[1], userPositions[3], userPositions[4])
					|| ifWin(userPositions[2], userPositions[3], userPositions[4])){
					
					return true;
				}
				break;
		}	
		return false;
    }
    
    public boolean ifAIWin(String[] positions, int numAIMove){
    	int[] aiPositions = new int[numAIMove];
		int index = 0;
		for(int i = 0; i < positions.length; i++){
			if(positions[i] == "_O_"){
				aiPositions[index] = i;				
				index++;
			}
		}	
		
		switch (numAIMove){	
			case 3:
				if(ifWin(aiPositions[0], aiPositions[1], aiPositions[2])){
					
					return true;
				}
				break;
		
			case 4:
				if( ifWin(aiPositions[0], aiPositions[1], aiPositions[2])
					||ifWin(aiPositions[0], aiPositions[2], aiPositions[3]) 
					|| ifWin(aiPositions[1], aiPositions[2], aiPositions[3]) ){
					
					return true;
				}
				break;
		}
		return false;
    }
  
    public int getWSPos(String[] positions, String symbol){
    	
    	for(int i = 0; i < 9; i+=3){   		
    		if(positions[i]==symbol && positions[i+1]==symbol && positions[i+2]=="___")					
    			return i+2; 
    		if(positions[i]==symbol && positions[i+2]==symbol && positions[i+1]=="___")					
    			return i+1; 
    		if(positions[i+1]==symbol && positions[i+2]==symbol && positions[i]=="___")					
    			return i; 
    	}
    	
    	for(int i = 0; i < 3; i++){  
    		if(positions[i]==symbol && positions[i+3]==symbol && positions[i+6]=="___")					
    			return i+6; 
    		if(positions[i]==symbol && positions[i+6]==symbol && positions[i+3]=="___")					
    			return i+3; 
    		if(positions[i+3]==symbol && positions[i+6]==symbol && positions[i]=="___")					
    			return i;   		
    	}
    	
    	if(positions[0]==symbol && positions[4]==symbol && positions[8]=="___")	
    		return 8;
    	if(positions[0]==symbol && positions[8]==symbol && positions[4]=="___")	
    		return 4;
    	if(positions[4]==symbol && positions[8]==symbol && positions[0]=="___")	
    		return 0;
    	
    	if(positions[2]==symbol && positions[4]==symbol && positions[6]=="___")	
    		return 6;
    	if(positions[2]==symbol && positions[6]==symbol && positions[4]=="___")	
    		return 4;
    	if(positions[4]==symbol && positions[6]==symbol && positions[2]=="___")	
    		return 2;
    	
      	return -1;
    }
    
    public int getAIPosition(String[] positions, int numVacancy){
   
    	if(numVacancy <= 6){ 
    		if(numVacancy <= 4){
    			int winningPos = getWSPos(positions, "_O_");
        		if (winningPos != -1){
        			return winningPos;
        		}
    		}
    		
    		int stoppingPos = getWSPos(positions, "_X_");
    		if (stoppingPos != -1){
    			return stoppingPos;
    		}
    	}
    	
    	int index = 0;		
    	int[] emptySpaces = new int[numVacancy];   	
    	for(int i = 0; i < positions.length; i++){
    		if(positions[i] == "___"){
    			emptySpaces[index] = i;				
    			index++;
    		}
    	}
    	return emptySpaces[(int)(Math.random()*index)];		
    }
    //-----------------------------------Game Logic End-----------------------------------	
}



