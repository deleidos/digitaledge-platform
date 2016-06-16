import com.deleidos.rtws.commons.util.scripting.annotation.ScriptSignature

@ScriptSignature(signature="determineWinningTeam(visitingTeam, visitingTeamScore, homeTeam, homeTeamScore)",
		description="Determines the winning team based on scores.")
def determineWinningTeam(visitingTeam, visitingTeamScore,homeTeam, homeTeamScore) {
	int hScr = Integer.valueOf(homeTeamScore)
	int vScr = Integer.valueOf(visitingTeamScore)
	
	if (hScr > vScr) {
		return homeTeam;
		
	} else if (hScr < vScr) {
		return visitingTeam;
	
	} else {
		return "TIE"
	}	
}