package com.github.yona168

import java.nio.file.Path
import java.nio.file.Paths

fun main(){
    val parser= keyValueParser(Regex.fromLiteral("yay"));
    val stuff=parser.parse(getResource("test.my") as Path)
    return;
}