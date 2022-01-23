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

import java.lang.Math.round
import java.sql.{Connection, DriverManager}
import scala.Console._
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
  //Retrieving total covid cases method
  def totalcases():Unit={
    var loop2 = 1
   while (loop2==1) {
     println(
       s"""Do you want the total cases to be grouped by:\n
         |1. Continent\n
         |2. Country\n
         |3. worldwide total""".stripMargin)

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
           println(f"${YELLOW}"+resultQuery.getString("continent") + s"${RESET} " + resultQuery.getString("total"))
         }
         println("Do you want to continue(1), or go back to the previous menu(2)?")
         loop2 = readLine().toInt
       case 2 =>
         println("Please see the table below by country:")
         val resultQuery = connection.createStatement().executeQuery(
           """SELECT location, sum(total_cases) as total FROM covidstat
             |group by location
             |order by total;
             |""".stripMargin)
         while (resultQuery.next()) {
           println(f"${YELLOW}"+resultQuery.getString("location") + s"${RESET} " + resultQuery.getString("total"))
         }
         println("Do you want to continue(1), or go back to the previous menu(2)?")
         loop2 = readLine().toInt
       case 3 =>
         val resultQuery = connection.createStatement().executeQuery(
           """SELECT sum(total_cases) as total FROM covidstat;
             |
             |""".stripMargin)
         while (resultQuery.next()) {
           println("The worldwide total cases is:" + " " + resultQuery.getString("total"))
         }
         println("Do you want to continue(1), or go back to the previous menu(2)?")
         loop2 = readLine().toInt
     }
   }
  }
  // vaccine and death method
  def vacdeath():Unit= {
    var loop3 = 1
    while (loop3 == 1) {
      println(
        s"""Please make your selections:\n
           |1. Searching statistics by country\n
           |2. Death and Vaccination\n
           |3. worldwide total""".stripMargin)

      val i = readLine().toInt
      i match {
        case 1 =>
          println("Please enter the country name:")
          var country = readLine()
          println(s"Please see the statistics for $country below:")
          val resultQuery = connection.createStatement().executeQuery(
            s"""SELECT * FROM covidstat
               |where location = "$country"
               |""".stripMargin)
          while (resultQuery.next()) {
            println(f"${YELLOW_B}Country:${RESET}")
            println(resultQuery.getString("location"))
            println(f"${YELLOW_B}Continent:${RESET}")
            println(resultQuery.getString("continent"))
            println(f"${CYAN_B}Last update date:${RESET}")
            println(resultQuery.getString("last_updated_date"))
            println(f"${GREEN_B}Population:${RESET}")
            println(resultQuery.getString("population"))
            println(f"${RED_B}Total cases:${RESET}")
            println(resultQuery.getString("total_cases"))
            println(f"${RED_B}New cases:${RESET}")
            println(resultQuery.getString("new_cases"))
            println(f"${MAGENTA_B}Total Deaths:${RESET}")
            println(resultQuery.getString("total_deaths"))
            println(f"${MAGENTA_B}New deaths${RESET}")
            println(resultQuery.getString("new_deaths"))
            println(f"${YELLOW_B}Total cases per million:${RESET}")
            println(resultQuery.getString("total_cases_per_million"))
            println(f"${GREEN_B}People vaccinated${RESET}")
            println(resultQuery.getString("people_vaccinated"))
          }
          println("\nDo you want to continue(1), or quit(2)?")
          loop3 = readLine().toInt
        case 2 =>
          println("Please enter the country name:")
          var country = readLine()
          println(s"Please see the death and vaccination info for $country below:")
          val resultQuery = connection.createStatement().executeQuery(
            s"""SELECT location,new_deaths, people_vaccinated, round(100*people_vaccinated/population,2) as vaccination_rate FROM covidstat
               |where location = "$country"
               |""".stripMargin)
          while (resultQuery.next()) {
            println(f"${YELLOW_B}Country:${RESET}" + " " + resultQuery.getString("location") + " " + f"${CYAN_B}New Death:${RESET}" + " " + resultQuery.getString("new_deaths") + " " + f"${GREEN_B}People Vaccinated:${RESET}" + resultQuery.getString("people_vaccinated") + " " + f"${CYAN_B}Vaccination rate:${RESET}" + resultQuery.getString("vaccination_rate")+"%")
          }
          println("\nDo you want to continue(1), or quit(2)?")
          loop3 = readLine().toInt
        case 3 =>
          println("Please the worldwide total vaccination and death info below:")
                    val resultQuery = connection.createStatement().executeQuery(
            s"""SELECT sum(new_deaths) as total_deaths, sum(people_vaccinated) as total_vac, sum(population) as total_pop FROM covidstat
               |
               |""".stripMargin)
          while (resultQuery.next()) {
            val totalvac = resultQuery.getString("total_vac")
            val totalpop = resultQuery.getString("total_pop")
            val vaccrate = round(100*totalvac.toDouble/totalpop.toDouble)
            println(f"${CYAN_B}Total New Death:${RESET}" + " " + resultQuery.getString("total_deaths") + " " + f"${GREEN_B}Total People Vaccinated:${RESET}" + BigDecimal(totalvac) + " " + f"${CYAN_B}Total Vaccination rate:${RESET} " + vaccrate+"%")
          }
          println("\nDo you want to continue(1), or quit(2)?")
          loop3 = readLine().toInt

      }
    }
  }


  //The beginning of the program
  println(f"${CYAN_B}${BLACK} Welcome to the worldwide Covid Cases query database, please login first to make queries....${RESET}\n")

  while(auth()==false) {
    println("The username/password is wrong! Please try again!")
   }
  println("Login Successful!\n")
  var loop1=1
  while(loop1 == 1){
    println("Now please choose what topic about covid you are interested in:\n")
    println("1. Total worldwide cases as of 1/18/2022.\n")
    println("2. Vaccination and death Search by country.\n")
    println("3. \n")
    println("4. To end the program.\n")
  var i= readLine().toInt
    i match {
      case 1 =>totalcases()

      case 2 =>vacdeath()

      //    case 3 =>
      case 4 => loop1 = 0
        connection.close()
        println("Thanks for using this program, good bye!")
    }
  }

}



