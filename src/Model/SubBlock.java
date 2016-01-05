package Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SubBlock {

    @XmlAttribute
    protected String userDefined;


    public String getUserDefined() {
		return userDefined;
	}


	public void setUserDefined(String userDefined) {
		this.userDefined = userDefined;
	}


	@XmlValue
    protected String description;


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

}

/*
@XmlRootElement(name = "subBlock")
public class SubBlock {
	private String userDefined;
	
	@XmlAttribute
	public String getUserDefined() {
		return this.userDefined;
	}
	public void setUserDefined(String ud) {
		this.userDefined = ud;
	}
	
}
*/