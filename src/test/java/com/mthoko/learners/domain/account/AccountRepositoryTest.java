package com.mthoko.learners.domain.account;

import com.mthoko.learners.domain.account.credentials.Credentials;
import com.mthoko.learners.domain.account.credentials.CredentialsRepo;
import com.mthoko.learners.domain.account.member.Member;
import com.mthoko.learners.domain.account.member.MemberRepo;
import com.mthoko.learners.domain.device.DeviceRepo;
import com.mthoko.learners.domain.simcard.SimCardRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    CredentialsRepo credentialsRepo;

    @Autowired
    SimCardRepo simCardRepo;

    @Autowired
    DeviceRepo deviceRepo;

    @BeforeEach
    void setupDatabase() {

    }

    @Test
    void example_database_test() {
        // Given
        String email = "mthoko78@gmail.com";
        Account account = createEntity("Mthokozisi", "Mhlanga", "0684243087", email, "Mthoko78");
        List<Account> rows = List.of(
                account
        );

        AccountService accountService = new AccountServiceImpl(
                accountRepo,
                null,
                memberRepo,
                credentialsRepo,
                simCardRepo,
                null,
                deviceRepo,
                null,
                null);

        // When
        accountService.saveAll(rows); // this is just the stock JPA method as an example, but ideally we would only test custom JPA queries.
        // Then
        assertEquals(1, accountService.count());
        Optional<Account> saved = accountService.findByEmail(email);
        assertNotNull(saved);
        Account actual = saved.get();
        assertEquals(account, actual);
        assertEquals(account.getMember(), actual.getMember());
        assertEquals(account.getCredentials(), actual.getCredentials());
    }

    private Account createEntity(String name, String surname, String phone, String email, String password) {
        Account account = new Account(
                new Member(name, surname, phone, email),
                new Credentials(null, password),
                new ArrayList<>(),
                new ArrayList<>(),
                phone,
                null,
                true);
        return account;
    }

}