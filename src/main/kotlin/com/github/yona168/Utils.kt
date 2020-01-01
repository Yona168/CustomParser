package com.github.yona168

import java.nio.file.Files.readAllLines
import java.nio.file.Path
import java.nio.file.Paths

fun readLines(file: Path)=readAllLines(file).filter{it!=null && !it.isBlank()}.map(String::trim);
fun getResource(name: String)= InvalidFormatException::class.java.classLoader.getResource(name)?.run { Paths.get(toURI()) }
