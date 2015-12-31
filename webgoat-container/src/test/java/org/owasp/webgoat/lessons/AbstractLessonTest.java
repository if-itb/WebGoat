package org.owasp.webgoat.lessons;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.owasp.webgoat.session.WebSession;

public class AbstractLessonTest  {

    private AbstractLesson lesson = new AbstractLesson() {
        protected Element createContent(WebSession s) {
            return new ElementContainer();
        }
        public Category getCategory() {
            return Category.XSS;
        }
        protected Integer getDefaultRanking() { 
            return new Integer(5);
        }
        protected Category getDefaultCategory() {
            return Category.INTRODUCTION;
        }
        protected boolean getDefaultHidden() {
            return false;
        }
        protected List<String> getHints(WebSession s) {
            return Arrays.<String>asList();
        }
        public String getInstructions(WebSession s) {
            return "Instructions";
        }
        public String getTitle() {
            return "title";
        }
        public String getCurrentAction(WebSession s) {
            return "an action";
        }
        public void restartLesson() {
        }
        public void setCurrentAction(WebSession s, String lessonScreen) {
        }
    };

    @Test
    public void testLinks() {
        String mvcLink = lesson.getLink();
        assertThat(mvcLink, CoreMatchers.startsWith("#attack/"));
        assertThat(mvcLink, CoreMatchers.endsWith("/900"));

        String srvLink = lesson.getServletLink();
        assertThat(srvLink, CoreMatchers.startsWith("attack?Screen="));
        assertThat(srvLink, CoreMatchers.endsWith("&menu=900"));
    }
    /*
    @Test
    public void tesIsAuthorized() {
    	WebSession s = null;
    	boolean auth = lesson.isAuthorized(s, "a OR 1=1", "a OR 1=1");
    	assertEquals(auth, false);

    	auth = lesson.isAuthorized(s, "", "");
    	assertEquals(auth, false);
    	
    	auth = lesson.isAuthorized(s, " ", " ");
    	assertEquals(auth, false);
    } */
}


