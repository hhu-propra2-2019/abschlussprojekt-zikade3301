package mops.module;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

@AnalyzeClasses(packages = "mops.module")
public class ArchUnitTests {

    @ArchTest
    static final ArchRule controllerIsAnnotatedWithRequestMapping =
            classes().that().haveNameMatching(".*Controller")
                    .should().beAnnotatedWith(RequestMapping.class)
                    .andShould(new Rule("/module", "Request-Mapping incorrect!"));

    public static class Rule extends ArchCondition<JavaClass> {

        private final transient String correctValue;

        public Rule(String correctValue, String description, Object... args) {
            super(description, args);
            this.correctValue = correctValue;
        }

        @Override
        public void check(JavaClass item, ConditionEvents events) {
            // This will never be null,
            // as we check for an annotation of type RequestMapping beforehand.
            RequestMapping annotation = item.getAnnotationOfType(RequestMapping.class);

            if (annotation.value().length == 0 || !annotation.value()[0].equals(correctValue)) {
                events.add(SimpleConditionEvent.violated(item,
                        "Request-Mapping annotation doesn't contain the required parameter."));
            }
        }
    }

    @ArchTest
    static final ArchRule controllerIsAnnotatedWithSessionScope =
            classes().that().haveNameMatching(".*Controller")
                    .should().beAnnotatedWith(SessionScope.class);
}
