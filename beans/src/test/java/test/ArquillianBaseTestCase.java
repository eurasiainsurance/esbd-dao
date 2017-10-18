package test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;

import tech.lapsa.insurance.esbd.beans.ESBDDates;
import tech.lapsa.lapsa.arquillian.archive.ArchiveBuilderFactory;

@RunWith(Arquillian.class)
public abstract class ArquillianBaseTestCase {

    @Deployment
    public static Archive<?> createDeployment() {
	return ArchiveBuilderFactory.newEarBuilder() //
		.withRuntimeDependencies()
		.withModule(ArchiveBuilderFactory.newEjbBuilder() //
			.withPackageOf(ESBDDates.class) //
			.build() //
			.dumpingTo(System.out::println) //
		) //
		.build() //
		.dumpingTo(System.out::println) //
		.asEnterpriseArchive();
    }
}
