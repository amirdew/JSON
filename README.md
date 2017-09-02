# JSON
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f638c4691bb9401fa68bb0164dbbd8a5)](https://www.codacy.com/app/amirdew/JSON?utm_source=github.com&utm_medium=referral&utm_content=amirdew/JSON&utm_campaign=badger)
[![](https://jitpack.io/v/amirdew/JSON.svg)](https://jitpack.io/#amirdew/JSON)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-JSON%20Parser-green.svg?style=flat)](https://android-arsenal.com/details/1/5393)
<br>
<img src="https://raw.githubusercontent.com/amirdew/JSON/master/json-image.jpg"/>
<br>
simple and easy json parser, json generator, and data holder based on JSONArray and JSONObject for android,
<br>
like [SwiftyJSON](https://github.com/SwiftyJSON/SwiftyJSON) but for android
## Add to your project

To use JSON you must add it as a dependency in your Gradle build:

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
Step 2. Add the dependency
```groovy
dependencies {
    compile 'com.github.amirdew:JSON:v1.0.0'
}
```

## Usage - parse json

You can create JSON object from string and access data with key() and index() methods.

```groovy

String simpleJsonString = "{\"id\":1,\"name\":\"A green door\",\"price\":12.5,\"tags\":[\"home\",\"green\"]}";

JSON json = new JSON(simpleJsonString);
        
//access data
String firstTag = json.key("tags").index(0).stringValue();
Double price = json.key("price").doubleValue();
```


products json:
```groovy
[
  {
    "id": 2,
    "name": "An ice sculpture",
    "price": 12.50,
    "tags": ["cold", "ice"],
    "dimensions": {
      "length": 7.0,
      "width": 12.0,
      "height": 9.5
    },
    "warehouseLocation": {
      "latitude": -78.75,
      "longitude": 20.4
    }
  },
  {
    "id": 3,
    "name": "A blue mouse",
    "price": 25.50,
    "dimensions": {
      "length": 3.1,
      "width": 1.0,
      "height": 1.0
    },
    "warehouseLocation": {
      "latitude": 54.4,
      "longitude": -32.7
    }
  }
]
```
loop:
```groovy
for(int i=0; i<products.count(); i++){
   
   json productInfo = products.index(i);
   String productName = productInfo.key("name").stringValue();
}
```

#### JSON is exception and null free, you can use key() and index() many times without worry about any exception.

```groovy
 int someValue = products.index(8).key("someKey").index(1).key("someOther").intValue();
 //someValue = 0
```

#### check index or key is exist or is null:

```groovy
 if( products.index(3).key("someKey").isNull() ){ 
    /*...*/ 
 }

if( products.index(1).key("someKey").exist() ){
   /*...*/ 
 }
```



## Available methods - parse

| Method |Input type | Return type | Default | Description |
|:----:|:------:|:------:|:----:|:---------:|
|key()|String|JSON| - |if object is a dictionary return JSON object with input key
|index()|int|JSON| - |if object is a array return JSON object with input index
|stringValue()|-|String| empty string ("") | return .toString() of object
|intValue()|-|int| 0 | return integer value if possible
|longValue()|-|long| 0 |return long value if possible
|doubleValue()|-|Double| 0 |return Double value if possible
|booleanValue()|-|boolean| false |return true if object is kind of boolean and true or is kind of string and equal "true"
|value()|-|Object| - |return related object
|count()|-|int| 0 |if related object is a dictionary return number of keys, if related object is a array return length of array
|isNull()|-|boolean| - |return true if related object is null
|exist()|-|boolean| - |return true if related object with index or key exists
|getJsonArray()|-|JSONArray| null |return related JSONArray
|getJsonObject()|-|JSONObject| null |return related JSONObject



## Usage - generate json, data holding
You can use JSON.dic() and JSON.array() static methods to generate json string or hold and pass data

```groovy
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
        
        
  String jsonString = generatedJsonObject.toString();
```
result:
```groovy
{
  "someKey": "someValue",
  "someArrayKey": [
    "first",
    1,
    2,
    {
      "emptyArrayKey": []
    }
  ]
}
```

#### add, edit, remove:
note: now its work for first level only

```groovy
  generatedJsonObject.addEditWithKey("someArrayKey","someOtherValue");
```
result:
```groovy
{
  "someKey": "someValue",
  "someArrayKey": "someOtherValue"
}
```

## Available methods - generate, edit, remove

| Method |Input type | Description |
|:----:|:------:|:---------:|
|add()|Object|if current related object is an array add object at end of array 
|remove()|Object|if current related object is an array and input object exist, remove it from array
|addWithIndex()|Object, int|if current related object is an array replace input object with object for input index 
|removeWithIndex()|int|if current related object is an array remove object for input index
|addEditWithKey()|String, Object|if current related object is a dictionary add input object with input key
|removeWithKey()|String|if current related object is a dictionary remove object with input key
|static create()|Object|create new instance of JSON with input object
|static dic()|Object...|if number of input objects is even create JSONObject use even objects as key and odd obejcts as value
|static array()|Object...|create JSONArray use input objects





