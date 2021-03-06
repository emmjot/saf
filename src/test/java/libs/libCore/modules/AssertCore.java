package libs.libCore.modules;

import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class AssertCore {

    private SharedContext ctx;

    // PicoContainer injects class SharedContext
    public AssertCore(SharedContext ctx) {
        this.ctx = ctx;
    }


    /**
     * Validates value of a parameter available in ValidatableResponse
     * Supported actions are equalTo, containsString, containsInAnyOrder, greaterThan, lessThan
     *
     * @param vResp, ValidatableResponse, response that is going to be validated
     * @param key, String, name of the response property that which value is going to be validated
     * @param action, String, determines kind of assertion that is going to be performed
     * @param expectedValue, Object, expected value
     */
    public void validatableResponseBodyTableAssertion(ValidatableResponse vResp, String key, String action, Object expectedValue){

        String type = expectedValue.getClass().getName();
        String cType = null;

        //log null pointer exception in case message body is empty
        try {
            cType = vResp.extract().path(key).getClass().getName();
        } catch (NullPointerException e) {
            Log.error("Key " + key + " not found in the message body", e );
        }

        Log.debug("Current is " + vResp.extract().path(key));
        Log.debug("Its type is " + cType);

        Log.debug("Action is " + action);

        Log.debug("Expected value is " + expectedValue);
        Log.debug("Its type is " + type);

        if (action.equalsIgnoreCase("equalTo")){
            try {
                vResp.body(key, equalTo(expectedValue));
            } catch (AssertionError e) {
                Log.error("", e);
            }
        } else if (action.equalsIgnoreCase("containsString")){
            try {
                vResp.body(key, containsString(expectedValue.toString()));
            } catch (AssertionError e) {
                Log.error("", e);
            }
        } else if (action.equalsIgnoreCase("containsInAnyOrder")){
            try {
                vResp.body(key, containsInAnyOrder(expectedValue));
            } catch (AssertionError e) {
                Log.error("", e);
            }
        } else if (action.equalsIgnoreCase("greaterThan")){
            if (cType.contains("Int")) {
                try {
                    vResp.body(key, greaterThan((int) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else if (cType.contains("Long")) {
                try {
                    vResp.body(key, greaterThan((Long) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else if (cType.contains("Double")) {
                try {
                    vResp.body(key, greaterThan((Double) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else if (cType.contains("Float")) {
                try {
                    vResp.body(key, greaterThan((Float) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else {
                Log.error("Type not supported for greaterThen comparison. " +
                        "Please use one of Int, Long, Double, Float");
            }
        } else if (action.equalsIgnoreCase("lessThan")){
            if (cType.contains("Int")) {
                try {
                    vResp.body(key, lessThan((int) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else if (cType.contains("Long")) {
                try {
                    vResp.body(key, lessThan((Long) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else if (cType.contains("Double")) {
                try {
                    vResp.body(key, lessThan((Double) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else if (cType.contains("Float")) {
                try {
                    vResp.body(key, lessThan((Float) expectedValue));
                } catch (AssertionError e) {
                    Log.error("", e);
                }
            } else {
                Log.error("Type not supported for lessThan comparison. " +
                        "Please use one of Int, Long, Double, Float");
            }
        } else {
            Log.error("Action " + action + " not supported. Please use one of " +
                    "equalTo, containsInAnyOrder, containsString, greaterThan, lessThan");
        }
    }

}
