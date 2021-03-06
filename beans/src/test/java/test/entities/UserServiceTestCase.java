package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.TestConstants.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.UserEntityService.UserEntityServiceLocal;
import tech.lapsa.esbd.domain.entities.UserEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class UserServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private UserEntityServiceLocal service;

    // @Test
    // public void testGetAll() {
    // final List<UserEntity> all = service.getAll();
    // assertThat(all,
    // allOf(
    // not(nullValue()),
    // not(empty())));
    // }

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    UserEntity u = service.getById(14539);
	    assertThat(u, not(nullValue()));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_USER_ID);
    }
}
