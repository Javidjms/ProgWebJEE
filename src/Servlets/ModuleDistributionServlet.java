package Servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Connexion.ConnexionJDBC;
import Model.Course;
import Model.InfoBlock;
import Model.ModuleDAO;
import Model.ModuleDAOImpl;
import Model.SubBlock;
import Model.TeachingActivity;

/**
 *Servlet de la page de découpage d'un module
 */
@WebServlet("/moduledistribution")
public class ModuleDistributionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String promo;
	private String module;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageName="/moduledistribution1.jsp";
		String redirectionPageName="/index.jsp";
		HttpSession session = request.getSession();
		boolean superlogged =(boolean) (session.getAttribute("superlogged") != null?session.getAttribute("superlogged"):false);
		boolean logged =(boolean) (session.getAttribute("logged") != null?session.getAttribute("logged"):false);
		if(superlogged || !logged){
			pageName =redirectionPageName;
		}
		else{
			promo = (String) session.getAttribute("promo");
			try {
				ConnexionJDBC connexionjdbc = new ConnexionJDBC();
				getModuleListbyPromo(request,connexionjdbc);
				connexionjdbc.close();
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(pageName);
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    
	}

	


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageName="/moduledistribution2.jsp";
		String redirectionPageName="/index.jsp";
		HttpSession session = request.getSession();
		boolean superlogged =(boolean) (session.getAttribute("superlogged") != null?session.getAttribute("superlogged"):false);
		boolean logged =(boolean) (session.getAttribute("logged") != null?session.getAttribute("logged"):false);
		if(superlogged || !logged){
			pageName =redirectionPageName;
		}
		else{
		    module = (String) (request.getParameter("input") != null?request.getParameter("input"):" ");
			request.setAttribute("module", module);
			try {
				setVolumeHoraire(request,module);
				ConnexionJDBC connexionjdbc = new ConnexionJDBC();
				getEnseignantList(request,connexionjdbc);
				getContenuModuleList(request, connexionjdbc);
				connexionjdbc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(pageName);
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Recupere la liste des modules par promo
	 * @param request
	 * @param connexion
	 * @throws SQLException
	 */
	private void getModuleListbyPromo(HttpServletRequest request,
			ConnexionJDBC connexion) throws SQLException {
		PreparedStatement preparedStatement = connexion.prepareStatement("SELECT module,libelle from module where public like ? order by libelle");
		preparedStatement.setString( 1, "%"+promo+"%" );
		ResultSet resultat =preparedStatement.executeQuery();
		ArrayList<HashMap<String,String>> resultat2= new ArrayList<HashMap<String,String>>();
		while(resultat.next()){
			HashMap<String,String> row = new HashMap<String,String>();
			row.put("id",resultat.getString("module"));
			row.put("libelle",resultat.getString("libelle"));
			resultat2.add(row);
		}
		request.setAttribute("modulelist", resultat2);
		preparedStatement.close();
		
		
	}
	
	/**
	 * Recupere et Met a jour les volumes horaires
	 * @param request
	 * @param module
	 */
	private void setVolumeHoraire(HttpServletRequest request,String module){
		ModuleDAO moduleDAO = ModuleDAOImpl.getInstance();
		Course course =moduleDAO.getCourse(module);
		TeachingActivity teachingactivity = course.getTeachingActivity();
		InfoBlock infoblock=teachingactivity.getInfoBlock();
		int VolumeHoraireCM=0;
		int VolumeHoraireTd=0;
		int VolumeHoraireTp=0;
		for(SubBlock sublock:infoblock.getSubBlockList()){
			switch (sublock.getUserDefined()) {
			case "volumeHoraireCM":
				VolumeHoraireCM=Integer.parseInt(sublock.getDescription());
				break;

			case "volumeHoraireTd":
				VolumeHoraireTd=Integer.parseInt(sublock.getDescription());
				break;
				
			case "volumeHoraireTp":
				VolumeHoraireTp=Integer.parseInt(sublock.getDescription());
				break;
			default:
				break;
			}
		}
		request.setAttribute("VolumeHoraireCM", ""+VolumeHoraireCM);
		request.setAttribute("VolumeHoraireTd", ""+VolumeHoraireTd);
		request.setAttribute("VolumeHoraireTp", ""+VolumeHoraireTp);
	}
	/**
	 * Retourne la liste des enseignants
	 * @param request
	 * @param connexion
	 * @throws SQLException
	 */
	private void getEnseignantList(HttpServletRequest request, ConnexionJDBC connexion) throws SQLException{
		Statement statement = connexion.getStatement();
		ResultSet resultat =statement.executeQuery("SELECT login from enseignant");
		ArrayList<String> resultat2= new ArrayList<String>();
		resultat2.add("null");
		while(resultat.next()){
			resultat2.add(resultat.getString("login"));
		}
		request.setAttribute("enseignantlist", resultat2);
		statement.close();
	}
	
	/**
	 * Retourne la liste des contenus modules
	 * @param request
	 * @param connexion
	 * @throws SQLException
	 */
	private void getContenuModuleList(HttpServletRequest request, ConnexionJDBC connexion) throws SQLException{
		PreparedStatement preparedStatement = connexion.prepareStatement("SELECT partie,type,nbHeures,enseignant from contenumodule where module = ?");
		preparedStatement.setString( 1, module );
		ResultSet resultat =preparedStatement.executeQuery();
		ArrayList<HashMap<String,String>> resultat2= new ArrayList<HashMap<String,String>>();
		while(resultat.next()){
			HashMap<String,String> row = new HashMap<String,String>();
			row.put("partie",resultat.getString("partie"));
			row.put("type",resultat.getString("type"));
			row.put("nbHeures",resultat.getString("nbHeures"));
			row.put("enseignant",resultat.getString("enseignant"));
			resultat2.add(row);
		}
		request.setAttribute("contenumodulelist", resultat2);
		preparedStatement.close();
	}


}
