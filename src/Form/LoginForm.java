package Form;

import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import Connexion.ConnexionJDBC;


/**
 *Classe utilisant un formulaire de Connexion (DDE et RA) 
 *
 */
public final class LoginForm {
	private String pathName = "http://localhost:8080/PlanningParSemaineJ2EE/responsables.xml";
	private ConnexionJDBC connexionjdbc =null;
	private String login;
	private String password;
	private String errorMessage="";
	private String nbEleves;
	private ResultSet resultat;
	private HttpSession s;
	private String promo;
	public static final int COOKIE_MAX_AGE = 60 * 60 * 24* 365; // 1 an
	
	/**
	 * Connexion d'un utilisateur
	 * @param request
	 * @return success
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean userConnect(HttpServletRequest request  ) throws ClassNotFoundException, SQLException {
		boolean success = true;
		/* Récupération des champs du formulaire */
		s = request.getSession();
		login = getParameter( request, "login" );
		password = getParameter( request, "password" );
		connexionjdbc = new ConnexionJDBC();
		success &=checkLogin();
		if(!success){
			errorMessage = "Identifiant inconnu";
		}
		else{
			success &=checkAccountType();	
		}
		s.setAttribute("error",errorMessage);
		connexionjdbc.close();
		return success;
	}

	
	/**
	 * Retourne le paramètre
	 * @param request
	 * @param field
	 * @return la valeur du paramètre
	 */
	private static String getParameter( HttpServletRequest request, String field ) {
		String value = (String) request.getParameter(field);
		if ( value == null || value.trim().length() == 0 ) {
				return null;
		} else {
			return value;
		}
		
	}
	
	/**
	 * Verifie si l'identifiant est correct
	 * @return
	 * @throws SQLException
	 */
	public boolean checkLogin() throws SQLException{
		 PreparedStatement preparedStatement =  connexionjdbc.prepareStatement( "SELECT login, nom , prenom, pwd FROM enseignant where login=?;" );
		 preparedStatement.setString(1, login);
		 resultat = preparedStatement.executeQuery();
		 return resultat.next() ;
	}
	
	/**
	 * Verifie le type du compte de l'utilisateur (RA ou DDE)
	 * @return
	 */
	public boolean checkAccountType(){
		boolean success = true; 
		try {
	            InputStream file = new URL(pathName).openStream();
	            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder =  builderFactory.newDocumentBuilder();
	            Document xmlDocument = builder.parse(file);
	            XPath xPath =  XPathFactory.newInstance().newXPath();
	            String expression1 = "//directeurdesetudes";
	            String expression2 = "//responsable";
	            NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(xmlDocument, XPathConstants.NODESET);
	            for (int i = 0; i < nodeList1.getLength(); i++) {
	                String dde =nodeList1.item(i).getFirstChild().getNodeValue(); 
	                if(dde.equals(login)){
	                return checkPasswordDDE();
	                }
	            }	            
            	NodeList nodeList2 = (NodeList) xPath.compile(expression2).evaluate(xmlDocument, XPathConstants.NODESET);
	            for (int j = 0; j < nodeList2.getLength(); j++) {
	                String ra =nodeList2.item(j).getFirstChild().getNodeValue(); 
	                if(ra.equals(login)){
	                	String expressionpromo = "//personne[responsable='"+login+"']/promo"; 
	                	NodeList nodeListpromo = (NodeList) xPath.compile(expressionpromo).evaluate(xmlDocument, XPathConstants.NODESET);
	                	promo = nodeListpromo.item(0).getFirstChild().getNodeValue();
	                	String expressionnbeleve = "//personne[responsable='"+login+"']/nbeleve"; 
	                	NodeList nodeListnbeleve = (NodeList) xPath.compile(expressionnbeleve).evaluate(xmlDocument, XPathConstants.NODESET);
	                	nbEleves = nodeListnbeleve.item(0).getFirstChild().getNodeValue();
	                	return checkPasswordRA();
	                }
	            }
	               
            	errorMessage="Droit d'accès interdit ";
            	success=false;
	             
			 }
			 catch(Exception e){
				 errorMessage =e.toString();
				 success=false;
		 }
	            
		return success;
	}


	/**
	 * Verifie le MDP du responsable d'année
	 * @return success
	 * @throws SQLException
	 */
	private boolean checkPasswordRA() throws SQLException {
		boolean success =checkPassword();
		if(!success){
			errorMessage="Mot de passe non reconnu";
		}
		else{
			s.setAttribute("logged", true);
			s.setAttribute("superlogged", false);
			s.setAttribute("promo",promo);
			s.setAttribute("login", login);
			s.setAttribute("nom", resultat.getString("nom"));
			s.setAttribute("prenom", resultat.getString("prenom"));
			s.setAttribute("type", "Responsable Année ");
			s.setAttribute("nbeleves", nbEleves);
		}
		return success;
	}


	/**
	 * Verifie le MDP du DDE
	 * @return success
	 * @throws SQLException
	 */
	private boolean checkPasswordDDE() throws SQLException {
		boolean success =checkPassword();
		if(!success){
			errorMessage="Mot de passe non reconnu";
		}
		else{
			s.setAttribute("logged", true);
			s.setAttribute("superlogged", true);
			s.setAttribute("login", login);
			s.setAttribute("nom", resultat.getString("nom"));
			s.setAttribute("prenom", resultat.getString("prenom"));
			s.setAttribute("type", "Directeur des Etudes ");
			
		}
		return success;
		
		
	}
	
	/**
	 * Verifie le MDP directement dans la BDD
	 * @return <code>true</code> si le MDP est correct
	 * @throws SQLException
	 */
	private boolean checkPassword() throws SQLException {
		String pwd =resultat.getString("pwd");
		return pwd.equals(password);
		
	}
	
	
	
	
	
}