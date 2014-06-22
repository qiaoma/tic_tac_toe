package ttt.model;

public class GameMonthHistory {
	private String opponentName;
	private double gameLength;
	private String outcome;
	
	public GameMonthHistory(){
		
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public double getGameLength() {
		return gameLength;
	}

	public void setGameLength(double gameLength) {
		this.gameLength = gameLength;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
	
}
