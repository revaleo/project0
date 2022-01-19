/*Project Proposal
The intend of this program is to search through a public database,
to find out about the protein structure and genetic sequence of covid19 virus.
It will be associated with 3 tables: a mock username/password login credential table,
a protein structure table that lists all known different proteins about the virus,
and a gene sequencing table that is associated with each protein.
The functionality of this program is that, it will ask for user/pass for login, after login,
user will be able to given a few options to find out more about the structures of various
proteins, it can also specifically query about the gene sequence of a particular protein.

https://www.ncbi.nlm.nih.gov/Structure/SARS-CoV-2.html
https://www.ncbi.nlm.nih.gov/protein/YP_009724390.1

* */
import main.connection

import java.sql.{Connection, DriverManager}
import scala.util.Random
import scala.io.StdIn.readLine


object main extends App {
  val url = "jdbc:mysql://localhost:3306/project0"
  val username = "root"
  val password = "123456"

  var connection: Connection = null

  // make the connection
  connection = DriverManager.getConnection(url, username, password)

  // Authentication
def auth():Boolean={
  println("Please enter your username for authentication:")
  val userinput = readLine()
  println("Please enter your password for authentication:")
  val passinput = readLine()
  val resultQuery = connection.createStatement().executeQuery("SELECT * FROM credentials")
  var comb = scala.collection.mutable.Map[String, String]()
 while (resultQuery.next()) {
   val username = resultQuery.getString("username")
   val password = resultQuery.getString("password")
   comb+=(username->password)
 }

  if (comb.contains(userinput) && passinput == comb(userinput))
  true
   else
  false
}
println(auth())
  connection.close()
}

