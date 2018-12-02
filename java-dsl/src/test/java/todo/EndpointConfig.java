/*
 * Copyright 2006-2017 the original author or authors.
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

import com.consol.citrus.container.SequenceAfterSuite;
import com.consol.citrus.container.SequenceAfterTest;
import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.dsl.runner.TestRunner;
import com.consol.citrus.dsl.runner.TestRunnerAfterSuiteSupport;
import com.consol.citrus.dsl.runner.TestRunnerAfterTestSupport;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.selenium.endpoint.SeleniumBrowser;
import com.consol.citrus.validation.json.JsonTextMessageValidator;
import com.consol.citrus.variable.GlobalVariables;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.BrowserType;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;

/**
 * @author Christoph Deppisch
 */
@Configuration
@PropertySource("citrus.properties")
public class EndpointConfig {

    @Bean
    public GlobalVariables globalVariables() {
        GlobalVariables variables = new GlobalVariables();
        variables.getVariables().put("project.name", "Citrus Integration Tests");
        return variables;
    }


    @Bean
    @DependsOn("browser")
    public SequenceAfterSuite afterSuite(SeleniumBrowser browser) {
        return new TestRunnerAfterSuiteSupport() {
            @Override
            public void afterSuite(TestRunner runner) {
                runner.selenium(builder -> builder.browser(browser).stop());
            }
        };
    }

    @Bean
    public SequenceAfterTest afterTest() {
        return new TestRunnerAfterTestSupport() {
            @Override
            public void afterTest(TestRunner runner) {
                runner.sleep(500);
            }
        };
    }


    @Bean(name="jtv")
    public JsonTextMessageValidator getJsonTextMessageValidator() {
        return new JsonTextMessageValidator();
    }


    @Bean
    public HttpClient todoListClient() {
        return CitrusEndpoints.http()
                .client()
                .requestUrl("https://jsonplaceholder.typicode.com")
                .requestMethod(HttpMethod.GET).contentType("application/json").build();
    }



    @Bean(name="tc")
    public HttpClient tc() {
        return CitrusEndpoints.http()
                .client()
                .requestUrl("http://localhost:8080")
                .build();
    }

    @Bean
    public SeleniumBrowser browser() {
        return CitrusEndpoints.selenium()
                .browser()
                .type(BrowserType.CHROME)
                .build();
    }

}
