import kotlinx.ast.common.AstSource
import kotlinx.ast.common.print
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

fun main() {
    val source = AstSource.File(
        "src/main/kotlin/Main.kt"
    )
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)
    kotlinFile.print()
//    kotlinFile.summary(attachRawAst = true)
//        .onSuccess { astList ->
//            astList.forEach(Ast::print)
//        }.onFailure { errors ->
//            errors.forEach(::println)
//        }
}