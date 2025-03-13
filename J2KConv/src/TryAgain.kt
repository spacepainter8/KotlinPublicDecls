import java.io.File

fun checkAccessMods (line: String, lineSep: List<String>, type:Int) {
    if (!lineSep.contains("private") && !lineSep.contains("protected") && !lineSep.contains("internal")
        && !lineSep.contains("{private") && !lineSep.contains("{protected") && !lineSep.contains("{internal")) {
        if (type == 0) println(line)
        else {
            val secondLeftBrack = line.trim().substring(1, line.trim().length).indexOf("{")
            print(line.substring(0, secondLeftBrack+1))
            parseLine(line.substring(secondLeftBrack+1, line.length))
        }
    } else {
        if (line.trim()[0] == '{') print(line.substring(0, line.indexOf("{")+1))
        val leftBrack = line.trim().substring(1, line.trim().length).indexOf("{")
        if (leftBrack != -1) {
            print("{")
            parseLine(line.substring(leftBrack+1, line.length))
        }

    }
}

fun classArgParse (line:String) {
    var args = line.split(",")
    for (i in args.indices) {
        if (!args[i].trim().contains("private ") && !args[i].trim().contains("protected ")
            && !args[i].trim().contains("internal ")) {
            print(args[i])
            if (i != args.lastIndex) print(", ")
        }
    }
}

fun parseClassLine(line: String) {
    val parenInd = line.indexOf("(")
    val secondLeftBrack = line.trim().substring(1, line.trim().length).indexOf("{")
    if (parenInd != -1){
        print(line.substring(0, parenInd+1))
        val rightParenInd = line.indexOf(")")
        classArgParse(line.substring(parenInd+1, rightParenInd))
        print(")")
//        println("AAAAA " + line)
        if (rightParenInd != line.length-1 && secondLeftBrack != -1)
            print(line.substring(rightParenInd+1, secondLeftBrack+1))
        else {
            if (rightParenInd == line.length-1)
                println()
            else println(line.substring(rightParenInd+1, line.length))
            return
        }
//        println("AAAAA" + " " + line.substring(secondLeftBrack+1, line.length))
        parseLine(line.substring(secondLeftBrack+1, line.length))
    } else {
        if (secondLeftBrack == -1) println(line)
        else {
            print(line.substring(0, secondLeftBrack + 1))
            parseLine(line.substring(secondLeftBrack + 1, line.length))
        }
    }
}

fun parseLine(argLine: String){
//    println("HERE  " + argLine)
    if (argLine.isEmpty()) return
    var line = argLine
    if (line.contains(Regex(".*//.*"))) line = line.substring(0, line.indexOf("//"))
    var lineSep = line.trim().split(" ")
    lineSep = lineSep.filter { it.trim().isNotEmpty() }

    // Parsing the line
    if (line.trim() == "{" || line.trim()=="}") println(line)
    else if (line.contains(Regex(".*\\{class .*"))){
        parseClassLine(line)
    } else if (lineSep.contains("class")) {
        if (!lineSep.contains("private") && !lineSep.contains("protected") && !lineSep.contains("internal")
            && !lineSep.contains("{private") && !lineSep.contains("{protected") && !lineSep.contains("{internal")){
            parseClassLine(line)
        } else {
            val brackInd = line.indexOf("{")
            print("{")
            parseLine(line.substring(brackInd+1, line.length))
        }
    } else if (line.contains(Regex(".*\\{object .*")) || line.contains(Regex(".*\\{interface .*"))) {
        val secondLeftBrack = line.trim().substring(1, line.trim().length).indexOf("{")
        print(line.substring(0, secondLeftBrack+1))
        parseLine(line.substring(secondLeftBrack+1, line.length))
    } else if (lineSep.contains("object") || lineSep.contains("interface")) {
        checkAccessMods(line, lineSep, 1)
    } else if (line.contains(Regex(".*\\{fun .*")) || line.contains(Regex(".*\\{var .*")) ||
        line.contains(Regex(".*\\{val .*"))){
        println(line)
    } else if (lineSep.contains("fun") || lineSep.contains("var") || lineSep.contains("val")){
        checkAccessMods(line, lineSep, 0)
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: ./TryAgain.kt <directory>")
        return
    }


    val fileName = args[0]
    val file = File(fileName)
    if (!file.exists()) {
        println("File does not exist")
        return
    }


    // Read the file line by line
    val buffer = file.bufferedReader()
    var line: String? = buffer.readLine()
    while (line != null) {
        // Discard the commented out part of the line
        parseLine(line)

        line = buffer.readLine()
    }



    buffer.close()

}