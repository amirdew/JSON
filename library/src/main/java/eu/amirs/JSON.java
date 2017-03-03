package eu.amirs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by amirkhorsandi on 3/3/17.
 */

public class JSON {

    private JSONObject jsObj;
    private JSONArray jsArr;
    private Object value;


    public JSON(Object jsonString) {

        if (ArrayList.class.isInstance(jsonString))
            jsonString = new JSONArray((ArrayList) jsonString);


        if (jsonString == null) {
            value = null;
        } else if (jsonString.toString().trim().startsWith("{")) {
            //json object
            try {
                jsObj = new JSONObject(jsonString.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (jsonString.toString().trim().startsWith("[")) {
            //json array
            try {
                jsArr = new JSONArray(jsonString.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else
            value = jsonString;


    }


    @Override
    public String toString() {

        if (isNull()) return "";
        return this.value().toString();
    }

    public int count() {

        if (jsObj != null) return jsObj.length();
        if (jsArr != null) return jsArr.length();
        return 0;
    }

    public JSON key(String keyStr) {


        if (jsObj == null) return new JSON(null);


        try {

            Object obj = jsObj.get(keyStr);
            if (obj != null)
                return new JSON(obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSON(null);
    }

    public JSON index(int index) {


        if (jsArr == null) return new JSON(null);

        try {
            return new JSON(jsArr.get(index));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSON(null);

    }


    public String  stringValue() {

        if (this.value() == null)
            return "";
        if (isNull())
            return "";

        return String.valueOf(this.value());

    }

    public int intValue() {

        if (this.value() == null) return 0;

        try {
            return Integer.valueOf(this.value().toString());
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }

    public long longValue() {

        if (this.value() == null) return 0;

        try {
            return Long.valueOf(this.value().toString());
        } catch (java.lang.NumberFormatException e) {
            return 0;
        }
    }


    public Double doubleValue() {

        if (this.value() == null) return 0.0;

        try {
            return Double.valueOf(this.value().toString());
        } catch (java.lang.NumberFormatException e) {
            return 0.0;
        }
    }

    public boolean booleanValue() {

        if (this.value() == null) return false;

        return this.stringValue().equals("true");

    }

    public Object value() {

        if (nullableValue() != null) return nullableValue();

        return new JSON(null);
    }


    public boolean isNull() {

        if (value == null && jsArr == null && jsObj == null)
            return true;
        if (String.valueOf(this.value()).equals("null"))
            return true;

        return false;

    }

    private Object nullableValue() {

        if (value != null) return value;

        if (jsObj != null) return jsObj.toString();

        if (jsArr != null) return jsArr.toString();


        return null;
    }


    public boolean exist() {
        return nullableValue() != null;
    }


    public JSONObject getJsonObject() {
        return jsObj;
    }

    public JSONArray getJsonArray() {
        return jsArr;
    }


    public void removeWithKey(String Key) {

        if (Key == null)
            return;

        JSONObject jsonObject = this.getJsonObject();

        if (jsonObject == null)
            return;

        jsonObject.remove(Key);

        this.jsObj = jsonObject;
    }

    public void addEditWithKey(String Key, Object Object) {

        if (Object == null || Key == null)
            return;

        JSONObject jsonObject = this.getJsonObject();

        if (jsonObject == null)
            return;


        Object normalizedObject = Object;


        if (JSON.class.isInstance(Object)) {
            if (((JSON) Object).getJsonArray() != null)
                normalizedObject = ((JSON) Object).getJsonArray();
            else if (((JSON) Object).getJsonObject() != null)
                normalizedObject = ((JSON) Object).getJsonObject();
            else
                normalizedObject = ((JSON) Object).value();
        }


        try {
            jsonObject.putOpt(Key, normalizedObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.jsObj = jsonObject;

    }


    public void addWithIndex(Object inputObject, int index) {


        if (inputObject == null)
            return;

        JSONArray jsonArr = this.getJsonArray();

        if (jsonArr == null)
            return;

        try {

            if (JSON.class.isInstance(inputObject)) {
                if (((JSON) inputObject).getJsonArray() != null) {

                    if (index == -1)
                        jsonArr.put(((JSON) inputObject).getJsonArray());
                    else
                        jsonArr.put(index, ((JSON) inputObject).getJsonArray());

                } else if (((JSON) inputObject).getJsonObject() != null) {

                    if (index == -1)
                        jsonArr.put(((JSON) inputObject).getJsonObject());
                    else
                        jsonArr.put(index, ((JSON) inputObject).getJsonObject());

                } else {
                    if (index == -1)
                        jsonArr.put(((JSON) inputObject).value());
                    else
                        jsonArr.put(index, ((JSON) inputObject).value());
                }

            } else {
                if (index == -1)
                    jsonArr.put(inputObject);
                else if(index > -1)
                    jsonArr.put(index, inputObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        this.jsArr = jsonArr;
    }

    public void add(Object inputObject) {
        addWithIndex(inputObject, -1);
    }


    public void removeWithIndex(int index) {

        JSONArray jsonArr = this.getJsonArray();

        if (jsonArr == null)
            return;

        JSONArray newJsonArr = new JSONArray();

        for (int i = 0; i < jsonArr.length(); i++) {


            try {
                if (i != index) {
                    newJsonArr.put(jsonArr.get(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        this.jsArr = newJsonArr;

    }


    public void remove(Object inputObject) {

        if (inputObject == null)
            return;

        JSONArray jsonArr = this.getJsonArray();

        if (jsonArr == null)
            return;

        JSONArray newJsonArr = new JSONArray();

        for (int i = 0; i < jsonArr.length(); i++) {


            try {
                if (JSONObject.class.isInstance(inputObject) && JSONObject.class.isInstance(jsonArr.get(i))) {

                    if (!jsonObjectComparesEqual((JSONObject)jsonArr.get(i), (JSONObject)inputObject, null, null) ) {
                        newJsonArr.put(jsonArr.get(i));
                    }

                } else {
                    if (!jsonArr.get(i).equals(inputObject)) {
                        newJsonArr.put(jsonArr.get(i));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        this.jsArr = newJsonArr;
    }


    public static JSON create(Object object) {

        return new JSON(object.toString());
    }


    public static JSONObject dic(Object... values) {

        JSONObject mainDic = new JSONObject();
        for (int i = 0; i < values.length; i += 2) {

            try {

                Object valueObject = values[i + 1];

                if (JSON.class.isInstance(valueObject)) {

                    if (((JSON) valueObject).getJsonArray() != null)
                        valueObject = ((JSON) valueObject).getJsonArray();

                    else if (((JSON) valueObject).getJsonObject() != null)
                        valueObject = ((JSON) valueObject).getJsonObject();
                }

                mainDic.put((String) values[i], valueObject == null ? JSONObject.NULL:valueObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mainDic;
    }


    public static JSONArray array(Object... values) {

        JSONArray mainList = new JSONArray();
        for (Object obj : values)
            mainList.put(obj);
        return mainList;
    }




    public static boolean jsonObjectComparesEqual(JSONObject x, JSONObject y, Collection<String> only, Collection<String> except) {
        Set<String> keys = keySet(x);
        keys.addAll(keySet(y));
        if (only != null) {
            keys.retainAll(only);
        }
        if (except != null) {
            keys.removeAll(except);
        }
        for (String s : keys) {
            Object a = null, b = null;
            try {
                a = x.get(s);
            } catch (JSONException e) {
            }
            try {
                b = x.get(s);
            } catch (JSONException e) {
            }
            if (a != null) {
                if (!a.equals(b)) {
                    return false;
                }
            } else if (b != null) {
                if (!b.equals(a)) {
                    return false;
                }
            }
        }
        return true;
    }
    private static Set<String> keySet(JSONObject j) {
        Set<String> res = new TreeSet<String>();
        for (String s : new AsIterable<String>(j.keys())) {
            res.add(s);
        }
        return res;
    }

    private static class AsIterable<T> implements Iterable<T> {
        private Iterator<T> iterator;
        public AsIterable(Iterator<T> iterator) {
            this.iterator = iterator;
        }
        public Iterator<T> iterator() {
            return iterator;
        }
    }

}
