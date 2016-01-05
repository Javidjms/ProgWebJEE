package Model;

import java.util.List;

public interface ModuleDAO {
	public Module getModule(String id);
	public List<Module> getModules();
	public Course getCourse(String id);
	public void updateCourse(Course course);
}
