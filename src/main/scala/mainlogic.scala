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
import java.sql.{Connection, DriverManager}

object mainlogic extends App {
  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://localhost/databasename"
  val username = "root"
  val password = ""

  var connection:Connection = null
  try {
    // make the connection
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)

    // create the statement, and run the select query
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT host, user FROM user")
    while ( resultSet.next() ) {
      val host = resultSet.getString("host")
      val user = resultSet.getString("user")
      println("host, user = " + host + ", " + user)
    }
  } catch {
    case e => e.printStackTrace
  }
  connection.close()
}
