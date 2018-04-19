package fr.igestion.taglib.html;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class CollectionTag extends SimpleTagSupport {

	public CollectionTag() {
		// TODO Auto-generated constructor stub
	}
	
	private String collectionName;
	
	private String tagName;
	
	private String idProperty;
	
	private String nameProperty;
	
	private String textProperty;
	
	private String otherAttributes;
	
	@Override
	public void doTag() {
		
		try {

			if (collectionName != null && collectionName.trim().length() > 0) {

				String collectionName = (String) ExpressionEvaluatorManager.evaluate("collectionName", getCollectionName(), String.class, null);

				Collection<?> collection = (Collection<?>) getJspContext().findAttribute(collectionName);

				if (collection != null && collection.size() > 0) {

					Writer out = getJspContext().getOut();
					String tagName = (String) ExpressionEvaluatorManager.evaluate("tagName", getTagName(), String.class, null);
					String idProperty = this.idProperty == null ? null :(String) ExpressionEvaluatorManager.evaluate("idProperty", getIdProperty(), String.class, null);
					String nameProperty = this.nameProperty == null ? null :(String) ExpressionEvaluatorManager.evaluate("nameProperty", getNameProperty(), String.class, null);
					String textProperty = this.textProperty == null ? null : (String) ExpressionEvaluatorManager.evaluate("textProperty", getTextProperty(), String.class, null);
					String otherAttributes = this.otherAttributes == null ? null : (String) ExpressionEvaluatorManager.evaluate("otherAttributes", getOtherAttributes(), String.class, null);

					for (Object obj : collection) {

						out.write("<" + tagName + " ");

						String id = getValue(obj, idProperty);
						if (id != null) {
							out.write("id=\"" + getValue(obj, idProperty) + "\" ");
						}

						String name = getValue(obj, nameProperty);
						if (name != null) {
							out.write("name=\"" + getValue(obj, nameProperty) + "\" ");
						}

						if (StringUtils.isNotBlank(otherAttributes)) {
							for (String attribute : otherAttributes.split(" ")) {
								int pos = attribute.indexOf("=");
								String attributeName = null;
								String attributeValue = null;
								if (pos > 0) {
									attributeName = attribute.substring(0, pos);
									if (pos+1 < attribute.length()) { 
										attributeValue = getValue(obj, attribute.substring(pos+1));
									}										
									if (attributeValue == null) {
										attributeValue = attribute.substring(pos+1);
									}
								} else {
									attributeName = attribute;
									attributeValue = getValue(obj, attributeName);
								}
								if (attributeName != null) {
									String value = attributeValue == null ? "" : attributeValue;
									out.write(attributeName + "=\"" + value + "\" ");
								}
							}
						}

						out.write(">");

						String text = getValue(obj, textProperty);
						if (text != null) {
							out.write(text);
						}

						out.write("</" + tagName + ">");
						out.write("\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private String getValue(Object obj, String attributeName) throws SecurityException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException {
		
		String result = null;
		try {
			Method method = getGetter(obj, attributeName);
			if (method != null) {
				result = method.invoke(obj).toString();
			}
		} catch (NoSuchMethodException e) {
		}
		return result;
	}
	
	private Method getGetter(Object obj, String attributeName) throws NoSuchMethodException, SecurityException { 
		
		Method result = null;
		if (obj != null && StringUtils.isNotBlank(attributeName)) {
			result = obj.getClass().getMethod("get" + StringUtils.capitalize(attributeName));
		}
		return result;
	}
	/**
	 * @return the collection
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * @return the idProperty
	 */
	public String getIdProperty() {
		return idProperty;
	}

	/**
	 * @param idProperty the idProperty to set
	 */
	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}

	/**
	 * @return the nameProperty
	 */
	public String getNameProperty() {
		return nameProperty;
	}

	/**
	 * @param nameProperty the nameProperty to set
	 */
	public void setNameProperty(String nameProperty) {
		this.nameProperty = nameProperty;
	}

	/**
	 * @return the textProperty
	 */
	public String getTextProperty() {
		return textProperty;
	}

	/**
	 * @param textProperty the textProperty to set
	 */
	public void setTextProperty(String textProperty) {
		this.textProperty = textProperty;
	}

	/**
	 * @return the otherAttributes
	 */
	public String getOtherAttributes() {
		return otherAttributes;
	}

	/**
	 * @param otherAttributes the otherAttributes to set
	 */
	public void setOtherAttributes(String otherAttributes) {
		this.otherAttributes = otherAttributes;
	}

}
