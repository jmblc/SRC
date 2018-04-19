package fr.igestion.crm.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectPersitenceJson implements IObjectPersistence {
	
	private static final GsonBuilder builder = new GsonBuilder();
	private static final Gson gson = builder.create();

	String directory = "";
	Class<?> mappedClass = null;	
	

	public ObjectPersitenceJson() {
	}

	@Override
	public Object loadObject(String id) throws IOException {
		Object result = null;
		File saveFile = new File(directory.concat(id));
		System.out.println(saveFile.getAbsolutePath());		
		BufferedReader reader = new BufferedReader(new FileReader(saveFile));
		result = gson.fromJson(reader, mappedClass);
		reader.close();
		return result;
	}

	@Override
	public void saveObject(String id, Object obj) throws IOException {
		File saveFile = new File(directory.concat(id));
		BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
		gson.toJson(obj, writer);
		writer.close();
	}

	@Override
	public void initParams(Object... params) {
		if (params != null && params.length > 0) {
			this.directory = (String) params[0];
		}
		if (params != null && params.length > 1) {
			this.mappedClass = (Class<?>) params[1];
		}
	}

}
