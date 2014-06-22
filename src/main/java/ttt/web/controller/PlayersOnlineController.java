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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.async.DeferredResult;

import ttt.model.Game;
import ttt.model.GameMonthHistory;
import ttt.model.PlayersOnlineService;
import ttt.model.User;
import ttt.model.dao.UserDao;

@Controller
public class PlayersOnlineController {

	@Autowired
	private PlayersOnlineService playersService;
	
	@Autowired
    private UserDao userDao;
	
	@RequestMapping("/playerOption.html")
	public String playerOption(){
	
		return "playerOption";
	}
	
	@RequestMapping("/hostJoin.html")
	public String hostJoin(HttpSession session, Principal principal){
		
		return "hostJoin";
	}
	
	@RequestMapping("/host.html")
	public String host(HttpSession session, Principal principal){

		String username = principal.getName();
		User hostPlayer = userDao.getUser(username);
		session.setAttribute("hostUser", hostPlayer);
		
		Game game = new Game();
		session.setAttribute("game2", game);
		
		return "host";
	}
	
	@RequestMapping("/join.html")
	public String join(HttpSession session, Principal principal){

		String username = principal.getName();
		User joinPlayer = userDao.getUser(username);
		session.setAttribute("joinUser", joinPlayer);
		
		Game game = new Game();
		session.setAttribute("game2", game);
		
		return "join";
	}
	
	@RequestMapping("/askJoinPlayer.json")
	public String askJoinPlayer(HttpSession session, ModelMap models){
		
		User hostPlayer = (User)session.getAttribute("hostUser");			
		
		if( playersService.getJoinUsers().isEmpty() ){
			playersService.addHostUser(hostPlayer);
			models.put("waitingJoinUser", true);
			
		}else{
			User joinPlayer = playersService.getJoinUser();
			playersService.removeJoinUser(hostPlayer);
			
			Game game = (Game)session.getAttribute("game2");
			game.setPlayer1(hostPlayer);
			game.setPlayer2(joinPlayer);
			
			Date startTime = new Date();		
			game.setStartTime(startTime);
			game.setGameType(2);
			
			playersService.addGame(game);
			models.put("joinUsername", joinPlayer.getUsername());
			models.put("waitingJoinUser", false);
		}
		
		return "jsonView";
	}
	
	@RequestMapping("/askHostPlayer.json")
	public String askHostPlayer(HttpSession session, ModelMap models){
		
		User joinPlayer = (User)session.getAttribute("joinUser");
		if( playersService.getHostUsers().isEmpty() ){
			playersService.addJoinUser(joinPlayer);
			models.put("waitingHostUser", true);
			
		}else{
			User hostPlayer = playersService.getHostUser();
			playersService.removeHostUser(joinPlayer);
			
			Game game = (Game)session.getAttribute("game2");
			game.setPlayer1(hostPlayer);
			game.setPlayer2(joinPlayer);
			
			Date startTime = new Date();		
			game.setStartTime(startTime);
			game.setGameType(2);
			playersService.addGame(game);
			models.put("hostUsername", hostPlayer.getUsername());
			models.put("waitingHostUser", false);
		}
		
		return "jsonView";
	}
	
	@RequestMapping("/playBoardHost.html")
	public String playBoardHost( @RequestParam String pos, HttpSession session, ModelMap models) {		
		
		User hostPlayer = (User)session.getAttribute("hostUser");	
		Game game = playersService.getGame(hostPlayer);
		session.setAttribute("game2", game);
		models.put("gameStatus", "start");
		
		String outcome = game.getOutcome();
		String[] positions = game.getPositions();		
		int posi = Integer.parseInt(pos);
		
		if(outcome.equals("unfinish") && positions[posi] == "___"){		
			game.setUserPositions(posi);				
			int numUserMove = game.getNumUserMove();
			
			if( numUserMove >= 3 && ifUserWin(positions, numUserMove)){
				//user win
				playersService.displayMoveToJoin(posi, "loss");
				saveGameResult(game, "win");
			}else if(game.getNumVacancy() == 0){
				//game tied
				playersService.displayMoveToJoin(posi, "tie");
				saveGameResult(game, "tie");
			}else{		
				int numAIMove = game.getNumAIMove();		
				numAIMove = game.getNumAIMove();
				if( numAIMove >= 3 && ifAIWin(positions, numAIMove)){
					//AI win
					playersService.displayMoveToJoin(posi, "win");
					saveGameResult(game, "loss");
				}else{
					models.put("processHost", "true");
					playersService.displayMoveToJoin(posi, outcome);					 
				}
			}	
			
		}else{
			models.put("processHost", "false");
		}
		
		return "host";
	}
	
	@RequestMapping("/playBoardJoin.html")
	public String playBoardJoin( @RequestParam String pos, HttpSession session, ModelMap models) {		
		
		User joinPlayer = (User)session.getAttribute("joinUser");
		Game game = playersService.getGame(joinPlayer);
		session.setAttribute("game2", game);
		models.put("gameStatus", "start");
		
		String outcome = game.getOutcome();
		String[] positions = game.getPositions();		
		int posi = Integer.parseInt(pos);
		
		if(outcome.equals("unfinish") && positions[posi] == "___"){		
			game.setAIPositions(posi);
			
			int numUserMove = game.getNumUserMove();
			
			if( numUserMove >= 3 && ifUserWin(positions, numUserMove)){
				//user win
				playersService.displayMoveToHost(posi, "win");
				saveGameResult(game, "win");
			}else if(game.getNumVacancy() == 0){
				//game tied
				playersService.displayMoveToHost(posi, "tie");
				saveGameResult(game, "tie");
			}else{		
				int numAIMove = game.getNumAIMove();	
				numAIMove = game.getNumAIMove();
				if( numAIMove >= 3 && ifAIWin(positions, numAIMove)){
					//AI win
					playersService.displayMoveToHost(posi, "loss");
					saveGameResult(game, "loss");
				}else{
				
					models.put("processJoin", "true");
					playersService.displayMoveToHost(posi, outcome);					
				}
			}				
		}else{
			models.put("processJoin", "false");
		}
		
		return "join";
	}
/*	
	@RequestMapping("/newGameForPlayer.html")
	public String newGameForPlayer( HttpSession session, Principal principal ) {
		Game finishGame = (Game)session.getAttribute("game2");
		session.removeAttribute("game2");
		User hostPlayer = finishGame.getPlayer1();
		User joinPlayer = finishGame.getPlayer2(); 
		playersService.removeGame(finishGame);
		playersService.removeUser(hostPlayer);
		playersService.removeUser(joinPlayer);
	    return "hostJoin";
	}
*/	
	@RequestMapping("/gameHistoryPlayer.html")
	public String gameHistoryPlayer( HttpSession session, ModelMap models, Principal principal ) {
		
		
		String username = principal.getName();
		User player1 = userDao.getUser(username);
		
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
	
	@RequestMapping("/hostPlayerWaiting.json")
	@ResponseBody
	public DeferredResult<String> waitingHostPlayer(){
		
		DeferredResult<String> result = new DeferredResult<>();
		playersService.addHostResult(result);
		
		return result;
	}
	
	@RequestMapping("/joinPlayerWaiting.json")
	@ResponseBody
	public DeferredResult<String> waitingJoinPlayer(){
		
		DeferredResult<String> result = new DeferredResult<>();
		playersService.addJoinResult(result);
		
		return result;
	}
	
	//---------------------------------------------------------------------
	@RequestMapping("/hostWaitingJoinMove.json")
	@ResponseBody
	public DeferredResult<String> hostWaitingJoinMove(){
	
		DeferredResult<String> result = new DeferredResult<>();
		//playersService.setGameJoinResult(hostPlayer, result);
		playersService.addHostResult(result);
		
		return result;
	}
	
	@RequestMapping("/joinWaitingHostMove.json")
	@ResponseBody
	public DeferredResult<String> joinWaitingHostMove(){
		
		DeferredResult<String> result = new DeferredResult<>();
		//playersService.setGameJoinResult(joinPlayer, result);
		playersService.addJoinResult(result);
		
		return result;
	}
	
	@RequestMapping(value="logoutPlayer", method=RequestMethod.GET)
	 public String logoutPlayer(SessionStatus sessionStatus, HttpSession session){
	    
	    sessionStatus.setComplete();
	    session.invalidate();
	    
	    //return "logoutConfirm";
	    return "redirect:/j_spring_security_logout";
	 }
	
	//--------------------------------------------------------------------------
	void saveGameResult(Game game, String result){
		Date endTime = new Date();
		game.setEndTime(endTime);	
		game.setOutcome(result);
		game.setPosition(null);
		game.setSaveTime(null);
		userDao.saveGame(game);
	
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
