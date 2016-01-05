package Servlets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Connexion.ConnexionJDBC;
import Form.LoginForm;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/index.jsp")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      

    protected void doGet( HttpServletRequest request,HttpServletResponse response ) throws ServletException, IOException{
    		String pageName="/login.jsp";
    		try {
				ConnexionJDBC connexionjdbc = new ConnexionJDBC();
				if(enseignantIsEmpty(connexionjdbc) || semaineIsEmpty(connexionjdbc)){
					fillEnseignant(connexionjdbc);
					fillSemaine(connexionjdbc);
				}
			} catch (ClassNotFoundException | SQLException | XPathExpressionException | ParserConfigurationException | SAXException e) {
				
				e.printStackTrace();
			}
    		
    		/* Affichage de la page de connexion */
    		HttpSession session = request.getSession();
    		boolean superlogged =(session.getAttribute("superlogged")!=null?(boolean)session.getAttribute("superlogged"):false);
    		boolean logged =(session.getAttribute("logged")!=null?(boolean)session.getAttribute("logged"):false);
    		if(superlogged){
    			pageName="/menuDDE.jsp";
    		}
    		else if(!superlogged && logged){
    			pageName="/menuRA.jsp";
    		}
    		this.getServletContext().getRequestDispatcher(pageName).forward( request, response );
    }


	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String pageName;
		/* Récupération de la session depuis la requête */
		HttpSession session = request.getSession();
		LoginForm form = new LoginForm();
		try {
			form.userConnect(request);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		String errorMessage = (String) session.getAttribute("error");
		if(!errorMessage.equals("")){
			request.setAttribute("error", errorMessage);
			pageName="/login.jsp";
		}
		else{
			request.setAttribute("error","");
			boolean superlogged =(boolean) session.getAttribute("superlogged");
			pageName=(superlogged?"/menuDDE":"/menuRA");
		}
		this.getServletContext().getRequestDispatcher(pageName).forward( request, response );
	}
	
	/**
	 * Retourne si la table Semaine est vide
	 * @param connexionjdbc
	 * @return
	 * @throws SQLException
	 */
	private boolean semaineIsEmpty(ConnexionJDBC connexionjdbc) throws SQLException {
		ResultSet resultat =  connexionjdbc.getStatement().executeQuery( "SELECT * from semaine" );
		return !resultat.next() ;
	}

	/**
	 * Retourne si la table Enseigne est vide
	 * @param connexionjdbc
	 * @return
	 * @throws SQLException
	 */
	private boolean enseignantIsEmpty(ConnexionJDBC connexionjdbc) throws SQLException {
		ResultSet resultat =  connexionjdbc.getStatement().executeQuery( "SELECT * from enseignant" );
		return !resultat.next() ;
	}

	/**
	 * Remplit la table Semaine
	 * @param connexionjdbc
	 * @throws SQLException
	 */
	private void fillSemaine(ConnexionJDBC connexionjdbc) throws SQLException {
		for(int i=3;i<41;i++){
			PreparedStatement preparedStatement=connexionjdbc.prepareStatement( "INSERT INTO semaine (semaine,nombreHeuresMax) values (?,32) ON DUPLICATE KEY UPDATE nombreHeuresMax=VALUES(nombreHeuresMax)");
			preparedStatement.setInt(1, i);
			preparedStatement.executeUpdate();
		}
		int[] tabVacance = {9,17,18,25,33};
		for(int j:tabVacance){
			PreparedStatement preparedStatement=connexionjdbc.prepareStatement( "INSERT INTO semaine (semaine,nombreHeuresMax) values (?,0) ON DUPLICATE KEY UPDATE nombreHeuresMax=VALUES(nombreHeuresMax)");
			preparedStatement.setInt(1, j);
			preparedStatement.executeUpdate();
		}
	}

	/**
	 * 
	 * Remplit la table Enseignant
	 * @param connexionjdbc
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws SQLException
	 */
	private void fillEnseignant(ConnexionJDBC connexionjdbc) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, SQLException {
		String pathName = "http://localhost:8080/PlanningParSemaineJ2EE/responsables.xml";
		InputStream file = new URL(pathName).openStream();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder =  builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(file);
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression1 = "//personne";
        NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(xmlDocument, XPathConstants.NODESET);
        for (int i = 0; i < nodeList1.getLength(); i++) {
        	 String login = ((NodeList) xPath.compile("//personne/*[1]").evaluate(xmlDocument, XPathConstants.NODESET)).item(i).getFirstChild().getNodeValue();
        	 String nom = ((NodeList) xPath.compile("//nom").evaluate(xmlDocument, XPathConstants.NODESET)).item(i).getFirstChild().getNodeValue();
	         String prenom = ((NodeList) xPath.compile("//prenom").evaluate(xmlDocument, XPathConstants.NODESET)).item(i).getFirstChild().getNodeValue();
	         PreparedStatement preparedStatement =connexionjdbc.prepareStatement("INSERT INTO enseignant (login,nom,prenom,statut) values (?,?,?,'titulaire') on duplicate key UPDATE nom=values(nom),prenom=values(prenom),statut=values(statut)");
	         preparedStatement.setString(1, login);
	         preparedStatement.setString(2, nom);
	         preparedStatement.setString(3, prenom);
	         preparedStatement.executeUpdate();
	         preparedStatement.close();
        	}
        }


}
 