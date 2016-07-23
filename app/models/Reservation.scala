package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.dbio
import slick.dbio.Effect.Read
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import slick.driver.PostgresDriver.api._
import java.sql.Timestamp

case class Reservation(
  id:Long,
  start:Timestamp,
  length:Long,
  broadcast:String,
  ch:String,
  tvTuner:String,
  created:Timestamp = new Timestamp(System.currentTimeMillis()),
  updated:Timestamp = new Timestamp(System.currentTimeMillis())
)


class ReservationRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._
  private val Reservations = TableQuery[ReservationTable]

  def all(): DBIO[Seq[Reservation]] =
    Reservations.result

  def create(id:Long, start:Timestamp, length:Long, broadcast:String, ch:String, tvTuner:String): Future[Long] = {
    val createTime = new Timestamp(System.currentTimeMillis())
    val reservation = Reservation(id, start, length, broadcast, ch, tvTuner, createTime, createTime)
    db.run(Reservations returning Reservations.map(_.id) += reservation)
  }

  private class ReservationTable(tag: Tag) extends Table[Reservation](tag, "RESERVATION") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    def start = column[Timestamp]("START")
    def length = column[Long]("LENGTH")
    def broadcast = column[String]("BROADCAST")
    def ch = column[String]("CHANNEL")
    def tvTuner = column[String]("TVTUNER")
    def created = column[Timestamp]("CREATED")
    def updated = column[Timestamp]("UPDATED")
    def * = (id, start, length, broadcast, ch, tvTuner, created, updated) <> (Reservation.tupled, Reservation.unapply)
  }
}
