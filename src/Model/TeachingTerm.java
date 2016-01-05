package Model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class TeachingTerm {
	

	@XmlValue
    protected String value;


	public String getValue() {
		return value;
	}

	public void setValue(String val)
	{
		this.value =val;
	
	}
}
