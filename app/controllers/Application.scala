package controllers

import javax.inject.Inject
import java.sql.Timestamp

import play.api.Play.current
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.db.slick._
import play.api.mvc._
import slick.driver.PostgresDriver.api._
import slick.driver.JdbcProfile

import java.util.Calendar
import java.util.TimeZone
import models.TVListings
import models._
import org.joda.time.DateTime


class Application @Inject() ( reservationRepo: ReservationRepo) extends Controller {


  def index = Action {
    Ok(views.html.tvlistings.render())
  }
  def testListings = Action {
    reservationRepo.create(0, new Timestamp(System.currentTimeMillis()), 0, "", "", "")
    Ok(views.html.tvlistingsTest.render())
  }

  def getJSON = Action {
    val timeZoneJson = {
      val b = Map.newBuilder[String,String]
      TimeZone.getAvailableIDs().foreach{ timeZoneId =>
        val tz = TimeZone.getTimeZone(timeZoneId)
        b += tz.getDisplayName() -> tz.getID()
      }
      b.result
    }
    val jsonObject = Json.toJson(
        Map(
            "localDate" -> Json.toJson(Calendar.getInstance(TimeZone.getDefault()).getTime().toString()),
            "utcDate" -> Json.toJson(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().toString()),
            "timeZone" -> Json.toJson(timeZoneJson)
            )
    )
    Ok(jsonObject)
  }
  def getProgrammesJSON(year:String, monthOfYear:String, dayOfMonth:String, hourOfDay:String, minuteOfHour:String, length:String, broadcast:String, ch:String) = Action{
    val litings = new TVListings("public/listings/" + ch + ".xml")
    val start = new DateTime(year.toInt, monthOfYear.toInt, dayOfMonth.toInt, hourOfDay.toInt, minuteOfHour.toInt)
    val stop = start.plusMinutes(length.toInt)
    var programmeList:Seq[JsValue] = litings.programmeList(start, stop)
    Ok(Json.toJson(programmeList))
  }
  def getChannelNameJSON(ch:String) = Action{
    val litings = new TVListings("public/listings/" + ch + ".xml")
    Ok(Json.toJson(litings.channelName))
  }
  def reservationProgramme(year:String, monthOfYear:String, dayOfMonth:String, hourOfDay:String, minuteOfHour:String, length:String, broadcast:String, ch:String) = Action{
//    val litings = new TVListings("public/listings/" + ch + ".xml")
    val start = Calendar.getInstance(TimeZone.getDefault())
    start.set(year.toInt, monthOfYear.toInt, dayOfMonth.toInt, hourOfDay.toInt, minuteOfHour.toInt, 0)
//    val start = new Date(year.toInt, monthOfYear.toInt, dayOfMonth.toInt, hourOfDay.toInt, minuteOfHour.toInt, 0)
//    val stop = start.plusMinutes(length.toInt)
//    var programmeList:Seq[JsValue] = litings.programmeList(start, stop)
//    Ok(Json.toJson(programmeList))
    reservationRepo.create(0, new Timestamp(start.getTimeInMillis), length.toLong, broadcast, ch, "")

    var outString = "Number is "
//    val conn = DB.getConnection()
//    try {
//      val stmt = conn.createStatement
//      val rs = stmt.executeQuery("SELECT 9 as testkey ")
//      while (rs.next()) {
//        outString += rs.getString("testkey")
//      }
//    } finally {
//      conn.close()
//    }
//    Ok(outString)


    Ok(outString)
  }

}
