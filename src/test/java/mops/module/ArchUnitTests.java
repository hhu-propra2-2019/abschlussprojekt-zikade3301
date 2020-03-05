package mops.module;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.web.bind.annotation.RequestMapping;

@AnalyzeClasses(packages = "mops.module")
public class ArchUnitTests {

    @ArchTest
    static final ArchRule controllerIsAnnotaedWithRequestMapping =
            classes().that().haveNameMatching("*Controller")
                    .should().beAnnotatedWith(RequestMapping.class);

}
