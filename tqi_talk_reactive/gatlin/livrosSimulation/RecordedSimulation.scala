package livrosSim

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://detectportal.firefox.com")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:54.0) Gecko/20100101 Firefox/54.0")

	val headers_1 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

    val uri1 = "http://localhost:8081/livraria"

	val scn = scenario("RecordedSimulation")
		// Livros
		.exec(http("Request 1 - Login")
			.post(uri1 + "/login")
			.headers(headers_1)
		)
		.pause(1 second)
		.exec(http("Request 2 - Livros")
			.get(uri1 + "/livros")
			.headers(headers_1)
		).pause(5 second)
		.exec(http("Request 3 - Compra")
			.post(uri1 + "/compras/livros/1")
			.headers(headers_1)
		)

	setUp(
		scn.inject(
			constantUsersPerSec(1000) during (20 seconds)
		)
		//scn.inject(rampUsers(600) over (1 minutes))

	)
}
