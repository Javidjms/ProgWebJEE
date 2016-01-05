package Model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "course")
public class Course {
	private String id;
	private CourseName courseName;
	private CourseDescription courseDescription;
	private TeachingActivity teachingActivity;
	private TeachingTerm teachingTerm;
	private LearningObjectives learningObjectives;
	
	@XmlElement(name="learningObjectives")
	public LearningObjectives getLearningObjectives() {
		return learningObjectives;
	}

	public void setLearningObjectives(LearningObjectives learningObjectives) {
		this.learningObjectives = learningObjectives;
	}

	private Level level;
	
	@XmlElement(name="level")
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	@XmlElement(name="courseName")
	public CourseName getCourseName() {
		return courseName;
	}
	
	public void setCourseName(CourseName courseName) {
		this.courseName = courseName;
	}
	@XmlAttribute(name = "id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name="courseDescription")
	public CourseDescription getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(CourseDescription courseDescription) {
		this.courseDescription = courseDescription;
	}
	@XmlElement(name="teachingActivity")
	public TeachingActivity getTeachingActivity() {
		return teachingActivity;
	}

	public void setTeachingActivity(TeachingActivity teachingActivity) {
		this.teachingActivity = teachingActivity;
	}
	
	@XmlElement(name="teachingTerm")
	public TeachingTerm getTeachingTerm() {
		return teachingTerm;
	}
	
	public void setTeachingTerm(TeachingTerm teachingTerm) {
		this.teachingTerm = teachingTerm;
	}
	
}
