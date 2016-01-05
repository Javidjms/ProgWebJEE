package Model;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;

import org.glassfish.jersey.client.ClientResponse;

public class ModuleDAOImpl implements ModuleDAO {
	
	private  JAXBContext contextCourse;
	
	private ModuleDAOImpl(){
		//singleton
		try {
			contextCourse = JAXBContext.newInstance(Course.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Instance unique pré-initialisée */
	private static ModuleDAOImpl INSTANCE = new ModuleDAOImpl();
	
	/** Point d'accès pour l'instance unique du singleton */
	public static ModuleDAOImpl getInstance()
	{	
		return INSTANCE;
	}
	
	@Override
	public Module getModule(String id) {        
		return null;
	}

	@Override
	public List<Module> getModules() {
		
		ArrayList<Module> moduleList = null;
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://127.0.0.1:8081").path("Modules");

        Builder builder =   target.request();
      
        String result  = builder.get(String.class);
        JAXBContext context;
		try {
			context = JAXBContext.newInstance(Modules.class);
			Unmarshaller um = context.createUnmarshaller();
			Modules modules = (Modules) um.unmarshal(new StringReader(result));
			moduleList = modules.getModuleList();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
     
		return moduleList;
	}
	public Course getCourse(String id){
		Course course = null;
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://127.0.0.1:8081").path("Course/"+id);
        
        Builder builder =   target.request();
        String result  = builder.get(String.class);
        
		try {		
			Unmarshaller um = contextCourse.createUnmarshaller();
			course = (Course) um.unmarshal(new StringReader(result));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return course;
	}

	@Override
	public void updateCourse(Course course) {
		
		Client client = ClientBuilder.newClient();
		String id = course.getId();
        WebTarget target = client.target("http://127.0.0.1:8081").path("Course/"+id);
       
        StringWriter body = new StringWriter();
		try {
			Marshaller marshaller = contextCourse.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marshaller.marshal(course, body);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
       
        Response postResponse = target.request(MediaType.TEXT_PLAIN)
                        .put(Entity.entity(body.toString(), MediaType.APPLICATION_XML));
        //System.out.println("retour HTTP : "+postResponse.getStatus()+" "+postResponse.toString());
	}
}
