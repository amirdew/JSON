package eu.amirs.jsonparsersample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import eu.amirs.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String resultText = "";
        TextView resultTextView = (TextView) findViewById(R.id.result);

        //parse simple json string: (http://json-schema.org/example1.html)
        String simpleJsonString = "{\"id\":1,\"name\":\"A green door\",\"price\":12.5,\"tags\":[\"home\",\"green\"]}";

        JSON json = new JSON(simpleJsonString);
        resultText += "first tag in simple json: " + json.key("tags").index(0).stringValue();


        //parse json from asset file:
        JSON products = new JSON(readFileFromAsset("products.json")).key("products");
        for (int i = 0; i < products.count(); i++) {
            JSON productInfo = products.index(i);
            resultText += String.format("\n\n product[%d]: id:%d, name:'%s', price:%.2f, length:%.2f",
                    i,
                    productInfo.key("id").intValue(),
                    productInfo.key("name").stringValue(),
                    productInfo.key("price").doubleValue(),
                    productInfo.key("dimensions").key("length").doubleValue()
            );
        }


        //exception and null free:
        resultText += "\n\n parse not exist: \n";
        resultText += products.index(8).key("someKey").index(1).key("someOther").intValue();

        //check if exist or is null
        if( products.index(3).key("someKey").isNull() ){ /*...*/ }

        if( products.index(1).key("someKey").exist() ){ /*...*/ }


        //default values:

        //string ""
        //boolean false
        //int,double 0






        //generate json string:
        JSON generatedJsonObject = JSON.create(
                JSON.dic(
                        "someKey", "someValue",
                        "someArrayKey", JSON.array(
                                "first",
                                1,
                                2,
                                JSON.dic(
                                        "emptyArrayKey", JSON.array()
                                )
                        )
                )
        );

        resultText += "\n\n\n generated JSON: \n";
        resultText += generatedJsonObject.toString();



        //add, edit or remove
        //edit if exist otherwise add
        generatedJsonObject.addEditWithKey("someArrayKey","someOtherValue");

        resultText += "\n\n\n generated JSON: \n";
        resultText += generatedJsonObject.toString();

        //generatedJsonObject.removeWithKey("someOtherKey");
        //...




        resultTextView.setText(resultText);
    }




    private String readFileFromAsset(String fileName){


        BufferedReader reader = null;
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try{
            inputStream = getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while((line = reader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        } finally {

            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }

            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        return builder.toString();
    }



}
