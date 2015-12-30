package com.irfan.findbug;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.owasp.webgoat.session.DatabaseUtilities;
import org.owasp.webgoat.session.WebgoatContext;

public class DatabaseUtilitiesTest {

	@Test
	public void testFunctionGetHsqldbConnectionEmptyPassword() throws NoSuchFieldException, SQLException, IllegalArgumentException, IllegalAccessException {
		//prepare for webgoatcontext
		HttpServlet servlet = new HttpServlet() {
			@Override
			public String getInitParameter(String name) {
				if (WebgoatContext.DATABASE_PASSWORD.equals(name)){
					return "";
				}else if (WebgoatContext.DATABASE_DRIVER.equals(name)){
					return "java.lang.reflect.Field";
				}
				return "hsqldb";
			}
			
			@Override
			public String getServletName() {
				return "hsqldb";
			}
		};
		WebgoatContext context = new WebgoatContext(servlet);
		
		//set private field realConnectionString
		Field field = WebgoatContext.class.getDeclaredField("realConnectionString");
		field.setAccessible(true);
		field.set(context, "hsqldb");
		
		try{
			DatabaseUtilities.getConnection("irfan", context);
		}catch (Exception e){
			assertThat(e.getMessage(), CoreMatchers.equalTo("Couldn't load the database driver: " + "Password database cannot be null or empty"));
		}
	}
	
	@Test
	public void testFunctionGetHsqldbConnectionNullPassword() throws NoSuchFieldException, SQLException, IllegalArgumentException, IllegalAccessException {
		//prepare for webgoatcontext
		HttpServlet servlet = new HttpServlet() {
			@Override
			public String getInitParameter(String name) {
				if (WebgoatContext.DATABASE_PASSWORD.equals(name)){
					return null;
				}else if (WebgoatContext.DATABASE_DRIVER.equals(name)){
					return "java.lang.reflect.Field";
				}
				return "hsqldb";
			}
			
			@Override
			public String getServletName() {
				return "hsqldb";
			}
		};
		WebgoatContext context = new WebgoatContext(servlet);
		
		//set private field realConnectionString
		Field field = WebgoatContext.class.getDeclaredField("realConnectionString");
		field.setAccessible(true);
		field.set(context, "hsqldb");
		
		try{
			DatabaseUtilities.getConnection("irfan", context);
		}catch (Exception e){
			assertThat(e.getMessage(), CoreMatchers.equalTo("Couldn't load the database driver: " + "Password database cannot be null or empty"));
		}
	}
}
