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

package controllers

import controllers.actions._
import models._
import models.requests.{AuthenticatedRequest, ServiceInfoRequest}
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.{Html, HtmlFormat}
import services.CtService
import uk.gov.hmrc.domain.CtUtr
import views.ViewSpecBase
import views.html.partials.account_summary
import views.html.subpage
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class SubpageControllerSpec extends ControllerSpecBase with MockitoSugar with ScalaFutures with ViewSpecBase {


  val accountSummary = Html("Account Summary")
  val mockAccountSummaryHelper = mock[AccountSummaryHelper]
  when(mockAccountSummaryHelper.getAccountSummaryView(Matchers.any())(Matchers.any(), Matchers.any())).thenReturn(Future.successful(accountSummary))

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new SubpageController(frontendAppConfig, messagesApi, FakeAuthAction, FakeServiceInfoAction, mockAccountSummaryHelper)

  def ctEnrolment(activated: Boolean = true) =  CtEnrolment(CtUtr("utr"), isActivated = true)
  def requestWithEnrolment(activated: Boolean): ServiceInfoRequest[AnyContent] = {
    ServiceInfoRequest[AnyContent](AuthenticatedRequest(FakeRequest(), "", ctEnrolment(activated)), HtmlFormat.empty)
  }

  val fakeRequestWithEnrolments = requestWithEnrolment(activated = true)

  def viewAsString(balanceInformation: String = "") =
    subpage(frontendAppConfig, ctEnrolment(), accountSummary)(HtmlFormat.empty)(fakeRequestWithEnrolments, messages).toString

  "Subpage Controller" must {
    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequestWithEnrolments)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(balanceInformation = "No balance information to display")
    }
  }



}




