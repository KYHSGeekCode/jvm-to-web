import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.*
import kotlinx.ast.common.klass.KlassDeclaration
import kotlinx.ast.common.klass.KlassIdentifier
import kotlinx.ast.grammar.kotlin.common.summary
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

fun main() {
    val source = AstSource.File(
        "src/main/kotlin/Tester.kt"
    )
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)
    val tree = kotlinFile.summary(attachRawAst = false).get()
    printSingle(kotlinFile)
    for (node in tree) {
        printSingle(node)
        printAst(node)
    }
//    print(kotlinFile.astInfo())
}

fun printSingle(node: Ast) {

}

fun printAst(node: Ast) {
    println(node.javaClass.kotlin.simpleName)
    println(node.description)
//    print(node.astInfo())
    if (node is AstWithAstInfo) {
        print("Node is AstWithAstInfo:")
        println(node.info)
    }
    if (node is DefaultAstNode) {
        println("${node.description} ${node.javaClass.name} is defaultAstNode")
    }
    if (node is DefaultAstTerminal) {
        println("${node.description} ${node.javaClass.name}  is defaultAstTerminal, text = ${node.text}, description = ${node.description}")
    }
    if (node is AstWithAttributes) {
        println("Has attributes")
    }
    if (node is KlassDeclaration) {
        println("Is class declaration, ${node.identifier}, ${node.keyword}")
    }
    if (node is KlassIdentifier) {
        println("Is class identifier, ${node.identifier}, ${node.nullable}, ${node.parameter}, ${node.rawName}")
    }
    println()
//    println(node.summary(attachRawAst = false))
    val nodes = node.summary(attachRawAst = true).get()
    for (componentNode in nodes) {
        if (node != componentNode) {
            printAst(componentNode)
        } else {
            for (child in componentNode.summary(attachRawAst = false).get()) {
                if (child != componentNode)
                    printAst(child)
            }
        }

    }
}

fun Ast.astInfo(indent: Int): List<String> {
    val info = ((this as? AstWithAstInfo)?.info?.toString() ?: "").padEnd(34)
    val self = "$info${"  ".repeat(indent)}$description"
    return if (this is AstNode) {
        listOf(self) + children.flatMap { child ->
            child.astInfo(indent + 1)
        }
    } else {
        listOf(self)
    }
}

fun Ast.astInfo(): String {
    return astInfo(indent = 0).joinToString("\n", postfix = "\n")
}