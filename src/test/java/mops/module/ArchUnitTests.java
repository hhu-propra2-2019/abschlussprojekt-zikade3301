package mops.module;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.Optional;

@AnalyzeClasses(packages = "mops.module")
public class ArchUnitTests {

    public static class rule extends ArchCondition<JavaClass> {

        public rule(String description, Object... args) {
            super(description, args);
        }

        @Override
        public void check(JavaClass item, ConditionEvents events) {
            Optional<JavaAnnotation<JavaClass>>
                    annotation = item.getAnnotations().stream().filter(javaAnn -> javaAnn.getClass().equals(RequestMapping.class))
            .findAny();

            annotation.ifPresent(a->a.get("value"));
        }
    }


    @ArchTest
    static final ArchRule controllerIsAnnotaedWithRequestMapping =
            classes().that().haveNameMatching(".*Controller")
                    .should().beAnnotatedWith(RequestMapping.class).andShould()

}
