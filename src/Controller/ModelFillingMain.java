package Controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.*;
import Connexion.ConnexionJDBC;

public class ModelFillingMain {
	private ArrayList<String> messages= new ArrayList<String>();
	
	public static int conversionYear(String stringYear){
		int year=-1;
		try{
			year=Integer.parseInt(stringYear);
			year-=2;
		}catch(Exception e){
			
		}
		return year;
	}
	
	public static String conversionPromo(String spublic){
		String promo="";
		if(spublic.contains("L")){
			promo="LSI";
		}
		else if(spublic.contains("E")){
			promo="EII";
		}
		else if(spublic.contains("O")){
			promo="OPT";
		}
		else if(spublic.contains("I")){
			promo="IMR";
		}
		else if(spublic.contains("Z")){
			promo="Tous";
		}
		else{
			promo="";
		}
		
		return promo;
	}
	
	public  ArrayList<String> execute() {
		ModuleDAO moduleDAO = ModuleDAOImpl.getInstance();
		
		ConnexionJDBC connexionjdbc=null;
		messages.add("Connexion à la base de données");
		try {
			connexionjdbc = new ConnexionJDBC();
			messages.add("Connexion à la base de données réussie ");
		} catch (ClassNotFoundException | SQLException e1) {
			messages.add("<b><font color=red>Echec de connexion à la base de données : "+e1.getMessage()+"</font></b>");
		}
		PreparedStatement preparedStatement = null;		
		ArrayList<Module> moduleList = (ArrayList<Module>) moduleDAO.getModules();
		for (Module module : moduleList) {
			String moduleId =module.getId();
			 String[] IdSplitted1 =moduleId.split("_");
			 String[] IdSplitted2 =IdSplitted1[1].split("[0-9]",2);
			 String spublic = IdSplitted2[0];
			 String yearC= IdSplitted1[1].replaceAll("[^0-9]", "").substring(0,1);
			 int year = conversionYear(yearC);
			 String promo = conversionPromo(spublic);
			 if(year !=-1 && promo!=""){
				 String y;
				 if(promo!="Tous")
					 y = Integer.toString(year);
				 else
					 y = "";
				 promo=promo.concat(y);
			 }
			 else{
				 continue;
			 }
			 Course c =moduleDAO.getCourse(moduleId);
			 String libelle = c.getCourseName().getCourseName().getText();
			 libelle= libelle.substring(0,(libelle.length()>40?40:libelle.length()));
			 TeachingTerm teachingterm = c.getTeachingTerm();
			// String level= c.getLevel().getValue();
			 String semestre="";
			 if(year==3){
				 semestre="S1";
			 }
			 else if(teachingterm==null){
				 semestre="AN";
			 }
			 else{
				 semestre=(!(teachingterm.getValue().isEmpty())?teachingterm.getValue():"S1");
			 }
			 try {
				 preparedStatement = connexionjdbc.prepareStatement( "INSERT INTO Module (module, public, semestre, libelle,responsable) VALUES(?,?,?,?,NULL);" );
				 preparedStatement.setString( 1, moduleId );
				 preparedStatement.setString( 2, promo );
				 preparedStatement.setString( 3, semestre );
				 preparedStatement.setString( 4, libelle );
				 preparedStatement.executeUpdate();
				 messages.add("<b><font color=green>Insertion du module:"+moduleId+" libelle:"+libelle+" dans la base de données effectuées avec succès</font></b>");
			 } catch (SQLException e) {
				messages.add("<b><font color=red>Erreur lors de l'insertion de : "+moduleId+" Cause:"+e.getMessage()+"</font></b>");
			}
			 				 
			
			 
		}
		messages.add( "Fin de la Requete." );
		 if ( preparedStatement != null ) {
			 try {
				 preparedStatement.close();
			 } catch ( SQLException ignore ) {}
			 	
		 }
		 messages.add( "Fin de la Connection." );
		 if ( connexionjdbc != null ) {
			 connexionjdbc.close();
		 }
		 
		 return messages;
		
		
	}

}
