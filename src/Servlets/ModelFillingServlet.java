package Servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Controller.ModelFillingMain;

/**
 *Servlet de la page de peuplement de la table module
 */
public class ModelFillingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageName="/modulefilling.jsp";
		String redirectionPageName="/index.jsp";
		ModelFillingMain model = new ModelFillingMain();
		HttpSession session = request.getSession();
		boolean superlogged =(boolean) (session.getAttribute("superlogged") != null?session.getAttribute("superlogged"):false);
		if(!superlogged){
			pageName =redirectionPageName;
			}
		else{
			ArrayList<String> messages = model.execute(); 
			request.setAttribute("messages", messages);
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
}
