/*Project Proposal
The intend of this program is to search through a compiled public available dataset about
 worldwide covid-19 cases on 1/18/2022 to find out about the various statistic information.
It will be associated with 2 tables: a mock username/password login credential table,
a detailed statistical information table
The functionality of this program is that, it will ask for user/pass for login, after querying
 the credential table, if login successful, then it will allow user to query details about the
 covid-19 statistics and do calculation.

Referenced data:

https://github.com/owid/covid-19-data/blob/master/public/data/latest/owid-covid-latest.csv

* */
import main.connection

import java.sql.{Connection, DriverManager}
import scala.util.Random
import scala.io.StdIn.readLine


object main extends App {
  val url = "jdbc:mysql://localhost:3306/project0"
  val username = "root"
  val password = "123456"
 // make the connection
 var connection = DriverManager.getConnection(url, username, password)

  // Authentication method
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
  //Retrieving total covid cases
  def totalcases():Unit={
    var loop = 1
   while (loop==1) {
     println("Do you want it to be grouped by continent(1), or country(2), or worldwide total(3)?")

     val i = readLine().toInt
     i match {
       case 1 =>
         println("Please see the table below by continent:")
         val resultQuery = connection.createStatement().executeQuery(
           """SELECT continent, sum(total_cases) as total FROM covidstat
             |group by continent
             |order by total;
             |""".stripMargin)
         while (resultQuery.next()) {
           println(resultQuery.getString("continent") + " " + resultQuery.getString("total"))
         }
         println("Do you want to continue(1), or quit(2)?")
         loop = readLine().toInt
       case 2 =>
         println("Please see the table below by country:")
         val resultQuery = connection.createStatement().executeQuery(
           """SELECT location, sum(total_cases) as total FROM covidstat
             |group by location
             |order by total;
             |""".stripMargin)
         while (resultQuery.next()) {
           println(resultQuery.getString("location") + " " + resultQuery.getString("total"))
         }
         println("Do you want to continue(1), or quit(2)?")
         loop = readLine().toInt
       case 3 =>
         val resultQuery = connection.createStatement().executeQuery(
           """SELECT sum(total_cases) as total FROM covidstat;
             |
             |""".stripMargin)
         while (resultQuery.next()) {
           println("The worldwide total cases is:" + " " + resultQuery.getString("total"))
         }
         println("Do you want to continue(1), or quit(2)?")
         loop = readLine().toInt
     }
   }
  }
  //def newcases():Unit{}

  //The beginning of the program
  println("Welcome to the worldwide Covid Cases query database, please login first to make queries....")
  totalcases()

  while(auth()==false)
    println("The username/password is wrong! Please try again!")

  println("Login Successful! Now please choose what topic about covid you are interested in:")
  println("1. Total worldwide cases as of 1/18/2022.")
  println("2. New worldwide cases on 1/18/2022.")
  println("3. ")
 // var i= readLine()
//    i match {
 //     case 1 =>
 //     case 2 =>
  //    case 3 =>
 //   }

  connection.close()

}



