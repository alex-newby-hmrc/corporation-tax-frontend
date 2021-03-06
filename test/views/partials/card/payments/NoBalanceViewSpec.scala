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

package views.partials.card.payments

import models.CtEnrolment
import models.requests.AuthenticatedRequest
import org.jsoup.nodes.Document
import play.api.mvc.AnyContent
import play.api.test.FakeRequest
import uk.gov.hmrc.domain.CtUtr
import views.ViewSpecBase
import views.html.partials.card.payments.{no_balance, no_data}

class NoBalanceViewSpec extends ViewSpecBase {


  def ctEnrolment(activated: Boolean = true) =  CtEnrolment(CtUtr("utr"), isActivated = true)

  def requestWithEnrolment(activated: Boolean): AuthenticatedRequest[AnyContent] = {
    AuthenticatedRequest[AnyContent](FakeRequest(), "", ctEnrolment(activated))
  }
  val fakeRequestWithEnrolments: AuthenticatedRequest[AnyContent] = requestWithEnrolment(activated = true)
  def view = () => no_balance(frontendAppConfig)(fakeRequestWithEnrolments, messages)
  lazy val doc: Document = asDocument(view())

  "Partial no_balance view" must {

    "must have text 'You have no tax to pay.' " in {
      doc.getElementsByTag("p").first().text() mustBe "You have no tax to pay."
    }

    "must have 'View your Corporation Tax statement' link" in {
      doc.getElementById("view-ct-statement").text() mustBe "View your Corporation Tax statement"
      doc.getElementById("view-ct-statement").attr("href", "http://localhost:8080/portal/corporation-tax/org/utr/account/balanceperiods?lang=eng")
      doc.getElementById("view-ct-statement").attr("data-journey-click", "link - click:CT cards:View your CT statement")
    }

    "must have 'Make a Corporation Tax payment ' link" in {
      doc.getElementById("make-ct-payment").text() mustBe "Make a Corporation Tax payment"
      doc.getElementById("make-ct-payment").attr("href", "http://localhost:9731/business-account/corporation-tax/make-a-payment")
      doc.getElementById("make-ct-payment").attr("data-journey-click", "link - click:CT cards:Make a CT payment")
    }
  }
}

