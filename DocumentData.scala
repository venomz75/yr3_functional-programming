package coursework
import coursework.Document.{Book, Section}

object DocumentData {

  val ctec1902 = new Section(
    "CTEC1902 Computer Programming I",
    """This module introduces programming in Scala.
      |It concentrates on the fundamental concepts
      |including loops, selection, and functions.""".stripMargin.split("\n").toList)

  val ctec1904 = new Section(
    "CTEC1904 Computer Ethics",
    """This module introduces the basic principles
      |of computer ethics, encouraging students to
      |consider the wider social and ethical issues
      |around software development.""".stripMargin.split("\n").toList)

  val ctec1906 = new Section(
    "CTEC1906 Computer Systems",
    """This module introduces the basic architecture
      |of a computer and its operating system. Practical
      |experience is gained using Unix shellscript.""".stripMargin.split("\n").toList)

  val ctec1908 = new Section(
    "CTEC1908 Mathematics for Computing",
    """This module includes Boolean logic and set
      |theory. Emphasis is placed on truth tables
      |and understanding fundamental algebraic laws.""".stripMargin.split("\n").toList)


  val ctec1903 = new Section(
    "CTEC1902 Computer Programming II",
    """This module continues programming in Scala
      |picking up from CTEC1902 and introducing more
      |advanced ideas including some OO principles.""".stripMargin.split("\n").toList)

  val ctec1905 = new Section(
    "CTEC1905 Computer Law and Cyber Security",
    """This module introduces two important concepts.
      |Firstly, the basic legislation that is relevant
      |to the use of IT, and secondly, the basic
      |principles of computer security.""".stripMargin.split("\n").toList)

  val ctec1907 = new Section(
    "CTEC1907 Computer Networks",
    """This module introduces the architecture
      |and protocols that are used in modern
      |computer networks.""".stripMargin.split("\n").toList)


  val ctec1909 = new Section(
    "CTEC1909 Database Design and Implementation",
    """This module introduces the theory of databases
      |including normalisation and uses SQL in practical
      |sessions with an Oracle database.""".stripMargin.split("\n").toList)

  val level41 = new Section("First Term",
    """The first term introduces programming, operating
      |systems, discrete maths, and computer ethics.""".stripMargin.split("\n").toList,
    List(ctec1902, ctec1904, ctec1906, ctec1908))

  val level42 = new Section("Second Term",
    """The second term introduces more advanced programming,
      |computer networks, database theory and practice, and
      |the fundamentals of computer law and cyber security.""".stripMargin.split("\n").toList,
    List(ctec1903, ctec1905, ctec1907, ctec1909))

  val level4 = new Section("First Year: Foundations",
    """The first year introduces the foundational subjects
      |required to study computer science. These subjects also
      |lay the ground for the more advanced work in subsequent
      |years.""".stripMargin.split("\n").toList,
    List(level41, level42)
  )


  val ctec2905 = new Section(
    "CTEC2905 Object Oriented Design",
    """This module introduces the design principles
      |for developing Object Oriented software. This
      |is an established technology and uses UML with Java.""".stripMargin.split("\n").toList)

  val ctec2907 = new Section(
    "CTEC2907 Web Application Development",
    """This module covers the server and client side
      |of developing web-based applications with
      |an emphasis on security. The module uses PHP.""".stripMargin.split("\n").toList)

  val ctec2909 = new Section(
    "CTEC2909 Data Structures and Algorithms",
    """This module introduces the classic computer science
      |data structures (stacks, queues, trees, etc.) and
      |algorithms including search and sort techniques.""".stripMargin.split("\n").toList)

  val ctec2904 = new Section(
    "CTEC2904 Software and Security Management",
    """This module expands on the basics of cyber security
      |from the first year and puts these issues into a
      |wider organisational context along with supporting
      |development methodologies.""".stripMargin.split("\n").toList)


  val ctec2906 = new Section(
    "CTEC2906 Object Oriented Development",
    """This module continues from CTEC2905 Object Oriented
      |Design. Technical coding patterns are emphasised and
      |Java is used to support the practical work.""".stripMargin.split("\n").toList)

  val ctec2207 = new Section(
    "CTEC2207 Agile Team Development",
    """This module simulates the team working found in
      |real companies. It adopts an agile development
      |methodology. Studetns work on Visual Studio with C#.""".stripMargin.split("\n").toList)

  val ctec2910 = new Section(
    "CTEC2910 Concurrent and Parallel Algorithms",
    """This module introduces the principles behind coding
      |for multi-threaded and mutli-core architectures. The
      |principles of controlling shared acces to mutable state
      |are covered in detail.""".stripMargin.split("\n").toList)

  val imat2704 = new Section(
    "IMAT2704 Introduction to Research",
    """This module develops students' research skills.
      |The module encourages a foucs on concepts drawn from
      |the broader IT industry.""".stripMargin.split("\n").toList)

  val level51 = new Section("First Term",
    """The first term introduces programming, operating
      |systems, discrete maths, and computer ethics.""".stripMargin.split("\n").toList,
    List(ctec2905, ctec2907, ctec2909, ctec2904))

  val level52 = new Section("Second Term",
    """The second term introduces more advanced programming,
      |computer networks, database theory and practice, and
      |the fundamentals of computer law and cyber security.""".stripMargin.split("\n").toList,
    List(ctec2906, ctec2207, ctec2910, imat2704))

  val level5 = new Section("Second Year: Design",
    """The second year emphasises practical skills in software
      |development. Classic computer science subjects that
      |cover the relevant theory feature prominently.""".stripMargin.split("\n").toList,
    List(level51, level52)
  )

  val ctec3451 = new Section(
    "CTEC3451 Development Project",
    """A 30-credit module where students demonstrate
      |their ability to develop a practical solution
      |to a real-world problem.""".stripMargin.split("\n").toList)

  val imat3423 = new Section(
    "IMAT3423 Systems Building: Methods",
    """A module that introduces students to the
      |various software development methods/paradigms
      |used in industry.""".stripMargin.split("\n").toList)

  val level61 = new Section("First Term",
    """The first term includes options alongside the
      |compulsory module IMAT3423.""".stripMargin.split("\n").toList,
    List(imat3423))

  val level62 = new Section("Second Term",
    """The second term includes options.""".stripMargin.split("\n").toList)

  val level6 = new Section("Final Year: Options",
    """The final year introduces some compulsory modules
      |and some options. The final year project runs over
      |the two terms.""".stripMargin.split("\n").toList,
    List(level61, level62, ctec3451)
  )

  val compsci: Book = new Book("Computer Science", List(level4, level5, level6))

  val testSection = new Section("This is a test section",
    """IT ONLY CONTAINS ONE LINE.""".stripMargin.split("\n").toList
  )
  
  val test = new Section("######################",
      """##########################
        |##########################
        |##########################""".stripMargin.split("\n").toList)
}