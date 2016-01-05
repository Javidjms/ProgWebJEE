package Model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;





@XmlRootElement(name = "LearningObjectives")
public class LearningObjectives {

	private ArrayList<SubBlock> subBlockList;
	
	@XmlElement(name="subBlock")
	public ArrayList<SubBlock> getSubBlockList() {
		return this.subBlockList;
	}

	public void setSubBlockList(ArrayList<SubBlock> list) {
		this.subBlockList = list;
	}
	
	
}
