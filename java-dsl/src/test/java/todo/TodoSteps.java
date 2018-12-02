/*
 * Copyright 2006-2016 the original author or authors.
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

package todo;

import com.consol.citrus.Citrus;
import com.consol.citrus.annotations.CitrusFramework;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.config.CitrusSpringConfig;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.builder.SeleniumActionBuilder;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.validation.json.JsonTextMessageValidator;
import cucumber.api.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * @author Dev Maheshwar
 */

@ContextConfiguration(classes = EndpointConfig.class)
public class TodoSteps extends TestNGCitrusTestDesigner{

    TestContext tc = new TestContext();

    /** Test designer filled with actions by step definitions */
    @CitrusResource
    private TestDesigner designer;

    @Autowired
    private HttpClient todoListClient;

    @Qualifier("tc")
    @Autowired
    private HttpClient todoclient;

    @Qualifier("jtv")
    @Autowired
    private JsonTextMessageValidator jtmv;

    @Autowired
    private SeleniumBrowser browser;

    @Given("^Todo list is empty$")
    public void empty_todos() {


        /*
            Sample Http Client Call
         */

        designer.variable("todoName", "todo_citrus:randomNumber(4)");
        designer.variable("todoDescription", "Description: ${todoName}");

        designer.http()
                .client(todoListClient)
                .send()
                .get("/todos/1").accept("application/json; charset=utf-8").contentType("application/json; charset=utf-8").build().doExecute(tc);


        String response = null;
        designer.http()
                .client(todoListClient)
                .receive()
                .response(HttpStatus.OK).contentType("application/json; charset=utf-8").validator(jtmv).build().doExecute(tc);


        tc.getMessageStore().getMessage("receive(todoListClient)").getPayload();



        System.out.println(tc.getMessageStore().getMessage("receive(todoListClient)").getPayload());


        /*
            Sample Selenium call
         */


//        System.setProperty("webdriver.chrome.driver", "/Users/dmp001j/IdeaProjects/citrus-samples/samples-cucumber/sample-cucumber-spring2/java-dsl/chromedriver");


        designer.selenium()
                .browser(browser)
                .start();

        designer.selenium()
                .navigate(todoclient.getEndpointConfiguration().getRequestUrl() + "/todolist");

        designer.selenium()
                .find()
                .element(By.xpath("(//li[@class='list-group-item'])[last()]"))
                .text("No todos found");

        designer.selenium()
                .setInput("${todoName}")
                .element(By.name("title"));

        designer.selenium()
                .setInput("${todoDescription}")
                .element(By.name("description"));

        designer.selenium().click()
                .element(By.tagName("button"));

        designer.selenium()
                .waitUntil()
                .element(By.xpath("(//li[@class='list-group-item']/span)[last()]"))
                .timeout(2000L)
                .visible();

        designer.selenium()
                .find()
                .element(By.xpath("(//li[@class='list-group-item']/span)[last()]"))
                .text("${todoName}");


        designer.http()
                .client(todoclient)
                .send()
                .delete("/api/todolist");

        designer.http()
                .client(todoclient)
                .receive()
                .response(HttpStatus.OK);


        System.out.println();


    }

    @When("^(?:I|user) adds? entry \"([^\"]*)\"$")
    public void add_entry(String todoName) {
//        designer.http()
//                .client(todoListClient)
//                .send()
//                .post("/todolist")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//                .payload("title=" + todoName);
//
//        designer.http()
//                .client(todoListClient)
//                .receive()
//                .response(HttpStatus.FOUND);

        System.out.println("****************** In the second step ****************");
    }

    @When("^(?:I|user) removes? entry \"([^\"]*)\"$")
    public void remove_entry(String todoName) throws UnsupportedEncodingException {
//        designer.http()
//                .client(todoListClient)
//                .send()
//                .delete("/api/todo?title=" + URLEncoder.encode(todoName, "UTF-8"));
//
//        designer.http()
//                .client(todoListClient)
//                .receive()
//                .response(HttpStatus.OK)
//                .messageType(MessageType.PLAINTEXT);
        System.out.println("****************** In the fourth step ****************");

    }

    @Then("^(?:the )?number of todo entries should be (\\d+)$")
    public void verify_todos(int todoCnt) {
//        designer.http()
//                .client(todoListClient)
//                .send()
//                .get("/api/todolist/count");
//
//        designer.http()
//                .client(todoListClient)
//                .receive()
//                .response(HttpStatus.OK)
//                .messageType(MessageType.PLAINTEXT)
//                .payload(String.valueOf(todoCnt));

        System.out.println("****************** In the third step ****************");
    }

    @Then("^(?:the )?todo list should be empty$")
    public void verify_empty_todos() {
        verify_todos(0);
        System.out.println("****************** In the fifth step ****************");
    }

}
