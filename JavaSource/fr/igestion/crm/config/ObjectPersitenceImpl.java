package fr.igestion.crm.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectPersitenceImpl implements IObjectPersistence {
	
	String directory = "";

	public ObjectPersitenceImpl() {	
	}
	
	@Override
	public Object loadObject(String id) throws IOException, ClassNotFoundException {
		Object result = null;
		File saveFile = new File(directory.concat(id));
		System.out.println(saveFile.getAbsolutePath());
		final FileInputStream fichier = new FileInputStream(saveFile);
		ObjectInputStream ois = new ObjectInputStream(fichier);
		result = ois.readObject();
		ois.close();
		return result;
	}	

	@Override
	public void saveObject(String id, Object obj) throws IOException {
		ObjectOutputStream oos = null;
		File saveFile = new File(directory.concat(id));
		final FileOutputStream fichier = new FileOutputStream(saveFile);
		oos = new ObjectOutputStream(fichier);
		oos.writeObject(obj);
		oos.close();
	}

	@Override
	public void initParams(Object... params) {
		if (params != null && params.length > 0) {
			this.directory = (String) params[0];
		}
		
	}

}
