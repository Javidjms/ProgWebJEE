package Model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Modules {
	private ArrayList<Module> moduleList;
	
	@XmlElement(name = "module")
	public void setModuleList(ArrayList<Module> moduleList) {
	    this.moduleList = moduleList;
	  }

	  public ArrayList<Module> getModuleList() {
		  System.out.println(">>> Modules : getModuleList ");
	    return moduleList;
	  }
	  private void dumpList(){
		  if(this.moduleList != null){
			
			for (Module module : this.moduleList) {
			      System.out.println("Module: " + module.getId());
			    }
		  }
	  }
}
