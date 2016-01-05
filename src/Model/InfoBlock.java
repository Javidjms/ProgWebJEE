package Model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;

public class InfoBlock {

	private ArrayList<SubBlock> subBlockList;
	
	@XmlElement(name="subBlock")
	public ArrayList<SubBlock> getSubBlockList() {
		return this.subBlockList;
	}

	public void setSubBlockList(ArrayList<SubBlock> list) {
		this.subBlockList = list;
	}
	
}
