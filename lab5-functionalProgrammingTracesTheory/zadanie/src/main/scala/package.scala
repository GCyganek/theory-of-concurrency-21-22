package pl.edu.agh

import scala.util.matching.Regex

package object tw {

  // regexy potrzebne do odczytania kolejnych linii z pliku wejściowego
  val AlphabetPattern: Regex = """A = [{][a-t,]+[}]""".r
  val TransactionPattern: Regex = """[(][a-t][)] [u-z] := [u-z+-^*/]+""".r
  val WordPattern: Regex = """w = [a-t]+""".r

  // przykładowe pliki wejściowe
  val exampleInputFilename1 = "example_input1.txt"
  val exampleInputFilename2 = "example_input2.txt"

  // metody pomocnicze do odpowiedniego wyświetlania otrzymanych wyników (tak jak we wzorcowym outputcie)
  def printFNF(fnf: List[List[Char]]): Unit = {
    print("FNF([w]) = ")
    fnf.foreach(group => {
      print('(')
      group.foreach(transaction => print(transaction))
      print(')')
    })
    println()
  }

  def printRelation(relation: List[(Char, Char)], relationName: String): Unit = {
    print(s"$relationName={")
    relation.foreach(pair => if (pair != relation.last) { print(s"$pair,") } else { print(pair) })
    println("}")
  }

  def printGraph(graph: Graph): Unit = {
    println("digraph g{")
    graph.edges.foreach(println)
    graph.nodes.sortBy(_.id).foreach(node => println(s"${node.id}[label=${node.transaction}]"))
    println("}")
  }
}
