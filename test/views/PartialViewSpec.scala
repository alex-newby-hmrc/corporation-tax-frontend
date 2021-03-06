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

package views

import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.domain.CtUtr
import views.behaviours.ViewBehaviours
import views.html.partial

class PartialViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "partial"

  val fakeSummary = Html("<p>This is the account summary</p>")

  def createView = () => partial(CtUtr("UTR"), fakeSummary, frontendAppConfig)(fakeRequest, messages)

  "Partial view" must {
    "pass the title" in {
      asDocument(createView()).text() must include ("Corporation Tax")
    }

    "pass the utr of the user" in {
      asDocument(createView()).text() must include ("Your Unique Taxpayer Reference (UTR) is UTR.")
    }

    "have a more details link" in {
      assertLinkById(asDocument(createView()), "ct-account-details-link", "More Corporation Tax details",
        "http://localhost:9731/business-account/corporation-tax", "link - click:Corporation Tax:More Corporation Tax details")
    }

    "pass the account summary partial" in {
      asDocument(createView()).html() must include(fakeSummary.toString())
    }
  }
}

class PartialViewNotActivatedSpec extends ViewBehaviours {

  val messageKeyPrefix = "partial"

  val fakeSummary = Html("<p>This is the account summary</p>")

  def createView = () => partial(CtUtr("UTR"), fakeSummary, frontendAppConfig, false)(fakeRequest, messages)

  "Partial view when the account is not activated" must {
    "pass the title" in {
      asDocument(createView()).text() must include ("Corporation Tax")
    }

    "pass the utr of the user" in {
      asDocument(createView()).text() must include ("Your Unique Taxpayer Reference (UTR) is UTR.")
    }

    "not have a more details link" in {
      asDocument(createView()).getElementById("ct-account-details-link") mustBe null
    }

    "pass the account summary partial" in {
      asDocument(createView()).html() must include(fakeSummary.toString())
    }
  }
}
