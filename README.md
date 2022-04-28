# IOT-COMMUNICATION

![maven-v1.0.2](https://img.shields.io/badge/maven-v1.0.2-brightgreen)

## CopyRight

@2019-2021 Oscura, All Rights Reserved

## How to get

```
<dependency>
    <groupId>com.github.xingshuangs</groupId>
    <artifactId>iot-communication</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Description

Now, it is a tool for iot communication.

# Instance
Get single data
```
/*********************************** EXAMPLE1 ***********************************/
String src = "FFFFFF8100006459C179EB85C0EB9895551D68C7E5A4A9E6B094E5A5BD323341";
HexParse parse = new HexParse(HexUtil.toHexArray(src));
List<DataUnit> list = new ArrayList<>();
list.add(new DataUnit<>(3, "bool"));
list.add(new DataUnit<>(3, "byte"));
list.add(new DataUnit<>(3, "ubyte"));
list.add(new DataUnit<>(6, "short"));
list.add(new DataUnit<>(0, "int"));
list.add(new DataUnit<>(0, "uint"));
list.add(new DataUnit<>(8,"float"));
list.add(new DataUnit<>(12,"double"));
list.add(new DataUnit<>(20, 9, "string"));
parse.parseDataList(list);
list.forEach(x-> System.out.println(x.getValue()));

// result
true
-127
129
25689
-127
4294967169
-15.62
-56516.66664
天气好
```

Get Array data
```
/*********************************** EXAMPLE2 ***********************************/
String src = "FFFFFF8100006459C179EB85C0EB9895551D68C7E5A4A9E6B094E5A5BD323341";
HexParse parse = new HexParse(HexUtil.toHexArray(src));
List<DataUnit> listArray = new ArrayList<>();
listArray.add(new DataUnit<Boolean>(3, 6, 4, "bool"));
listArray.add(new DataUnit<>(3, 2, "byte"));
listArray.add(new DataUnit<>(3, 2, "ubyte"));
listArray.add(new DataUnit<>(4, 2, "short"));
listArray.add(new DataUnit<>(0, 2, "int"));
listArray.add(new DataUnit<>(0, 2, "uint"));
parse.parseDataList(listArray);
listArray.forEach(System.out::println);

// result
DataUnit{name='', description='', unit='', bytes=[], value=[false, true, false, false], byteOffset=3, bitOffset=6, count=4, dataType='bool', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[-127, 0], byteOffset=3, bitOffset=0, count=2, dataType='byte', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[129, 0], byteOffset=3, bitOffset=0, count=2, dataType='ubyte', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[0, 25689], byteOffset=4, bitOffset=0, count=2, dataType='short', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[-127, 25689], byteOffset=0, bitOffset=0, count=2, dataType='int', littleEndian=false}
DataUnit{name='', description='', unit='', bytes=[], value=[4294967169, 25689], byteOffset=0, bitOffset=0, count=2, dataType='uint', littleEndian=false}
```
