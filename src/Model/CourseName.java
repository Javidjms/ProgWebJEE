package Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "courseName")
public class CourseName {
	private Text text;
	
	@XmlElement(name="text")
	public Text getCourseName() {
		return text;
	}

	public void setCourseName(Text text) {
		this.text = text;
	}
	
	

}
