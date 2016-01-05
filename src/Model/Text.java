package Model;

import javax.xml.bind.annotation.XmlValue;

public class Text {
	
	@XmlValue
	private String text;

	public Text(String string) {
		this.text=string;
		// TODO Auto-generated constructor stub
	}
	public Text() {
		// TODO Auto-generated constructor stub
	}

	
	public String getText(){return this.text;}
	//public void setText(String val){this.text=val;}
	

}
