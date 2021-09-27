/**
  * CTEC3904 Functional Software Development 2021
  * Coursework source file
  * Original author: drs
  *
  * This is minimally commented - refer to the coursework specification
  * for more detail.
  */
package coursework
import scala.language.implicitConversions

object Document {

  /**
    * Converts a section number as a list to a string:
    * e.g. List(1,2,1,3) ==> "1.2.1.3"
    */
  def fromSectionNumber(number: List[Int]): String =
    number.map(_.toString).mkString(".")

  /**
    * Converts a string representation of a section number to a list
    * e.g. "1.2.1.3" ==> List(1,2,1,3)
    */
  implicit def toSectionNumber(s: String): List[Int] =
    s.split('.').toList.map(_.toInt)
    
  /**
    * SECTIONS:
    * A section of a text document has a heading, content (lines of text) and
    * an ordered list of subsections (which may be empty)
    */
  class Section(
                 heading: String = "?",
                 content: List[String] = List.empty,
                 subsections: List[Section] = List.empty) {

    /**
      * Turns a section into a string for printing. This formats any subsections
      * using indentation at each level.
      */
    override def toString: String = { //method 1
      val indentation = "    "
      val subs = subsections map (_.toString) flatMap ((_: String).split("\\n") map (indentation + _))
      val underline = "-" * heading.length
      (s"\n$heading\n$underline" :: content ++ subs).mkString("\n")
    }
   
    /**
      * Returns a bad section in which the reason it is bad is embedded in the text.
      * This is a crude form of error handling which does not throw an exception and
      * exploits the fact that these objects contain strings which can be used to
      * encode the reason for the failure. In real life this would be handled differently
      * probably using Option[Section] for return types. However, for the purpose of this
      * coursework we did not want the clutter of error-handling to obfuscate the model.
      * By returning a "bad" Section we are able to maintain the return type of each
      * function uniformly as Section rather than introduce Option[Section].
      * NB It would be nice to have the notational simplicity of the current approach
      * and also include the Option error handling but to do this requires monads - an
      * advanced concept which is not covered on the course at this stage.
      */
    def bad(reason: String) = new Section("Bad section", List(reason))

    /**
      * Performs a numbering of all sections and subsections using the number given
      * as a prefix. This is achieved by prepending the (sub)section numbers to the
      * section headers at each nested level.
      */
    def performSectionNumbering(ns: List[Int]): Section = { //method 2
      val newTitle = s"${fromSectionNumber(ns)} $heading"
      val subNumbers = (1 to subsections.length map { n => ns :+ n }).toList
      val newSubsections = subsections.zip(subNumbers).map { case (s, ps) => s.performSectionNumbering(ps) }
      new Section(newTitle, content, newSubsections)
    }

    /**
      * Inserts a new subsection after the given subsection number. For this operation
      * the subsection numbers are assumed to be sequentially and hierarchically numbered
      * 1 (1.1, 1.2, ...), 2 (2.1, 2.2, ...), 3 (3.1, 3.2, ...), etc. To insert a new
      * subsection s after subsection 2 then use insertSubsection("2", s). This technique
      * is extended to nested levels. For example, insertSubsection("2.3.1", s).
      * To insert before the first subsection use the number 0. E.g., to insert a new
      * subsection s as a new initial subsection use insertSubsection("0", s). If the new
      * subsection is to be inserted as a new initial subsection at a lower level the same
      * principle applies, e.g.,  insertSubsection("1.2.0", s).
      */
    def insertSubsection(after: List[Int], newSection: Section): Section = //method 3
      after match {
        case List(m: Int) if m >= 0 && m <= subsections.length =>
          subsections.splitAt(m) match {
            case (left, right) => new Section(heading, content, left ++ (newSection :: right))
          }
        case m :: n :: ps if m > 0 && m <= subsections.length =>
          subsections.splitAt(m - 1) match {
            case (left, s :: right) =>
              new Section(heading, content, left ++ (s.insertSubsection(n :: ps, newSection) :: right))
          }
        case _ => bad(s"trying to insert after invalid position ${fromSectionNumber(after)}")
      }

    /**
      * Removes the subsection identified by the given section number.
      */
    def deleteSubsection(number: List[Int]): Section = //method 4
      number match {
        case List(m: Int) if m > 0 && m <= subsections.length =>
          subsections.splitAt(m - 1) match {
            case (left, s :: right) =>
              new Section(heading, content, left ++ right)
          }
        case m :: n :: ps if m > 0 && m <= subsections.length =>
          subsections.splitAt(m - 1) match {
            case (left, s :: right) =>
              new Section(heading, content, left ++ (s.deleteSubsection(n :: ps) :: right))
          }
        case _ => bad(s"trying to delete invalid position ${fromSectionNumber(number)}")
      }

    /**
      * Returns the subsection identified by the number given.
      */
    def getSubsection(number: List[Int]): Section = //method 5
      number match {
        case List(m: Int) if m > 0 && m <= subsections.length => subsections(m - 1)
        case m :: n :: ps if m > 0 && m <= subsections.length => subsections(m - 1).getSubsection(n :: ps)
        case _ => bad(s"getSubsection: section ${fromSectionNumber(number)} does not exist")
      }

    /**
      * Moves a subsection from one part of the section to another. If the source
      * or target subsections do not exist then returns the receiver (this) unchanged.
      * Otherwise returns the updated subsection in which the moved subsection is placed
      * after the subsection identified by target. NB The target subsection number refers
      * to the document structure after the source subsection has been removed.
      */
    def moveSubsection(source: List[Int], target: List[Int]): Section =
      deleteSubsection(source).insertSubsection(target, getSubsection(source))


    /**
      * Copies the subsection referenced by source to another position following
      * subsection target. Thus it inserts after target. This behaves like
      * moveSubsection except that the original source section remains in place
      * and a duplicate is inserted.
      */
    def copySubsection(source: List[Int], target: List[Int]): Section =
      insertSubsection(target, getSubsection(source))

    /**
      * Applies a transformation function to every piece of text in the section,
      * including all subsections.
      */
    def mapText(f: List[String] => List[String]): Section =
      new Section(heading, f(content), subsections.map(_.mapText(f)))
  }


  /**
    * BOOKS:
    * A Book has a title, and sequence of sections (chapters).
    */
  class Book(title: String, chapters: List[Section] = List.empty) {

    val numberOfChapters: Int = chapters.length

    /**
      * Returns a bad book in which the reason it is bad is embedded in the text.
      * See Section.bad for a rationale for why error handling is done this way
      * in this example.
      */
    def bad(reason: String) = new Book(s"Bad book: $reason")

    /**
      * Inserts a new chapter after the number given. Use after=0 to indicate that the
      * new chapter should become the first.
      */
    def insertChapter(after: Int, newChapter: Section): Book =
      chapters splitAt after match {
        case (left, right) => new Book(title, left ++ (newChapter :: right))
      }

    override def toString: String = {
      val newTitle = title.toUpperCase + "\n"
      val numbers = 1 to numberOfChapters
      val numberedChapters = (chapters zip numbers).map {
        case (c, n) => c.performSectionNumbering(List(n))
      }
      newTitle + (numberedChapters map (_.toString)).mkString("\n")
    }

    /**
      * Returns the updated book in which the action a has been applied to the
      * given chapter of book b. This operation lifts a Section method to work
      * on a given chapter of a book. Thus, e.g., to insert a new subsection s
      * after "3.1.2" in book b, you would write:
      * b.lift(3, _.insertSubsection("1.2", s))
      */
    def lift(chapter: Int, a: Section => Section): Book =
      if (numberOfChapters == 0)
        bad(s"book has no chapters")
      else if (chapter < 1 || chapter > numberOfChapters)
        bad(s"chapter $chapter does not exist")
      else {
        val (left, c :: right) = chapters splitAt (chapter - 1)
        new Book(title, left ++ (a(c) :: right))
      }

    /**
      * Removes a chapter from the book. If the specified chapter does not exist
      * then returns the receiver (this) unchanged.
      */
    def removeChapter(number: Int): Book =
      if (number > 0 && number <= numberOfChapters)
        new Book(title, chapters.take(number - 1) ++ chapters.drop(number))
      else
        bad(s"Cannot remove chapter $number as this does not exist")

    /**
      * Just take the first n chapters of a book
      */
    def first(n: Int, newTitle: String = title): Book =
      new Book(newTitle, chapters.take(n))

    /**
      * Just take the last n chapters of a book
      */
    def last(n: Int, newTitle: String = title): Book =
      new Book(newTitle, chapters.reverse.take(n).reverse)

    /**
      * Extracts the given subsection from each chapter and creates a new book from these.
      */
    def slice(number: List[Int], newTitle: String = title): Book =
      new Book(newTitle, chapters.map(_.getSubsection(number)))

    /**
      * Returns a new book like the current one except that all of the content (not
      * titles or headings) is changed to upper case.
      */
    def toUpperCase(newTitle: String = title): Book =
      new Book(newTitle, chapters.map(_.mapText(_.map(_.toUpperCase))))

    /**
      * Returns a new book like the current one except that the contents of each
      * subsection is limited to no more than n lines.
      */
    def shorten(n: Int, newTitle: String = title): Book = ???

    /**
      * Returns a new book like the current one except that the contents of each
      * subsection has every occurrence of character "oldChar" replaced by "newChar".
      */
    def replaceChars(oldChar: Char, newChar: Char, newTitle: String = title): Book = ???

    /**
      * Exchanges the positions of two chapters in the book. If either of the two
      * chapter numbers do not exist then returns the receiver (this) unchanged.
      */
    def swapChapters(fst: Int, snd: Int): Book = ???

    /**
      * Inserts the new subsection at the start of each of the given chapters. So, for
      * example, given some new section s, and a book b:
      * |Chapter X
      * |  Section A
      * |  Section B
      * |Chapter Y
      * |  Section C
      * |Chapter Z
      * |  Section D
      * |  Section E
      *
      * Then b.insertAtZero("1,3", s) would generate:
      *
      * |Chapter X
      * |  s
      * |  Section A
      * |  Section B
      * |Chapter Y
      * |  Section C
      * |Chapter Z
      * |  s
      * |  Section D
      * |  Section E
      */
    def insertAtZero(chsString: String, newSection: Section): Book = {  //CHOSEN METHOD TO DEVELOP
      val chs = chsString.split(',').toList.map(_.toInt)
      
      def modifyChapters (chs: List[Int], newSection: Section, book: Book): Book = {
        if (chs.isEmpty) return book
        else {
          val chapter = chapters(chs(0)-1).insertSubsection("0", newSection)
          val newBook = book.removeChapter(chs(0)).insertChapter(chs(0)-1,chapter)
          modifyChapters(chs.tail, newSection, newBook)
        }
      }
      
      if (chs.max > numberOfChapters) bad(s"Input(s) higher than number of chapters.") else
      if (chs.min < 1) bad(s"Input cannot contain chapter zero.") else
      modifyChapters(chs, newSection, this)
    }
  }


  def main(args: Array[String]): Unit = {
    import coursework.DocumentData._
    /*
     * The book compsci has been defined in DocumentData along with all of
     * its subsections.
     *
     * Uncomment the experiments one at a time to see how the methods work.
     * Look at the code above so that you can appreciate how they have been
     * implemented.
     *
     * Adapt these experiments and try some of your own.
     */

//        /* EXPERIMENT 1 - Print out compsci */
//         println(compsci) // prints a Book
//
//        /* EXPERIMENT 2 - Print out the first year (level 4) only */
//         println(level4) // prints a Section
//
//        /* EXPERIMENT 3 - Print out the first year (level 4) adding section numbering */
//         println(level4.performSectionNumbering("1.2.3")) // prints a numbered section
//
//        /* EXPERIMENT 4 - Start the section numbering at another place */
//         println(level4.performSectionNumbering("9.3.7.8"))
//
//        /* EXPERIMENT 5 - insert a section after 2.1.1 */
//         println(compsci.lift(2, _.insertSubsection("1.1", testSection)))
//
//        /* EXPERIMENT 6 - Slice the book and return only the first subsections */
//         println(compsci.slice("1"))
//
//        /* EXPERIMENT 7 - A deeper slice */
//         println(compsci.slice("1.1")) // What happens if you make it "2.1"?
//
//        /* EXPERIMENT 8 - Turn the text into upper case */
//         println(compsci.toUpperCase())
//
//        /* EXPERIMENT 9 - Combinations... */
//        println(compsci.first(2)
//          .toUpperCase()
//          .slice("2.1")
//          .lift(1, _.insertSubsection("0", testSection)))
    
          /* EXPERIMENTS 10+ - insertAtZero... */
//        println(compsci.insertAtZero("1", testSection)) //chapter 1
    
//        println(compsci.insertAtZero("2", test)) //chapter 2
    
        println(compsci.insertAtZero("1,2", testSection)) //chapter 1 and 2
    
//        println(compsci.insertAtZero("1,2,3", testSection)) //chapter 1, 2 and 3
    
//        println(compsci.insertAtZero("3,2,1,3", testSection))  //repeating a chapter doesn't affect the function
    
//        println(compsci.insertAtZero("0", testSection)) //chapter number below 1
    
//        println(compsci.insertAtZero("4", testSection)) //chapter number too high

  }
}