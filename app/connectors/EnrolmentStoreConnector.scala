/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package connectors

import _root_.models.UserEnrolments
import com.google.inject.ImplementedBy
import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.http.Status
import play.api.libs.json.JsSuccess
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class EnrolmentStoreConnectorImpl @Inject()(override val http: HttpClient, config: FrontendAppConfig)
                                           (implicit val ec: ExecutionContext) extends EnrolmentStoreConnector {

  def getEnrolments(credId: String)(implicit headerCarrier: HeaderCarrier): Future[Either[String, UserEnrolments]] =
    http.GET[HttpResponse](buildURL(credId)).map { response =>
      response.status match {
        case Status.OK =>
          response.json.validate[UserEnrolments] match {
            case JsSuccess(userEnrolments, _) => Right(userEnrolments)
            case _ => Left("Unable to parse data from enrolment API")
          }
        case _ => Left(errorMessage(response))
      }
    }.recover {
      case _: Exception => Left("Exception thrown from enrolment API")
    }

  def errorMessage(response: HttpResponse): String =
    response.status match {
      case Status.NOT_FOUND => "User not found from enrolment API"
      case Status.BAD_REQUEST => "Bad request to enrolment API"
      case Status.FORBIDDEN => "Forbidden from enrolment API"
      case Status.SERVICE_UNAVAILABLE => "Unexpected error from enrolment API"
      case Status.NO_CONTENT => "No content from enrolment API"
      case _ => "Enrolment API couldn't handle response code"
    }

  private def buildURL(credId: String): String = s"${config.enrolmentStoreUrl}/enrolment-store/users/$credId/enrolments?service=IR-CT"

}

@ImplementedBy(classOf[EnrolmentStoreConnectorImpl])
trait EnrolmentStoreConnector {
  def http: HttpClient

  def getEnrolments(credId: String)(implicit headerCarrier: HeaderCarrier): Future[Either[String, UserEnrolments]]
}
