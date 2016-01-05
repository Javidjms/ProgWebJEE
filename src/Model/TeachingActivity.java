package Model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "teachingActivity")
public class TeachingActivity {
	private InfoBlock infoBlock;
	
	@XmlElement(name="infoBlock")
	public InfoBlock getInfoBlock() {
		return this.infoBlock;
	}

	public void setInfoBlock(InfoBlock ib) {
		this.infoBlock = ib;
	}
	
	

}
