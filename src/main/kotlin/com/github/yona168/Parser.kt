package com.github.yona168

import java.lang.String.join
import java.nio.file.Path
import java.util.stream.IntStream

interface Parser {
    @Throws(InvalidFormatException::class)
    fun parse(lines: List<String>): Map<String, Any>
    @Throws(InvalidFormatException::class)
    fun parse(file: Path)=parse(readLines(file))
    @Throws(InvalidFormatException::class)
    fun parse(string: String)=parse(string.split("\n"))
}

fun keyValueParser(separator: Regex, valueHandler: (value: String)->Any = {it}): Parser {
    return object:Parser{
        override fun parse(lines: List<String>): Map<String, Any> {
            val separated = lines.map { line -> line.split(separator) }
            val badLineIndex=(0..separated.lastIndex).firstOrNull{ separated[it].size!=2 }
            if(badLineIndex!=null){
                throw InvalidFormatException(lines[badLineIndex])
            }
            return separated.groupBy({it[0]}){valueHandler(it[1])}.mapValues { it.value[0] }
        }
    }
}
enum class IndentOption{
    TABS,
    SPACES
}
fun whitespaceParser(amount: Int=2, indentOption:IndentOption=IndentOption.SPACES):Parser{
    return object:Parser{
        private fun getChildrenOf(index: Int, spacesOnParent: Int, lines: List<String>):Map<String, Any>{
            val parentMap=HashMap<String, Any>();
            val spacesByChildren=" ".repeat(spacesOnParent+amount);
            val childMaps=(index..lines.lastIndex).filter{lines[it].startsWith(spacesByChildren)}
                .map{kvIndex->getChildrenOf(kvIndex, spacesOnParent+amount, lines)}
            return childMaps.fold(emptySequence<Map.Entry<String, Any>>(), { sequence, map: Map<String, Any>->sequence+map.asSequence()})
                .distinct().groupBy({it.key}){it.value}
        }
        override fun parse(lines: List<String>): Map<String, Any> {
            val result= mutableMapOf<String, Any>()
            val trimmedLines=lines.map(String::trimEnd)
            val parentIndeces=(0..trimmedLines.lastIndex).filter { !trimmedLines[0].substring(0,1).isBlank() }
            parentIndeces.forEach{index->result[trimmedLines[index]]=getChildrenOf(index, 0,lines)}
            return result;
        }
    }
}