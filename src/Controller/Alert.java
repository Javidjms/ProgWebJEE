package Controller;
public class Alert {

	/**
	 * Envoi un message de warning 
	 * @param message
	 * @return
	 */
	public static String warning(String message){
		String s = "<div class='alert alert-warning alert-dismissible' role='alert'><strong>Erreur</strong> ";
		s+=message;
		s+="</div>'";	
		return s;
		
	}
	
	/**
	 * Envoi un message de succ�s 
	 * @param message
	 * @return
	 */
	public static String success(String message){
		String s = "<div class='alert alert-success alert-dismissible' role='alert'><strong>Succ�s</strong> ";
		s+=message;
		s+="</div>'";	
		return s;
		
	}
}
