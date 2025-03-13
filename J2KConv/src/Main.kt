import java.io.File

// type 0 -> function, type 1 -> val/var
fun work (list: List<String>, type: Int, line: String) {
    // look for the occurence of fun, val or var
    var index = -1
    if (type == 0) index = list.indexOf("fun")
    else {
        index = list.indexOf("val")
        if (index == -1) index = list.indexOf("var")
    }
    if (index == -1) return


    val first:String = if (type == 0) "fun" else "val"
    val second:String = if (type == 0) "fun" else "var"
    val splitter = if (type == 0) "(" else ":"

    // look for *public* declarations
    if (list[0] != "private" && list[0] != "protected" && list[0] != "internal"){
        val name = list[index+1].split(splitter)[0]
        println(line)
    }
}

fun classWork (list:List<String>, line:String): Boolean {
    var index = -1
    index = list.indexOf("class")
    var interfaceIndex = -1
    interfaceIndex = list.indexOf("interface")
    var objectInterface = -1
    objectInterface = list.indexOf("object")
    if (index == -1 && interfaceIndex == -1 && objectInterface == -1) return false

    // looking for *public* class, interface, object declaration
    if (index == -1) index = if (interfaceIndex != -1) interfaceIndex else objectInterface
    if (list[0] != "private" && list[0] != "protected" && list[0] != "internal"){
        var name = list[index+1].split("(")[0]
        name = name.split(":")[0]
        name = name.split("{")[0]
        if (interfaceIndex != -1 || objectInterface != -1) println(line)
        else {
            for (i in 0..index) print(list[i] + " ")
            print(name)
            if (list[index+1].contains("(")) print("(")
        }
    }

    if (interfaceIndex != -1 || objectInterface != -1) return true

    // look into declarations in the constructor
    // keep track of most recent access modifier
    var access = ""
    for (i in index..list.lastIndex) {
        if (list[i].contains(Regex(".*\\(?,?(public|private|protected|internal)"))) {
            access = Regex(".*\\(?,?(public|private|protected|internal)").find(list[i])?.groupValues?.get(1).toString()
        }
        else if (list[i].contains(Regex(".*\\(?,?(val|var)"))) {
            if (access == "public" || access == "") {
                var name = list[i + 1].split(":")[0]
                //pubDecls.add(list[i].takeLast(3) + " " + name)
            }
            access = ""
        }

    }
    //


    return true
}


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: ./Main.kt <directory>")
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
    var line: String? = buffer.readLine().trim()
    while (line != null) {
        // Split the line and remove spaces
        var list = line.split(" ")
        list = list.filter { it.trim().isNotEmpty() }

        // check fun, val and var
        if (!list.isEmpty()) {
            if (list[0] == "//") {
                line = buffer.readLine();
                continue
            } else if (list[0] == "{" || list[0] == "}") {
                println(line)
                line = buffer.readLine();
                continue
            }
            val isClass = classWork(list, line)
            if (!isClass) {
                work(list, 0, line)
                work(list, 1, line)
            }

        }

        line = buffer.readLine()
    }



    buffer.close()

}