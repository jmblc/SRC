package fr.igestion.crm.config;

public interface IObjectPersistence {
	
	public void initParams(Object... params);
	public Object loadObject(String id) throws Exception;
	public void saveObject(String id, Object obj) throws Exception;

}
