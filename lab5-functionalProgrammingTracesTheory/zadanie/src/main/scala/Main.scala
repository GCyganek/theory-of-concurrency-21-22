package pl.edu.agh.tw

import scala.collection.mutable
import scala.io.Source

/*

  Przyjmuję założenie, że plik wejściowy wygląda w następujący sposób:
      A = {a,b,c,d}
      w = baadcb
      (a) x := x+y
      (b) y := y+2z
      (c) x := 3x+z
      (d) z := y-z
  Oznacza to, że w pierwszej linii jest alfabet A zawierający kolejne transakcje (mogą one być oznaczone tylko przez
  pojedyncze litery a-t), w drugiej jest słowo 'w' złożone z transakcji alfabetu A, a w kolejnych liniach rozpisane
  poszczególne transakcje korzystające ze zmiennych (mogą one być oznaczone tylko przez pojedyncze litery u-z)

 */

/*
  klasy Node (transaction to literka oznaczająca odpowiednią transakcję) oraz Graph będą potrzebne do utworzenia grafu
  zależności o postaci minimalnej
 */
case class Node(transaction: Char, children: List[Node], id: Int)

case class Graph(nodes: List[Node], edges: List[String])

object Main extends App {

  if (args.length == 0) {
    println("Error: No input file")
    System.exit(1)
  }

  /*
  funkcje potrzebne przy przetwarzaniu pliku z danymi wejściowymi
   */
  val isLowerLetter: Char => Boolean = (c: Char) => c.isLetter && c.isLower
  val isTransaction: Char => Boolean = (c: Char) => c <= 't' && isLowerLetter(c)
  val isVariable: Char => Boolean = (c: Char) => c >= 'u' && isLowerLetter(c)
  val firstTransactionInWordIndex: String => Int = s => s.indexWhere(element => isTransaction(element))

  /*
  funkcja sprawdzająca, czy podane dwie transakcje są od siebie zależne (czy transakcje zmieniają tą samą zmienną /
  czy jedna z nich korzysta ze zmiennej modyfikowanej przez drugą)
   */

  val checkIfTransactionDependentOnAnother: (List[Char], List[Char]) => Boolean = (transaction1, transaction2) => {
    val variable1 = transaction1.head
    val variables1 = transaction1.tail
    val variable2 = transaction2.head
    val variables2 = transaction2.tail
    variables1.contains(variable2) || variables2.contains(variable1) || variable1 == variable2
  }

  /*
  odczytany alfabet A zapisuję do listy alphabet, słowo 'w' do listy word, a poszczególne transakcje w hashmapie
  transactions
   */

  var alphabet: List[Char] = Nil
  var word: List[Char] = Nil
  var transactions = new mutable.HashMap[Char, List[Char]]()

  /*
  odczytywanie pliku wejściowego (korzystam z regexów zdefiniowanych w obiekcie "package")
   */

  val bufferedSource = Source.fromResource(args(0))

  bufferedSource.getLines.foreach {
    case line@AlphabetPattern() => alphabet = line.filter(char => isTransaction(char)).toList
    case line@WordPattern() => word = line.substring(firstTransactionInWordIndex(line)).toList
    case line@TransactionPattern() =>
      val transaction: Char = line.filter(char => isTransaction(char)).toList.head
      val transactionVariables = line.filter(char => isVariable(char)).toList
      transactions += (transaction -> transactionVariables)
    case _ => println("undefined")
  }

  bufferedSource.close()

  /*
  === 1. ===========================================================================
  wyznaczanie relacji zależności D - tworzę listę wszystkich możliwych par transakcji z alfabetu A, a następnie
  korzystając z wcześniej zdefiniowanej funkcji filtruję tę listę pozostawiając tylko pary transakcji zależnych
   */

  val D = alphabet.flatMap(char1 => alphabet.map(char2 => (char1, char2)))
    .filter(pair => checkIfTransactionDependentOnAnother(transactions(pair._1), transactions(pair._2)))

  printRelation(D, "D")

  /*
  === 2. ===========================================================================
  wyznaczanie relacji niezależności I - analogicznie do wyznaczania D, jednak tym razem filtrując listę par
  transakcji pozostawiamy tylko te pary, które są niezależne od siebie
   */

  val I = alphabet.flatMap(char1 => alphabet.map(char2 => (char1, char2)))
    .filter(pair => !checkIfTransactionDependentOnAnother(transactions(pair._1), transactions(pair._2)))

  printRelation(I, "I")

  /*
  === 3. ===========================================================================
  wyznaczanie postaci normalnej Foaty FNF([w]) śladu [w]
   */

  /*
  funkcje zwracające odpowiednio listy zależnych i niezależnych transakcji od transakcji podanej jako argument funkcji
   */
  val dependentTransactionsList: Char => List[Char] = transaction => D.filter(pair => pair._1 == transaction).map(pair => pair._2)
  val independentTransactionsList: Char => List[Char] = transaction => I.filter(pair => pair._1 == transaction).map(pair => pair._2)

  /*
  główna funkcja rekurencyjna służąca do wyznaczenia FNF([w]) i zapisująca wynik do zmiennej result zwracanej po przejściu
  przez całe słowo wejściowe w
   */
  val getFNF: (List[Char], List[List[Char]]) => List[List[Char]] = (word, result) => {
    word match {
      case Nil => result
      case ::(head, tail) =>
        val independentTransactions = independentTransactionsList(head).toSet
        val verifiedIndependentTransactions = verifyIndependentTransactions(independentTransactions, tail)
        getFNF(verifiedIndependentTransactions
          .foldLeft(tail)((acc, e) => deleteFirstOccurrence(acc, e)), result ++ List(List(head) ++ verifiedIndependentTransactions))
    }
  }

  /*
  sprawdza, czy któraś z transakcji niezależnych (independentTransactions) od obecnie sprawdzanej w słowie wejściowym 'w'
  występuje w reszcie słowa wejściowego 'w' (tail) i mogłaby zostać dołączona do wspólnej klasy Foaty z tą transakcją
   */
  val verifyIndependentTransactions: (Set[Char], List[Char]) => List[Char] = (independentTransactions, tail) => {
    tail.foldLeft((independentTransactions, List.empty[Char]))(
    (acc: (Set[Char], List[Char]), tailElement: Char) => {
        if (acc._1.contains(tailElement)) {
          (acc._1 -- dependentTransactionsList(tailElement), List(tailElement) ++ acc._2)
        } else {
          (acc._1 -- dependentTransactionsList(tailElement), acc._2)
        }
      })._2
  }

  /*
  służy do usunięcia z pozostałej do sprawdzenia części 'w' (tail) te transakcje, które zostały właśnie dodane do
  wspólnej klasy Foaty wraz z transakcją sprawdzaną w aktualnej iteracji funkcji getFNF()
   */
  val deleteFirstOccurrence: (List[Char], Char) => List[Char] = (tail, transactionToDelete) => {
    val index = tail.indexOf(transactionToDelete)
    if (index < 0) tail
    else if (index == 0) tail.tail
    else {
      val (left, right) = tail.splitAt(index)
      left ++ right.tail
    }
  }

  val FNF = getFNF(word, List.empty)
  printFNF(FNF)

  /*
  === 4. ===========================================================================
  rysowanie grafu zależności w postaci minimalnej dla słowa 'w'
   */

  val createNodesFromWord: List[Char] => List[Node] = word => {
    word.zipWithIndex.map({ case (t, id) => Node(t, List.empty[Node], id)})
  }

  val dependentNodesList: (Char, List[Node]) => List[Node] = (transaction, nodes) => {
    nodes.filter(node => dependentTransactionsList(transaction).toSet(node.transaction))
  }

  val independentNodesList: (Char, List[Node]) => List[Node] = (transaction, nodes) => {
    nodes.filter(node => independentTransactionsList(transaction).toSet(node.transaction))
  }

  /*
  funkcja createGraph tworzy graf zależności w postaci minimalnej na podstawie listy obiektów Node utworzonej
  funkcją createNodesFromWord powyżej ze słowem wejściowym 'w' jako argumentem
  polega na wykorzystaniu na odwróconej wejściowej liście wierzchołków funkcji foldLeft z akumulatorem acc składającym się z
  Set.empty[Node] oraz osobnego obiektu Graph, w którym tworzymy w kolejnych iteracjach nasz graf
  -> do początkowego Set.empty[Node] składającego się na acc zapisujemy utworzone w danej iteracji Node, które
  będą musiały być później sprawdzone w kolejnych iteracjach, aby stworzyć potrzebne krawędzie między tymi Node'ami, które
  są zależne (utrzymując założenie, że ma być to graf minimalny)
  -> do obiektu Graph zapisujemy kolejno utworzone Node'y oraz krawędzie między nimi. Po przejściu przez wszystkie iteracje
  funkcji foldLeft otrzymamy graf końcowy spełniający nasze wymagania
   */
  val createGraph: List[Node] => Graph = nodes => {
    nodes.reverse.foldLeft(
      Set.empty[Node], Graph(List.empty[Node], List.empty[String])
    )(
      (acc: (Set[Node], Graph), headNode: Node) => {

        val dependentNodes = dependentNodesList(headNode.transaction, acc._1.toList)
        val independentNodes = independentNodesList(headNode.transaction, acc._1.toList)

        val connectedNodes: List[Node] =
          dependentNodes match {
            case Nil => dependentNodesList(headNode.transaction, independentNodes.flatMap(node => node.children))
            case _ => dependentNodes
          }

        val transformedHeaderNode = Node(headNode.transaction, connectedNodes, headNode.id)

        (
          acc._1 -- dependentNodes + transformedHeaderNode,
          Graph(acc._2.nodes.::(transformedHeaderNode), acc._2.edges.:::(connectedNodes.map(node => s"${headNode.id} -> ${node.id}")))
        )

      })._2
  }

  val graph = createGraph(createNodesFromWord(word))

  printGraph(graph)
  println("=== Copy graph to: https://dreampuf.github.io/GraphvizOnline/ ===")
}