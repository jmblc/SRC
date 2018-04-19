package fr.igestion.taglib.html;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class HtmlElementGeneratorTag extends BodyTagSupport {

	private static final long serialVersionUID = -3614318764133162466L;

	private String tagName;	
	private String beanName;	
	private String var;	
	private String idProperty;	
	private String nameProperty;	
	private String valueProperty;	
	private String textProperty;	
	private String otherAttributes;	
	private String mapAttributes;	
	Writer out;	
	Collection<Object> collection;
	Map<String, String[]> mappedAttributes;
	Iterator<Object> iterator;
	int index;
	boolean noBody;
	
	public HtmlElementGeneratorTag() {
	}
	
	private void reset() {
		collection = null;
		mappedAttributes = null;
		iterator = null;
		index = 0;
		noBody = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() {
		
		reset();

		if (beanName != null && beanName.trim().length() > 0) {

			try {
				tagName = (String) ExpressionEvaluatorManager.evaluate("tagName", getTagName(), String.class, null);
				beanName = (String) ExpressionEvaluatorManager.evaluate("beanName", getBeanName(), String.class, null);
				idProperty = idProperty == null ? null : (String) ExpressionEvaluatorManager.evaluate("idProperty", getIdProperty(), String.class, null);
				nameProperty = nameProperty == null ? null : (String) ExpressionEvaluatorManager.evaluate("nameProperty", getNameProperty(), String.class, null);
				valueProperty = valueProperty == null ? null : (String) ExpressionEvaluatorManager.evaluate("valueProperty", getValueProperty(), String.class, null);
				textProperty = textProperty == null ? null : (String) ExpressionEvaluatorManager.evaluate("textProperty", getTextProperty(), String.class, null);
				otherAttributes = otherAttributes == null ? null : (String) ExpressionEvaluatorManager.evaluate("otherAttributes", getOtherAttributes(), String.class, null);
				mapAttributes = mapAttributes == null ? null : (String) ExpressionEvaluatorManager.evaluate("mapAttributes", getMapAttributes(), String.class, null);
				
			} catch (JspException e) {
				e.printStackTrace();
			}
			Object bean = pageContext.findAttribute(beanName);

			if (bean != null && bean instanceof Collection) {
				collection = (Collection<Object>) bean;
			} else if (bean != null) {
				collection = new Vector<Object>();
				collection.add(bean);
			}
		}
		
		if (collection == null || collection.size() == 0) {
			collection = new Vector<Object>();
			collection.add(pageContext);
		} 
			
		out = pageContext.getOut();
		iterator = collection.iterator();
		mappedAttributes = getMappedAttributes(mapAttributes);		

		return processNext() ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
	
	@Override
	public int doAfterBody() {
		
		noBody = false;		
		if (index > 0) {
			try {
				close();				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		int result = processNext() ? EVAL_BODY_AGAIN : SKIP_BODY;

		return result;
	}
	
	@Override
	public int doEndTag() throws JspException {
		
		if (noBody && index > 0) {
			try {
				close();				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			while (processNext()) {
				close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	protected boolean processNext() {

		try {

			if (iterator != null && iterator.hasNext()) {

				Object obj = iterator.next();

				if (StringUtils.isNotBlank(var)) {
					pageContext.setAttribute(var, obj);
					pageContext.setAttribute(var + "_index", index);
				}
				StringBuffer texte = new StringBuffer();
				
				String ecrit = "<" + tagName + " ";
				texte.append(ecrit) ;

				String id = getValue(obj, idProperty);
				if (id != null) {
					ecrit = "id=\"" + id + "\" ";
					texte.append(ecrit) ;			
				}
				String name = getValue(obj, nameProperty);
				if (name != null) {
					ecrit = "name=\"" + name + "\" ";
					texte.append(ecrit) ;			
				}
				String value = getValue(obj, valueProperty);
				if (value != null) {
					ecrit = "value=\"" + value + "\" ";
					texte.append(ecrit) ;		
				}
				Map<String, String> attributes = getOtherAttributes(obj, otherAttributes);
				for (String attributeName : attributes.keySet()) {
					String attributeValue = attributes.get(attributeName);
					ecrit = attributeName + "=\"" + attributeValue + "\" ";
					texte.append(ecrit) ;			
				}
				
				ecrit = getMappedResult(obj);
				if (ecrit != null) {
					texte.append(ecrit) ;
				}	

				ecrit = ">";
				texte.append(ecrit) ;

				ecrit = getValue(obj, textProperty);
				if (ecrit != null) {
					texte.append(ecrit) ;
				}
				
				out.write(texte.toString());
				
				index++;
				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String close() throws IOException {
		String ecrit = "</" + tagName + "> \n";
		out.write(ecrit);
		return ecrit;
	}
	
	protected Map<String, String[]> getMappedAttributes(String mapAttributes) {
		
		HashMap<String, String[]> result = new HashMap<String, String[]>();
		
		if (StringUtils.isNotBlank(mapAttributes)) {
			
			for (String attribute : mapAttributes.split("\\|")) {
				
				String mappingKey = null;
				String mappedAttributeName = null;	
				String mappedValue = null;
				String mappedValueOtherwise = null;	
				
				int pos = attribute.indexOf("->");
				if (pos > 0) {
					mappingKey = attribute.substring(0, pos);
					if (pos+2 < attribute.length()) { 
						String mappedAttribute = attribute.substring(pos+2);
						pos = mappedAttribute.indexOf(":");
						if (pos < 0) {
							pos = mappedAttribute.indexOf("=");
						}
						if (pos > 0) {
							mappedAttributeName = mappedAttribute.substring(0, pos);
							if (pos+1 < mappedAttribute.length()) { 
								mappedValue = mappedAttribute.substring(pos+1);
								if (!"style".equals(mappedAttributeName.trim())) {
									pos = mappedValue.indexOf(":");
									if (pos < 0) {
										pos = mappedValue.indexOf("=");
									}
									if (pos > 0) {									
										if (pos+1 < mappedValue.length()) { 
											mappedValueOtherwise = mappedValue.substring(pos+1);
										}
										mappedValue = mappedValue.substring(0, pos);
									}
								}								
							}
						}
					}
				}
				if (mappingKey != null && mappedAttributeName != null && mappedValue != null) {
					String[] mappedResult = new String[2];
					mappedResult[0] = mappedAttributeName.trim() + "=\"" + mappedValue.trim() + "\" ";
					if (mappedValueOtherwise != null) {
						mappedResult[1] = mappedAttributeName.trim() + "=\"" + mappedValueOtherwise.trim() + "\" ";
					}
					result.put(mappingKey.trim(), mappedResult);
				}
			}
		}
		return result;
	}
	
	protected String getMappedResult(Object obj) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		StringBuffer mappedResult = new StringBuffer();
		
		Vector<String> mapped = new Vector<String>();		
		
		if (obj != null && mappedAttributes != null && !mappedAttributes.isEmpty()) {
			
			for (String mappedAttribute : mappedAttributes.keySet()) {
				boolean rechercheEgalite = false;
				boolean rechercheDifference = false;
				int pos = mappedAttribute.indexOf(":");
				if (pos < 0) {
					pos = mappedAttribute.indexOf("=");
				}
				if (pos > 0) {
					rechercheEgalite = true;
				} else {
					pos = mappedAttribute.indexOf("#");
					if (pos > 0) {
						rechercheDifference = true;
					}
				}
				if (pos > 0) {
					String mappedAttributeName = mappedAttribute.substring(0, pos).trim();
					if (!mapped.contains(mappedAttributeName)) {
						String mappedAttributeValue = null;
						if (pos + 1 < mappedAttribute.length()) {
							mappedAttributeValue = mappedAttribute.substring(pos + 1).trim();
						}
						String checkedValue = String.valueOf(getValue(obj, mappedAttributeName));
						String result = null;
						if ((checkedValue.equals(mappedAttributeValue) && rechercheEgalite) || (!checkedValue.equals(mappedAttributeValue) && rechercheDifference)) {
							result = mappedAttributes.get(mappedAttribute)[0];
						} else if (mappedAttributes.get(mappedAttribute).length > 1) {
							result = mappedAttributes.get(mappedAttribute)[1];
						}
						if (result != null) {
							mappedResult.append(result);
							mapped.add(mappedAttributeName);
						}
					}
				}
			}
		}
		
		return mappedResult.toString();
		
	}
	
	protected Map<String, String> getOtherAttributes(Object obj, String otherAttributes) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		HashMap<String, String> result = new HashMap<String, String>();
		
		if (StringUtils.isNotBlank(otherAttributes)) {
			
			for (String attribute : otherAttributes.split(" ")) {
				String attributeName = null;
				String attributeValue = null;				
				int pos = attribute.indexOf(":");
				if (pos < 0) {
					pos = attribute.indexOf("=");
				}
				if (pos > 0) {
					attributeName = attribute.substring(0, pos);
					if (pos+1 < attribute.length()) { 
						attributeValue = getValue(obj, attribute.substring(pos+1));
						if (attributeValue == null) {
							attributeValue = attribute.substring(pos+1);
						}
					}
				} else {
					attributeName = attribute;
					attributeValue = getValue(obj, attributeName);
				}
				if (attributeName != null && attributeValue != null) {
					result.put(attributeName, attributeValue);
				}
			}
		}
		return result;
	}
	
	
	protected String getValue(Object obj, String attributeName) throws SecurityException, IllegalAccessException, 
		IllegalArgumentException, InvocationTargetException {
		
		String result = null;
		
		if (attributeName != null) {

			if (obj == pageContext) {
				Object attr = pageContext.findAttribute(attributeName);
				if (attr != null) {
					result = String.valueOf(attr);
				}
			} else if (obj instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) obj;
				if (map.get(attributeName) != null) {
					result = String.valueOf(map.get(attributeName));
				}
			} else {
				try {
					Method method = getGetter(obj, attributeName);
					if (method != null) {
						result = "";
						Object tmp = method.invoke(obj);
						if (tmp != null) {
							result = tmp.toString();
						}
					}
				} catch (NoSuchMethodException e) {
				}
			}
		}
		return result;
	}
	
	protected Method getGetter(Object obj, String attributeName) throws NoSuchMethodException, SecurityException { 
		
		Method result = null;
		if (obj != null && StringUtils.isNotBlank(attributeName)) {
			result = obj.getClass().getMethod("get" + StringUtils.capitalize(attributeName));
		}
		return result;
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

	/**
	 * @return the beanName
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @param beanName the beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * @return the valueProperty
	 */
	public String getValueProperty() {
		return valueProperty;
	}

	/**
	 * @param valueProperty the valueProperty to set
	 */
	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}

	/**
	 * @return the mapAttributes
	 */
	public String getMapAttributes() {
		return mapAttributes;
	}

	/**
	 * @param mapAttributes the mapAttributes to set
	 */
	public void setMapAttributes(String mapAttributes) {
		this.mapAttributes = mapAttributes;
	}


	/**
	 * @return the var
	 */
	public String getVar() {
		return var;
	}


	/**
	 * @param var the var to set
	 */
	public void setVar(String var) {
		this.var = var;
	}

}
