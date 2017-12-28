package wind.zhihunews;

import android.app.Application;
import android.test.ApplicationTestCase;

import wind.zhihunews.net.Api;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {


    Api api;


    public ApplicationTest() {
        super(Application.class);
    }

    public void setUp() throws Exception {
        super.setUp();

    }


    public void testApi() {

    }
}